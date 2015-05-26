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

import javax.enterprise.context.ApplicationScoped;
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
 * @version 1.2.0
 * @since 1.1.0, 07/03/2015
 */
@ApplicationScoped
public class SecurityConfiguration {

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
                            .supportType(User.class)
                            .supportAllFeatures()
                        .addContextInitializer(this.contextInitializer)
                        .setCredentialHandlerProperty(
                                PasswordCredentialHandler.PASSWORD_ENCODER, 
                                new BCryptPasswordEncoder(9));
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
                .forPath("/logout.xhtml")
                    .logout()
                    .redirectTo("/home.xhtml?faces-redirect=true")
                .forPath("/javax.faces.resource/*")
                    .unprotected();
        
//        http
//            .csrf()
//                .disable()
//            .formLogin()
//                .loginPage("/home.xhtml")
//                .loginProcessingUrl("/loginSpring")
//                .failureUrl("/home.xhtml?failure=true")
//                .defaultSuccessUrl("/main/dashboard.xhtml?faces-redirect=true")
//                .permitAll()
//            .and()
//            .authorizeRequests()
//                .anyRequest()
//                    .authenticated()
//                .antMatchers("/main/entries/cards/**")
//                    .hasRole(authority.CARD_VIEW)
//                .antMatchers("/main/entries/contacts/**")
//                    .hasRole(authority.CONTACT_VIEW)
//                .antMatchers("/main/entries/costCenter/**")
//                    .hasRole(authority.COST_CENTER_VIEW)
//                .antMatchers("/main/entries/movementClass/**")
//                    .hasRole(authority.MOVEMENT_CLASS_VIEW)
//                .antMatchers("/main/entries/wallets/**")
//                    .hasRole(authority.WALLET_VIEW)
//                .antMatchers("/main/financial/cardInvoice/**")
//                    .hasRole(authority.CARD_INVOICE_VIEW)
//                .antMatchers("/main/financial/movement/**")
//                    .hasRole(authority.MOVEMENT_VIEW)
//                .antMatchers("/main/financial/transfer/**")
//                    .hasRole(authority.BALANCE_TRANSFER_VIEW)
//                .antMatchers("/main/miscellany/closing/**")
//                    .hasRole(authority.CLOSING_VIEW)
//                .antMatchers("/main/miscellany/financialPeriod/**")
//                    .hasRole(authority.FINANCIAL_PERIOD_VIEW)
//                .antMatchers("/main/tools/user/**")
//                    .hasRole(authority.ACCOUNTS_VIEW)
//                .antMatchers("/main/tools/configurations/**")
//                    .hasRole(authority.CONFIGURATION_VIEW)
//                .antMatchers("/main/tools/privateMessage/**")
//                    .hasRole(authority.PRIVATE_MESSAGES_VIEW)
//            .and()
//            .logout()
//                .invalidateHttpSession(true)
//                .logoutUrl("/main/logout.xhtml")
//                .logoutSuccessUrl("/home.xhtml?faces-redirect=true")
//                .permitAll();
    }
}
