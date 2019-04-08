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
package br.com.webbudget.domain.logics.registration.financialperiod;

import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.repositories.registration.FinancialPeriodRepository;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.time.LocalDate;

/**
 * The {@link FinancialPeriod} dates validator
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 27/09/2018
 */
@Dependent
public class PeriodDatesValidator implements PeriodSavingLogic {

    @Inject
    private FinancialPeriodRepository financialPeriodRepository;

    /**
     * {@inheritDoc}
     *
     * @param value
     */
    @Override
    public void run(FinancialPeriod value) {

        final LocalDate start = value.getStart();
        final LocalDate end = value.getEnd();

        // check for periods with the same period of this one
        final Long total = this.financialPeriodRepository.validatePeriodDates(start, end);

        if (total > 0) {
            throw new BusinessLogicException("error.financial-period.colliding-dates");
        }

        // if the start is after the end, show error
        if (start.isAfter(end)) {
            throw new BusinessLogicException("error.financial-period.invalid-start-end");
        }
    }
}
