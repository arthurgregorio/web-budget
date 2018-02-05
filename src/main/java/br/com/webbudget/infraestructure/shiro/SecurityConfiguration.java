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

import br.com.webbudget.domain.entities.security.Permissions;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import lombok.NoArgsConstructor;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 03/02/2018
 */
@NoArgsConstructor
@ApplicationScoped
public class SecurityConfiguration {

    @Inject
    private Instance<UserDetailsService> userDetailsService;
    
    @Inject
    private Permissions permissions;

    private FilterChainResolver filterChainResolver;
    private DefaultWebSecurityManager securityManager;

    /**
     * @return the default security manager for this application
     */
    @Produces
    public WebSecurityManager produceWebSecurityManager() {

        if (this.securityManager == null) {

            if (this.userDetailsService.isUnsatisfied()) {
                throw new IllegalStateException("No bean instance for UserDetailsService is provided, check you implementation!");
            }
            
            // creates a custom security realm
            final AuthorizingRealm realm =
                    new SecurityRealm(this.userDetailsService.get());
            
            realm.setCachingEnabled(true);
            realm.setCacheManager(new EhCacheManager());

            // instantiate the custom password matcher based on bcrypt
            final PasswordMatcher passwordMatcher = new PasswordMatcher();

            passwordMatcher.setPasswordService(new BCryptPasswordService());

            realm.setCredentialsMatcher(passwordMatcher);

            // enable the remember me function based on cookies 
            final CookieRememberMeManager rememberMeManager
                    = new CookieRememberMeManager();

            rememberMeManager.setCipherKey(this.createCypherKey());
            
            // create the security manager
            this.securityManager = new DefaultWebSecurityManager(realm);

            this.securityManager.setRememberMeManager(rememberMeManager);
        }
        return this.securityManager;
    }

    /**
     * @return the default filter chain resolver for this application
     */
    @Produces
    public FilterChainResolver getFilterChainResolver() {

        if (this.filterChainResolver == null) {

            final FilterChainManager manager = new DefaultFilterChainManager();

            manager.addFilter("authc", this.configureFormAuthentication());
            
            manager.addFilter("logout", new LogoutFilter());
            manager.addFilter("anon", new AnonymousFilter());
            manager.addFilter("roles", new RolesAuthorizationFilter());
            
            final PermissionsAuthorizationFilter permsFilter = 
                    new PermissionsAuthorizationFilter();
            
            permsFilter.setUnauthorizedUrl("/error/401.xhtml");
            
            manager.addFilter("perms", permsFilter);
            
            // build the http security rules
            final Map<String, String> chains = this.buildHttpSecurity();
            
            chains.keySet().stream().forEach(path -> {
                manager.createChain(path, chains.get(path));
            });
            
            manager.createChain("/main/**", "authc");
            manager.createChain("/error/**", "anon");
            manager.createChain("/logout", "logout");
            
            final PathMatchingFilterChainResolver resolver
                    = new PathMatchingFilterChainResolver();
            
            resolver.setFilterChainManager(manager);

            this.filterChainResolver = resolver;
        }
        return this.filterChainResolver;
    }

    /**
     * @return a custom cypher key for the cookie
     */
    private byte[] createCypherKey() {
        return String.format("0x%s", Hex.encodeToString(new AesCipherService()
                .generateNewKey().getEncoded())).getBytes();
    }

    /**
     * @return the default form authentication mechanism for this realm
     */
    private FormAuthenticationFilter configureFormAuthentication() {

        final FormAuthenticationFilter formAuthenticator
                = new FormAuthenticationFilter();

        formAuthenticator.setLoginUrl("/index.xhtml");
        formAuthenticator.setSuccessUrl("/main/dashboard.xhtml");

        return formAuthenticator;
    }

    /**
     * 
     * @return 
     */
    private Map<String, String> buildHttpSecurity() {
        
        final HttpSecurityBuilder builder = new PermissionHttpSecurityBuilder();
                
        builder.addRule("/main/entries/card/**", this.permissions.getGROUP_ACCESS(), true)
                .addRule("/main/entries/contact/**", this.permissions.getCONTACT_ACCESS(), true)
                .addRule("/main/entries/costCenter/**", this.permissions.getCOST_CENTER_ACCESS(), true)
                .addRule("/main/entries/wallet/**", this.permissions.getWALLET_ACCESS(), true)
                .addRule("/main/entries/movementClass/**", this.permissions.getMOVEMENT_CLASS_ACCESS(), true)
                .addRule("/main/financial/movement/**", this.permissions.getMOVEMENT_ACCESS(), true)
                .addRule("/main/financial/cardInvoice/**", this.permissions.getCARD_INVOICE_ACCESS(), true)
                .addRule("/main/financial/transference/**", this.permissions.getBALANCE_TRANSFERENCE_ACCESS(), true)
                .addRule("/main/tools/configuration/**", this.permissions.getCONFIGURATION_ACCESS(), true)
                .addRule("/main/tools/message/send/**", this.permissions.getMESSAGE_SEND(), true)
                .addRule("/main/tools/user/**", this.permissions.getUSER_ACCESS(), true)
                .addRule("/main/tools/group/**", this.permissions.getGROUP_ACCESS(), true)
                .addRule("/main/miscellany/closing/**", this.permissions.getCLOSING_ACCESS(), true)
                .addRule("/main/miscellany/financialPeriod/**", this.permissions.getFINANCIAL_PERIOD_ACCESS(), true);
        
        return builder.build();
    }
}
