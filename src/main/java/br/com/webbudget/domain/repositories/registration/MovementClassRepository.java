/*
 * Copyright (C) 2013 Arthur Gregorio, AG.Software
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

import br.com.webbudget.domain.entities.registration.*;
import br.com.webbudget.domain.repositories.LazyDefaultRepository;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.Criteria;

import javax.persistence.metamodel.SingularAttribute;
import java.util.Collection;
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
public interface MovementClassRepository extends LazyDefaultRepository<MovementClass> {

    /**
     * Find a {@link MovementClass} by the name and cost center name
     *
     * @param name the name of the movement class
     * @param costCenterName the name of the cost center
     * @return a {@link Optional} of {@link MovementClass}
     */
    Optional<MovementClass> findByNameAndCostCenter_name(String name, String costCenterName);

    /**
     * Find a {@link MovementClass} by the type and the cost center id
     *
     * @param classType the {@link MovementClassType} enum type
     * @param costCenter the {@link CostCenter}
     * @return a {@link List} of {@link MovementClass}
     */
    List<MovementClass> findByMovementClassTypeAndCostCenter(MovementClassType classType, CostCenter costCenter);

    /**
     * Find all {@link MovementClass} filtering by the status and {@link CostCenter}
     *
     * @param active if is active or not
     * @param costCenter the {@link CostCenter}
     * @return the {@link List} of {@link MovementClass}
     */
    List<MovementClass> findByActiveAndCostCenterOrderByNameAsc(boolean active, CostCenter costCenter);

    /**
     * Find all {@link MovementClass} using the {@link CostCenter} as a filter
     *
     * @param costCenter to use as a filter
     * @return a {@link List} of {@link MovementClass}
     */
    List<MovementClass> findByCostCenterOrderByNameAsc(CostCenter costCenter);

    /**
     * {@inheritDoc}
     *
     * @param criteria
     */
    @Override
    default void setOrder(Criteria<MovementClass, MovementClass> criteria) {
        criteria.orderAsc(MovementClass_.name);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    default SingularAttribute<MovementClass, Boolean> getEntityStateProperty() {
        return MovementClass_.active;
    }

    /**
     * {@inheritDoc}
     *
     * @param filter
     * @return
     */
    @Override
    default Collection<Criteria<MovementClass, MovementClass>> getRestrictions(String filter) {
        return List.of(
                this.criteria().likeIgnoreCase(MovementClass_.name, this.likeAny(filter)),
                this.criteria().join(MovementClass_.costCenter,
                        where(CostCenter.class).likeIgnoreCase(CostCenter_.name, this.likeAny(filter))));
    }
}
