package br.com.webbudget.infraestructure.shiro;

import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 03/10/2016
 */
public class Credential {

    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private boolean rememberMe;    
    
    /**
     * @return 
     */
    public UsernamePasswordToken asToken() {
        return new UsernamePasswordToken(this.username, this.password, this.rememberMe);
    }
}
