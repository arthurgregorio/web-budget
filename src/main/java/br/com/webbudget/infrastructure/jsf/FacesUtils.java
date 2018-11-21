package br.com.webbudget.infrastructure.jsf;

import org.primefaces.PrimeFaces;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.Iterator;

/**
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
