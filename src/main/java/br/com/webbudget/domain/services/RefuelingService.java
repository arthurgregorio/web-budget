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

import br.com.webbudget.domain.entities.journal.Refueling;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.repositories.journal.RefuelingRepository;
import br.com.webbudget.domain.repositories.registration.VehicleRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * The {@link Refueling} service, all the logic related to this entity need to be here
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 27/05/2018
 */
@ApplicationScoped
public class RefuelingService {

    @Inject
    private VehicleRepository vehicleRepository;
    @Inject
    private RefuelingRepository refuelingRepository;
    
    /**
     * Method to save the {@link Refueling}
     * 
     * @param refueling the {@link Refueling} to be saved
     */
    @Transactional
    public void save(Refueling refueling) {
        
        if (!refueling.isFuelsValid()) {
            throw new BusinessLogicException("error.refueling.invalid-fuels");
        }
        
        // get the last odometer to calculate the distance traveled
        final Long lastOdometer = this.refuelingRepository
                .findLastOdometerByVehicle(refueling.getVehicle())
                .orElse(0L);
        
        // calculate the distance
        refueling.setFirstRefueling(lastOdometer == 0);
        refueling.calculateDistance(lastOdometer);
        
        // get a list of unaccounted refueling
        final List<Refueling> unaccounteds = this.refuelingRepository
                .findUnaccountedByVehicle(refueling.getVehicle());

        // if its not a full tank, don't calculate the performance, if is full
        // tank start the process for performance calculation
        if (refueling.isFullTank()) {

            // calculate the last odometer by the unaccounted refueling or with
            // the last full tank refueling
            if (!unaccounteds.isEmpty()) {
                final long totalDistance = unaccounteds.
                        stream()
                        .mapToLong(Refueling::getDistance)
                        .sum() + refueling.getDistance();

                // get total utilized in liters
                final BigDecimal liters = unaccounteds.stream()
                        .map(Refueling::getLiters)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .add(refueling.getLiters());

                // put the unaccounted litters and calculate
                refueling.calculateAverageConsumption(totalDistance, liters);
            } else {
                refueling.calculateAverageConsumption();
            }

            // set the unaccounted to accounted and save
            unaccounteds.forEach(unaccounted -> {
                unaccounted.setAccounted(true);
                unaccounted.setAccountedBy(refueling.getCode());
                this.refuelingRepository.save(unaccounted);
            });

            // set as accounted
            refueling.setAccounted(true);
        } else {
            refueling.setAccounted(false);
        }

        // finally, save the refueling
        this.refuelingRepository.save(refueling);

        final long vehicleOdometer = this.vehicleRepository
                .findLastOdometer(refueling.getVehicle().getId());

        // check if the vehicle odometer needs to be updated
        if (refueling.getOdometer() > vehicleOdometer) {
            refueling.updateVehicleOdometer();
            this.vehicleRepository.save(refueling.getVehicle());
        }
        
        // TODO after save the refueling, save the financial movement 
    }

    /**
     * Use this method to delete a {@link Refueling}
     * 
     * @param refueling the {@link Refueling} to be deleted
     */
    @Transactional
    public void delete(Refueling refueling) {
        
        final Refueling last = this.refuelingRepository.findLastByVehicle(
                refueling.getVehicle());
        
        // check if its the last refueling 
        if (last.getId().compareTo(refueling.getId()) != 0) {
            throw new BusinessLogicException("error.refueling.not-last");
        }

        // list the accounted refuelings by this refueling to change his status
        final List<Refueling> accounteds = this.refuelingRepository
                .findByMovementCode(refueling.getCode());

        accounteds.forEach(accounted -> {
            accounted.setAccounted(false);
            accounted.setAccountedBy(null);
            this.refuelingRepository.save(refueling);
        });

        // delete the refueling
        this.refuelingRepository.attachAndRemove(refueling);

        // fire the event to delete the movement linked to this refueling
        if (isNotBlank(refueling.getMovementCode())) {
            // TODO fire the event to delete the movement
        }
    }
}