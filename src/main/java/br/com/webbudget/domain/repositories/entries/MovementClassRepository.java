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

import br.com.webbudget.domain.entities.entries.MovementClass;
import br.com.webbudget.domain.entities.entries.MovementClass_;
import br.com.webbudget.domain.repositories.DefaultRepository;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.Criteria;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 1.0.0, 04/03/2013
 */
@Repository
public interface MovementClassRepository extends DefaultRepository<MovementClass> {

    /**
     * 
     * @return 
     */
    @Override
    public default SingularAttribute<MovementClass, Boolean> getBlockedProperty() {
        return MovementClass_.blocked;
    }

    /**
     * 
     * @param filter
     * @return 
     */
    @Override
    public default Criteria<MovementClass, MovementClass> getRestrictions(String filter) {
       return criteria()
                .likeIgnoreCase(MovementClass_.name, filter); 
    }
}
