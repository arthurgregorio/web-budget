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
package br.com.webbudget.application.controller.registration;

import static br.com.webbudget.application.components.NavigationManager.PageType.ADD_PAGE;
import static br.com.webbudget.application.components.NavigationManager.PageType.DELETE_PAGE;
import static br.com.webbudget.application.components.NavigationManager.PageType.DETAIL_PAGE;
import static br.com.webbudget.application.components.NavigationManager.PageType.LIST_PAGE;
import static br.com.webbudget.application.components.NavigationManager.PageType.UPDATE_PAGE;
import br.com.webbudget.application.components.ViewState;
import br.com.webbudget.application.components.table.Page;
import br.com.webbudget.application.controller.FormBean;
import br.com.webbudget.domain.entities.financial.Closing;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.repositories.registration.FinancialPeriodRepository;
import br.com.webbudget.domain.services.FinancialPeriodService;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.3.0
 * @since 1.0.0, 23/03/2014
 */
@Named
@ViewScoped
public class FinancialPeriodBean extends FormBean<FinancialPeriod> {

    @Getter
    private Closing closing;

    @Inject
    private FinancialPeriodService financialPeriodService;
    
    @Inject
    private FinancialPeriodRepository financialPeriodRepository;

    /**
     * 
     */
    @Override
    public void initialize() {
        super.initialize();
        this.temporizeHiding(this.getDefaultMessagesComponentId());
    }

    /**
     * 
     * @param id
     * @param viewState 
     */
    @Override
    public void initialize(long id, ViewState viewState) {
        this.viewState = viewState;
        this.value = this.financialPeriodRepository.findOptionalById(id)
                .orElseGet(FinancialPeriod::new);
    }

    /**
     * 
     */
    @Override
    protected void initializeNavigationManager() {
        this.navigation.addPage(LIST_PAGE, "listFinancialPeriods.xhtml");
        this.navigation.addPage(ADD_PAGE, "formFinancialPeriod.xhtml");
        this.navigation.addPage(UPDATE_PAGE, "formFinancialPeriod.xhtml");
        this.navigation.addPage(DETAIL_PAGE, "detailFinancialPeriod.xhtml");
        this.navigation.addPage(DELETE_PAGE, "detailFinancialPeriod.xhtml");
    }

    /**
     * 
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @return 
     */
    @Override
    public Page<FinancialPeriod> load(int first, int pageSize, String sortField, SortOrder sortOrder) {
        return this.financialPeriodRepository.findAllBy(this.filter.getValue(), 
                this.filter.getEntityStatusValue(), first, pageSize);
    }

    /**
     * 
     */
    @Override
    public void doSave() {
        this.financialPeriodService.save(this.value);
        this.value = new FinancialPeriod();
        this.validateOpenPeriods();
        this.addInfo(true, "financial-period.saved");
    }

    /**
     * 
     */
    @Override
    public void doUpdate() { }

    /**
     * 
     * @return 
     */
    @Override
    public String doDelete() {
        this.financialPeriodService.delete(this.value);
        this.addInfoAndKeep("financial-period.deleted");
        return this.changeToListing();
    }

    /**
     * 
     * @param id
     * @return 
     */
    public String changeToClosing(long id) {
        return "";
    }
    
    /**
     * 
     */
    public void validateOpenPeriods() {

//        // validamos se ha algum periodo em aberto
//        final List<FinancialPeriod> periods
//                = this.financialPeriodService.listOpenFinancialPeriods();
//
//        for (FinancialPeriod open : periods) {
//            if (open != null && (!open.isClosed() || !open.isExpired())) {
//                // se ja houver aberto, nega o que foi dito antes
//                this.hasOpenPeriod = true;
//                break;
//            }
//        }
    }
    
//    /**
//     * @param periodId
//     */
//    public void initialize(long periodId) {
//
//        this.revenueClasses = new ArrayList<>();
//        this.expensesClasses = new ArrayList<>();
//        
//        // pega o periodo e os movimentos
//        try {
//            this.period = this.financialPeriodService
//                    .findPeriodById(periodId);
//            
//            final List<Movement> movements = this.movementService
//                    .listOnlyMovementsByPeriod(this.period);
//            
//            this.calculator = new MovementCalculator(movements);
//            
//            // carrega as classes
//            this.loadExpensesByClass();
//            this.loadRevenuesByClass();
//            
//            // monta o grafico por dias
//            final LineChartModel lineChartModel = 
//                    this.periodDetailService.bulidDailyChart(this.period);
//            
//            this.drawLineChart("dailySummaryChart", lineChartModel);
//        } catch (Exception ex) {
//            this.logger.error("Cant fill period {} details",
//                    this.period.getIdentification(), ex);
//        }
//    }
//    
//    /**
//     * @return volta para a listagem de periodos
//     */
//    public String doCancel() {
//        return "listFinancialPeriods.xhtml?faces-redirect=true";
//    }
//
//    /**
//     * @return procentagem da meta de pagamentos no credito
//     */
//    public int getPaidOnCreditPercentage() {
//        return this.percentageOf(this.calculator.getTotalPaidOnCreditCard(), 
//                this.period.getCreditCardGoal());
//    }
//
//    /**
//     * @return porcentagem da meta de despesas
//     */
//    public int getExpensesGoalPercentage() {
//        return this.percentageOf(this.calculator.getExpensesTotal(), 
//                this.period.getExpensesGoal());
//    }
//    
//    /**
//     * @return porcentagem da meta de receitas
//     */
//    public int getRevenuesGoalPercentage() {
//        return this.percentageOf(this.calculator.getRevenuesTotal(), 
//                this.period.getRevenuesGoal());
//    }
//
//    /**
//     * 
//     */
//    private void loadExpensesByClass() {
//        this.expensesClasses = this.periodDetailService
//                .fetchTopClassesAndValues(this.period, MovementClassType.OUT);
//    }
//    
//    /**
//     * 
//     */
//    private void loadRevenuesByClass() {
//        this.revenueClasses = this.periodDetailService
//                .fetchTopClassesAndValues(this.period, MovementClassType.IN);
//    }
}
