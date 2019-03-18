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

import br.com.webbudget.domain.entities.configuration.Group;
import br.com.webbudget.domain.entities.configuration.Group_;
import br.com.webbudget.domain.repositories.LazyDefaultRepository;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.Criteria;

import javax.persistence.metamodel.SingularAttribute;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * The {@link Group} repository
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 28/12/2017
 */
@Repository
public interface GroupRepository extends LazyDefaultRepository<Group> {

    /**
     * Fin a {@link Group} by the name
     *
     * @param name the name of the {@link Group} to search
     * @return an {@link Optional} of the {@link Group}
     */
    Optional<Group> findByName(String name);

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    default SingularAttribute<Group, Boolean> getEntityStateProperty() {
        return Group_.active;
    }

    /**
     * {@inheritDoc}
     *
     * @param filter
     * @return
     */
    @Override
    default Collection<Criteria<Group, Group>> getRestrictions(String filter) {
        return List.of(this.criteria().likeIgnoreCase(Group_.name, this.likeAny(filter)));
    }
}
