/*
 * Copyright (C) 2016 Arthur Gregorio, AG.Software
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

import java.util.Optional;

/**
 * Facade to give the hability to retrieve informations about the user and his
 * permissions in conjunction with the security real
 *
 * @author Arthur Gregorio
 *
 * @version 3.0.0
 * @since 1.0.0, 31/01/2018
 */
public interface UserDetailsService {

    /**
     * 
     * @param username
     * @return 
     */
    Optional<UserDetails> findUserDetailsByUsername(String username);
}
