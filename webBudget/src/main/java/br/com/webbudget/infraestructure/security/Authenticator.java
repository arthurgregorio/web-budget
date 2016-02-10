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
package br.com.webbudget.infraestructure.security;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.picketlink.annotations.PicketLink;
import org.picketlink.authentication.Authenticator.AuthenticationStatus;
import org.picketlink.authentication.BaseAuthenticator;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.credential.Credentials.Status;
import static org.picketlink.idm.credential.Credentials.Status.ACCOUNT_DISABLED;
import static org.picketlink.idm.credential.Credentials.Status.EXPIRED;
import static org.picketlink.idm.credential.Credentials.Status.VALID;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.credential.UsernamePasswordCredentials;
import org.slf4j.Logger;

/**
 * O autenticador do sistema, por ele realizamos o processo de autenticacao de 
 * um usuario atraves de suas credenciais informadas na tela de login
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.0.0, 26/06/2015
 */
@Named
@PicketLink
@ApplicationScoped
public class Authenticator extends BaseAuthenticator {

    @Inject
    private Logger logger;
    
    @Inject
    private CustomCredentials wbCredentials;
    
    @Inject
    private IdentityManager identityManager;
    
    /**
     * Autentica o usuario no banco de dados pelo modelo de seguranca
     */
    @Override
    public void authenticate() {

        final UsernamePasswordCredentials userCredentials = 
                new UsernamePasswordCredentials(this.wbCredentials.getUsername(),
                        new Password(this.wbCredentials.getPassword()));

        try {
            this.identityManager.validateCredentials(userCredentials);

            this.defineStatus(userCredentials.getStatus());
            
            if (this.getStatus() == AuthenticationStatus.SUCCESS) {
                this.setAccount(userCredentials.getValidatedAccount());
            } 
        } catch (Exception ex) {
            this.setStatus(AuthenticationStatus.FAILURE);
            logger.error("Error in an attempt to authenticate {}", 
                    this.wbCredentials.getUsername(), ex);
        }
    }
    
    /**
     * Define no contexto de segurancao pelo autenticador qual o status da 
     * autenticacao do usuario
     * 
     * @param status o status a ser checado para a autenticacao
     */
    private void defineStatus(Status status) {
       
        switch (status) {
            case ACCOUNT_DISABLED:
                this.setStatus(AuthenticationStatus.DEFERRED);
                break;
            case EXPIRED:
                this.setStatus(AuthenticationStatus.DEFERRED);
                break;
            case VALID:
                this.setStatus(AuthenticationStatus.SUCCESS);
                break;
            default:
                this.setStatus(AuthenticationStatus.FAILURE);
                break;
        }
    }
}
