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
package br.com.webbudget.infraestructure.shiro;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 03/02/2018
 */
public abstract class HttpSecurityBuilder {
    
    private final String authcOperator;
    private final Map<String, String> rules;

    /**
     * 
     */
    public HttpSecurityBuilder() {
        this.authcOperator = "authc";
        this.rules = new HashMap<>();
    }
    
    /**
     * 
     * @param path
     * @param rule
     * @return 
     */
    public HttpSecurityBuilder addRule(String path, String rule) {
        this.rules.put(path, this.format(rule));
        return this;
    }
    
    /**
     * 
     * @param path
     * @param rule
     * @param requireAuthc
     * @return 
     */
    public HttpSecurityBuilder addRule(String path, String rule, boolean requireAuthc) {
        
        rule = this.authcOperator + "," + this.format(rule);
        
        this.rules.put(path, rule);
        return this;
    }

    /**
     * 
     * @param path
     * @param rule
     * @return 
     */
    private String format(String rule) {
        return String.format(this.getDefaultPrefix(), rule);
    }
    
    /**
     * 
     * @return 
     */
    public Map<String, String> build() {
        return Collections.unmodifiableMap(this.rules);
    }
    
    /**
     * 
     * @return 
     */
    public abstract String getDefaultPrefix();  
}
