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
package br.com.webbudget.domain.model.repository.logbook;

import br.com.webbudget.application.component.table.Page;
import br.com.webbudget.application.component.table.PageRequest;
import br.com.webbudget.domain.model.entity.logbook.Vehicle;
import br.com.webbudget.domain.model.repository.IGenericRepository;
import java.util.List;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.3.0, 05/06/2016
 */
public interface IVehicleRepository extends IGenericRepository<Vehicle, Long> {

    /**
     * 
     * @param vehicle
     * @return 
     */
    public int findLastOdometer(Vehicle vehicle);
    
    /**
     *
     * @param isBlocked
     * @return
     */
    public List<Vehicle> listByStatus(Boolean isBlocked);

    /**
     * 
     * @param isBlocked
     * @param pageRequest
     * @return 
     */
    public Page<Vehicle> listLazilyByStatus(Boolean isBlocked, PageRequest pageRequest);
}
