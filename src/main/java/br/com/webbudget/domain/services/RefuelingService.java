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

import br.com.webbudget.application.components.builder.PeriodMovementBuilder;
import br.com.webbudget.domain.entities.financial.Apportionment;
import br.com.webbudget.domain.entities.financial.PeriodMovement;
import br.com.webbudget.domain.entities.financial.PeriodMovementType;
import br.com.webbudget.domain.entities.journal.Refueling;
import br.com.webbudget.domain.events.PeriodMovementDeleted;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.repositories.journal.RefuelingRepository;
import br.com.webbudget.domain.repositories.registration.VehicleRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

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
    private PeriodMovementService periodMovementService;

    @Inject
    private VehicleRepository vehicleRepository;
    @Inject
    private RefuelingRepository refuelingRepository;

    /**
     * Method to save the {@link Refueling}
     *
     * @param refueling the {@link Refueling} to be saved
     * @param shouldCreateMovement indicate if we should create the financial movement or not
     */
    @Transactional
    public void save(Refueling refueling, boolean shouldCreateMovement) {

        if (!refueling.isFuelsValid()) {
            throw new BusinessLogicException("error.refueling.invalid-fuels");
        }

        // get the last odometer to calculate the distance traveled
        final long lastOdometer = this.refuelingRepository
                .findLastOdometerByVehicle(refueling.getVehicle())
                .orElse(0L);

        // calculate the distance
        refueling.setFirstRefueling(lastOdometer == 0);
        refueling.calculateDistance(lastOdometer);

        // get a list of unaccounted refueling
        final List<Refueling> unaccountedItems = this.refuelingRepository.findUnaccountedByVehicle(refueling.getVehicle());

        // if its not a full tank, don't calculate the performance, if is full
        // tank start the process for performance calculation
        if (refueling.isFullTank()) {

            // calculate the last odometer by the unaccounted refueling or with the last full tank refueling
            if (!unaccountedItems.isEmpty()) {
                final long totalDistance = unaccountedItems
                        .stream()
                        .mapToLong(Refueling::getDistance)
                        .sum() + refueling.getDistance();

                // get total utilized in liters
                final BigDecimal liters = unaccountedItems.stream()
                        .map(Refueling::getLiters)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .add(refueling.getLiters());

                // put the unaccounted litters and calculate
                refueling.calculateAverageConsumption(totalDistance, liters);
            } else {
                refueling.calculateAverageConsumption();
            }

            // set the unaccounted to accounted and save
            unaccountedItems.forEach(unaccounted -> {
                unaccounted.setAccounted(true);
                unaccounted.setAccountedBy(refueling.getCode());
                this.refuelingRepository.save(unaccounted);
            });

            // set as accounted
            refueling.setAccounted(true);
        } else {
            refueling.setAccounted(false);
        }

        // create the period movement for this refueling
        if (shouldCreateMovement) {
            final PeriodMovement periodMovement = this.periodMovementService.save(this.createPeriodMovementFor(refueling));
            refueling.setPeriodMovement(periodMovement);
        }

        // finally, save the refueling
        this.refuelingRepository.save(refueling);

        final long vehicleOdometer = this.vehicleRepository.findLastOdometer(refueling.getVehicle().getId());

        // check if the vehicle odometer needs to be updated
        if (refueling.getOdometer() > vehicleOdometer) {
            refueling.updateVehicleOdometer();
            this.vehicleRepository.save(refueling.getVehicle());
        }
    }

    /**
     * Use this method to delete a {@link Refueling}
     *
     * @param refueling the {@link Refueling} to be deleted
     */
    @Transactional
    public void delete(Refueling refueling) {

        final Refueling last = this.refuelingRepository.findLastByVehicle(refueling.getVehicle());

        // check if its the last refueling 
        if (last.getId().compareTo(refueling.getId()) != 0) {
            throw new BusinessLogicException("error.refueling.not-last");
        }

        // list all accounted refueling by this refueling to change his status
        final List<Refueling> accountedItems = this.refuelingRepository.findByAccountedBy(refueling.getCode());

        accountedItems.forEach(accounted -> {
            accounted.setAccounted(false);
            accounted.setAccountedBy(null);
            this.refuelingRepository.save(refueling);
        });

        // delete the refueling
        this.refuelingRepository.attachAndRemove(refueling);

        // delete the linked period movement just if he is not accounted yet
        if (refueling.isFinancialMovementPresent() && !refueling.getPeriodMovement().isAccounted()) {
            this.periodMovementService.delete(refueling.getPeriodMovement());
        }
    }

    /**
     * Listen to events about the deletion of a {@link PeriodMovement}, in this case, remove the link between both
     * entities preventing the constraint violation
     *
     * @param periodMovement to be deleted
     */
    @Transactional
    public void removeRefuelingLinkWithPeriodMovement(@Observes @PeriodMovementDeleted PeriodMovement periodMovement) {
        this.refuelingRepository.findByPeriodMovement(periodMovement)
                .ifPresent(refueling -> {
                    refueling.setPeriodMovement(null);
                    this.refuelingRepository.save(refueling);
                });
    }

    /**
     * This method should create the {@link PeriodMovement} for the given {@link Refueling}
     *
     * @param refueling to be launched as {@link PeriodMovement}
     */
    @Transactional
    public void createFinancialMovement(Refueling refueling) {

        if (refueling.getFinancialPeriod().isClosed()) {
            throw new BusinessLogicException("error.refueling.closed-financial-period");
        }

        final var periodMovement = this.periodMovementService.save(this.createPeriodMovementFor(refueling));
        refueling.setPeriodMovement(periodMovement);

        this.refuelingRepository.save(refueling);
    }

    /**
     * Create a period movement for the given refueling {@link Refueling}
     *
     * @param refueling to used to create a {@link PeriodMovement}
     * @return the {@link PeriodMovement} to be saved
     */
    private PeriodMovement createPeriodMovementFor(Refueling refueling) {

        final PeriodMovementBuilder builder = new PeriodMovementBuilder()
                .identification(refueling.getMovementDescription())
                .description(refueling.getMovementDescription())
                .value(refueling.getCost())
                .dueDate(refueling.getEventDate())
                .financialPeriod(refueling.getFinancialPeriod())
                .type(PeriodMovementType.MOVEMENT)
                .addApportionment(new Apportionment(refueling.getCost(), refueling.getMovementClass()));

        return builder.build();
    }
}