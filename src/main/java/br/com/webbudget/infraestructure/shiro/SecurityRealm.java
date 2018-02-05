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
 * @version 1.0.0
 * @since 3.0.0, 03/02/2018
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
