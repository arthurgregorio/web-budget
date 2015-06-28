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
package br.com.webbudget.application.controller;

import br.com.webbudget.domain.security.Authorization;
import br.com.webbudget.domain.security.Role;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import org.picketlink.Identity;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.basic.BasicModel;
import static org.picketlink.idm.model.basic.BasicModel.getRole;

/**
 * Bean utlizado pelo sistema para requisitar as authorities disponiveis no
 * sistemas
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.0.0, 27/06/2015
 */
@Named
@ApplicationScoped
public class AuthorizationBean {

    @Getter
    @Inject
    private Authorization authorization;
    
    @Inject
    private Instance<Identity> identityInstance;

    /**
     * 
     * @return 
     */
    private Identity getIdentity() {
        return this.identityInstance.get();
    }

    /**
     * Checa pela role do usuario logado
     *
     * @param applicationRole
     * @return
     */
    private boolean hasRole(String roleName) {
        
        
        return BasicModel.hasRole(this.relationshipManager, account, role);
    }
}
