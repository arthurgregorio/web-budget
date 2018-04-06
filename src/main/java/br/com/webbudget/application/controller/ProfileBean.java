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

import br.com.webbudget.domain.entities.security.User;
import br.com.webbudget.domain.services.UserAccountService;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 28/03/2018
 */
@Named
@ToString
@ViewScoped
public class ProfileBean extends AbstractBean {

    @Inject
    private UserSessionBean userSessionBean;
    
    @Inject
    private UserAccountService userAccountService;
    
    @Getter
    private PasswordChangeDTO passwordChangeDTO;
    
    /**
     * 
     */
    public void changePassword() {
        
        final User principal = this.userSessionBean.getPrincipal();
        
        this.userAccountService.changePasswordForCurrentUser(
                this.passwordChangeDTO, principal);
        
        this.passwordChangeDTO = new PasswordChangeDTO();
        this.addInfo(true, "profile.password-changed");
    }
    
    /**
     * 
     */
    public void showChangePasswordPopup() {
        this.passwordChangeDTO = new PasswordChangeDTO();
        this.updateAndOpenDialog("changePasswordDialog", "dialogChangePassword");
    }
    
    /**
     * 
     */
    public class PasswordChangeDTO {
        
        @Getter
        @Setter
        @NotBlank(message = "{profile.actual-password}")
        private String actualPassword;
        @Getter
        @Setter
        @NotBlank(message = "{profile.new-password}")
        private String newPassword;
        @Getter
        @Setter
        @NotBlank(message = "{profile.new-password-confirmation}")
        private String newPasswordConfirmation;

        /**
         * 
         * @return 
         */
        public boolean isNewPassMatching() {
            return this.newPassword.equals(newPasswordConfirmation);
        }
    }
}
