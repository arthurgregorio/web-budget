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
package br.com.webbudget.application.controller;

import br.com.webbudget.application.components.ui.AbstractBean;
import br.com.webbudget.domain.services.RecoverPasswordService;
import lombok.Getter;
import lombok.Setter;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Recover password controller
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 29/12/2017
 */
@Named
@ViewScoped
public class RecoverPasswordBean extends AbstractBean {

    @Getter
    @Setter
    private String email;

    @Inject
    private RecoverPasswordService recoverPasswordService;

    /**
     * Call the service to reset the password
     */
    public void recoverPassword() {

        this.recoverPasswordService.recover(this.email);

        this.closeDialog("dialogRecoverPassword");
        this.addInfoAndKeep("recover-password.email-sent");
    }

    /**
     * Open the recover password dialog
     */
    public void showRecoverPassDialog() {
        this.email = null;
        this.updateAndOpenDialog("recoverPasswordDialog", "dialogRecoverPassword");
    }
}