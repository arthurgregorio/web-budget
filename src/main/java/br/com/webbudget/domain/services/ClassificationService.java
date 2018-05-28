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
import br.com.webbudget.domain.repositories.entries.CostCenterRepository;
import br.com.webbudget.domain.repositories.entries.MovementClassRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

/**
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
     *
     * @param costCenter
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
     *
     * @param costCenter
     * @return
     */
    @Transactional
    public CostCenter update(CostCenter costCenter) {
        return this.costCenterRepository.save(costCenter);
    }

    /**
     *
     * @param costCenter
     */
    @Transactional
    public void delete(CostCenter costCenter) {
        this.costCenterRepository.attachAndRemove(costCenter);
    }
    
    /**
     * 
     * @param movementClass 
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
     *
     * @param movementClass
     * @return
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
     * 
     * @param movementClass 
     */
    @Transactional
    public void delete(MovementClass movementClass) {
        this.movementClassRepository.attachAndRemove(movementClass);
    }
    
    /**
     * The method to check if the bugdet of the costcenter is available for use
     *
     * @param movementClass the class to be validate againts his cost center
     * @return true if has budget or exception if no budget is found
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

            // caso o valor disponivel seja menor que o desejado, exception!
            if (available.compareTo(movementClass.getBudget()) < 0) {
                final String value = "R$ " + String.format("%10.2f", available);
                throw new BusinessLogicException("error.movement-class.no-budget", value);
            }
        }
        return true;
    }
}