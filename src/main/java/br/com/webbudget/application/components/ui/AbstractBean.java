/*
 * Copyright (C) 2014 Arthur Gregorio, AG.Software
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
package br.com.webbudget.application.components.ui;

import br.com.webbudget.infrastructure.jsf.FacesUtils;
import br.com.webbudget.infrastructure.i18n.MessageSource;
import org.omnifaces.util.Messages;
import org.slf4j.Logger;

import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import java.io.Serializable;

/**
 * The base bean of the controllers of this application, this class contains all the basic methods to the controllers
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 1.0.0, 04/03/2014
 */
public abstract class AbstractBean implements Serializable {

    @Inject
    protected Logger logger;

    @Inject
    protected FacesContext facesContext;

    /**
     * @return the name of the default messages component, by default is <code>messages</code>
     */
    protected String getDefaultMessagesComponentId() {
        return "messages";
    }

    /**
     * Method to translate i18n keys to the corresponding text
     *
     * @param i18nKey the i18n key
     * @return the i18nKey
     */
    protected String translate(String i18nKey) {
        return MessageSource.get(i18nKey);
    }

    /**
     * Add a info message to the {@link FacesContext}
     *
     * @param message the text of the message or the i18n key
     * @param parameters to be used in the message
     * @param updateDefault if the main message component needs to be updated
     */
    protected void addInfo(boolean updateDefault, String message, Object... parameters) {
        Messages.addInfo(null, this.translate(message), parameters);
        if (updateDefault) {
            this.updateDefaultMessages();
        }
    }

    /**
     * Add a info message to the {@link FacesContext} in the {@link Flash} scope
     *
     * @param message the text of the message or the i18n key
     * @param parameters to be used in the message
     */
    protected void addInfoAndKeep(String message, Object... parameters) {
        Messages.addInfo(null, this.translate(message), parameters);
        this.facesContext.getExternalContext().getFlash().setKeepMessages(true);
    }

    /**
     * Add a error message to the {@link FacesContext}
     *
     * @param message the text of the message or the i18n key
     * @param parameters to be used in the message
     * @param updateDefault if the main message component needs to be updated
     */
    protected void addError(boolean updateDefault, String message, Object... parameters) {
        Messages.addError(null, this.translate(message), parameters);
        if (updateDefault) {
            this.updateDefaultMessages();
        }
    }

    /**
     * Convenience method to open dialogs
     *
     * @param widgetVar the name of the dialog to be opened
     */
    protected void openDialog(String widgetVar) {
        this.executeScript("PF('" + widgetVar + "').show()");
    }

    /**
     * Same as {@link #openDialog(String)} but before open it, a update by the component ID is performed
     *
     * @param id the dialog component id
     * @param widgetVar the name of the dialog to be opened
     */
    protected void updateAndOpenDialog(String id, String widgetVar) {
        this.updateComponent(id);
        this.executeScript("PF('" + widgetVar + "').show()");
    }

    /**
     * Convenience method to close dialogs
     *
     * @param widgetVar the name of the dialog to close
     */
    protected void closeDialog(String widgetVar) {
        this.executeScript("PF('" + widgetVar + "').hide()");
    }

    /**
     * Update the default messages component
     */
    protected void updateDefaultMessages() {
        this.temporizeHiding(this.getDefaultMessagesComponentId());
    }

    /**
     * Method to put a timer in and after the time expires, hide the component
     *
     * @param componentId the component id
     */
    protected void temporizeHiding(String componentId) {
        FacesUtils.temporizeHiding(componentId);
    }

    /**
     * Convenience method to update one component by the client id
     *
     * @param componentId the id of the component
     */
    protected void updateComponent(String componentId) {
        FacesUtils.updateComponent(componentId);
    }

    /**
     * Convenience method to execute scripts on the front-end
     *
     * @param script the script to be executed
     */
    protected void executeScript(String script) {
        FacesUtils.executeScript(script);
    }
}
