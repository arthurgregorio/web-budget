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

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.env.DefaultWebEnvironment;
import org.apache.shiro.web.env.EnvironmentLoaderListener;
import org.apache.shiro.web.env.WebEnvironment;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 03/02/2018
 */
@WebListener
public class SecurityWebListener extends EnvironmentLoaderListener {

    @Inject
    private WebSecurityManager webSecurityManager;
    @Inject
    private FilterChainResolver filterChainResolver;
    
    /**
     * 
     * @param event 
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        
        event.getServletContext().setInitParameter(
                ENVIRONMENT_CLASS_PARAM, DefaultWebEnvironment.class.getName());
        
        super.contextInitialized(event);
    }

    /**
     * 
     * @param servletContext
     * @return 
     */
    @Override
    protected WebEnvironment createEnvironment(ServletContext servletContext) {
        
        final DefaultWebEnvironment environment = (DefaultWebEnvironment) 
                super.createEnvironment(servletContext);
        
        environment.setSecurityManager(this.webSecurityManager);
        environment.setFilterChainResolver(this.filterChainResolver);
        
        return environment;
    }
}
