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
package br.com.webbudget.application.controller.registration;

import br.com.webbudget.application.components.dto.Color;
import br.com.webbudget.application.components.ui.AbstractBean;
import br.com.webbudget.application.components.ui.NavigationManager;
import br.com.webbudget.application.components.ui.chart.LineChartDataset;
import br.com.webbudget.application.components.ui.chart.LineChartModel;
import br.com.webbudget.application.components.ui.chart.PieChartDataset;
import br.com.webbudget.application.components.ui.chart.PieChartModel;
import br.com.webbudget.domain.entities.financial.PeriodMovement;
import br.com.webbudget.domain.entities.registration.CostCenter;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.entities.registration.MovementClass;
import br.com.webbudget.domain.entities.registration.MovementClassType;
import br.com.webbudget.domain.entities.view.*;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.repositories.registration.FinancialPeriodRepository;
import br.com.webbudget.domain.repositories.view.*;
import br.com.webbudget.infrastructure.i18n.MessageSource;
import lombok.Getter;
import lombok.Setter;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.webbudget.application.components.ui.NavigationManager.Parameter.of;
import static br.com.webbudget.application.components.ui.chart.ChartUtils.percentageOf;

/**
 * Controller for the {@link FinancialPeriod} statistics view
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 27/04/2019
 */
@Named
@ViewScoped
public class PeriodStatisticsBean extends AbstractBean {

    @Getter
    private boolean loaded;

    @Getter
    @Setter
    private UseByMovementClass selectedUse;

    @Getter
    private BigDecimal revenues;
    @Getter
    private BigDecimal expenses;
    @Getter
    private BigDecimal balance;

    @Getter
    private FinancialPeriod financialPeriod;

    @Getter
    private List<UseByMovementClass> revenuesByMovementClass;
    @Getter
    private List<UseByMovementClass> expensesByMovementClass;

    @Inject
    private DailyUseRepository dailyUseRepository;
    @Inject
    private PeriodResultRepository periodResultRepository;
    @Inject
    private FinancialPeriodRepository financialPeriodRepository;
    @Inject
    private UseByCostCenterRepository useByCostCenterRepository;
    @Inject
    private OpenPeriodResultRepository openPeriodResultRepository;
    @Inject
    private UseByMovementClassRepository useByMovementClassRepository;

    /**
     * Initialize this view
     *
     * @param financialPeriodId to search for the {@link FinancialPeriod}
     */
    public void initialize(Long financialPeriodId) {

        this.revenues = BigDecimal.ZERO;
        this.expenses = BigDecimal.ZERO;
        this.balance = BigDecimal.ZERO;

        this.financialPeriod = this.financialPeriodRepository.findById(financialPeriodId)
                .orElseThrow(() -> new BusinessLogicException("error.period-statistics.period-not-found"));
    }

    /**
     * Load the charts model to display data on the UI
     */
    public void loadCharts() {

        this.loadRevenuesDailyChart();
        this.loadExpensesDailyChart();

        this.loadRevenuesCostCenterChart();
        this.loadExpensesCostCenterChart();

        if (this.financialPeriod.isClosed()) {

            final PeriodResult periodResult = this.periodResultRepository
                    .findByFinancialPeriodId(this.financialPeriod.getId()).orElseGet(PeriodResult::new);

            this.revenues = periodResult.getRevenues();
            this.expenses = periodResult.getExpenses();
            this.balance = periodResult.getBalance();
        } else {

            final OpenPeriodResult openPeriodResult = this.openPeriodResultRepository.findByFinancialPeriodIdAndExpired(
                    this.financialPeriod.getId(), this.financialPeriod.isExpired()).orElseGet(OpenPeriodResult::new);

            this.revenues = openPeriodResult.getRevenues();
            this.expenses = openPeriodResult.getExpenses();
            this.balance = openPeriodResult.getBalance();
        }

        this.expensesByMovementClass = this.useByMovementClassRepository
                .findByFinancialPeriodIdAndDirection(this.financialPeriod.getId(), MovementClassType.EXPENSE);
        this.revenuesByMovementClass = this.useByMovementClassRepository
                .findByFinancialPeriodIdAndDirection(this.financialPeriod.getId(), MovementClassType.REVENUE);

        this.loaded = true;
    }

    /**
     * Load revenues use chart by {@link CostCenter}
     */
    private void loadRevenuesCostCenterChart() {

        final List<UseByCostCenter> revenues = this.useByCostCenterRepository
                .findByFinancialPeriodIdAndDirection(this.financialPeriod.getId(), MovementClassType.REVENUE);

        final BigDecimal total = revenues.stream()
                .map(UseByCostCenter::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        final PieChartDataset dataset = new PieChartDataset("default");

        revenues.forEach(use -> {
            dataset.addColor(use.getCostCenterColor());
            dataset.addData(percentageOf(use.getValue(), total, true));
        });

        final PieChartModel model = new PieChartModel();

        model.addData(dataset);
        model.addAllLabel(revenues.stream()
                .map(UseByCostCenter::getCostCenter)
                .collect(Collectors.toList()));

        this.executeScript("drawPieChart(" + model.toJson() + ", 'revenuesUseByCostCenterChart')");
    }

    /**
     * Load expenses use chart by {@link CostCenter}
     */
    private void loadExpensesCostCenterChart() {

        final List<UseByCostCenter> expenses = this.useByCostCenterRepository
                .findByFinancialPeriodIdAndDirection(this.financialPeriod.getId(), MovementClassType.EXPENSE);

        final BigDecimal total = expenses.stream()
                .map(UseByCostCenter::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        final PieChartDataset dataset = new PieChartDataset("default");

        expenses.forEach(use -> {
            dataset.addColor(use.getCostCenterColor());
            dataset.addData(percentageOf(use.getValue(), total, true));
        });

        final PieChartModel model = new PieChartModel();

        model.addData(dataset);
        model.addAllLabel(expenses.stream()
                .map(UseByCostCenter::getCostCenter)
                .collect(Collectors.toList()));

        this.executeScript("drawPieChart(" + model.toJson() + ", 'expensesUseByCostCenterChart')");
    }

    /**
     * Load the chart of daily expenses
     */
    private void loadExpensesDailyChart() {

        final List<DailyUse> expenses = this.dailyUseRepository
                .findByFinancialPeriodIdAndDirection(this.financialPeriod.getId(), MovementClassType.EXPENSE);

        final Color red = new Color(220, 20, 60);

        final LineChartDataset<BigDecimal> expensesDataset = new LineChartDataset<>();

        expensesDataset.setLabel(MessageSource.get("period-statistics.chart.expenses"));
        expensesDataset.setBorderColor(red.toString());
        expensesDataset.setBackgroundColor(red.transparent().toString());
        expensesDataset.addAllData(expenses.stream()
                .map(DailyUse::getValue)
                .collect(Collectors.toList()));

        final LineChartModel<BigDecimal> model = new LineChartModel<>();

        model.addDataset(expensesDataset);
        model.addAllLabels(expenses.stream()
                .map(DailyUse::getPaymentDateAsString)
                .collect(Collectors.toList()));

        this.executeScript("drawLineChart(" + model.toJson() + ", 'dailyExpensesUseChart')");
    }

    /**
     * Load the chart of daily revenues
     */
    private void loadRevenuesDailyChart() {

        final List<DailyUse> revenues = this.dailyUseRepository
                .findByFinancialPeriodIdAndDirection(this.financialPeriod.getId(), MovementClassType.REVENUE);

        final Color green = new Color(34, 139, 34);

        final LineChartDataset<BigDecimal> revenuesDataset = new LineChartDataset<>();

        revenuesDataset.setLabel(MessageSource.get("period-statistics.chart.revenues"));
        revenuesDataset.setBorderColor(green.toString());
        revenuesDataset.setBackgroundColor(green.transparent().toString());
        revenuesDataset.addAllData(revenues.stream()
                .map(DailyUse::getValue)
                .collect(Collectors.toList()));

        final LineChartModel<BigDecimal> model = new LineChartModel<>();

        model.addDataset(revenuesDataset);
        model.addAllLabels(revenues.stream()
                .map(DailyUse::getPaymentDateAsString)
                .collect(Collectors.toList()));

        this.executeScript("drawLineChart(" + model.toJson() + ", 'dailyRevenuesUseChart')");
    }

    /**
     * Helper method to get the start date of this period as {@link String}
     *
     * @return the start date as {@link String}
     */
    public String getStartAsString() {
        return this.financialPeriod.getStart().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    /**
     * Helper method to get the end date of this period as {@link String}
     *
     * @return the end date as {@link String}
     */
    public String getEndAsString() {
        return this.financialPeriod.getEnd().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    /**
     * Redirect to the {@link PeriodMovement} list filtered by the {@link FinancialPeriod}, {@link CostCenter} and
     * {@link MovementClass}
     */
    public void changeToPeriodMovements() {

        final long periodId = this.financialPeriod.getId();
        final long costCenterId = this.selectedUse.getCostCenterId();
        final long movementClassId = this.selectedUse.getMovementClassId();

        NavigationManager.redirect("/secured/financial/movement/period/listPeriodMovements.xhtml", of("periodId", periodId),
                of("costCenterId", costCenterId), of("movementClassId", movementClassId));
    }
}
