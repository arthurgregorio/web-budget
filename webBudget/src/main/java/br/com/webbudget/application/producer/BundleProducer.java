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
package br.com.webbudget.application.producer;

import br.com.webbudget.application.producer.qualifier.I18n;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 * Produtor de bundles do sistam, com ele conseguimos produzir qualquer bundle
 * necessario para uso em qualquer lugar onde possamos usar injecao
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.0.0, 21/05/2015
 */
public class BundleProducer {

    @Inject
    private FacesContext facesContext;
    
    /**
     * 
     * @return 
     */
    @I18n
    @Produces
    @ApplicationScoped
    ResourceBundle produceMessageBundle() {
        return ResourceBundle.getBundle("i18n.messages", this.getCurrentLocale());
    }
    
    /**
     * Retorna a locale atual do sistema
     * 
     * @return a localizacao atual do sistema
     */
    private Locale getCurrentLocale() {
        return this.facesContext.getViewRoot().getLocale();
    }
}