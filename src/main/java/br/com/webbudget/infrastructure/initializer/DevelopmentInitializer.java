/*
 * Copyright (C) 2018 Arthur Gregorio, AG.Software
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
package br.com.webbudget.infrastructure.initializer;

import br.com.webbudget.domain.entities.tools.*;
import br.com.webbudget.domain.repositories.tools.AuthorizationRepository;
import br.com.webbudget.domain.repositories.tools.GrantRepository;
import br.com.webbudget.domain.repositories.tools.GroupRepository;
import br.com.webbudget.domain.repositories.tools.UserRepository;
import br.eti.arthurgregorio.shiroee.auth.PasswordEncoder;
import org.apache.deltaspike.core.api.exclude.Exclude;
import org.apache.deltaspike.core.api.projectstage.ProjectStage.Production;
import org.slf4j.Logger;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.UserTransaction;
import java.util.List;
import java.util.Optional;

import static org.apache.deltaspike.core.api.projectstage.ProjectStage.SystemTest;

/**
 * The development {@link EnvironmentInitializer}
 *
 * Create the default data to the app and is meant to be used only in development, for production initialization use the
 * {@link ProductionInitializer} with Migration from Flyway
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 27/12/2017
 */
@RequestScoped
@Exclude(ifProjectStage = {Production.class, SystemTest.class})
public class DevelopmentInitializer implements EnvironmentInitializer {

    @Inject
    private Logger logger;

    @Inject
    private Permissions permissions;

    @Inject
    private UserRepository userRepository;
    @Inject
    private GrantRepository grantRepository;
    @Inject
    private GroupRepository groupRepository;
    @Inject
    private AuthorizationRepository authorizationRepository;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserTransaction transaction;

    /**
     * {@inheritDoc }
     */
    @Override
    public void initialize() {

        this.logger.warn("Initializing application in development mode");

        try {
            this.transaction.begin();

            this.createAuthorizations();
            this.createDefaultGroup();
            this.createDefaultUser();

            this.transaction.commit();
        } catch (Exception commitException) {
            try {
                this.transaction.rollback();
            } catch (Exception rollbackException) {
                throw new EJBException(rollbackException);
            }
            throw new EJBException(commitException);
        }
    }

    /**
     * Sync the authorizations with the database
     */
    private void createAuthorizations() {

        final List<Authorization> authorizations
                = this.permissions.toAuthorizationList();

        authorizations.forEach(authorization -> {

            final Optional<Authorization> optionalAuthz = this.authorizationRepository
                    .findOptionalByFunctionalityAndPermission(authorization
                            .getFunctionality(), authorization.getPermission());

            if (!optionalAuthz.isPresent()) {
                this.authorizationRepository.save(authorization);
            }
        });
    }

    /**
     * Create the default user group
     */
    private void createDefaultGroup() {

        final Group group = this.groupRepository
                .findOptionalByName("Administradores")
                .orElseGet(() -> new Group("Administradores"));

        if (!group.isSaved()) {

            this.logger.info("Creating default group");

            this.groupRepository.save(group);

            final List<Authorization> authorizations
                    = this.authorizationRepository.findAll();

            authorizations.forEach(authorization -> {
                this.grantRepository.save(new Grant(group, authorization));
            });
        }
    }

    /**
     * Create the default system user
     */
    private void createDefaultUser() {

        final Optional<User> optionalUser =
                this.userRepository.findOptionalByUsername("admin");

        if (!optionalUser.isPresent()) {

            this.logger.info("Creating default user");

            final Group group = this.groupRepository
                    .findOptionalByName("Administradores")
                    .orElseThrow(() -> new IllegalStateException("Can't find the Administrators group"));

            final User user = new User();

            user.setName("Administrador");
            user.setEmail("contato@webbudget.com.br");
            user.setUsername("admin");
            user.setPassword(this.passwordEncoder.encryptPassword("admin"));

            user.setGroup(group);

            this.userRepository.save(user);
        }
    }
}
