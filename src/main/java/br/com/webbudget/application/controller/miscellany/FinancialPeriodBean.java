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

import static br.com.webbudget.application.components.NavigationManager.PageType.ADD_PAGE;
import static br.com.webbudget.application.components.NavigationManager.PageType.DELETE_PAGE;
import static br.com.webbudget.application.components.NavigationManager.PageType.DETAIL_PAGE;
import static br.com.webbudget.application.components.NavigationManager.PageType.LIST_PAGE;
import static br.com.webbudget.application.components.NavigationManager.PageType.UPDATE_PAGE;
import br.com.webbudget.application.components.ViewState;
import br.com.webbudget.application.controller.FormBean;
import br.com.webbudget.domain.entities.miscellany.Closing;
import br.com.webbudget.domain.entities.miscellany.FinancialPeriod;
import br.com.webbudget.domain.repositories.miscellany.FinancialPeriodRepository;
import br.com.webbudget.domain.services.FinancialPeriodService;
import java.util.List;
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
    public List<FinancialPeriod> load(int first, int pageSize, String sortField, SortOrder sortOrder) {
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
}
