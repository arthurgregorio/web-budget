/*
 * Copyright (C) 2015 Arthur Gregorio, AG.Software
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
package br.com.webbudget.infraestructure;

import br.com.webbudget.application.producer.qualifier.I18n;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.slf4j.Logger;

/**
 * Factory que constroi e mantem em sessao uma instancia para uso do bundle de 
 * i18n da aplicacao
 *
 * @author Arthur Gregorio
 *
 * @version 1.1.0
 * @since 1.0.0, 28/02/2014
 */
@ApplicationScoped
public class MessagesFactory {

    @Inject
    private Logger logger;
    
    @I18n
    @Inject
    private ResourceBundle resourceBundle;
    
    /**
     * Prove as mensagens para os beans da aplicacao, por este metodo conseguimos
     * pegar as mensagens do contexto no bundle e usa-las nos managedbeans
     * 
     * Caso nao encontre a chave ou ela esteja em branco, retorna a propria chave
     * 
     * @param key a chave para pegar a traducao
     * @return a traducao/texto para a chave indicada
     */
    public String get(String key) {
        
        try {
            final String message = this.resourceBundle.getString(key);
            return (message == null || message.isEmpty()) ? key : message;
        } catch (MissingResourceException ex) {
            this.logger.error("Can't find message for {}", key);
            return key;
        }
    } 
}
