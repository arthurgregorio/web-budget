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
package br.com.webbudget.infrastructure.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.FacesContext;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A helper class to obtain the i18n messages through the given key
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 16/02/2018
 */
public final class MessageSource {

    private static final Logger LOG;
    private static final ResourceBundle MESSAGES;
    
    static {
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        
        MESSAGES = ResourceBundle.getBundle("i18n.messages", facesContext.getApplication().getDefaultLocale());
        
        LOG = LoggerFactory.getLogger(MessageSource.class);
    }
    
    /**
     * Give a key and get the message if the key exists
     * 
     * @param key i18n key
     * @return the message
     */
    public static String get(String key) {
        
        if (MESSAGES.containsKey(checkNotNull(key))) {
            return MESSAGES.getString(key);
        }
        
        LOG.error("No message found for key {}", key);
        
        return key;
    }

    /**
     * Same as {@link #get(String)} but this one take some parameters to format the text found
     *
     * @param key to the desired text
     * @param parameters parameters to be used in the formatter
     * @return formatted message with parameters
     */
    public static String get(String key, Object... parameters) {
        return MessageFormat.format(get(key), parameters);
    }
}
