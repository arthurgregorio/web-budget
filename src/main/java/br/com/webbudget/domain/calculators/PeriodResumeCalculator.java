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

import br.com.webbudget.application.components.ui.chart.ChartUtils;
import br.com.webbudget.domain.entities.financial.Closing;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.entities.view.OpenPeriodResume;
import br.com.webbudget.domain.repositories.financial.ClosingRepository;
import br.com.webbudget.domain.repositories.view.OpenPeriodResumeRepository;
import lombok.Getter;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.math.BigDecimal;

/**
 * Calculator used to transform and calculate the data about the {@link FinancialPeriod} results
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 17/04/2019
 */
@Dependent
public class PeriodResumeCalculator {

    @Getter
    private Closing lastClosing;
    @Getter
    private OpenPeriodResume openPeriodResume;

    @Inject
    private ClosingRepository closingRepository;
    @Inject
    private OpenPeriodResumeRepository openPeriodResumeRepository;

    /**
     * Load this calculator with some data
     */
    public void load() {
        this.lastClosing = this.closingRepository.findLastClosing().orElseGet(Closing::new);
        this.openPeriodResume = this.openPeriodResumeRepository.load().orElseGet(OpenPeriodResume::new);

        if (this.openPeriodResume.getRevenues().compareTo(BigDecimal.ZERO) != 0
                || this.openPeriodResume.getExpenses().compareTo(BigDecimal.ZERO) != 0) {
            this.openPeriodResume.setAccumulated(this.lastClosing.getAccumulated().add(this.openPeriodResume.getBalance()));
        }
    }

    /**
     * Compare the revenues to check if the value is greater, equal or less than the last {@link Closing} value
     *
     * @return 0 for equal, 1 for greater or -1 to less than values
     */
    public int compareRevenues() {
        return this.openPeriodResume.getRevenues().compareTo(this.lastClosing.getRevenues());
    }

    /**
     * Calculate the percentage increased or decreased in relation to the last {@link Closing}
     *
     * @return the percentage of increase or decrease
     */
    public int calculateRevenuesPercentage() {
        if (this.openPeriodResume.getRevenues().compareTo(BigDecimal.ZERO) != 0) {
            final BigDecimal difference = this.openPeriodResume.getRevenues().subtract(this.lastClosing.getRevenues());
            return ChartUtils.percentageOf(difference.abs(), this.openPeriodResume.getRevenues().abs());
        }
        return this.lastClosing.getFinancialPeriod() == null ? 0 : 100;
    }

    /**
     * Compare the expenses to check if the value is greater, equal or less than the last {@link Closing} value
     *
     * @return 0 for equal, 1 for greater or -1 to less than values
     */
    public int compareExpenses() {
        return this.openPeriodResume.getExpenses().compareTo(this.lastClosing.getExpenses());
    }

    /**
     * Calculate the percentage increased or decreased in relation to the last {@link Closing}
     *
     * @return the percentage of increase or decrease
     */
    public int calculateExpensesPercentage() {
        if (this.openPeriodResume.getExpenses().compareTo(BigDecimal.ZERO) != 0) {
            final BigDecimal difference = this.openPeriodResume.getExpenses().subtract(this.lastClosing.getExpenses());
            return ChartUtils.percentageOf(difference.abs(), this.openPeriodResume.getExpenses().abs());
        }
        return this.lastClosing.getFinancialPeriod() == null ? 0 : 100;
    }

    /**
     * Compare the expenses to check if the value is greater, equal or less than the last {@link Closing} value
     *
     * @return 0 for equal, 1 for greater or -1 to less than values
     */
    public int compareBalances() {
        return this.openPeriodResume.getBalance().compareTo(this.lastClosing.getBalance());
    }

    /**
     * Calculate the percentage increased or decreased in relation to the last {@link Closing}
     *
     * @return the percentage of increase or decrease
     */
    public int calculateBalancesPercentage() {
        if (this.openPeriodResume.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            final BigDecimal difference = this.openPeriodResume.getBalance().subtract(this.lastClosing.getBalance());
            return ChartUtils.percentageOf(difference.abs(), this.openPeriodResume.getBalance().abs());
        }
        return this.lastClosing.getFinancialPeriod() == null ? 0 : 100;
    }

    /**
     * Compare the expenses to check if the value is greater, equal or less than the last {@link Closing} value
     *
     * @return 0 for equal, 1 for greater or -1 to less than values
     */
    public int compareAccumulates() {
        return this.openPeriodResume.getAccumulated().compareTo(this.lastClosing.getAccumulated());
    }

    /**
     * Calculate the percentage increased or decreased in relation to the last {@link Closing}
     *
     * @return the percentage of increase or decrease
     */
    public int calculateAccumulatesPercentage() {
        if (this.openPeriodResume.getAccumulated().compareTo(BigDecimal.ZERO) != 0) {
            final BigDecimal difference = this.openPeriodResume.getAccumulated().subtract(this.lastClosing.getAccumulated());
            return ChartUtils.percentageOf(difference.abs(), this.openPeriodResume.getAccumulated().abs());
        }
        return this.lastClosing.getFinancialPeriod() == null ? 0 : 100;
    }

    /**
     * Calculate goal percentage of completion
     *
     * @return percentage of completion for this goal
     */
    public int getExpensesGoalPercentage() {
        return ChartUtils.percentageOf(this.openPeriodResume.getExpenses(),
                this.openPeriodResume.getExpensesGoal(), true);
    }

    /**
     * Calculate goal percentage of completion
     *
     * @return percentage of completion for this goal
     */
    public int getRevenuesGoalPercentage() {
        return ChartUtils.percentageOf(this.openPeriodResume.getRevenues(),
                this.openPeriodResume.getRevenuesGoal(), true);
    }

    /**
     * Calculate goal percentage of completion
     *
     * @return percentage of completion for this goal
     */
    public int getCreditCardsGoalPercentage() {
        return ChartUtils.percentageOf(this.openPeriodResume.getCreditCardExpenses(),
                this.openPeriodResume.getCreditCardGoal(), true);
    }
}