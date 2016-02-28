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
package br.com.webbudget.application.controller.miscellany;

import br.com.webbudget.application.controller.AbstractBean;
import br.com.webbudget.domain.entity.movement.FinancialPeriod;
import br.com.webbudget.domain.entity.movement.Movement;
import br.com.webbudget.domain.entity.movement.MovementClass;
import br.com.webbudget.domain.entity.movement.MovementClassType;
import br.com.webbudget.domain.misc.MovementCalculator;
import br.com.webbudget.domain.misc.chart.donut.DonutChartModel;
import br.com.webbudget.domain.misc.chart.line.LineChartModel;
import br.com.webbudget.domain.service.FinancialPeriodService;
import br.com.webbudget.domain.service.MovementService;
import br.com.webbudget.domain.service.PeriodDetailService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;

/**
 * Bean responsavel pelo controle da view de detalhes do periodo
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 1.0.0, 11/04/2014
 */
@Named
@ViewScoped
public class PeriodDetailBean extends AbstractBean {

    @Getter
    private FinancialPeriod period;
    @Getter
    private MovementCalculator calculator;
    
    @Getter
    private List<MovementClass> revenueClasses;
    @Getter
    private List<MovementClass> expensesClasses;
    
    @Inject
    private MovementService movementService;
    @Inject
    private PeriodDetailService periodDetailService;
    @Inject
    private FinancialPeriodService financialPeriodService;

    /**
     * @param periodId
     */
    public void initialize(long periodId) {

        this.revenueClasses = new ArrayList<>();
        this.expensesClasses = new ArrayList<>();
        
        // pega o periodo e os movimentos
        try {
            this.period = this.financialPeriodService
                    .findPeriodById(periodId);
            
            final List<Movement> movements = this.movementService
                    .listOnlyMovementsByPeriod(this.period);
            
            this.calculator = new MovementCalculator(movements);
            
            // carrega as classes
            this.loadExpensesByClass();
            this.loadRevenuesByClass();
            
            // monta o grafico por dias
            final LineChartModel lineChartModel = 
                    this.periodDetailService.bulidDailyChart(this.period);
            
            this.drawLineChart("dailySummaryChart", lineChartModel);
            
            final DonutChartModel revenuesCostCenterModel = this.periodDetailService
                    .buidCostCenterChart(period, MovementClassType.IN);
            
            this.drawDonutChart("revenuesByCostCenter", revenuesCostCenterModel);

            final DonutChartModel expensesCostCenterModel = this.periodDetailService
                    .buidCostCenterChart(this.period, MovementClassType.OUT);
                    
            this.drawDonutChart("expensesByCostCenter", expensesCostCenterModel);
        } catch (Exception ex) {
            this.logger.error("Cant fill period {} details",
                    this.period.getIdentification(), ex);
        }
    }
    
    /**
     * @return volta para a listagem de periodos
     */
    public String doCancel() {
        return "listFinancialPeriods.xhtml?faces-redirect=true";
    }

    /**
     * @return procentagem da meta de pagamentos no credito
     */
    public int getPaidOnCreditPercentage() {
        return this.percentageOf(this.calculator.getTotalPaidOnCreditCard(), 
                this.period.getCreditCardGoal());
    }

    /**
     * @return porcentagem da meta de despesas
     */
    public int getExpensesGoalPercentage() {
        return this.percentageOf(this.calculator.getExpensesTotal(), 
                this.period.getExpensesGoal());
    }
    
    /**
     * @return porcentagem da meta de receitas
     */
    public int getRevenuesGoalPercentage() {
        return this.percentageOf(this.calculator.getRevenuesTotal(), 
                this.period.getRevenuesGoal());
    }

    /**
     * 
     */
    private void loadExpensesByClass() {
        this.expensesClasses = this.periodDetailService
                .fetchTopClassesAndValues(this.period, MovementClassType.OUT);
    }
    
    /**
     * 
     */
    private void loadRevenuesByClass() {
        this.revenueClasses = this.periodDetailService
                .fetchTopClassesAndValues(this.period, MovementClassType.IN);
    }
    
    /**
     * Executa uma fucking regra of Three para saber a porcentagem de um valor
     * sobre o outro
     * 
     * @param x o x da parada
     * @param total o total que seria o 100%
     * 
     * @return a porcentagem
     */
    private int percentageOf(BigDecimal x, BigDecimal total) {
        
        // se um dos dois valores for null retorna 0 de cara
        if (x == null || total == null) {
            return 0;
        }
        
        BigDecimal percentage = BigDecimal.ZERO;
        
        if (x.compareTo(total) > 0) {
            return 100;
        } else {
            percentage = x.multiply(new BigDecimal(100))
                            .divide(total, 2, RoundingMode.HALF_UP);
        }
        
        return percentage.intValue() > 100 ? 100 : percentage.intValue();
    }
}
