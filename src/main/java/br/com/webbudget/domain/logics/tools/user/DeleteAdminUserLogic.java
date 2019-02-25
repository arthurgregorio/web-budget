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
package br.com.webbudget.domain.logics.tools.user;

import br.com.webbudget.domain.entities.configuration.User;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.logics.BusinessLogic;
import br.com.webbudget.infrastructure.cdi.qualifiers.AuthenticatedUser;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 * {@link BusinessLogic} to validte if you are deleting the admin
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 09/08/2018
 */
@Dependent
public class DeleteAdminUserLogic implements UserDeletingLogic {

    @Inject
    @AuthenticatedUser
    private User principal;

    /**
     * {@inheritDoc }
     *
     * @param value
     */
    @Override
    public void run(User value) {

        final String principalUsername = this.principal.getUsername();

        // prevent to delete you own user 
        if (principalUsername.equals(value.getUsername())) {
            throw new BusinessLogicException("error.user.delete-principal");
        }

        // prevent to delete the main admin
        if (value.isAdministrator()) {
            throw new BusinessLogicException("error.user.delete-administrator");
        }
    }
}
