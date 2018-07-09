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
package br.com.webbudget.infrastructure.utils;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.faces.application.ProjectStage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 * FIXME refactor this JavaDoc
 *
 * Classe utilitaria para uso em alguns pontos da aplicacao sem a necessidade de
 * ficar repetindo alguns codigos tais como pegar configuracoes ou a URL base da
 * aplicacaao para uso em alguma template
 *
 * @author Arthur Gregorio
 *
 * @version 1.1.0
 * @since 2.0.0, 07/07/2015
 */
public final class Configurations {

    private static final ResourceBundle CONFIG_PROPERTIES;
    
    static {
        CONFIG_PROPERTIES = ResourceBundle.getBundle("application");
    }
    
    /**
     * Busca no bundle de configuracoes da aplicacao uma determinada chave para
     * uma configuracao
     *
     * @param configuration a chave da qual queremos a configuracao
     * @return a configuracao para a chave informada
     */
    public static String get(String configuration) {
        try {
            return CONFIG_PROPERTIES.getString(configuration);
        } catch (MissingResourceException ex) {
            return null;
        }
    }

    /**
     * Constroi a URL base da aplicacao
     *
     * @return a URL base da aplicaco + contexto
     */
    public static String baseURL() {

        final FacesContext facesContext = FacesContext.getCurrentInstance();

        final HttpServletRequest request = (HttpServletRequest) 
                facesContext.getExternalContext().getRequest();

        final StringBuilder builder = new StringBuilder();

        String actualPath = request.getRequestURL().toString();

        builder.append(actualPath.replace(request.getRequestURI(), ""));
        builder.append(request.getContextPath());

        return builder.toString();
    }

    /**
     * Checa em que estagio do projeto estamos, as opcoes sao as definidas no
     * enum {@link ProjectStage}
     *
     * @param stage o estagio do projeto
     * @return se estamos usando ele ou nao
     */
    public static boolean isRunningStage(ProjectStage stage) {
        return FacesContext.getCurrentInstance()
                .isProjectStage(stage);
    }
}
