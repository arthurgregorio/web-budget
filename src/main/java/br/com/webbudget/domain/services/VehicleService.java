/*
 * Copyright (C) 2018 Arthur Gregorio, AG.Software
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
package br.com.webbudget.domain.services;

import br.com.webbudget.domain.entities.logbook.Vehicle;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.repositories.entries.VehicleRepository;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 20/04/2018
 */
@ApplicationScoped
public class VehicleService {

    @Inject
    private VehicleRepository vehicleRepository;
    
    /**
     *
     * @param vehicle
     */
    @Transactional
    public void save(Vehicle vehicle) {
        
        final Optional<Vehicle> found = this.vehicleRepository
                .findOptionalByLicensePlate(vehicle.getLicensePlate());
        
        if (found.isPresent()) {
            throw new BusinessLogicException("error.vehicle.duplicated");
        }
        
        this.vehicleRepository.save(vehicle);
    }

    /**
     *
     * @param vehicle
     * @return
     */
    @Transactional
    public Vehicle update(Vehicle vehicle) {
        return this.vehicleRepository.save(vehicle);
    }

    /**
     *
     * @param vehicle
     */
    @Transactional
    public void delete(Vehicle vehicle) {
        this.vehicleRepository.attachAndRemove(vehicle);
    }
}
