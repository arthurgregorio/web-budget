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
package br.com.webbudget.domain.repositories.registration;

import br.com.webbudget.domain.entities.registration.Contact;
import br.com.webbudget.domain.entities.registration.Contact_;
import br.com.webbudget.domain.repositories.LazyDefaultRepository;
import org.apache.deltaspike.data.api.EntityGraph;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.Criteria;

import javax.persistence.metamodel.SingularAttribute;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * The {@link Contact} repository
 *
 * @author Arthur Gregorio
 *
 * @version 3.0.0
 * @since 1.2.0, 12/04/2015
 */
@Repository
public interface ContactRepository extends LazyDefaultRepository<Contact> {

    /**
     * {@inheritDoc }
     *
     * @param id
     * @return
     */
    @Override
    @EntityGraph(value = "Contact.withTelephones")
    Optional<Contact> findById(Long id);

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    default SingularAttribute<Contact, Boolean> getEntityStateProperty() {
        return Contact_.active;
    }

    /**
     * {@inheritDoc}
     *
     * @param filter
     * @return
     */
    @Override
    default Collection<Criteria<Contact, Contact>> getRestrictions(String filter) {
        return List.of(
                this.criteria().likeIgnoreCase(Contact_.name, this.likeAny(filter)),
                this.criteria().likeIgnoreCase(Contact_.city, this.likeAny(filter)),
                this.criteria().likeIgnoreCase(Contact_.email, this.likeAny(filter)),
                this.criteria().likeIgnoreCase(Contact_.city, this.likeAny(filter)),
                this.criteria().likeIgnoreCase(Contact_.document, this.likeAny(filter)));
    }
}
