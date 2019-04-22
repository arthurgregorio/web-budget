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
package br.com.webbudget.infrastructure.shiro;

import br.com.webbudget.domain.entities.configuration.Permissions;
import br.eti.arthurgregorio.shiroee.config.HttpSecurityConfiguration;
import br.eti.arthurgregorio.shiroee.config.http.HttpSecurityBuilder;
import br.eti.arthurgregorio.shiroee.config.http.PermissionHttpSecurityBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * The implementation of the {@link HttpSecurityConfiguration} for this project
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 06/03/2018
 */
@ApplicationScoped
public class PathSecurityConfiguration implements HttpSecurityConfiguration {

    @Inject
    private Permissions permissions;

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public HttpSecurityBuilder configureHttpSecurity() {

        final HttpSecurityBuilder builder = new PermissionHttpSecurityBuilder();

        builder.add("/secured/configuration/user/**", this.permissions.getUSER_ACCESS(), true)
                .add("/secured/configuration/group/**", this.permissions.getGROUP_ACCESS(), true)
                .add("/secured/configuration/configuration/**", this.permissions.getCONFIGURATION_ACCESS(), true)
                .add("/secured/registration/card/**", this.permissions.getCARD_ACCESS(), true)
                .add("/secured/registration/vehicle/**", this.permissions.getVEHICLE_ACCESS(), true)
                .add("/secured/registration/contact/**", this.permissions.getCONTACT_ACCESS(), true)
                .add("/secured/registration/wallet/**", this.permissions.getWALLET_ACCESS(), true)
                .add("/secured/registration/costCenter/**", this.permissions.getCOST_CENTER_ACCESS(), true)
                .add("/secured/registration/financialPeriod/**", this.permissions.getFINANCIAL_PERIOD_ACCESS(), true)
                .add("/secured/registration/movementClass/**", this.permissions.getMOVEMENT_CLASS_ACCESS(), true)
                .add("/secured/journal/refueling/**", this.permissions.getREFUELING_ACCESS(), true)
                .add("/secured/financial/movement/fixed/**", this.permissions.getPERIOD_MOVEMENT_ACCESS(), true)
                .add("/secured/financial/movement/period/**", this.permissions.getFIXED_MOVEMENT_ACCESS(), true)
                .add("/secured/financial/closing/**", this.permissions.getCLOSING_ACCESS(), true)
                .add("/secured/financial/transference/**", this.permissions.getTRANSFERENCE_ACCESS(), true)
                .add("/secured/financial/creditCardInvoice/**", this.permissions.getCREDIT_CARD_INVOICE_ACCESS(), true);

        return builder;
    }
}
