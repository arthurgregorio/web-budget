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
import br.com.webbudget.domain.service.FinancialPeriodService;
import br.com.webbudget.domain.service.MovementService;
import br.com.webbudget.domain.service.PeriodDetailService;
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
                    .listMovementsByPeriod(this.period);
            
            this.calculator = new MovementCalculator(movements);
            
            // carrega as classes
            this.loadExpensesByClass();
            this.loadRevenuesByClass();
        } catch (Exception ex) {
            this.logger.error(ex.getMessage());
            this.addError(true, "error.undefined-error", ex.getMessage());
        }
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
     * @return volta para a listagem de periodos
     */
    public String doCancel() {
        return "listFinancialPeriods.xhtml?faces-redirect=true";
    }
}
