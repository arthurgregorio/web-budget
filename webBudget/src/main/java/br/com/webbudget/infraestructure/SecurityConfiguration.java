/*
 * Copyright (C) 2015 Arthur
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

package br.com.webbudget.infraestructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 07/03/2015
 */
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    
    /**
     * 
     * @param auth
     * @throws Exception 
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(this.authProvider());
    }

    /**
     * 
     * @param web
     * @throws Exception 
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/favicon.ico*")
                .antMatchers("/javax.faces.resource/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .csrf().disable()
            .formLogin()
                .loginPage("/home.xhtml")
                .defaultSuccessUrl("/main/dashboard.xhtml")
                .failureUrl("/home.xhtml?error=true")
                .permitAll()
            .and()
            .sessionManagement()
                .invalidSessionUrl("/invalidSession.xhtml")
                .sessionFixation().none()
            .and()
            .logout()
                .invalidateHttpSession(true)
                .logoutUrl("/main/logout")
                .logoutSuccessUrl("/home.xhtml?logout=true")
                .deleteCookies("JSESSIONID")
                .permitAll()
            .and()
                .authorizeRequests()
                    .antMatchers("/main/entries/cards/**").hasRole("")
                    .antMatchers("/main/entries/contacts/**").hasRole("")
                    .antMatchers("/main/entries/costCenter/**").hasRole("")
                    .antMatchers("/main/entries/movementClass/**").hasRole("")
                    .antMatchers("/main/entries/wallets/**").hasRole("")
                    .antMatchers("/main/financial/cardInvoice/**").hasRole("")
                    .antMatchers("/main/financial/movement/**").hasRole("")
                    .antMatchers("/main/financial/transfer/**").hasRole("")
                    .antMatchers("/main/miscellany/closing/**").hasRole("")
                    .antMatchers("/main/miscellany/financialPeriod/**").hasRole("")
                    .antMatchers("/main/tools/privateMessage/**").hasRole("")
                    .antMatchers("/main/tools/user/**").hasRole("");
    }

    /**
     * 
     * @return 
     */
    @Bean
    public DaoAuthenticationProvider authProvider() {
        
        final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        
        provider.setUserDetailsService(this.userDetailsService);
        provider.setPasswordEncoder(this.encoder());
        
        return provider;
    }

    /**
     * 
     * @return 
     */
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }
}
