/*
 * Copyright (C) 2019 Arthur Gregorio, AG.Software
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

import br.com.webbudget.domain.entities.configuration.Configuration;
import br.com.webbudget.domain.repositories.DefaultRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import java.util.Optional;

/**
 * The {@link Configuration} repository
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 16/03/2019
 */
@Repository
public interface ConfigurationRepository extends DefaultRepository<Configuration> {

    /**
     * Find the current {@link Configuration}
     *
     * @return an {@link Optional} of the current configuration
     */
    @Query("FROM Configuration c " +
            "WHERE c.id = (SELECT MAX(co.id) FROM Configuration co)")
    Optional<Configuration> findCurrent();
}
