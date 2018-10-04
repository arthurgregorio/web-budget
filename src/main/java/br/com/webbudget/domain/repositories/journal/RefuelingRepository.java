/*
 * Copyright (C) 2016 Arthur Gregorio, AG.Software
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
package br.com.webbudget.domain.repositories.journal;

import br.com.webbudget.domain.entities.financial.Movement;
import br.com.webbudget.domain.entities.journal.Refueling;
import br.com.webbudget.domain.entities.journal.Refueling_;
import br.com.webbudget.domain.entities.registration.Vehicle;
import br.com.webbudget.domain.repositories.DefaultRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.Criteria;

import java.util.List;
import java.util.Optional;

/**
 * The {@link Refueling} repository
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 2.3.0, 05/06/2016
 */
@Repository
public interface RefuelingRepository extends DefaultRepository<Refueling> {

    /**
     * Use this method to find all {@link Refueling} linked to a {@link Movement}
     *
     * @param movementCode the movement code to find the {@link Refueling}
     * @return a list of {@link Refueling}
     */
    List<Refueling> findByMovementCode(String movementCode);

    /**
     * Use this method to find all the {@link Refueling} not accounted by any other {@link Refueling}
     *
     * @param vehicle the {@link Vehicle} to search the {@link Refueling}
     * @return the list of unaccounted {@link Refueling}
     */
    @Query("FROM Refueling re WHERE re.accounted = false AND re.vehicle = ?1")
    List<Refueling> findUnaccountedByVehicle(Vehicle vehicle);

    /**
     * This method is used to find the last {@link Refueling} odometer from a {@link Vehicle}
     *
     * @param vehicle the {@link Vehicle} to find the last odometer
     * @return the last odometer
     */
    @Query("SELECT MAX(re.odometer) FROM Refueling re WHERE re.vehicle = ?1")
    Optional<Long> findLastOdometerByVehicle(Vehicle vehicle);

    /**
     * Find the last {@link Refueling} of a given {@link Vehicle}
     *
     * @param vehicle the {@link Vehicle} to find the {@link Refueling}
     * @return the {@link Refueling}
     */
    @Query("FROM Refueling re WHERE re.id = (SELECT MAX(re.id) FROM Refueling re WHERE re.vehicle = ?1)")
    Refueling findLastByVehicle(Vehicle vehicle);

    /**
     * {@inheritDoc}
     *
     * @param criteria
     */
    @Override
    default void setOrder(Criteria<Refueling, Refueling> criteria) {
        criteria.orderDesc(Refueling_.eventDate)
                .orderDesc(Refueling_.id);
    }

    /**
     * {@inheritDoc}
     *
     * @param filter
     * @return
     */
    @Override
    default Criteria<Refueling, Refueling> getRestrictions(String filter) {
        return criteria()
                .likeIgnoreCase(Refueling_.place, filter)
                .likeIgnoreCase(Refueling_.movementCode, filter);
    }
}
