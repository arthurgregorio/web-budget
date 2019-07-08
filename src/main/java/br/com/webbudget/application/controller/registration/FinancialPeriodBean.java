/*
 * Copyright (C) 2014 Arthur Gregorio, AG.Software
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

import br.com.webbudget.application.components.ui.FormBean;
import br.com.webbudget.application.components.ui.ViewState;
import br.com.webbudget.application.components.ui.filter.FinancialPeriodFilter;
import br.com.webbudget.application.components.ui.table.LazyDataProvider;
import br.com.webbudget.application.components.ui.table.LazyModel;
import br.com.webbudget.application.components.ui.table.Page;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.repositories.registration.FinancialPeriodRepository;
import br.com.webbudget.domain.services.ClosingService;
import br.com.webbudget.domain.services.FinancialPeriodService;
import lombok.Getter;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

import static br.com.webbudget.application.components.ui.NavigationManager.PageType.*;
import static br.com.webbudget.application.components.ui.NavigationManager.Parameter.of;

/**
 * The {@link FinancialPeriod} controller
 *
 * @author Arthur Gregorio
 *
 * @version 1.3.0
 * @since 1.0.0, 23/03/2014
 */
@Named
@ViewScoped
public class FinancialPeriodBean extends FormBean<FinancialPeriod> implements LazyDataProvider<FinancialPeriod> {

    private long periodToReopen;

    @Getter
    private boolean hasOpenPeriod;

    @Getter
    private FinancialPeriodFilter filter;

    @Getter
    private LazyDataModel<FinancialPeriod> dataModel;

    @Inject
    private ClosingService closingService;
    @Inject
    private FinancialPeriodService financialPeriodService;

    @Inject
    private FinancialPeriodRepository financialPeriodRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() {
        super.initialize();
        this.filter = new FinancialPeriodFilter();
        this.dataModel = new LazyModel<>(this);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param viewState
     */
    @Override
    public void initialize(long id, ViewState viewState) {
        this.viewState = viewState;
        this.value = this.financialPeriodRepository.findById(id).orElseGet(FinancialPeriod::new);
        this.checkForOpenPeriods();
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
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
                this.filter.getFinancialPeriodStatusValue(), first, pageSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doSave() {
        this.financialPeriodService.save(this.value);
        this.value = new FinancialPeriod();
        this.checkForOpenPeriods();
        this.addInfo(true, "saved");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doUpdate() {
        // financial period can't be updated
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public String doDelete() {
        this.financialPeriodService.delete(this.value);
        this.addInfoAndKeep("deleted");
        return this.changeToListing();
    }

    /**
     * Reopen a {@link FinancialPeriod}
     */
    public void doReopening() {
        this.closingService.reopen(this.value);
        this.updateComponent("itemsList");
        this.closeDialog("dialogReopeningConfirmation");
        this.addInfo(true, "info.financial-period.reopened", this.value.getIdentification());
        this.updateComponent("messages");
    }

    /**
     * Display a message to the user asking if he wants to reopen the {@link FinancialPeriod}
     */
    public void showReopenConfirmationDialog() {
        this.updateAndOpenDialog("reopeningConfirmationDialog", "dialogReopeningConfirmation");
    }

    public void showReopenConfirmationDialog(long periodId) {
        this.value = this.financialPeriodRepository.findById(periodId)
                .orElseThrow(() -> new IllegalStateException("Can't find financial period with id " + periodId));
        this.updateAndOpenDialog("reopeningConfirmationDialog", "dialogReopeningConfirmation");
    }

    /**
     * Helper method to navigate to the closing page of the selected {@link FinancialPeriod}
     *
     * @param financialPeriodId the id of the selected {@link FinancialPeriod}
     * @return the closing page to redirect
     */
    public String changeToClosing(long financialPeriodId) {
        return this.navigation.to("/secured/financial/closing/formClosing.xhtml",
                of("id", financialPeriodId));
    }

    /**
     * Helper method used to redirect to the statistics page
     *
     * @param financialPeriodId the id of the selected {@link FinancialPeriod}
     * @return the outcome to the statistics page
     */
    public String changeToStatistics(long financialPeriodId) {
        return this.navigation.to("/secured/registration/financialPeriod/financialPeriodStatistics.xhtml",
                of("id", financialPeriodId));
    }

    /**
     * This method is called in the form initialization to check if we already have an open {@link FinancialPeriod}
     */
    private void checkForOpenPeriods() {
        final List<FinancialPeriod> periods = this.financialPeriodRepository
                .findByClosedOrderByIdentificationAsc(false);
        this.hasOpenPeriod = periods.size() > 0;
    }

    /**
     * Clear the filter
     */
    public void clearFilters() {
        this.filter = new FinancialPeriodFilter();
    }
}
