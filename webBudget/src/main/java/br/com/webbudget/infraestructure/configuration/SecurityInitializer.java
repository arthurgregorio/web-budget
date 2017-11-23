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
package br.com.webbudget.infraestructure.configuration;

/**
 * Classe de inicializacao do modelo de seguranca do sistema, por ela toda o
 * mecanismo de seguranca sera inicializado para uso no sistema
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.0.0, 21/05/2015
 */
//@Startup
//@Singleton
//public class SecurityInitializer {
//
//    @Inject
//    private Authorization authorization;
//    
//    @Inject
//    private AccountService accountService;
//    
//    @Inject
//    @PicketLink
//    private PartitionManager partitionManager;
//
//    private static final String DEFAULT_ADMIN_USER = "admin";
//    private static final String DEFAULT_ADMIN_PASSWORD = "admin";
//    private static final String DEFAULT_ADMIN_GROUP = "Administradores";
//
//    /**
//     * Carga inicial do modelo de seguranca da aplicacao
//     */
//    @PostConstruct
//    protected void initialize() {
//
//        // cria ou recupera do banco a particao
//        final Partition partition = this.checkPartition();
//
//        // cria o gestor de identidades
//        final IdentityManager identityManager = this.partitionManager
//                .createIdentityManager(partition);
//
//        final List<String> authorizations = this.authorization.listAuthorizations();
//        
//        // checamos se todas as roles estao dentro do sistema
//        for (String role : authorizations) {
//            if (this.accountService.findRoleByName(role) == null) {
//                identityManager.add(new Role(role));
//            }
//        }
//
//        // checamos se existe o grupo default
//        if (this.accountService.findGroupByName(DEFAULT_ADMIN_GROUP) == null) {
//            identityManager.add(new Group(DEFAULT_ADMIN_GROUP));
//        }
//
//        // checa se existe o usuario admin
//        if (this.accountService.findUserByUsername(DEFAULT_ADMIN_USER) == null) {
//
//            final User user = new User(DEFAULT_ADMIN_USER);
//
//            user.setName("Administrador");
//            user.setCreatedDate(new Date());
//            user.setEnabled(true);
//            user.setExpirationDate(null);
//            user.setEmail("admin@webbudget.com.br");
//            
//            identityManager.add(user);
//
//            identityManager.updateCredential(
//                    user, new Password(DEFAULT_ADMIN_PASSWORD));
//
//            // setamos agora as permissoes no grupo
//            final Group group = this.accountService.findGroupByName(DEFAULT_ADMIN_GROUP);
//
//            // criamos um gerenciador de relacionamentos
//            final RelationshipManager relationshipManager
//                    = this.partitionManager.createRelationshipManager();
//
//            final List<Role> roles = this.accountService.listRoles();
//            
//            // adicionamos no grupo, todas as roles do sistema
//            for (Role role : roles) {
//                relationshipManager.add(new Grant(role, group));
//            }
//
//            // garantimos ao admin que ele faz parte do grupo administradores
//            relationshipManager.add(new GroupMembership(group, user));
//        }
//    }
//
//    /**
//     * Checamos se a particao de default de seguranca foi criada, se nao criamos
//     */
//    private Partition checkPartition() {
//
//        Partition partition = this.partitionManager.getPartition(
//                Partition.class, Partition.DEFAULT);
//
//        if (partition == null) {
//            partition = new Partition(Partition.DEFAULT);
//            this.partitionManager.add(partition, "jpa.config");
//        }
//
//        return partition;
//    }
//}
