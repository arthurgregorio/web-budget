/*
 * Copyright (C) 2014 Arthur Gregorio, AG.Software
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

import br.com.webbudget.domain.calculators.PeriodMovementCalculator;
import br.com.webbudget.domain.entities.financial.Closing;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.logics.financial.closing.ClosingSavingLogic;
import br.com.webbudget.domain.logics.financial.closing.ReopenPeriodLogic;
import br.com.webbudget.domain.repositories.financial.ClosingRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigDecimal;

/**
 * Service used by the {@link Closing} process
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 1.0.0, 09/04/2014
 */
@ApplicationScoped
public class ClosingService {

    @Inject
    private ClosingRepository closingRepository;

    @Inject
    private PeriodMovementCalculator periodMovementCalculator;

    @Any
    @Inject
    private Instance<ReopenPeriodLogic> reopenPeriodLogics;
    @Any
    @Inject
    private Instance<ClosingSavingLogic> closingSavingLogics;

    /**
     * Effectively close the {@link FinancialPeriod}
     *
     * @param financialPeriod to be closed
     */
    @Transactional
    public void close(FinancialPeriod financialPeriod) {

        // use the simulation to get the values to be saved as resume
        final Closing closing = this.simulate(financialPeriod);

        // run the business logic of the closing process
        this.closingSavingLogics.forEach(logic -> logic.run(closing));

        // calculate the accumulated and save
        final BigDecimal lastClosingAccumulated = this.closingRepository.findLastClosingAccumulatedValue()
                .orElse(BigDecimal.ZERO);

        closing.setAccumulated(lastClosingAccumulated.add(closing.getBalance()));

        this.closingRepository.save(closing);
    }

    /**
     * Reopen the {@link FinancialPeriod}
     *
     * @param financialPeriod to reopened
     */
    @Transactional
    public void reopen(FinancialPeriod financialPeriod) {

        this.closingRepository.findLastClosing().ifPresent(closing -> {
            if (!closing.getFinancialPeriod().getIdentification().equals(financialPeriod.getIdentification())) {
                throw new BusinessLogicException("error.closing.not-last");
            }
        });

        this.reopenPeriodLogics.forEach(logic -> logic.run(financialPeriod));
    }

    /**
     * Simulate the closing process for the given {@link FinancialPeriod}
     *
     * @param financialPeriod to be simulated
     */
    public Closing simulate(FinancialPeriod financialPeriod) {

        if (financialPeriod == null) {
            throw new BusinessLogicException("error.closing.no-period-selected");
        }

        this.periodMovementCalculator.load(financialPeriod);

        final Closing closing = new Closing();

        closing.setCreditCardExpenses(this.periodMovementCalculator.getCreditCardExpensesValue());
        closing.setDebitCardExpenses(this.periodMovementCalculator.getDebitCardExpensesValue());
        closing.setCashExpenses(this.periodMovementCalculator.getCashExpensesValue());

        closing.setRevenues(this.periodMovementCalculator.getRevenuesValue());
        closing.setExpenses(this.periodMovementCalculator.getExpensesValue());

        closing.setBalance(closing.getRevenues().subtract(closing.getExpenses()));

        closing.setFinancialPeriod(financialPeriod);

        return closing;
    }
}
