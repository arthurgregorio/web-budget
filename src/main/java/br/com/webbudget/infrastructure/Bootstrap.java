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
package br.com.webbudget.infrastructure;

import br.com.webbudget.domain.entities.tools.Authorization;
import br.com.webbudget.domain.entities.tools.Grant;
import br.com.webbudget.domain.entities.tools.Group;
import br.com.webbudget.domain.entities.tools.Permissions;
import br.com.webbudget.domain.entities.tools.User;
import br.com.webbudget.domain.repositories.tools.AuthorizationRepository;
import br.com.webbudget.domain.repositories.tools.GrantRepository;
import br.com.webbudget.domain.repositories.tools.GroupRepository;
import br.com.webbudget.domain.repositories.tools.UserRepository;
import br.eti.arthurgregorio.shiroee.auth.PasswordEncoder;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import org.slf4j.Logger;

/**
 * FIXME add migrations
 * FIXME create JavaDoc
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 1.0.0, 27/12/2017
 */
@Startup
@Singleton
public class Bootstrap {

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

    /**
     *
     */
    @PostConstruct
    protected void initialize() {

        this.logger.info("Bootstraping application....");

        this.createAuthorizations();
        this.createDefaultGroup();
        this.createDefaultUser();

        this.logger.info("Bootstraping finished...");
    }

    /**
     * Salva no banco as autorizacoes do sistema
     */
    private void createAuthorizations() {

        final List<Authorization> authorizations
                = this.permissions.toAuthorizationList();

        authorizations.stream().forEach(authorization -> {

            final Optional<Authorization> optionalAuthz = this.authorizationRepository
                    .findOptionalByFunctionalityAndPermission(authorization
                            .getFunctionality(), authorization.getPermission());

            if (!optionalAuthz.isPresent()) {
                this.authorizationRepository.save(authorization);
            }
        });
    }

    /**
     * Cria o grupo default do sistema
     */
    private void createDefaultGroup() {

        final Group group = this.groupRepository
                .findOptionalByName("Administradores")
                .orElseGet(() -> {
                    final Group newOne = new Group("Administradores");
                    return newOne;
                });

        if (!group.isSaved()) {

            this.logger.info("Creating default group");

            this.groupRepository.save(group);

            final List<Authorization> authorizations
                    = this.authorizationRepository.findAll();

            authorizations.stream().forEach(authorization -> {
                final Grant grant = new Grant(group, authorization);
                this.grantRepository.save(grant);
            });
        }
    }

    /**
     * Cria o usuario default do sistema
     */
    private void createDefaultUser() {

        final Optional<User> optionalUser = 
                this.userRepository.findOptionalByUsername("admin");

        if (!optionalUser.isPresent()) {
            
            this.logger.info("Creating default user");

            final Group group = this.groupRepository
                    .findOptionalByName("Administradores")
                    .get();
            
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
