package br.com.webbudget.infraestructure.shiro;

import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 *
 * @author Arthur Gregorio
 *
 * @since 3.0.0
 * @version 1.0.0, 31/01/2018
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
