/*
 * Copyright (C) 2019 Arthur Gregorio, AG.Software
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
package br.com.webbudget.domain.logics.financial.movement.period;

import br.com.webbudget.domain.entities.financial.Apportionment;
import br.com.webbudget.domain.entities.financial.PeriodMovement;
import br.com.webbudget.domain.exceptions.BusinessLogicException;

import javax.enterprise.context.Dependent;

/**
 * Validator to check if the user are not trying to save a {@link PeriodMovement} without an {@link Apportionment}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 23/02/2019
 */
@Dependent
public class ContainsApportionmentsValidator implements PeriodMovementSavingLogic, PeriodMovementUpdatingLogic {

    /**
     * {@inheritDoc}
     *
     * @param value
     */
    @Override
    public void run(PeriodMovement value) {
        if (value.getApportionments().isEmpty()) {
            throw new BusinessLogicException("error.period-movement.no-apportionments");
        }
    }
}
