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
package br.com.webbudget.application.controller;

import lombok.Getter;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 13/02/2018
 */
public final class NavigationManager {

    private final String rootView;

    /**
     * 
     * @param rootView 
     */
    public NavigationManager(String rootView) {
        this.rootView = rootView;
    }
    
    /**
     * 
     * @return 
     */
    public String toRoot() {
        return this.rootView + "?faces-redirect=true";
    }
    
    /**
     * 
     * @param parameter
     * @return 
     */
    public String toRootWithParameter(Parameter parameter) {
        return this.toRoot() + parameter.toString();
    }
    
    /**
     * 
     * @param parameters
     * @return 
     */
    public String toRootWithParameters(Parameter... parameters) {
       
        final StringBuilder builder = new StringBuilder(this.toRoot());
        
        for (Parameter p : parameters) {
            builder.append(p.toString());
        }
        
        return builder.toString();
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
