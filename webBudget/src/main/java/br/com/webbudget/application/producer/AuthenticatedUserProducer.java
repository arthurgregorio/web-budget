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
package br.com.webbudget.application.producer;

import br.com.webbudget.application.producer.qualifier.AuthenticatedUser;
import br.com.webbudget.domain.security.User;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import org.picketlink.Identity;

/**
 * Produtor que captura do contexto de seguranca o nosso usuario logado. Por 
 * esta classe podemos produzir instancias de nosso usuario logado sem a neces-
 * sidade de chamar o {@link Identity} do PL
 * 
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.0.0, 22/06/2015
 */
public class AuthenticatedUserProducer {

    @Inject
    private Identity identity;

    /**
     * @return o usuario logado no sistema
     */
    @Produces
    @RequestScoped
    @AuthenticatedUser
    User produceUser() {
        return (User) this.identity.getAccount();
    }
}
