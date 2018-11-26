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
package br.com.webbudget.infrastructure.jsf;

import org.primefaces.PrimeFaces;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.Iterator;

/**
 * Simple utility class to deal with the JSF and Primefaces features
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 20/11/2018
 */
public class FacesUtils {

    /**
     * Clear the messages component on the user screen
     *
     * @param context the view context to be used
     */
    public static void clearMessages(FacesContext context) {

        final Iterator<FacesMessage> messages = context.getMessages();

        while (messages.hasNext()) {
            messages.next();
            messages.remove();
        }
    }

    /**
     * After display the message, temporize the hiding of the message box
     *
     * @param componentId the ID of the component to temporize the hiding
     */
    public static void temporizeHiding(String componentId) {
        PrimeFaces.current().executeScript("setTimeout(\"$(\'#" + componentId + "\').slideUp(300)\", 8000)");
    }

    /**
     * Run a simple JS script on the UI
     *
     * @param script the script
     */
    public static void executeScript(String script) {
        PrimeFaces.current().executeScript(script);
    }

    /**
     * Update a single component by the ID
     *
     * @param componentId the ID of the component
     */
    public static void updateComponent(String componentId) {
        PrimeFaces.current().ajax().update(componentId);
    }
}
