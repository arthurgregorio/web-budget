/*
 * Copyright (C) 2017 Arthur Gregorio, AG.Software
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
package br.com.webbudget.domain.repositories.configuration;

import br.com.webbudget.domain.entities.configuration.Authorization;
import br.com.webbudget.domain.repositories.DefaultRepository;
import org.apache.deltaspike.data.api.Repository;

import java.util.Optional;

/**
 * The {@link Authorization} repository
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 28/12/2017
 */
@Repository
public interface AuthorizationRepository extends DefaultRepository<Authorization> {

    /**
     * Find an {@link Authorization} by the functionality and the permission
     * 
     * @param functionality the functionality of the {@link Authorization}
     * @param permission the permission of the {@link Authorization}
     * @return an {@link Optional} of the {@link Authorization}
     */
    Optional<Authorization> findByFunctionalityAndPermission(String functionality, String permission);
}
