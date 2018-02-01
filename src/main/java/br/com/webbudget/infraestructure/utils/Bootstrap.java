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
package br.com.webbudget.infraestructure.utils;

import br.com.webbudget.domain.entities.security.Authorization;
import br.com.webbudget.domain.entities.security.Grant;
import br.com.webbudget.domain.entities.security.Group;
import br.com.webbudget.domain.entities.security.Permissions;
import br.com.webbudget.domain.repositories.tools.AuthorizationRepository;
import br.com.webbudget.domain.repositories.tools.GroupRepository;
import br.com.webbudget.domain.repositories.tools.UserRepository;
import br.com.webbudget.domain.services.UserAccountService;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import org.slf4j.Logger;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
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
    private UserAccountService userAccountService;
    
    @Inject
    private UserRepository userRepository;
    @Inject
    private GroupRepository groupRepository;
    @Inject
    private AuthorizationRepository authorizationRepository;
    
    /**
     * 
     */
    @PostConstruct
    protected void initialize() {
        
        this.logger.info("Bootstraping webbudget application....");
        
        this.createAuthorizations();
        this.createDefaultGroup();
        this.createDefaultUser();
        
        this.logger.info("Bootstraping finished...");
    }

    /**
     * Salva no banco as autorizacoes do sistema
     */
    private void createAuthorizations() {
       
        final List<Authorization> authorizations = 
                this.permissions.toAuthorizationList();
        
        authorizations.stream().forEach(authorization -> {
        
            final Optional<Authorization> optionalAuthz = this.authorizationRepository
                    .findOptionalByFunctionalityAndPermission(authorization
                            .getFunctionality(), authorization.getPermission());
            
            if (!optionalAuthz.isPresent()) {
                authorization.setIncludedBy("system");
                this.userAccountService.save(authorization);
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
                    newOne.setIncludedBy("system");
                    return newOne;
                });
        
        if (!group.isSaved()) {

            this.logger.info("Creating default group");
            
            this.userAccountService.save(group);
            
            final List<Authorization> authorizations = 
                    this.authorizationRepository.findAll();
            
            authorizations.stream().forEach(authorization -> {
                
                final Grant grant = new Grant(group, authorization);
                grant.setIncludedBy("system");
                
                this.userAccountService.save(grant);
            });
        }
    }

    /**
     * Cria o usuario default do sistema
     */
    private void createDefaultUser() {
        
//        final User user = this.userRepository.findOptionalByUsername("admin")
//                .orElseGet(() -> {
//                    final User newOne = new User();
//                    newOne.setName("Administrador");
//                    newOne.setEmail("contato@webbudget.com.br");
//                    newOne.setUsername("admin");
//                    newOne.setPassword("admin");
//                    newOne.setProfile(Profile.createDefault());
//                    newOne.setIncludedBy("system");
//                    return newOne;
//                });
//        
//        if (!user.isSaved()) {
//            
//            this.logger.info("Creating default user");
//            
//            final Group group = this.groupRepository
//                    .findOptionalByName("Administradores")
//                    .get();
//            
//            user.setGroup(group);
//            
//            this.userAccountService.save(user);
//        }
    }
}
