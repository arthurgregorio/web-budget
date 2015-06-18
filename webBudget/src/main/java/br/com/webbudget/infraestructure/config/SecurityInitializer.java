/*
 * Copyright (C) 2015 Arthur Gregorio, AG.Software
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
package br.com.webbudget.infraestructure.config;

import br.com.webbudget.domain.security.Authorization;
import br.com.webbudget.domain.security.Grant;
import br.com.webbudget.domain.security.Group;
import br.com.webbudget.domain.security.GroupMembership;
import br.com.webbudget.domain.security.Partition;
import br.com.webbudget.domain.security.Role;
import br.com.webbudget.domain.security.User;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.query.IdentityQuery;
import org.picketlink.idm.query.IdentityQueryBuilder;
import org.slf4j.Logger;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.0.0, 21/05/2015
 */
@Startup
@Singleton
public class SecurityInitializer {

    private IdentityManager identityManager;

    @Inject
    private Logger logger;
    @Inject
    private Authorization authorization;
    @Inject
    private PartitionManager partitionManager;

    /**
     * Carga inicial do modelo de seguranca da aplicacao
     */
    @PostConstruct
    protected void initialize() {

        // cria ou recupera do banco a particao
        final Partition partition = this.checkForDefaultParition();

        // cria o gestor de identidades
        this.identityManager = this.partitionManager.createIdentityManager(partition);

        // checa pelos outros elementos do modelo de seguranca
        this.checkForDefaultGroups();
        this.checkForDefaultRoles();
        this.checkForDefaultUsers();
    }

    /**
     * Checamos se a particao de default de seguranca foi criada, se nao criamos
     */
    private Partition checkForDefaultParition() {

        Partition partition = this.partitionManager.getPartition(
                Partition.class, Partition.DEFAULT);

        if (partition == null) {

            this.logger.info("Creating jpa default realm");

            partition = new Partition(Partition.DEFAULT);

            this.partitionManager.add(partition, "jpa.config");
        }

        return partition;
    }

    /**
     * Criamos as roles padrao, caso nao exista
     */
    private void checkForDefaultRoles() {

        final IdentityQueryBuilder queryBuilder = this.identityManager.getQueryBuilder();
        final IdentityQuery<Role> query = queryBuilder.createIdentityQuery(Role.class);

        final Set<String> authorizations = this.authorization.getAllAuthorizations();

        for (String key : authorizations) {

            query.where(queryBuilder.equal(Role.NAME, key));

            final List<Role> roles = query.getResultList();

            if (roles.isEmpty()) {
                this.identityManager.add(new Role(key));
            }
        }
    }
    
    /**
     * Criamos o grupo padrao, caso nao exista
     */
    private void checkForDefaultGroups() {

        final IdentityQueryBuilder queryBuilder = this.identityManager.getQueryBuilder();
        final IdentityQuery<Group> query = queryBuilder.createIdentityQuery(Group.class);

        query.where(queryBuilder.equal(Group.NAME, "Administradores"));

        final List<Group> groups = query.getResultList();

        if (groups.isEmpty()) {
            
            final Group group = new Group();
            
            group.setName("Administradores");
            
            this.identityManager.add(group);
            
            
        }
    }

    /**
     * Checamos pelo usuario admin, se ele nao existir criamos
     */
    private void checkForDefaultUsers() {

        final IdentityQueryBuilder queryBuilder = this.identityManager.getQueryBuilder();
        final IdentityQuery<User> query = queryBuilder.createIdentityQuery(User.class);

        query.where(queryBuilder.equal(User.USER_NAME, "admin"));

        final List<User> users = query.getResultList();

        if (users.isEmpty()) {

            this.logger.info("Creating default users");

            final User user = new User("admin");

            user.setName("Administrador");
            user.setCreatedDate(new Date());
            user.setEnabled(true);
            user.setExpirationDate(null);
            user.setEmail("admin@webbudget.com");

            this.identityManager.add(user);

            this.identityManager.updateCredential(user, new Password("admin"));

            final IdentityQuery<Group> queryGrop = queryBuilder.createIdentityQuery(Group.class);

            queryGrop.where(queryBuilder.equal(Group.NAME, "Administradores"));

            final Group group = queryGrop.getResultList().get(0);

            // adicionamos ele na role de administrador
            final RelationshipManager relationshipManager
                    = this.partitionManager.createRelationshipManager();

            final IdentityQueryBuilder roleQueryBuilder = this.identityManager.getQueryBuilder();
            final IdentityQuery<Role> roleQuery = queryBuilder.createIdentityQuery(Role.class);

            roleQuery.where(roleQueryBuilder.equal(Role.NAME, ApplicationRoles.ADMINISTRATOR));

            final Role administratorRole = roleQuery.getResultList().get(0);

            relationshipManager.add(new Grant(administratorRole, group));
            relationshipManager.add(new GroupMembership(group, user));
        }
    }
}
