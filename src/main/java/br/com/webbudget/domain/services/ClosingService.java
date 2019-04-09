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

import br.com.webbudget.domain.entities.financial.Closing;
import br.com.webbudget.domain.entities.financial.PeriodMovement;
import br.com.webbudget.domain.entities.financial.PeriodMovementType;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.repositories.financial.ClosingRepository;
import br.com.webbudget.domain.repositories.financial.PeriodMovementRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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
    private PeriodMovementRepository periodMovementRepository;

    /**
     * Effectively close the {@link FinancialPeriod}
     *
     * @param financialPeriod to be closed
     */
    @Transactional
    public void close(FinancialPeriod financialPeriod) {

        final Closing closing = this.simulate(financialPeriod);

        // TODO calculate the accumulated
        // TODO update financial period status to closed
        // TODO update all period movement status to accounted
        // TODO update all credit card invoices to accounted and delete the invoices without movements
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

        final List<PeriodMovement> periodMovements = this.periodMovementRepository
                .findByFinancialPeriodAndPeriodMovementType(financialPeriod, PeriodMovementType.MOVEMENT);

        final List<PeriodMovement> expenses = periodMovements.stream()
                .filter(PeriodMovement::isExpense)
                .collect(Collectors.toList());

        final List<PeriodMovement> revenues = periodMovements.stream()
                .filter(PeriodMovement::isRevenue)
                .collect(Collectors.toList());

        final Closing closing = new Closing();

        closing.setCreditCardExpenses(expenses.stream()
                .filter(PeriodMovement::isPaidWithCreditCard)
                .map(PeriodMovement::getValueWithDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        closing.setDebitCardExpenses(expenses.stream()
                .filter(PeriodMovement::isPaidWithDebitCard)
                .map(PeriodMovement::getValueWithDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        closing.setCashExpenses(expenses.stream()
                .filter(PeriodMovement::isPaidWithCash)
                .map(PeriodMovement::getValueWithDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        closing.setRevenues(revenues.stream()
                .map(PeriodMovement::getValueWithDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        closing.setExpenses(expenses.stream()
                .map(PeriodMovement::getValueWithDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        closing.setBalance(closing.getRevenues().subtract(closing.getExpenses()));

        closing.setFinancialPeriod(financialPeriod);

        return closing;
    }
}
