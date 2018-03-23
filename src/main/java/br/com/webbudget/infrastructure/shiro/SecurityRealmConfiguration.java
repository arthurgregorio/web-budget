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
package br.com.webbudget.infrastructure.shiro;

import br.com.webbudget.domain.services.UserAccountService;
import br.eti.arthurgregorio.shiroee.auth.AuthenticationMechanism;
import br.eti.arthurgregorio.shiroee.auth.DatabaseAuthenticationMechanism;
import br.eti.arthurgregorio.shiroee.config.RealmConfiguration;
import br.eti.arthurgregorio.shiroee.realm.JdbcSecurityRealm;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.realm.Realm;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 06/03/2018
 */
@ApplicationScoped
public class SecurityRealmConfiguration implements RealmConfiguration {

    private Set<Realm> realms;     
    private AuthenticationMechanism mechanism;    
    
    @Inject
    private UserAccountService userAccountService;

    /**
     * 
     */
    @PostConstruct
    protected void initialize() {
        
        this.realms = new HashSet<>();
        
        this.mechanism = new DatabaseAuthenticationMechanism(
                this.userAccountService);
        
        this.configureJdbcRealm();
    }
    
    /**
     * 
     * @return 
     */
    @Override
    public Set<Realm> configureRealms() {
        return Collections.unmodifiableSet(this.realms);
    }    
    
    /**
     * 
     * @return 
     */
    private void configureJdbcRealm() {

        final JdbcSecurityRealm realm = new JdbcSecurityRealm(this.mechanism);
        
        realm.setCachingEnabled(true);
        realm.setCacheManager(new EhCacheManager());

        this.realms.add(realm);
    }
}
