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
package br.com.webbudget.domain.logics.financial.closing;

import br.com.webbudget.domain.entities.financial.Closing;
import br.com.webbudget.domain.entities.financial.PeriodMovement;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.logics.BusinessLogic;
import br.com.webbudget.domain.repositories.financial.PeriodMovementRepository;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.List;

/**
 * {@link BusinessLogic} used to change the status for all {@link PeriodMovement} linked to the {@link FinancialPeriod}
 * to be closed
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 10/04/2019
 */
@Dependent
public class ChangePeriodMovementStatusLogic implements ClosingSavingLogic {

    @Inject
    private PeriodMovementRepository periodMovementRepository;

    /**
     * {@inheritDoc}
     *
     * @param value
     */
    @Override
    public void run(Closing value) {

        final List<PeriodMovement> movements = this.periodMovementRepository
                .findByFinancialPeriod(value.getFinancialPeriod());

        movements.stream()
                .filter(PeriodMovement::isOpen)
                .findFirst()
                .ifPresent(movement -> {
                    throw new BusinessLogicException("error.closing.open-movement");
                });

        movements.forEach(movement -> this.periodMovementRepository.saveAndFlush(movement.prepareToAccount()));
    }
}
