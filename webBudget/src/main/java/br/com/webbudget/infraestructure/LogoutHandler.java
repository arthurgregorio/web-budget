package br.com.webbudget.infraestructure;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 19/05/2014
 */
public class LogoutHandler extends SimpleUrlLogoutSuccessHandler {

    /**
     * 
     * @param request
     * @param response
     * @param authentication
     * 
     * @throws IOException
     * @throws ServletException 
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, 
            Authentication authentication) throws IOException, ServletException {

        if (request.getHeader("Referer") != null && request.getHeader("Referer").contains("/main")) {
            final String uri = request.getHeader("Referer").replace("/main/home.xhtml", "");
            request.getSession().setAttribute("expiredSession", uri + "/home.xhtml?expired=true");
        }

        super.onLogoutSuccess(request, response, authentication);
    }
}
