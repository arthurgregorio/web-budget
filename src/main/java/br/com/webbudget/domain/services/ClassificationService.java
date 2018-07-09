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

import br.com.webbudget.domain.entities.registration.CostCenter;
import br.com.webbudget.domain.entities.registration.MovementClass;
import br.com.webbudget.domain.entities.registration.MovementClassType;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.repositories.registration.CostCenterRepository;
import br.com.webbudget.domain.repositories.registration.MovementClassRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * The service responsible by all the business logic involving the {@link MovementClass} and {@link CostCenter} classes
 *
 * This classes are part of the classification feature of the application, classes and cost centers enable the user to
 * classify the movements and control where the money is being spent or earned
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 09/04/2018
 */
@ApplicationScoped
public class ClassificationService {

    @Inject
    private CostCenterRepository costCenterRepository;
    @Inject
    private MovementClassRepository movementClassRepository;
    
    /**
     * Use this method to persist a {@link CostCenter}
     *
     * @param costCenter the {@link CostCenter} to persist
     */
    @Transactional
    public void save(CostCenter costCenter) {

        final Optional<CostCenter> found = this.costCenterRepository
                .findOptionalByName(costCenter.getName());

        if (found.isPresent()) {
            throw new BusinessLogicException("error.cost-center.duplicated");
        }
        this.costCenterRepository.save(costCenter);
    }

    /**
     * Use this method to update a persisted {@link CostCenter}
     *
     * @param costCenter the {@link CostCenter} to be updated
     * @return the updated {@link CostCenter}
     */
    @Transactional
    public CostCenter update(CostCenter costCenter) {
        return this.costCenterRepository.save(costCenter);
    }

    /**
     * Use this method to delete a persisted {@link CostCenter}
     *
     * @param costCenter the {@link CostCenter} to be deleted
     */
    @Transactional
    public void delete(CostCenter costCenter) {
        this.costCenterRepository.attachAndRemove(costCenter);
    }
    
    /**
     * Use this method to persist a {@link MovementClass}
     * 
     * @param movementClass the {@link MovementClass} to be persisted
     */
    @Transactional
    public void save(MovementClass movementClass) {

        final Optional<MovementClass> found = this.movementClassRepository
                .findOptionalByNameAndCostCenter_name(movementClass.getName(), 
                        movementClass.getCostCenter().getName());

        if (found.isPresent()) {
            throw new BusinessLogicException("error.movement-class.duplicated");
        }

        this.hasValidBudget(movementClass);
        this.movementClassRepository.save(movementClass);
    }

    /**
     * Use this method to update a persisted {@link MovementClass}
     *
     * @param movementClass the {@link MovementClass} to be updated
     * @return the updated {@link MovementClass}
     */
    @Transactional
    public MovementClass update(MovementClass movementClass) {

        final Optional<MovementClass> found = this.movementClassRepository
                .findOptionalByNameAndCostCenter_name(movementClass.getName(), 
                        movementClass.getCostCenter().getName());

        if (found.isPresent() && !found.get().equals(movementClass)) {
            throw new BusinessLogicException("error.movement-class.duplicated");
        }

        this.hasValidBudget(movementClass);
        return this.movementClassRepository.save(movementClass);
    }

    /**
     * Use this method to delete a persisted {@link MovementClass}
     * 
     * @param movementClass the {@link MovementClass} to be deleted
     */
    @Transactional
    public void delete(MovementClass movementClass) {
        this.movementClassRepository.attachAndRemove(movementClass);
    }
    
    /**
     * The method to check if the budget of the {@link CostCenter} is available for use
     *
     * @param movementClass the {@link MovementClass} to be validate with the {@link CostCenter}
     * @return <code>true</code> if has budget or exception if no budget was found
     */
    private boolean hasValidBudget(MovementClass movementClass) {

        final CostCenter costCenter = movementClass.getCostCenter();
        final MovementClassType classType = movementClass.getMovementClassType();

        if (costCenter.controlBudget(classType)) {

            final List<MovementClass> classes = this.movementClassRepository
                    .findByMovementClassTypeAndCostCenter(classType, costCenter);

            final BigDecimal consumed = classes.stream()
                    .filter(mc -> !mc.equals(movementClass))
                    .map(MovementClass::getBudget)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal available;

            if (classType == MovementClassType.IN) {
                available = costCenter.getRevenuesBudget().subtract(consumed);
            } else {
                available = costCenter.getExpensesBudget().subtract(consumed);
            }

            // if the desired value is lower than the available, throw a exception
            if (available.compareTo(movementClass.getBudget()) < 0) {
                final String value = "R$ " + String.format("%10.2f", available);
                throw new BusinessLogicException("error.movement-class.no-budget", value);
            }
        }
        return true;
    }
}