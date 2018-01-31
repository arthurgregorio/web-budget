package br.com.webbudget.infraestructure.shiro;

import javax.servlet.annotation.WebFilter;
import org.apache.shiro.web.servlet.ShiroFilter;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 29/09/2016
 */
@WebFilter("/*")
public class ShiroFilterActivator extends ShiroFilter {

    /**
     * 
     */
    public ShiroFilterActivator() { }
}
