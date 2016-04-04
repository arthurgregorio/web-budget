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
package br.com.webbudget.infraestructure.picketlink;

import br.com.webbudget.application.controller.UserSessionBean;
import br.com.webbudget.domain.model.security.Role;
import javax.enterprise.inject.spi.CDI;
import org.picketlink.idm.internal.ContextualRelationshipManager;
import org.picketlink.idm.internal.DefaultPartitionManager;
import org.picketlink.idm.model.IdentityType;

/**
 * Implementacao customizada da relationshipmanager para que o metodo de 
 * checagem da heranca entre os grants de roles para grupos seja invocado 
 * direcionando para um metodo customizado do dominio da aplicacao
 * 
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.1.2, 23/12/2015
 */
public class CustomRelationshipManager extends ContextualRelationshipManager {

    private UserSessionBean userSessionBean;
    
    /**
     * @see ContextualRelationshipManager(org.picketlink.idm.internal.DefaultPartitionManager) 
     * 
     * @param partitionManager 
     */
    public CustomRelationshipManager(DefaultPartitionManager partitionManager) {
        super(partitionManager);
    }

    /**
     * @see #inheritsPrivileges(org.picketlink.idm.model.IdentityType, org.picketlink.idm.model.IdentityType) 
     * 
     * @param identity
     * @param assignee
     * @return 
     */
    @Override
    public boolean inheritsPrivileges(IdentityType identity, IdentityType assignee) {
        if (assignee instanceof Role) {
            final Role role = (Role) assignee;
            return this.getUserSessionBean().hasRole(role.getAuthorization());
        }
        return false;
    }
    
    /**
     * @return a instancia do gerenciador de permissoes do usuario
     */
    private UserSessionBean getUserSessionBean() {
        if (this.userSessionBean == null) {
            this.userSessionBean = CDI
                    .current()
                    .select(UserSessionBean.class)
                    .get();
        } 
        return this.userSessionBean;
    }
}
