/*
 * Copyright (C) 2015 Arthur Gregorio, AG.Software
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
package br.com.webbudget.application.controller;

import br.com.webbudget.application.components.ui.AbstractBean;
import br.com.webbudget.domain.calculators.CostCenterTotalCalculator;
import br.com.webbudget.domain.calculators.PeriodResultCalculator;
import br.com.webbudget.domain.calculators.PeriodResumeCalculator;
import br.com.webbudget.domain.entities.financial.Closing;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.entities.registration.MovementClassType;
import br.com.webbudget.domain.entities.view.OpenPeriodResume;
import lombok.Getter;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Application dashboard controller
 *
 * @author Arthur Gregorio
 *
 * @version 3.0.0
 * @since 1.0.0, 27/02/2014
 */
@Named
@ViewScoped
public class DashboardBean extends AbstractBean {

    @Getter
    private boolean loaded;

    @Getter
    private OpenPeriodResume openPeriodResume;

    @Inject
    private transient PeriodResumeCalculator periodResumeCalculator;
    @Inject
    private transient PeriodResultCalculator periodResultCalculator;
    @Inject
    private transient CostCenterTotalCalculator costCenterTotalCalculator;

    /**
     * Initialize dashboard with data
     */
    public void initialize() {

        // load the data about the financial periods
        this.periodResumeCalculator.load();

        this.openPeriodResume = this.periodResumeCalculator.getOpenPeriodResume();

        // load the data about the result of closed periods
        this.periodResultCalculator.load();

        this.executeScript("drawLineChart(" + this.periodResultCalculator.toChartModel().toJson()
                + ", 'periodResultChart')");

        // load the data about the cost centers
        this.costCenterTotalCalculator.load(MovementClassType.REVENUE);
        this.executeScript("drawPieChart(" + this.costCenterTotalCalculator.toChartModel().toJson()
                + ", 'costCenterRevenuesChart')");

        this.costCenterTotalCalculator.load(MovementClassType.EXPENSE);
        this.executeScript("drawPieChart(" + this.costCenterTotalCalculator.toChartModel().toJson()
                + ", 'costCenterExpensesChart')");

        this.loaded = true;
    }

    /**
     * Compare both revenues from all open {@link FinancialPeriod} and with the last {@link Closing} to determine if
     * we got a increase or a decrease on the revenues
     *
     * @return 0 for equal values, 1 for greater than or -1 to less than
     */
    public int compareRevenue() {
        return this.periodResumeCalculator.compareRevenues();
    }

    /**
     * Method used to calculate the percentage increased or decreased
     *
     * @return the percentage increased or decreased
     */
    public int calculateRevenuePercentage() {
        return this.periodResumeCalculator.calculateRevenuesPercentage();
    }

    /**
     * Compare both expenses from all open {@link FinancialPeriod} and with the last {@link Closing} to determine if
     * we got a increase or a decrease on the expenses
     *
     * @return 0 for equal values, 1 for greater than or -1 to less than
     */
    public int compareExpense() {
        return this.periodResumeCalculator.compareExpenses();
    }

    /**
     * Method used to calculate the percentage increased or decreased
     *
     * @return the percentage increased or decreased
     */
    public int calculateExpensePercentage() {
        return this.periodResumeCalculator.calculateExpensesPercentage();
    }

    /**
     * Compare both balances from all open {@link FinancialPeriod} and with the last {@link Closing} to determine if
     * we got a increase or a decrease on the balances
     *
     * @return 0 for equal values, 1 for greater than or -1 to less than
     */
    public int compareBalance() {
        return this.periodResumeCalculator.compareBalances();
    }

    /**
     * Method used to calculate the percentage increased or decreased
     *
     * @return the percentage increased or decreased
     */
    public int calculateBalancePercentage() {
        return this.periodResumeCalculator.calculateBalancesPercentage();
    }

    /**
     * Compare both accumulated result from all open {@link FinancialPeriod} and with the last {@link Closing} to
     * determine if we got a increase or a decrease on the accumulated
     *
     * @return 0 for equal values, 1 for greater than or -1 to less than
     */
    public int compareAccumulated() {
        return this.periodResumeCalculator.compareAccumulates();
    }

    /**
     * Method used to calculate the percentage increased or decreased
     *
     * @return the percentage increased or decreased
     */
    public int calculateAccumulatedPercentage() {
        return this.periodResumeCalculator.calculateAccumulatesPercentage();
    }

    /**
     * Calculate goal percentage of completion
     *
     * @return percentage of completion for this goal
     */
    public int calculateExpensesGoalPercentage() {
        return this.periodResumeCalculator.getExpensesGoalPercentage();
    }

    /**
     * Calculate goal percentage of completion
     *
     * @return percentage of completion for this goal
     */
    public int calculateRevenuesGoalPercentage() {
        return this.periodResumeCalculator.getRevenuesGoalPercentage();
    }

    /**
     * Calculate goal percentage of completion
     *
     * @return percentage of completion for this goal
     */
    public int calculateCreditCardsGoalPercentage() {
        return this.periodResumeCalculator.getCreditCardsGoalPercentage();
    }
}
