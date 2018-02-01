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
package br.com.webbudget.domain.repositories.logbook;

import br.com.webbudget.application.components.table.Page;
import br.com.webbudget.application.components.table.PageRequest;
import br.com.webbudget.domain.entities.logbook.Refueling;
import br.com.webbudget.domain.entities.logbook.Vehicle;
import java.util.List;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 2.3.0, 05/06/2016
 */
@Repository
public interface IRefuelingRepository extends EntityRepository<Refueling, Long> {

    /**
     * 
     * @param refueling
     * @return 
     */
    public boolean isLast(Refueling refueling);
    
    /**
     * 
     * @param code
     * @return 
     */
    public Refueling findByMovementCode(String code);

    /**
     * 
     * @param code
     * @return 
     */
    public List<Refueling> listAccountedsBy(String code);
    
    /**
     * 
     * @param vehicle
     * @return 
     */
    public int findLastOdometerForVehicle(Vehicle vehicle);
    
    /**
     * 
     * @param vehicle
     * @return 
     */
    public List<Refueling> findUnaccountedsForVehicle(Vehicle vehicle);
    
    /**
     * 
     * @param filter
     * @param pageRequest
     * @return 
     */
    public Page<Refueling> listLazily(String filter, PageRequest pageRequest);
}
