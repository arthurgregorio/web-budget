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
import br.com.webbudget.domain.repositories.DefaultRepository;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.Criteria;

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
    
//    /**
//     * 
//     * @param refueling
//     * @return 
//     */
//    public boolean isLast(Refueling refueling);
//    
//    /**
//     * 
//     * @param code
//     * @return 
//     */
//    public Refueling findByMovementCode(String code);
//
//    /**
//     * 
//     * @param code
//     * @return 
//     */
//    public List<Refueling> listAccountedsBy(String code);
//    
//    /**
//     * 
//     * @param vehicle
//     * @return 
//     */
//    public int findLastOdometerForVehicle(Vehicle vehicle);
//    
//    /**
//     * 
//     * @param vehicle
//     * @return 
//     */
//    public List<Refueling> findUnaccountedsForVehicle(Vehicle vehicle);
}
