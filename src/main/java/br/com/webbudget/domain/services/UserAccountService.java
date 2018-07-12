/*
 * Copyright (C) 2017 Arthur Gregorio, AG.Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.com.webbudget.domain.services;

import br.com.webbudget.application.controller.ProfileBean.PasswordChangeDTO;
import br.com.webbudget.domain.entities.tools.Authorization;
import br.com.webbudget.domain.entities.tools.Grant;
import br.com.webbudget.domain.entities.tools.Group;
import br.com.webbudget.domain.entities.tools.StoreType;
import br.com.webbudget.domain.entities.tools.User;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.repositories.tools.AuthorizationRepository;
import br.com.webbudget.domain.repositories.tools.GrantRepository;
import br.com.webbudget.domain.repositories.tools.GroupRepository;
import br.com.webbudget.domain.repositories.tools.UserRepository;
import br.eti.arthurgregorio.shiroee.auth.PasswordEncoder;
import br.eti.arthurgregorio.shiroee.config.jdbc.UserDetails;
import br.eti.arthurgregorio.shiroee.config.jdbc.UserDetailsProvider;
import java.util.List;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.apache.shiro.SecurityUtils;

/**
 * The user account service
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 27/12/2017
 */
@ApplicationScoped
public class UserAccountService implements UserDetailsProvider {

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private UserRepository userRepository;
    @Inject
    private GrantRepository grantRepository;
    @Inject
    private GroupRepository groupRepository;
    @Inject
    private AuthorizationRepository authorizationRepository;

    /**
     *
     * @param user
     * @return
     */
    @Transactional
    public User save(User user) {

        // validate the email
        final Optional<User> emailOptional = this.userRepository
                .findOptionalByEmail(user.getEmail());

        if (emailOptional.isPresent()) {
            throw new BusinessLogicException("user.email-duplicated");
        }

        // validate the username
        final Optional<User> usernameOptional = this.userRepository
                .findOptionalByUsername(user.getUsername());

        if (usernameOptional.isPresent()) {
            throw new BusinessLogicException("user.username-duplicated");
        }

        // if the user is local...
        if (user.getStoreType() == StoreType.LOCAL) {

            if (!user.isPasswordValid()) {
                throw new BusinessLogicException("user.password-not-match-or-invalid");
            }

            user.setPassword(this.passwordEncoder
                    .encryptPassword(user.getPassword()));
        }

        // save
        return this.userRepository.save(user);
    }

    /**
     *
     * @param user
     */
    @Transactional
    public void update(User user) {

        // validate the email
        final Optional<User> userOptional = this.userRepository
                .findOptionalByEmail(user.getEmail());

        if (userOptional.isPresent()) {

            final User found = userOptional.get();

            if (!found.getUsername().equals(user.getUsername())) {
                throw new BusinessLogicException("user.email-duplicated");
            }
        }

        // if the user is local...
        if (user.getStoreType() == StoreType.LOCAL) {

            if (user.hasChangedPasswords()) {

                // check if passwords match
                if (!user.isPasswordValid()) {
                    throw new BusinessLogicException("user.password-not-match");
                }

                // crypt the user password
                user.setPassword(this.passwordEncoder.encryptPassword(
                        user.getPassword()));
            } else {
                final Optional<User> actualUser = this.userRepository
                        .findOptionalByUsername(user.getUsername());
                user.setPassword(actualUser.get().getPassword());
            }
        }

        this.userRepository.saveAndFlushAndRefresh(user);
    }

    /**
     *
     * @param user
     */
    @Transactional
    public void delete(User user) {

        final String principal = String.valueOf(SecurityUtils
                .getSubject().getPrincipal());

        // prevent to delete you own user
        if (principal.equals(user.getUsername())) {
            throw new BusinessLogicException("user.delete-principal");
        }

        // prevent to delete the main admin
        if (user.isAdministrator()) {
            throw new BusinessLogicException("user.delete-administrator");
        }

        this.userRepository.attachAndRemove(user);
    }

    /**
     *
     * @param passwordChangeDTO
     * @param user
     */
    @Transactional
    public void changePasswordForCurrentUser(PasswordChangeDTO passwordChangeDTO, User user) {

        final boolean actualMatch = this.passwordEncoder.passwordsMatch(
                passwordChangeDTO.getActualPassword(), user.getPassword());

        if (actualMatch) {

            if (passwordChangeDTO.isNewPassMatching()) {

                final String newPass = this.passwordEncoder.encryptPassword(
                        passwordChangeDTO.getNewPassword());

                user.setPassword(newPass);

                this.userRepository.saveAndFlushAndRefresh(user);

                return;
            }
            throw new BusinessLogicException("profile.new-pass-not-match");
        }
        throw new BusinessLogicException("profile.actual-pass-not-match");
    }

    /**
     *
     * @param group
     * @return
     */
    @Transactional
    public Group save(Group group) {
        return this.groupRepository.save(group);
    }

    /**
     *
     * @param group
     * @param authorizations
     */
    @Transactional
    public void save(Group group, List<Authorization> authorizations) {

        this.groupRepository.save(group);

        authorizations.stream().forEach(authz -> {
            Authorization authorization = this.authorizationRepository
                    .findOptionalByFunctionalityAndPermission(authz.getFunctionality(), authz.getPermission())
                    .get();
            this.grantRepository.save(new Grant(group, authorization));
        });
    }

    /**
     *
     * @param group
     */
    @Transactional
    public void update(Group group) {
        this.groupRepository.saveAndFlushAndRefresh(group);
    }

    /**
     *
     * @param group
     * @param authorizations
     */
    @Transactional
    public void update(Group group, List<Authorization> authorizations) {

        this.groupRepository.saveAndFlushAndRefresh(group);

        // list all old grants
        final List<Grant> oldGrants = this.grantRepository.findByGroup(group);

        oldGrants.forEach(grant -> {
            this.grantRepository.remove(grant);
        });

        // save the new ones
        authorizations.forEach(authz -> {
            Authorization authorization = this.authorizationRepository
                    .findOptionalByFunctionalityAndPermission(authz.getFunctionality(), authz.getPermission())
                    .get();
            this.grantRepository.save(new Grant(group, authorization));
        });
    }

    /**
     *
     * @param group
     */
    @Transactional
    public void delete(Group group) {

        // prevent to delete the main admin
        if (group.isAdministratorsGroup()) {
            throw new BusinessLogicException("group.delete-administrator");
        }

        this.groupRepository.attachAndRemove(group);
    }

    /**
     *
     * @param authorization
     */
    @Transactional
    public void save(Authorization authorization) {
        this.authorizationRepository.saveAndFlush(authorization);
    }

    /**
     *
     * @param grant
     * @return
     */
    @Transactional
    public Grant save(Grant grant) {
        return this.grantRepository.save(grant);
    }

    /**
     *
     * @param grant
     */
    @Transactional
    public void update(Grant grant) {
        this.grantRepository.saveAndFlushAndRefresh(grant);
    }

    /**
     *
     * @param grant
     */
    @Transactional
    public void remove(Grant grant) {
        this.grantRepository.attachAndRemove(grant);
    }

    /**
     *
     * @param username
     * @return
     */
    @Override
    public Optional<UserDetails> findUserDetailsByUsername(String username) {

        final Optional<User> user =
                this.userRepository.findOptionalByUsername(username);

        return Optional.ofNullable(user.orElse(null));
    }
}