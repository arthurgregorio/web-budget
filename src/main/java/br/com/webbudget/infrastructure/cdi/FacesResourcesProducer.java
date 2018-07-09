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
package br.com.webbudget.infrastructure.cdi;

import org.primefaces.context.RequestContext;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;

/**
 * The faces resources producer
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.0.0, 21/05/2015
 */
@ApplicationScoped
public class FacesResourcesProducer {

    /**
     * Produce a instance of the Primefaces {@link RequestContext}
     *
     * @return the {@link RequestContext} instance for injection
     */
    @Produces
    @RequestScoped
    RequestContext produceRequestContext() {
        return RequestContext.getCurrentInstance();
    }

    /**
     * Produce a instance of the JSF {@link FacesContext}
     *
     * @return the {@link FacesContext} instance for injection
     */
    @Produces
    @RequestScoped
    FacesContext produceFacesContext() {
        return FacesContext.getCurrentInstance();
    }
}