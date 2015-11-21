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

import br.com.webbudget.domain.entity.security.GrantTypeEntity;
import br.com.webbudget.domain.entity.security.GroupMembershipTypeEntity;
import br.com.webbudget.domain.entity.security.GroupTypeEntity;
import br.com.webbudget.domain.entity.security.PartitionTypeEntity;
import br.com.webbudget.domain.entity.security.PasswordTypeEntity;
import br.com.webbudget.domain.entity.security.RelationshipIdentityTypeEntity;
import br.com.webbudget.domain.entity.security.RelationshipTypeEntity;
import br.com.webbudget.domain.entity.security.RoleTypeEntity;
import br.com.webbudget.domain.entity.security.UserTypeEntity;
import br.com.webbudget.domain.security.Grant;
import br.com.webbudget.domain.security.Authorization;
import br.com.webbudget.domain.security.Group;
import br.com.webbudget.domain.security.GroupMembership;
import br.com.webbudget.domain.security.Partition;
import br.com.webbudget.domain.security.Role;
import br.com.webbudget.domain.security.User;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import org.picketlink.config.SecurityConfigurationBuilder;
import org.picketlink.event.SecurityConfigurationEvent;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.credential.encoder.BCryptPasswordEncoder;
import org.picketlink.idm.credential.handler.PasswordCredentialHandler;
import org.picketlink.internal.EntityManagerContextInitializer;

/**
 * Configura toda infra de seguranca do sistema atraves do spring security
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 1.1.0, 07/03/2015
 */
public class SecurityConfiguration {

    @Inject
    private Authorization authorization;
    @Inject
    private EntityManagerContextInitializer contextInitializer;
    
    /**
     * Configura o contexto de seguranca do picketlink atraves do evento de 
     * inicializacao do {@link IdentityManager}
     * 
     * @param event o evento de configuracao
     */
    public void configureInternal(@Observes SecurityConfigurationEvent event) {

        final SecurityConfigurationBuilder builder = event.getBuilder();
        
        builder.idmConfig()
                .named("jpa.config")
                .stores()
                .jpa()
                .supportType(
                        User.class,
                        Role.class,
                        Group.class,
                        Partition.class)
                .supportGlobalRelationship(  
                        Grant.class,  
                        GroupMembership.class)  
                .supportCredentials(true)
                .mappedEntity(
                        RoleTypeEntity.class,
                        UserTypeEntity.class,
                        GrantTypeEntity.class,
                        GroupTypeEntity.class,
                        PasswordTypeEntity.class,
                        PartitionTypeEntity.class,
                        RelationshipTypeEntity.class,
                        GroupMembershipTypeEntity.class,
                        RelationshipIdentityTypeEntity.class)
                .addContextInitializer(this.contextInitializer)
                .setCredentialHandlerProperty(
                        PasswordCredentialHandler.PASSWORD_ENCODER, 
                        new BCryptPasswordEncoder(10));
    }
    
    /**
     * Configuracao das regras de navegacao HTTP do sistema atraves do evento
     * de configuracado do picketlink
     * 
     * @param event o evento de configuracao
     */
    public void configureHttpSecurity(@Observes SecurityConfigurationEvent event) {
        
        final SecurityConfigurationBuilder builder = event.getBuilder();

        builder.http()
                .allPaths()
                    .authenticateWith()
                    .form()
                        .loginPage("/home.xhtml")
                        .errorPage("/home.xhtml?failure=true")
                .forPath("/logout")
                    .logout()
                    .redirectTo("/home.xhtml?faces-redirect=true")
                .forPath("/javax.faces.resource/*")
                    .unprotected()
                .forPath("/favicon.ico*")
                    .unprotected()
                .forPath("/main/entries/cards/*")
                    .authorizeWith().role(this.authorization.CARD_VIEW)
                .forPath("/main/entries/contacts/*")
                    .authorizeWith().role(this.authorization.CONTACT_VIEW)
                .forPath("/main/entries/costCenter/*")
                    .authorizeWith().role(this.authorization.COST_CENTER_VIEW)
                .forPath("/main/financial/movement/*")
                    .authorizeWith().role("authority.movement.access")
                .forPath("/main/entries/wallets/*")
                    .authorizeWith().role(this.authorization.WALLET_VIEW)
                .forPath("/main/financial/cardInvoice/*")
                    .authorizeWith().role(this.authorization.CARD_INVOICE_VIEW)
                .forPath("/main/entries/movementClass/*")
                    .authorizeWith().role(this.authorization.MOVEMENT_CLASS_VIEW)
                .forPath("/main/financial/transfer/*")
                    .authorizeWith().role(this.authorization.BALANCE_TRANSFER_VIEW)
                .forPath("/main/miscellany/closing/*")
                    .authorizeWith().role(this.authorization.CLOSING_VIEW)
                .forPath("/main/miscellany/financialPeriod/*")
                    .authorizeWith().role(this.authorization.FINANCIAL_PERIOD_VIEW)
                .forPath("/main/tools/user/*")
                    .authorizeWith().role(this.authorization.USER_VIEW)
                .forPath("/main/tools/group/*")
                    .authorizeWith().role(this.authorization.GROUP_VIEW)
                .forPath("/main/tools/configurations/*")
                    .authorizeWith().role(this.authorization.CONFIGURATION_VIEW)
                .forPath("/main/tools/privateMessage/*")
                    .authorizeWith().role(this.authorization.PRIVATE_MESSAGE_VIEW);
    }
}
