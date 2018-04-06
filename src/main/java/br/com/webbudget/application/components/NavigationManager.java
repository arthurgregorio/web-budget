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
package br.com.webbudget.application.components;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.faces.context.FacesContext;
import lombok.Getter;

/**
 * Gerenciador de navegacao para facilitar os redirects e a navegacao entre 
 * as paginas da aplicacao
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 13/02/2018
 */
public final class NavigationManager {

    private final Map<PageType, String> pages;

    /**
     * Construtor...
     */
    private NavigationManager() {
        this.pages = new HashMap<>();
    }
    
    /**
     * Cria um novo navegador de paginas
     * 
     * @return uma nova instancia do navegador
     */
    public static NavigationManager getInstance() {
        return new NavigationManager();
    }

    /**
     * Adiciona as paginas no mapa de paginas
     * 
     * @param pageType o tipo da pagina adicionada
     * @param page a pagina a ser ligada ao tipo
     * @return se uma pagina ja associada a um tipo for adicionada, retorna a 
     * pagina associada
     */
    public String addPage(PageType pageType, String page) {
        return this.pages.putIfAbsent(pageType, page);
    }
    
    /**
     * Navega para uma determinada pagina via navegacao implicita do JSF atraves
     * da action de um componente de acao
     * 
     * @param page a pagina a ser redirecionada 
     * @param parameters os parametros para serem usados
     * @return o outcome da pagina
     */
    public String to(PageType page, Parameter... parameters) {
        
        final String root = this.pages.get(page);
        
        final StringBuilder builder = new StringBuilder(root);
        
        builder.append("?faces-redirect=true");
        
        for (Parameter parameter : parameters) {
            builder.append(parameter);
        }
        
        builder.append(Parameter.of("viewState", page.getViewState()));
        
        return builder.toString();
    }
    
    /**
     * Navega para uma determinada pagina via navegacao explicita do JSF atraves
     * de um redirect pelo contexto
     * 
     * @param page a pagina a ser redirecionada 
     * @param parameters os parametros para serem usados
     */
    public void redirect(PageType page, Parameter... parameters) {
       
        final String url = this.to(page, parameters);
        
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(url);
        } catch (IOException ex) {
            throw new RuntimeException(String.format(
                    "Can't rendirect to url %s", url));
        }
    }
    
    /**
     * 
     */
    public enum PageType {
        
        LIST_PAGE(ViewState.LISTING),
        DETAIL_PAGE(ViewState.DETAILING),
        ADD_PAGE(ViewState.ADDING),
        UPDATE_PAGE(ViewState.EDITING),
        DELETE_PAGE(ViewState.DELETING);
        
        @Getter
        private final ViewState viewState;

        /**
         * 
         * @param viewState 
         */
        private PageType(ViewState viewState) {
            this.viewState = viewState;
        }
    }
    
    /**
     * 
     */
    public static class Parameter {

        @Getter
        private final String name;
        @Getter
        private final Object value;

        /**
         * 
         * @param name
         * @param value 
         */
        private Parameter(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        /**
         * 
         * @param name
         * @param value
         * @return 
         */
        public static Parameter of(String name, Object value) {
            return new Parameter(name, value);
        }
        
        /**
         * 
         * @return 
         */
        @Override
        public String toString() {
            return "&" + this.name + "=" + this.value;
        }
    }
}
