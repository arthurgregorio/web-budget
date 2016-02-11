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

import br.com.webbudget.domain.entity.movement.FinancialPeriod;
import br.com.webbudget.domain.entity.movement.Movement;
import br.com.webbudget.domain.misc.MovementCalculator;
import br.com.webbudget.domain.misc.chart.ChartDatasetBuilder;
import br.com.webbudget.domain.misc.chart.ClosingChartModel;
import br.com.webbudget.domain.misc.ex.InternalServiceError;
import br.com.webbudget.domain.service.FinancialPeriodService;
import br.com.webbudget.domain.service.MovementService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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

    @Getter
    private BigDecimal accumulated;
    
    @Getter
    private List<FinancialPeriod> closedPeriods;
    
    @Getter
    private ClosingChartModel closingChartModel;
    
    @Getter
    private MovementCalculator calculator;
    
    @Inject
    private MovementService movementService;
    @Inject
    private FinancialPeriodService financialPeriodService;

    /**
     * Inicializa a dashboard do sistema
     */
    public void initialize() {
        
        this.accumulated = BigDecimal.ZERO;
        
        this.closedPeriods = new ArrayList<>();

        this.initializePeriodSummary();
        this.initializeBalanceHistory();
        this.initializeClosingsGraph();
    }
    
    /**
     * Inicializa o bloco com as informacoes sobre os periodos ativos
     */
    private void initializePeriodSummary() {
        
        List<Movement> movements = new ArrayList<>();
        
        try {
            movements = this.movementService.listMovementsByOpenFinancialPeriod();
        } catch (InternalServiceError ex) {
            this.addError(true, ex.getMessage());
        } catch (Exception ex) {
            this.addError(true, "error.undefined-error", ex.getMessage());
        }
            
        // cria a calculadora coma lista gerada
        this.calculator = new MovementCalculator(movements);
    }
    
    /**
     * Inicializa o historico de saldos
     */
    public void initializeBalanceHistory() {
        
        try {
            this.closedPeriods = 
                    this.financialPeriodService.listLastSixClosedPeriods();
            
            final FinancialPeriod latestClosedPeriod = 
                    this.financialPeriodService.findLatestClosedPeriod();
            
            if (latestClosedPeriod != null) {
                this.accumulated = latestClosedPeriod.getAccumulated()
                        .add(this.calculator.getBalance());
            } else {
                this.accumulated = this.calculator.getBalance();
            }
        } catch (InternalServiceError ex) {
            this.addError(true, ex.getMessage());
        } catch (Exception ex) {
            this.logger.error("Internal error", ex);
            this.addError(true, "error.undefined-error", ex.getMessage());
        }
    }
    
    /**
     * Monta o grafico
     */
    public void initializeClosingsGraph() {
        
        final ChartDatasetBuilder<BigDecimal> revenueDatasetBuilder = 
                new ChartDatasetBuilder<>()
                .withLabel(this.translate("dashboard.revenue-serie"))
                .filledByColor("rgba(140,217,140,0.2)")
                .withStrokeColor("rgba(51,153,51,1)")
                .withPointColor("rgba(45,134,45,1)")
                .withPointStrokeColor("#fff")
                .withPointHighlightFillColor("#fff")
                .withPointHighlightStroke("rgba(45,134,45,1)");
        
        final ChartDatasetBuilder<BigDecimal> expenseDatasetBuilder = 
                new ChartDatasetBuilder<>()
                .withLabel(this.translate("dashboard.expenses-serie"))
                .filledByColor("rgba(255,153,153,0.2)")
                .withStrokeColor("rgba(255,77,77,1)")
                .withPointColor("rgba(204,0,0,1)")
                .withPointStrokeColor("#fff")
                .withPointHighlightFillColor("#fff")
                .withPointHighlightStroke("rgba(204,0,0,1)");

        this.closingChartModel = new ClosingChartModel();
        
        // coloca o nome das series e os dados
        this.closedPeriods.stream().forEach(period -> {
                      
            this.closingChartModel.addLabel(period.getIdentification());
            
            revenueDatasetBuilder.andData(period.getRevenuesTotal());
            expenseDatasetBuilder.andData(period.getExpensesTotal());
        });
        
        this.closingChartModel.addDataset(revenueDatasetBuilder.build());
        this.closingChartModel.addDataset(expenseDatasetBuilder.build());
        
        this.executeScript("createClosingChart(" + this.closingChartModel.toJson() + ")");
    }
    
    /**
     * @return a versao da aplicacao
     */
    public String getVersion() {
        return ResourceBundle.getBundle("webbudget").getString("application.version");
    }
}
