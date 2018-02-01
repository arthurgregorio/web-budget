package br.com.webbudget.infraestructure.shiro;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
        
/**
 *
 * @author Arthur Gregorio
 *
 * @since 3.0.0
 * @version 1.0.0, 31/01/2018
 */
@Named
@RequestScoped
public class Authenticator implements Serializable {
    
    private Subject subject;

    /**
     * 
     */
    @PostConstruct
    protected void initialize() {
        this.subject = SecurityUtils.getSubject();
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
    
    /**
     * 
     * @return 
     */
    public boolean authenticationIsNeeded() {
        return !this.subject.isAuthenticated();
    }
}
