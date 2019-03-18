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

import br.com.webbudget.domain.entities.configuration.Grant;
import br.com.webbudget.domain.entities.configuration.Group;
import br.com.webbudget.domain.repositories.DefaultRepository;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

/**
 * The {@link Grant} repository
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 28/12/2017
 */
@Repository
public interface GrantRepository extends DefaultRepository<Grant> {

    /**
     * Find a list o {@link Grant} from a given {@link Group}
     * 
     * @param group the {@link Group} to list his {@link Grant}
     * @return a {@link List} of {@link Grant}
     */
    List<Grant> findByGroup(Group group);
}
