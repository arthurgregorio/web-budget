package br.com.webbudget.infraestructure.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 *
 * @author Arthur Gregorio
 *
 * @since 3.0.0
 * @version 1.0.0, 31/01/2018
 */
public class SecurityRealm extends AuthorizingRealm {

    private final UserDetailsService userDetailsService;
    
    /**
     * 
     * @param userDetailsService 
     */
    public SecurityRealm(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * 
     * @param authenticationToken
     * @return
     * @throws AuthenticationException 
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) 
            throws AuthenticationException {
        
        final UsernamePasswordToken token = 
                (UsernamePasswordToken) authenticationToken;
        
        final UserDetails details = this.userDetailsService
                .findUserDetailsByUsername(token.getUsername())
                .orElseThrow(() -> new IncorrectCredentialsException(
                        "Invalid user or password"));
        
        return new SimpleAuthenticationInfo(details.getUsername(), 
                details.getPassword(), this.getName());
    }

    /**
     * 
     * @param principalCollection
     * @return 
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        
        final String username = (String) 
                this.getAvailablePrincipal(principalCollection);
        
        final UserDetails details = this.userDetailsService
                .findUserDetailsByUsername(username)
                .orElseThrow(() -> new IncorrectCredentialsException(
                        "Invalid user or password"));
        
        final SimpleAuthorizationInfo authzInfo = new SimpleAuthorizationInfo();
        
        authzInfo.setStringPermissions(details.getPermissions());
        
        return authzInfo;
    }
}
