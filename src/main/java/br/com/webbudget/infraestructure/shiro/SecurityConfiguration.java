package br.com.webbudget.infraestructure.shiro;

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
 * @since 3.0.0
 * @version 1.0.0, 31/01/2018
 */
@NoArgsConstructor
@ApplicationScoped
public class SecurityConfiguration {

    @Inject
    private Instance<UserDetailsService> userDetailsService;

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
            
            // FIXME create a better way to build de ACL's
            manager.createChain("/secured/tools/users/**", 
                    "authc, perms[user:access]");
            manager.createChain("/secured/tools/groups/**", 
                    "authc, perms[group:access]");
            
            manager.createChain("/secured/**", "authc");
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
        formAuthenticator.setSuccessUrl("/secured/dashboard.xhtml");

        return formAuthenticator;
    }
}
