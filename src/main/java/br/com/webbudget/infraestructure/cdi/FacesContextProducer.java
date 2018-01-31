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
package br.com.webbudget.infraestructure.cdi;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 * Produtor de contextos do sistema
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.0.0, 21/05/2015
 */
@ApplicationScoped
public class FacesContextProducer {

    /**
     * Produz um contexto valido do {@link RequestContext}
     *
     * @return um {@link RequestContext} valido
     */
    @Produces
    @RequestScoped
    RequestContext produceRequestContext() {
        return RequestContext.getCurrentInstance();
    }

    /**
     * Produz um contexto valido do {@link FacesContext}
     *
     * @return um {@link FacesContext} valido
     */
    @Produces
    @RequestScoped
    FacesContext produceFacesContext() {
        return FacesContext.getCurrentInstance();
    }
}
