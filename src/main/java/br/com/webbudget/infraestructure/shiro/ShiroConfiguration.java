package br.com.webbudget.infraestructure.shiro;

import br.eti.arthurgregorio.shirotest.model.service.AccountService;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
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
 * @since 1.0.0, 29/09/2016
 */
@ApplicationScoped
public class ShiroConfiguration {

    @Inject
    private AccountService accountService;

    private FilterChainResolver filterChainResolver;
    private DefaultWebSecurityManager securityManager;

    /**
     *
     */
    public ShiroConfiguration() { }

    /**
     * @return the default security manager for this application
     */
    @Produces
    public WebSecurityManager getSecurityManager() {

        if (this.securityManager == null) {

            // creates a custom security realm
            final AuthorizingRealm realm
                    = new SecurityRealm(this.accountService);

            // instantiate the custom password matcher based on bcrypt
            final PasswordMatcher passwordMatcher = new PasswordMatcher();

            passwordMatcher.setPasswordService(new BCryptPasswordService());

            realm.setCredentialsMatcher(passwordMatcher);

            // create the security manager
            this.securityManager = new DefaultWebSecurityManager(realm);

            // enable the remember me function based on cookies 
            final CookieRememberMeManager rememberMeManager
                    = new CookieRememberMeManager();

            rememberMeManager.setCipherKey(this.createCypherKey());

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

            final FilterChainManager chainManager = new DefaultFilterChainManager();

            chainManager.addFilter("anon", new AnonymousFilter());
            chainManager.addFilter("authc", this.configureFormAuthentication());
            chainManager.addFilter("perms", new PermissionsAuthorizationFilter());

            final PathMatchingFilterChainResolver resolver
                    = new PathMatchingFilterChainResolver();

            chainManager.createChain("/secured/**", "authc");
            chainManager.createChain("/index.xhtml", "anon");
            
            resolver.setFilterChainManager(chainManager);
            
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
