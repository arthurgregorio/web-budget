package br.com.webbudget.application.components;

import java.io.Serializable;
import java.util.Locale;
import javax.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;

/**
 * Factory que constroi e mantem em sessao uma instancia para uso do bundle de 
 * i18n da aplicacao
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 28/02/2014
 */
@Component
public class MessagesFactory implements Serializable {

    @Autowired
    private transient ReloadableResourceBundleMessageSource messages;
    
    /**
     * Dada uma key retorna o seu respectivo valor no bundle para a localizacao
     * atual da aplicacao
     * 
     * @param key a chave
     * @return o valor da chave
     */
    public String getMessage(String key) {
        
        final Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        
        try {
            return this.messages.getMessage(key, null, locale);
        } catch (NoSuchMessageException ex) {
            return (key == null) ? this.messages.getMessage("error.unknow", null, locale) : key;
        }
    }

    /**
     * Dada uma key retorna o seu respectivo valor no bundle para a localizacao
     * atual da aplicacao injetando na reposta os parametros para compor a 
     * mensagem
     * 
     * @param key a chave
     * @param parameters os parametros
     * @return o valor da chave
     */
    public String getMessage(String key, String... parameters) {

        final Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        
        try {
            return this.messages.getMessage(key, parameters, null, locale);
        } catch (NoSuchMessageException ex) {
            return (key == null) ? this.messages.getMessage("error.unknow", null, locale) : key;
        }
    }
}
