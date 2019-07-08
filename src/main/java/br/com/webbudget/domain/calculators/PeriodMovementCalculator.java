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
package br.com.webbudget.domain.calculators;

import br.com.webbudget.domain.entities.financial.PeriodMovement;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.entities.registration.Wallet;
import br.com.webbudget.domain.repositories.financial.PeriodMovementRepository;
import br.com.webbudget.domain.repositories.registration.FinancialPeriodRepository;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Simple calculator to encapsulate the logic of getting the information about the values for a given
 * {@link FinancialPeriod} or for all open (and expired only) {@link FinancialPeriod}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 09/04/2019
 */
@Dependent
public class PeriodMovementCalculator {

    private List<PeriodMovement> expenses;
    private List<PeriodMovement> revenues;
    private List<PeriodMovement> movements;

    @Inject
    private PeriodMovementRepository periodMovementRepository;
    @Inject
    private FinancialPeriodRepository financialPeriodRepository;

    /**
     * Load the calculator using a given {@link FinancialPeriod}
     *
     * @param financialPeriod to be used to search the {@link PeriodMovement} and load this calculator
     */
    public void load(FinancialPeriod financialPeriod) {
        this.movements = this.periodMovementRepository.findByFinancialPeriod(financialPeriod);
        this.splitByType();
    }

    /**
     * Load the calculator using only the actual open {@link FinancialPeriod}
     *
     * If more than one is open (or expired) it will be used too
     */
    public void load() {
        final List<FinancialPeriod> openPeriods = this.financialPeriodRepository.findByClosedOrderByIdentificationAsc(false);
        openPeriods.forEach(period -> this.movements.addAll(this.periodMovementRepository.findByFinancialPeriod(period)));
        this.splitByType();
    }


    /**
     * Split the {@link PeriodMovement} in two lists to help the process of getting the information
     */
    private void splitByType() {

        this.expenses = this.movements.stream()
                .filter(PeriodMovement::isExpense)
                .collect(Collectors.toList());

        this.revenues = this.movements.stream()
                .filter(PeriodMovement::isRevenue)
                .collect(Collectors.toList());
    }

    /**
     * Calculate the value with expenses paid with a credit card
     *
     * @return total value
     */
    public BigDecimal getCreditCardExpensesValue() {
        return this.expenses.stream()
                .filter(PeriodMovement::isPaidWithCreditCard)
                .map(PeriodMovement::getValueWithDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calculate the value with expenses paid with a debit card
     *
     * @return total value
     */
    public BigDecimal getDebitCardExpensesValue() {
        return this.expenses.stream()
                .filter(PeriodMovement::isPaidWithDebitCard)
                .map(PeriodMovement::getValueWithDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calculate the value with expenses paid with a cash (o direct to a {@link Wallet})
     *
     * @return total value
     */
    public BigDecimal getCashExpensesValue() {
        return this.expenses.stream()
                .filter(PeriodMovement::isPaidWithCash)
                .map(PeriodMovement::getValueWithDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calculate the value of revenues
     *
     * @return total value
     */
    public BigDecimal getRevenuesValue() {
        return this.revenues.stream()
                .map(PeriodMovement::getValueWithDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calculate the value of expenses
     *
     * @return total value
     */
    public BigDecimal getExpensesValue() {
        return this.expenses.stream()
                .filter(movement -> !movement.isPaidWithCreditCard())
                .map(PeriodMovement::getValueWithDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
