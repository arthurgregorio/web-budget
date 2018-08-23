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

import br.com.webbudget.domain.entities.journal.Refueling;
import br.com.webbudget.domain.entities.journal.Refueling_;
import br.com.webbudget.domain.entities.registration.Vehicle;
import br.com.webbudget.domain.repositories.DefaultRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.Criteria;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 2.3.0, 05/06/2016
 */
@Repository
public interface RefuelingRepository extends DefaultRepository<Refueling> {

    /**
     * 
     * @param movementCode
     * @return 
     */
    List<Refueling> findByAccountedBy(String movementCode);
    
    /**
     * 
     * @param vehicle 
     * @return 
     */
    @Query("FROM Refueling re WHERE re.accounted = false AND re.vehicle = ?1")
    List<Refueling> findUnaccountedByVehicle(Vehicle vehicle);
    
    /**
     * 
     * @param vehicle 
     * @return 
     */
    @Query("SELECT MAX(re.odometer) FROM Refueling re WHERE re.vehicle = ?1")
    Optional<Long> findLastOdometerByVehicle(Vehicle vehicle);
    
    /**
     * 
     * @param vehicle
     * @return 
     */
    @Query("FROM Refueling re WHERE re.id = (SELECT MAX(re.id) FROM Refueling re WHERE re.vehicle = ?1)")
    Refueling findLastByVehicle(Vehicle vehicle);
    
    /**
     * 
     * @param criteria 
     */
    @Override
    default void setOrder(Criteria<Refueling, Refueling> criteria) {
        criteria.orderDesc(Refueling_.eventDate);
    }
    
    /**
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
