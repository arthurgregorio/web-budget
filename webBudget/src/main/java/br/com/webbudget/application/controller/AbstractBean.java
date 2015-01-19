/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.webbudget.application.controller;

import br.com.webbudget.application.components.MessagesFactory;
import java.io.Serializable;
import java.util.Iterator;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import org.omnifaces.util.Messages;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 18/01/2015
 */
public abstract class AbstractBean implements Serializable {

    protected Logger logger;
    
    protected transient FacesContext facesContext;
    protected transient RequestContext requestContext;
    
    @Getter
    protected ViewState viewState;
    
    @Setter
    @ManagedProperty("#{messagesFactory}")
    private MessagesFactory messagesFactory;
    
    /**
     * Metodo que executa os hooks para inicializacao do bean
     */
    @PostConstruct
    public void runInitializers() {
        
        this.logger = this.initializeLogger();
       
        if (logger == null) {
            throw new NullPointerException("Logger instance can't be null!");
        }
        
        this.initializeCustom();
    }
    
    /**
     * Metodo tosco para carregar o contexto antes de usar, como o lixo do 
     * spring nao tem um meio de produzir as dependencias em escopo de request
     * fiz esse gambito para resolver provisoriamente ate a migracao para o JEE
     */
    private void loadContext() {
        this.facesContext = FacesContext.getCurrentInstance();
        this.requestContext = RequestContext.getCurrentInstance();
    }

    /**
     * 
     * @param componentId 
     */
    protected void update(String componentId) {
        this.loadContext();
        this.requestContext.update(componentId);
    }
    
    /**
     * 
     * @param script 
     */
    protected void execute(String script) {
        this.loadContext();
        this.requestContext.execute(script);
    }
    
    /**
     * 
     * @param widgetVar 
     */
    protected void openDialog(String widgetVar) {
        this.loadContext();
        this.requestContext.execute("PF('" + widgetVar +"').show()");
    }
    
    /**
     * 
     * @param id 
     * @param widgetVar
     */
    protected void openDialog(String id, String widgetVar) {
        this.loadContext();
        this.requestContext.update(id);
        this.requestContext.execute("PF('" + widgetVar +"').show()");
    }
    
    /**
     * 
     * @param widgetVar 
     */
    protected void closeDialog(String widgetVar) {
        this.loadContext();
        this.requestContext.execute("PF('" + widgetVar +"').hide()");
    }
    
    /**
     * Atualiza o componente de mensagens na view, usa o ID default 'messages'
     * 
     * @param useTimer se deve ou nao setar o timer
     */
    protected void updateMessages(boolean useTimer) {
        this.updateMessages("messages", useTimer);
    }
    
    /**
     * Atualiza o componente de mensagens na view
     * 
     * @param componentId o ID do componente de mensagens
     * @param useTimer se deve ou nao setar o timer
     */
    protected void updateMessages(String componentId, boolean useTimer) {
        
        this.loadContext();
        
        this.requestContext.update(componentId);
        
        if (useTimer) {
            this.requestContext.execute("setTimeout(\"$(\'#" + componentId + "\').slideUp(300)\", 8000)");
        }
    }
    
    /**
     * Joga uma mensagem de aviso na tela
     * 
     * @param message mensagem 
     * @param useTimer se deve ou nao setar o timer para fechar a mensagem apos 
     *                 um tempo de exibicao
     * @param parameters parametros para montar a mensagem
     */
    protected void warn(String message, boolean useTimer, Object... parameters) {
        Messages.addWarn(null, this.messagesFactory.getMessage(message), parameters);
        this.updateMessages(useTimer);
    }
    
    /**
     * Joga uma mensagem de informacao na tela
     * 
     * @param message mensagem 
     * @param useTimer se deve ou nao setar o timer para fechar a mensagem apos 
     *                 um tempo de exibicao
     * @param parameters parametros para montar a mensagem
     */
    protected void info(String message, boolean useTimer, Object... parameters) {
        Messages.addInfo(null, this.messagesFactory.getMessage(message), parameters);
        this.updateMessages(useTimer);
    }

    /**
     * Joga uma mensagem de erro na tela porem esta mensamge ficara visivel
     * ate ser fechada ou a tela ser atualizada
     * 
     * @param message mensagem 
     * @param updateMessages se deve ou nao atualizar o componente de mensagens
     *        default da view
     * @param parameters parametros para montar a mensagem
     */
    protected void fixedError(String message, boolean updateMessages, Object... parameters) {
        Messages.addError(null, this.messagesFactory.getMessage(message));
        if (updateMessages) {
            this.updateMessages(false);
        }
    }
    
    /**
     * Joga uma mensagem de erro na tela
     * 
     * @param message mensagem 
     * @param useTimer se deve ou nao setar o timer para fechar a mensagem apos 
     *                 um tempo de exibicao
     * @param parameters parametros para montar a mensagem
     */
    protected void error(String message, boolean useTimer, Object... parameters) {
        Messages.addError(null, this.messagesFactory.getMessage(message), parameters);
        this.updateMessages(useTimer);
    }
    
    /**
     * Pega todas as mensagens de erro para um componente informado pelo ID
     * 
     * @param componentId o id do componente para buscar as mensagens
     * @return as mensagens de erro internacionalizadas do componente
     */
    public String getErrorMessage(String componentId) {
        
        this.loadContext();
    
        final Iterator<FacesMessage> iterator = 
                this.facesContext.getMessages(componentId);
        
        if (iterator.hasNext()) {
            return this.messagesFactory.getMessage(iterator.next().getDetail());
        }
        return "";
    }
    
    /**
     * Se usado, inicializa partes customizadas do bena que implementar este metodo
     */
    public void initializeCustom() { }
    
    /**
     * Inicializa o logger, deve ser implementado
     * 
     * Ex.: return LoggerFactory.getLogger(SomeClass.class);
     * 
     * @return a instancia do logger
     */
    protected abstract Logger initializeLogger();
    
    /**
     * Enum que guarda os possiveis estados da tela
     */
    public enum ViewState {
        ADDING,
        LISTING, 
        EDITING,
        DELETING;
    }
}
