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
import br.com.webbudget.domain.entities.registration.MovementClass;
import br.com.webbudget.domain.entities.registration.MovementClassType;
import br.com.webbudget.domain.entities.registration.MovementClass_;
import br.com.webbudget.domain.repositories.DefaultRepository;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.Criteria;

import javax.persistence.metamodel.SingularAttribute;
import java.util.List;
import java.util.Optional;

/**
 * The {@link MovementClass} repository
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 1.0.0, 04/03/2013
 */
@Repository
public interface MovementClassRepository extends DefaultRepository<MovementClass> {

    /**
     * Find a {@link MovementClass} by the name and cost center name
     * 
     * @param name the name of the movement class
     * @param costCenterName the name of the cost center
     * @return a {@link Optional} of {@link MovementClass}
     */
    Optional<MovementClass> findOptionalByNameAndCostCenter_name(String name, String costCenterName);
    
    /**
     * Find a {@link MovementClass} by the type and the cost center id
     * 
     * @param classType the {@link MovementClassType} enum type
     * @param costCenter the {@link CostCenter}
     * @return a {@link List} of {@link MovementClass}
     */
    List<MovementClass> findByMovementClassTypeAndCostCenter(MovementClassType classType, CostCenter costCenter);
    
    /**
     * {@inheritDoc}
     * 
     * @return 
     */
    @Override
    default SingularAttribute<MovementClass, Boolean> getBlockedProperty() {
        return MovementClass_.blocked;
    }

    /**
     * {@inheritDoc}
     * 
     * @param filter
     * @return 
     */
    @Override
    default Criteria<MovementClass, MovementClass> getRestrictions(String filter) {
       return criteria()
                .likeIgnoreCase(MovementClass_.name, filter); 
    }
}
