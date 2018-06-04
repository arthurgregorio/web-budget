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
import br.com.webbudget.domain.repositories.entries.VehicleRepository;
import br.com.webbudget.domain.repositories.journal.RefuelingRepository;
import java.math.BigDecimal;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * The refuling service
 * 
 * This service is responsible to work with the business logic of the refulings
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
     * 
     * @param refueling 
     */
    @Transactional
    public void save(Refueling refueling) {
        
        if (!refueling.isFuelsValid()) {
            throw new BusinessLogicException("error.refueling.invalid-fuels");
        }
        
        // get the last odometer to calculate the distance traveled
        long lastOdometer = this.refuelingRepository
                .findLastOdometerByVehicle(refueling.getVehicle());
        
        // calculate the distance
        refueling.setFirstRefueling(lastOdometer == 0);
        refueling.calculateDistance(lastOdometer);
        
        // get the unacounted refuelings 
        final List<Refueling> unaccounteds = this.refuelingRepository
                .findUnaccountedsByVehicle(refueling.getVehicle());

        // if its not a full tank, dont calculate the performance, if is full 
        // tank star the process for performance calculation
        if (refueling.isFullTank()) {

            // calculate the last odometer by the unacounted refulings or with
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

                // adiciona os litros atuais e manda calcular a media
                refueling.calculateAverageComsumption(totalDistance, liters);
            } else {
                refueling.calculateAverageComsumption();
            }

            // set the unaccounted to accounted and save
            unaccounteds.stream().forEach(unaccounted -> {
                unaccounted.setAccounted(true);
                unaccounted.setAccountedBy(refueling.getCode());
                this.refuelingRepository.save(unaccounted);
            });

            // set as accounted
            refueling.setAccounted(true);
        } else {
            refueling.setAccounted(false);
        }

        // finaly, save the refueling
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
     * 
     * @param refueling 
     */
    @Transactional
    public void delete(Refueling refueling) {
        
        final Refueling last = this.refuelingRepository.findLastByVehicle(
                refueling.getVehicle());
        
        // check if its the last refueling 
        if (last.getId().compareTo(refueling.getId()) != 0) {
            throw new BusinessLogicException("error.refueling.not-last");
        }

        // list the accounted refulings by this refuling to change his status   
        final List<Refueling> accounteds = this.refuelingRepository
                .findByAccountedBy(refueling.getCode());

        accounteds.stream().forEach(accounted -> {
            accounted.setAccounted(false);
            accounted.setAccountedBy(null);
            this.refuelingRepository.save(refueling);
        });

        // delete the refuling
        this.refuelingRepository.attachAndRemove(refueling);

        // fire the event to delete the movement linked to this refueling
        if (isNotBlank(refueling.getMovementCode())) {
            // TODO fire the event to delete the movement
        }
    }
}