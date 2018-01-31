package br.com.webbudget.infraestructure.shiro;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.shiro.subject.Subject;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 21/02/2017
 */
@Named
@SessionScoped
public class UserSession implements Serializable {

    @Inject
    private Subject subject;

    /**
     * 
     * @return 
     */
    public boolean isValid() {
        return this.subject.isAuthenticated() && this.subject.getPrincipal() != null;
    }

    /**
     * 
     * @param credential 
     */
    public void login(Credential credential) {
        this.subject.login(credential.asToken());
    }

    /**
     * 
     */
    public void logout() {
        this.subject.logout();
    }
}
