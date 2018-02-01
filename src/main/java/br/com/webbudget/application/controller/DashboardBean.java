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

import br.com.webbudget.application.components.chart.donut.DonutChartModel;
import br.com.webbudget.domain.entities.miscellany.FinancialPeriod;
import br.com.webbudget.domain.entities.financial.Movement;
import br.com.webbudget.domain.misc.MovementCalculator;
import br.com.webbudget.application.components.chart.line.LineChartDatasetBuilder;
import br.com.webbudget.application.components.chart.line.LineChartModel;
import br.com.webbudget.domain.exceptions.ApplicationException;
import br.com.webbudget.domain.entities.entries.MovementClassType;
import br.com.webbudget.domain.services.FinancialPeriodService;
import br.com.webbudget.domain.services.MovementService;
import br.com.webbudget.domain.services.PeriodDetailService;
import br.com.webbudget.infraestructure.utils.Configurations;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;

/**
 * Mbean utilizado na dashboard do sistema, por ele carregamos os graficos da
 * dashboard e tambem alguns elementos da template, como o nome no botao de
 * informacoes da conta do usuario
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 1.0.0, 27/02/2014
 */
@Named
@ViewScoped
public class DashboardBean extends AbstractBean {

//    @Getter
//    private LineChartModel lineChartModel;
//
//    @Getter
//    private BigDecimal accumulated;
//    @Getter
//    private BigDecimal totalRevenueGoal;
//    @Getter
//    private BigDecimal totalExpensesGoal;
//    @Getter
//    private BigDecimal totalCreditCardGoal;
//
//    @Getter
//    private DonutChartModel expensesCostCenterModel;
//    @Getter
//    private DonutChartModel revenuesCostCenterModel;
//
//    @Getter
//    private int percentageExpenses;
//    @Getter
//    private int percentageRevenues;
//    @Getter
//    private int percentageCreditCard;
//
//    private List<FinancialPeriod> openPeriods;
//    private List<FinancialPeriod> closedPeriods;
//
//    @Getter
//    private MovementCalculator calculator;
//
//    @Inject
//    private MovementService movementService;
//    @Inject
//    private PeriodDetailService periodDetailService;
//    @Inject
//    private FinancialPeriodService financialPeriodService;
//
//    /**
//     * Inicializa a dashboard do sistema
//     */
//    public void initialize() {
//
//        this.accumulated = BigDecimal.ZERO;
//        this.totalRevenueGoal = BigDecimal.ZERO;
//        this.totalExpensesGoal = BigDecimal.ZERO;
//        this.totalCreditCardGoal = BigDecimal.ZERO;
//
//        this.closedPeriods = new ArrayList<>();
//
//        try {
//            this.openPeriods = this.financialPeriodService
//                    .listOpenFinancialPeriods();
//
//            this.initializePeriodSummary();
//            this.initializeBalanceHistory();
//            this.initializeClosingsGraph();
//            this.initializeCostCentersGraphs();
//
//            this.countGoals();
//            this.calculatePercentages();
//        } catch (InternalServiceError ex) {
//            this.addError(true, ex.getMessage(), ex.getParameters());
//        } catch (Exception ex) {
//            this.logger.error(ex.getMessage(), ex);
//            this.addError(true, "error.undefined-error", ex.getMessage());
//        }
//    }
//
//    /**
//     * @return a versao da aplicacao
//     */
//    public String getVersion() {
//        return Configuration.getConfiguration("application.version");
//    }
//
//    /**
//     * Porcentagem da meta de receitas
//     */
//    private void calculatePercentages() {
//
//        this.percentageCreditCard = this.percentageOf(
//                this.calculator.getTotalPaidOnCreditCard(), this.totalCreditCardGoal);
//
//        this.percentageExpenses = this.percentageOf(
//                this.calculator.getExpensesTotal(), this.totalExpensesGoal);
//
//        this.percentageRevenues = this.percentageOf(
//                this.calculator.getRevenuesTotal(), this.totalRevenueGoal);
//    }
//
//    /**
//     * Inicializa o bloco com as informacoes sobre os periodos ativos
//     */
//    private void initializePeriodSummary() {
//
//        final List<Movement> movements = new ArrayList<>();
//
//        this.openPeriods.stream().forEach(period -> {
//            movements.addAll(
//                    this.movementService.listOnlyMovementsByPeriod(period));
//        });
//
//        // cria a calculadora coma lista gerada
//        this.calculator = new MovementCalculator(movements);
//    }
//
//    /**
//     * Inicializa o historico de saldos
//     */
//    private void initializeBalanceHistory() {
//
//        this.closedPeriods
//                = this.financialPeriodService.listLastSixClosedPeriods();
//
//        final FinancialPeriod latestClosedPeriod
//                = this.financialPeriodService.findLatestClosedPeriod();
//
//        if (latestClosedPeriod != null) {
//            this.accumulated = latestClosedPeriod.getAccumulated()
//                    .add(this.calculator.getBalance());
//        } else {
//            this.accumulated = this.calculator.getBalance();
//        }
//    }
//
//    /**
//     * Monta o grafico
//     */
//    private void initializeClosingsGraph() {
//
//        final LineChartDatasetBuilder<BigDecimal> revenueDatasetBuilder
//                = new LineChartDatasetBuilder<>()
//                .withLabel(this.translate("dashboard.revenue-serie"))
//                .filledByColor("rgba(140,217,140,0.2)")
//                .withStrokeColor("rgba(51,153,51,1)")
//                .withPointColor("rgba(45,134,45,1)")
//                .withPointStrokeColor("#fff")
//                .withPointHighlightFillColor("#fff")
//                .withPointHighlightStroke("rgba(45,134,45,1)");
//
//        final LineChartDatasetBuilder<BigDecimal> expenseDatasetBuilder
//                = new LineChartDatasetBuilder<>()
//                .withLabel(this.translate("dashboard.expenses-serie"))
//                .filledByColor("rgba(255,153,153,0.2)")
//                .withStrokeColor("rgba(255,77,77,1)")
//                .withPointColor("rgba(204,0,0,1)")
//                .withPointStrokeColor("#fff")
//                .withPointHighlightFillColor("#fff")
//                .withPointHighlightStroke("rgba(204,0,0,1)");
//
//        this.lineChartModel = new LineChartModel();
//
//        // ordena pela inclusao, do mais velho para o menos novo
//        this.closedPeriods.sort((v1, v2)
//                -> v1.getInclusion().compareTo(v2.getInclusion()));
//
//        // coloca o nome das series e os dados
//        this.closedPeriods.stream().forEach(period -> {
//
//            this.lineChartModel.addLabel(period.getIdentification());
//
//            revenueDatasetBuilder.andData(period.getRevenuesTotal());
//            expenseDatasetBuilder.andData(period.getExpensesTotal());
//        });
//
//        this.lineChartModel.addDataset(revenueDatasetBuilder.build());
//        this.lineChartModel.addDataset(expenseDatasetBuilder.build());
//
//        if (!this.lineChartModel.isEmptyChart()) {
//            this.drawLineChart("closingsChart", lineChartModel);
//        }
//    }
//
//    /**
//     * A somatoria das metas para os periodos em aberto
//     */
//    private void countGoals() {
//        this.totalCreditCardGoal = this.openPeriods.stream()
//                .map(FinancialPeriod::getCreditCardGoal)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//        this.totalExpensesGoal = this.openPeriods.stream()
//                .map(FinancialPeriod::getExpensesGoal)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//        this.totalRevenueGoal = this.openPeriods.stream()
//                .map(FinancialPeriod::getRevenuesGoal)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//    }
//
//    /**
//     * Inicializa o grafico de consumo e receita por centro de custo
//     */
//    private void initializeCostCentersGraphs() {
//
//        this.revenuesCostCenterModel = this.periodDetailService
//                .buidCostCenterChart(this.openPeriods, MovementClassType.IN);
//
//        if (this.revenuesCostCenterModel.containsData()) {
//            this.drawDonutChart("revenuesByCostCenter", this.revenuesCostCenterModel);
//        }
//
//        this.expensesCostCenterModel = this.periodDetailService
//                .buidCostCenterChart(this.openPeriods, MovementClassType.OUT);
//
//        if (this.expensesCostCenterModel.containsData()) {
//            this.drawDonutChart("expensesByCostCenter", this.expensesCostCenterModel);
//        }
//    }
}
