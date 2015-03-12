/*
 * Copyright (C) 2015 Arthur Gregorio, AG.Software
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

import br.com.webbudget.domain.entity.users.User;
import br.com.webbudget.domain.repository.user.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 11/03/2015
 */
@Component
public class Authenticator implements AuthenticationProvider {

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private IUserRepository userRepository;

    /**
     *
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    @Transactional(readOnly = true)
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        final UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

        final User user = this.userRepository.findByUsername(token.getName());

        if (user == null) {
            throw new UsernameNotFoundException("authentication-user-not-found");
        } else if (!user.getPassword().equals(String.valueOf(token.getCredentials()))) {
            throw new BadCredentialsException("authentication.bad-credentials");
        }

        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    /**
     * Logout do usuario
     */
    public void logout() {
        SecurityContextHolder.clearContext();
        SecurityContextHolder.createEmptyContext();
    }
    
    /**
     *
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
