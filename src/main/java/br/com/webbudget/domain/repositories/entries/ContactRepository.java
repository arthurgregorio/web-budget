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
package br.com.webbudget.domain.repositories.entries;

import br.com.webbudget.domain.entities.registration.Contact;
import br.com.webbudget.domain.entities.registration.Contact_;
import br.com.webbudget.domain.repositories.DefaultRepository;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.Criteria;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 3.0.0
 * @since 1.2.0, 12/04/2015
 */
@Repository
public interface ContactRepository extends DefaultRepository<Contact> {

    /**
     * 
     * @return 
     */
    @Override
    public default SingularAttribute<Contact, Boolean> getBlockedProperty() {
        return Contact_.blocked;
    }

    /**
     * 
     * @param filter
     * @return 
     */
    @Override
    public default Criteria<Contact, Contact> getRestrictions(String filter) {
       return criteria()
               .likeIgnoreCase(Contact_.name, filter)
               .likeIgnoreCase(Contact_.email, filter)
               .likeIgnoreCase(Contact_.document, filter);
    }
}
