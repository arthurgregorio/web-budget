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

import br.com.webbudget.application.controller.configuration.ProfileBean.PasswordChangeDTO;
import br.com.webbudget.domain.entities.configuration.*;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.logics.tools.group.GroupDeletingLogic;
import br.com.webbudget.domain.logics.tools.user.UserDeletingLogic;
import br.com.webbudget.domain.logics.tools.user.UserSavingLogic;
import br.com.webbudget.domain.logics.tools.user.UserUpdatingLogic;
import br.com.webbudget.domain.repositories.configuration.*;
import br.eti.arthurgregorio.shiroee.auth.PasswordEncoder;
import br.eti.arthurgregorio.shiroee.config.jdbc.UserDetails;
import br.eti.arthurgregorio.shiroee.config.jdbc.UserDetailsProvider;
import org.apache.shiro.authc.UnknownAccountException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

/**
 * The service to manage all the operations of the {@link User} account control and the {@link Group}, {@link Grant} or
 * {@link Authorization} of the application
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 2.0.0, 27/12/2017
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
    private ProfileRepository profileRepository;
    @Inject
    private AuthorizationRepository authorizationRepository;

    @Any
    @Inject
    private Instance<UserSavingLogic> userSavingBusinessLogics;
    @Any
    @Inject
    private Instance<UserUpdatingLogic> userUpdatingBusinessLogics;
    @Any
    @Inject
    private Instance<UserDeletingLogic> userDeletingBusinessLogics;

    @Any
    @Inject
    private Instance<GroupDeletingLogic> groupDeletingBusinessLogics;

    /**
     * Persist a new {@link User}
     *
     * @param user the {@link User} to be persisted
     * @return the persisted {@link User}
     */
    @Transactional
    public User save(User user) {
        this.userSavingBusinessLogics.forEach(logic -> logic.run(user));
        return this.userRepository.save(user);
    }

    /**
     * Update an already persisted {@link User}
     *
     * @param user the {@link User} to be updated
     */
    @Transactional
    public void update(User user) {
        this.userUpdatingBusinessLogics.forEach(logic -> logic.run(user));
        this.userRepository.saveAndFlushAndRefresh(user);
    }

    /**
     * Delete a persisted {@link User}
     *
     * @param user the {@link User} to be deleted
     */
    @Transactional
    public void delete(User user) {
        this.userDeletingBusinessLogics.forEach(logic -> logic.run(user));
        this.userRepository.attachAndRemove(user);
    }

    /**
     * Use this method to change the password of a given {@link User}
     *
     * @param passwordChangeDTO the {@link PasswordChangeDTO} with the new values
     * @param user the {@link User} to be updated
     */
    @Transactional
    public void changePassword(PasswordChangeDTO passwordChangeDTO, User user) {

        final boolean actualMatch = this.passwordEncoder.passwordsMatch(
                passwordChangeDTO.getActualPassword(), user.getPassword());

        if (actualMatch) {
            if (passwordChangeDTO.isNewPassMatching()) {
                user.setPassword(this.passwordEncoder.encryptPassword(passwordChangeDTO.getNewPassword()));
                this.userRepository.saveAndFlushAndRefresh(user);
                return;
            }
            throw new BusinessLogicException("error.change-password.new-pass-not-match");
        }
        throw new BusinessLogicException("error.change-password.actual-pass-not-match");
    }

    /**
     * Persist a new {@link Group}
     *
     * @param group the {@link Group} to be persisted
     * @return the persisted {@link Group}
     */
    @Transactional
    public Group save(Group group) {
        return this.groupRepository.save(group);
    }

    /**
     * Persist a new {@link Group} along with his {@link Authorization}
     *
     * @param group the {@link Group}
     * @param authorizations the list of {@link Authorization} of this group
     */
    @Transactional
    public void save(Group group, List<Authorization> authorizations) {
        this.groupRepository.save(group);
        authorizations.forEach(auth -> this.authorizationRepository
                .findByFunctionalityAndPermission(auth.getFunctionality(), auth.getPermission())
                .ifPresent(authorization -> this.grantRepository.save(new Grant(group, authorization)))
        );
    }

    /**
     * Update an already persisted {@link Group}
     *
     * @param group the {@link Group} to be updated
     */
    @Transactional
    public void update(Group group) {
        this.groupRepository.saveAndFlushAndRefresh(group);
    }

    /**
     * Update an already persisted {@link Group} and his {@link Authorization}
     *
     * @param group the {@link Group} to be update
     * @param authorizations the new {@link List} of {@link Authorization} of this {@link Group}
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
        authorizations.forEach(auth ->
                this.authorizationRepository
                        .findByFunctionalityAndPermission(auth.getFunctionality(), auth.getPermission())
                        .ifPresent(authorization -> this.grantRepository.save(new Grant(group, authorization)))
        );
    }

    /**
     * Delete an already persisted {@link Group}
     *
     * @param group the {@link Group} to be deleted
     */
    @Transactional
    public void delete(Group group) {
        this.groupDeletingBusinessLogics.forEach(logic -> logic.run(group));
        this.groupRepository.attachAndRemove(group);
    }

    /**
     * Update the {@link User} {@link Profile}
     *
     * @param profile the {@link Profile} to be updated
     * @return the update {@link Profile}
     */
    @Transactional
    public Profile updateUserProfile(Profile profile) {
        return this.profileRepository.saveAndFlushAndRefresh(profile);
    }

    /**
     *
     * @param username
     * @return
     * @throws UnknownAccountException
     */
    @Override
    public UserDetails findByUsername(String username) throws UnknownAccountException {
        return this.userRepository.findByUsername(username).orElseThrow(UnknownAccountException::new);
    }
}