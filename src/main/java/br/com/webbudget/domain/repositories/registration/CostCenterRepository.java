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

import br.com.webbudget.domain.entities.registration.CostCenter;
import br.com.webbudget.domain.entities.registration.CostCenter_;
import br.com.webbudget.domain.repositories.DefaultRepository;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.Criteria;

import javax.persistence.metamodel.SingularAttribute;
import java.util.Optional;

/**
 * The {@link CostCenter} repository
 *
 * @author Arthur Gregorio
 *
 * @version 3.0.0
 * @since 1.0.0, 04/03/2013
 */
@Repository
public interface CostCenterRepository extends DefaultRepository<CostCenter> {

    /**
     * Find a {@link CostCenter} by the name
     * 
     * @param name the of the {@link CostCenter} to find
     * @return an {@link Optional} of the {@link CostCenter}
     */
    Optional<CostCenter> findOptionalByName(String name);
    
    /**
     * {@inheritDoc}
     * 
     * @return 
     */
    @Override
    default SingularAttribute<CostCenter, Boolean> getBlockedProperty() {
        return CostCenter_.blocked;
    }

    /**
     * {@inheritDoc}
     * 
     * @param filter
     * @return 
     */
    @Override
    default Criteria<CostCenter, CostCenter> getRestrictions(String filter) {
        return this.criteria()
                .likeIgnoreCase(CostCenter_.description, filter)
                .likeIgnoreCase(CostCenter_.name, filter);
    }

    /**
     * {@inheritDoc}
     * 
     * @param criteria 
     */
    @Override
    default void setOrder(Criteria<CostCenter, CostCenter> criteria) {
        criteria.orderAsc(CostCenter_.name);
    }
}
