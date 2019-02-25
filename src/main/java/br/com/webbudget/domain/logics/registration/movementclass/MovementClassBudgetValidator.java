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
package br.com.webbudget.domain.logics.registration.movementclass;

import br.com.webbudget.domain.entities.registration.CostCenter;
import br.com.webbudget.domain.entities.registration.MovementClass;
import br.com.webbudget.domain.entities.registration.MovementClassType;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.repositories.registration.MovementClassRepository;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

/**
 * Validate the utilization of budget of the {@link MovementClass} {@link CostCenter}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 29/09/2018
 */
@Dependent
public class MovementClassBudgetValidator implements MovementClassSavingLogic, MovementClassUpdatingLogic {

    @Inject
    private MovementClassRepository movementClassRepository;

    /**
     * {@inheritDoc}
     *
     * @param value
     */
    @Override
    public void run(MovementClass value) {

        final CostCenter costCenter = value.getCostCenter();
        final MovementClassType classType = value.getMovementClassType();

        if (costCenter.controlBudget(classType)) {

            final List<MovementClass> classes = this.movementClassRepository
                    .findByMovementClassTypeAndCostCenter(classType, costCenter);

            final BigDecimal consumed = classes.stream()
                    .filter(mc -> !mc.equals(value))
                    .map(MovementClass::getBudget)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal available;

            if (classType == MovementClassType.REVENUE) {
                available = costCenter.getRevenuesBudget().subtract(consumed);
            } else {
                available = costCenter.getExpensesBudget().subtract(consumed);
            }

            // if the desired value is lower than the available, throw a exception
            if (available.compareTo(value.getBudget()) < 0) {
                throw new BusinessLogicException("error.movement-class.no-budget",
                        "R$ " + String.format("%10.2f", available));
            }
        }
    }
}
