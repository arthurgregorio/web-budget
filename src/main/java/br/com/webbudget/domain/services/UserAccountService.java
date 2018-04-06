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
import br.com.webbudget.domain.entities.security.Authorization;
import br.com.webbudget.domain.entities.security.Grant;
import br.com.webbudget.domain.entities.security.Group;
import br.com.webbudget.domain.entities.security.StoreType;
import br.com.webbudget.domain.entities.security.User;
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
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
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
            throw new BusinessLogicException("error.user.email-duplicated");
        }
        
        // validate the username
        final Optional<User> usernameOptional = this.userRepository
                .findOptionalByUsername(user.getUsername());
        
        if (usernameOptional.isPresent()) {
            throw new BusinessLogicException("error.user.username-duplicated");
        }

        // if the user is local...
        if (user.getStoreType() == StoreType.LOCAL) {
            
            if (!user.isPasswordValid()) {
                throw new BusinessLogicException("error.user.password-not-match-or-invalid");
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
        final Optional<User> emailOptional = this.userRepository
                .findOptionalByEmail(user.getEmail());
        
        if (emailOptional.isPresent()) {
            
            final User found = emailOptional.get();
            
            if (!found.getUsername().equals(user.getUsername())) {
                throw new BusinessLogicException("error.user.email-duplicated");
            }
        }
        
        // if the user is local...
        if (user.getStoreType() == StoreType.LOCAL) {

            if (user.hasChangedPasswords()) {

                // check if passwords match
                if (!user.isPasswordValid()) {
                    throw new BusinessLogicException("error.user.password-not-match");
                }

                // crypt the user password
                user.setPassword(this.passwordEncoder.encryptPassword(
                        user.getPassword()));            
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
            throw new BusinessLogicException("error.user.delete-principal");
        }
        
        // prevent to delete the main admin
        if (user.isAdministrator()) {
            throw new BusinessLogicException("error.user.delete-administrator");
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
        
        final boolean actualsMatch = this.passwordEncoder.passwordsMatch(
                passwordChangeDTO.getActualPassword(), user.getPassword());

        if (actualsMatch) {
            
            if (passwordChangeDTO.isNewPassMatching()) {
                
                final String newPass = this.passwordEncoder.encryptPassword(
                        passwordChangeDTO.getNewPassword());
                
                user.setPassword(newPass);
                
                this.userRepository.saveAndFlushAndRefresh(user);
                
                return;
            }            
            throw new BusinessLogicException("error.profile.new-pass-not-match");        
        }
        throw new BusinessLogicException("error.profile.actual-pass-not-match");        
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
            final Authorization authorization = this.authorizationRepository
                    .findOptionalByFunctionalityAndPermission(
                            authz.getFunctionality(), authz.getPermission())
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

        // lista o grants antigos para deletar
        final List<Grant> oldGrants = this.grantRepository.findByGroup(group);

        oldGrants.stream().forEach(grant -> {
            this.grantRepository.remove(grant);
        });

        // grava os novos
        authorizations.stream().forEach(authz -> {

            Authorization authorization = this.authorizationRepository
                    .findOptionalByFunctionalityAndPermission(
                            authz.getFunctionality(), authz.getPermission())
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
     * @param authorizations
     * @param group
     */
    @Transactional
    public void grantAll(List<Authorization> authorizations, Group group) {
        authorizations.stream().forEach(authz -> {
            this.grantRepository.save(new Grant(group, authz));
        });
    }

    /**
     *
     * @param username
     * @return
     */
    public User findUserByUsername(String username) {
        return this.userRepository.findOptionalByUsername(username)
                .orElse(null);
    }
    
    /**
     * 
     * @param username
     * @return 
     */
    @Override
    public Optional<UserDetails> findUserDetailsByUsername(String username) {
        return Optional.ofNullable(this.userRepository.findOptionalByUsername(username)
                .orElse(null));
    }
}
