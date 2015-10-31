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

import java.util.ResourceBundle;
import javax.faces.application.ProjectStage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 * Classe utilitaria para uso em alguns pontos da aplicacao sem a necessidade de
 * ficar repetindo alguns codigos tais como pegar configuracoes ou a URL base da
 * aplicacaao para uso em alguma template
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.0.0, 07/07/2015
 */
public class ApplicationUtils {

    /**
     * Busca no bundle de configuracoes da aplicacao uma determinada chave para
     * uma configuracao
     *
     * @param configurationKey a chave da qual queremos a configuracao
     * @return a configuracao para a chave informada
     */
    public static String getConfiguration(String configurationKey) {

        final ResourceBundle bundle = ResourceBundle.getBundle("webbudget");

        return bundle.getString(configurationKey);
    }

    /**
     * Constroi a URL base da aplicacao
     * 
     * @return a URL base da aplicaco + contexto
     */
    public static String buildBaseURL() {

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
     * @param projectStage o estagio do projeto
     * @return se estamos usando ele ou nao
     */
    public static boolean isStageRunning(ProjectStage projectStage) {
        return FacesContext.getCurrentInstance()
                .isProjectStage(projectStage);
    }
}
