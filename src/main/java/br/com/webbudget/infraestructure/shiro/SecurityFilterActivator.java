package br.com.webbudget.infraestructure.shiro;

import javax.servlet.annotation.WebFilter;
import lombok.NoArgsConstructor;
import org.apache.shiro.web.servlet.ShiroFilter;

/**
 *
 * @author Arthur Gregorio
 *
 * @since 3.0.0
 * @version 1.0.0, 31/01/2018
 */
@WebFilter("/*")
@NoArgsConstructor
public class SecurityFilterActivator extends ShiroFilter { }
