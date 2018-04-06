package br.com.webbudget.application.controller;

import br.com.webbudget.infrastructure.utils.MessageSource;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.omnifaces.util.Messages;
import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 28/03/2018
 */
public abstract class AbstractBean implements Serializable {

    @Inject
    protected Logger logger;

    @Inject
    protected FacesContext facesContext;
    @Inject
    protected RequestContext requestContext;

    /**
     * @return o nome do componente default de mensagens da view
     */
    protected String getDefaultMessagesComponentId() {
        return "messages";
    }

    /**
     * Traduz uma mensagem pelo bundle da aplicacao
     *
     * @param message a chave da mensagem original
     * @return o texto
     */
    protected String translate(String message) {
        return MessageSource.get(message);
    }

    /**
     * Adiciona uma mensagem de informacao na tela
     *
     * @param message a mensagem
     * @param parameters os parametros da mensagem
     * @param updateDefault se devemos ou nao atualizar o componente default
     */
    protected void addInfo(boolean updateDefault, String message, Object... parameters) {
        Messages.addInfo(null, this.translate(message), parameters);
        if (updateDefault) {
            this.updateDefaultMessages();
        }
    }

    /**
     * Adiciona a mensagem para o escopo flash fazendo com que elea fique viva
     * por um tempo maior
     * 
     * @param message a mensagem
     * @param parameters os parametros da mensagem
     */
    protected void addInfoAndKeep(String message, Object... parameters) {
        Messages.addInfo(null, this.translate(message), parameters);
        this.facesContext.getExternalContext().getFlash().setKeepMessages(true);
    }

    /**
     * Adiciona uma mensagem de erro na tela
     *
     * @param message a mensagem
     * @param parameters os parametros da mensagem
     * @param updateDefault se devemos ou nao atualizar o componente default
     */
    protected void addError(boolean updateDefault, String message, Object... parameters) {
        Messages.addError(null, this.translate(message), parameters);
        if (updateDefault) {
            this.updateDefaultMessages();
        }
    }

    /**
     * Apenas abre uma dialog pelo seu widgetvar
     *
     * @param widgetVar o widgetvar para abri-la
     */
    protected void openDialog(String widgetVar) {
        this.executeScript("PF('" + widgetVar + "').show()");
    }

    /**
     * Dado o id de um dialog, atualiza a mesma e depois abre pelo widgetvar
     *
     * @param id o id da dialog para atualiza-la
     * @param widgetVar o widgetvar para abri-la
     */
    protected void updateAndOpenDialog(String id, String widgetVar) {
        this.updateComponent(id);
        this.executeScript("PF('" + widgetVar + "').show()");
    }

    /**
     * Fecha uma dialog aberta previamente
     *
     * @param widgetVar o widgetvar da dialog
     */
    protected void closeDialog(String widgetVar) {
        this.executeScript("PF('" + widgetVar + "').hide()");
    }

    /**
     * Caso o nome do componente default de mensagens tenha sido setado, este
     * metodo invocado apos adicionar mensagens faz com que ele seja atualizado
     * automaticamente
     */
    protected void updateDefaultMessages() {
        this.temporizeHiding(this.getDefaultMessagesComponentId());
    }

    /**
     * Dado um componente, atualiza o mesmo e depois temporiza o seu fechamento
     *
     * @param componentId o id do componente
     */
    protected void temporizeHiding(String componentId) {
        this.executeScript("setTimeout(\"$(\'#" + componentId + "\').slideUp(300)\", 8000)");
    }

    /**
     * Atualiza um componente pelo seu id no contexto atual
     *
     * @param componentId o id do componente
     */
    protected void updateComponent(String componentId) {
        PrimeFaces.current().ajax().update(componentId);
    }

    /**
     * Executa um JavaScript na pagina pelo FacesContext atual
     *
     * @param script o script a ser executado
     */
    protected void executeScript(String script) {
        PrimeFaces.current().executeScript(script);
    }
}
