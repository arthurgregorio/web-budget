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
package br.com.webbudget.application.controller.configuration;

import br.com.webbudget.application.components.ui.AbstractBean;
import br.com.webbudget.application.controller.UserSessionBean;
import br.com.webbudget.domain.entities.configuration.Profile;
import br.com.webbudget.domain.entities.configuration.ThemeType;
import br.com.webbudget.domain.entities.configuration.User;
import br.com.webbudget.domain.services.UserAccountService;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotBlank;

/**
 * The controller for the {@link User} {@link Profile} area
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 28/03/2018
 */
@Named
@ViewScoped
public class ProfileBean extends AbstractBean {

    @Getter
    private Profile profile;

    @Inject
    private UserSessionBean userSessionBean;

    @Inject
    private UserAccountService userAccountService;

    @Getter
    private PasswordChangeDTO passwordChangeDTO;

    /**
     * Initialize this bean with the current user preferences
     */
    @PostConstruct
    public void initialize() {
        this.profile = this.userSessionBean.getPrincipal().getProfile();
    }

    /**
     * Update the user profile
     */
    public void updateProfile() {
        this.profile = this.userAccountService.updateUserProfile(this.profile);
    }

    /**
     * Method used to dynamically change the user interface theme
     *
     * @param themeType the selected theme
     */
    public void changeTheme(ThemeType themeType) {
        this.profile.setActiveTheme(themeType);
        this.executeScript("changeSkin('" + this.profile.getActiveTheme().getValue() + "')");
    }

    /**
     * Use this method to get the current theme of the user preferences
     *
     * @return the current selected theme
     */
    public String getCurrentTheme() {
        return this.profile.getActiveTheme().getValue();
    }

    /**
     * Return the current theme color name
     *
     * @return the color name
     */
    public String getCurrentThemeColorName() {
        return this.profile.getActiveTheme().getColorName();
    }


    /**
     * Start the password changing process
     */
    public void changePassword() {

        final User principal = this.userSessionBean.getPrincipal();

        this.userAccountService.changePassword(this.passwordChangeDTO, principal);

        this.passwordChangeDTO = new PasswordChangeDTO();
        this.addInfo(true, "profile.password-changed");
    }

    /**
     * Open the change password popup
     */
    public void showChangePasswordDialog() {
        this.passwordChangeDTO = new PasswordChangeDTO();
        this.updateAndOpenDialog("changePasswordDialog", "dialogChangePassword");
    }

    /**
     * A simple DTO to transfer the data through the layers of the system
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
         * @return check if the new password is matching with the confirmation
         */
        public boolean isNewPassMatching() {
            return this.newPassword.equals(newPasswordConfirmation);
        }
    }
}
