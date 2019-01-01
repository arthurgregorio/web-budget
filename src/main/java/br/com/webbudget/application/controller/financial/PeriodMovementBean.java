/*
 * Copyright (C) 2018 Arthur Gregorio, AG.Software
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
package br.com.webbudget.application.controller.financial;

import br.com.webbudget.application.components.ViewState;
import br.com.webbudget.application.components.table.LazyDataProvider;
import br.com.webbudget.application.components.table.LazyModel;
import br.com.webbudget.application.components.table.Page;
import br.com.webbudget.application.components.table.filter.PeriodMovementFilter;
import br.com.webbudget.application.controller.FormBean;
import br.com.webbudget.domain.entities.financial.Apportionment;
import br.com.webbudget.domain.entities.financial.PeriodMovement;
import br.com.webbudget.domain.entities.registration.Contact;
import br.com.webbudget.domain.repositories.financial.PeriodMovementRepository;
import br.com.webbudget.domain.services.PeriodMovementService;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import static br.com.webbudget.application.components.NavigationManager.PageType.*;

/**
 * The {@link PeriodMovement} view controller
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 04/12/2018
 */
@Named
@ViewScoped
public class PeriodMovementBean extends FormBean<PeriodMovement> implements LazyDataProvider<PeriodMovement> {

    @Getter
    @Setter
    private Apportionment apportionment;

    @Getter
    private final PeriodMovementFilter filter;
    @Getter
    private final LazyDataModel<PeriodMovement> dataModel;

    @Getter
    @Inject
    private PeriodMovementFormBean formBean;

    @Inject
    private PeriodMovementService periodMovementService;

    @Inject
    private PeriodMovementRepository periodMovementRepository;

    /**
     * Constructor...
     */
    public PeriodMovementBean() {
        this.filter = PeriodMovementFilter.getInstance();
        this.dataModel = new LazyModel<>(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() {
        super.initialize();
        this.temporizeHiding(this.getDefaultMessagesComponentId());
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
        this.formBean.initializeForAdding();
        this.value = this.periodMovementRepository.findById(id).orElseGet(PeriodMovement::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initializeNavigationManager() {
        this.navigation.addPage(LIST_PAGE, "listPeriodMovements.xhtml");
        this.navigation.addPage(ADD_PAGE, "formPeriodMovement.xhtml");
        this.navigation.addPage(UPDATE_PAGE, "formPeriodMovement.xhtml");
        this.navigation.addPage(DETAIL_PAGE, "detailPeriodMovement.xhtml");
        this.navigation.addPage(DELETE_PAGE, "detailPeriodMovement.xhtml");
    }

    /**
     * {@inheritDoc }
     *
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @return
     */
    @Override
    public Page<PeriodMovement> load(int first, int pageSize, String sortField, SortOrder sortOrder) {
        return this.periodMovementRepository.findAllBy(this.filter, first, pageSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doSave() {
        this.periodMovementService.save(this.value);
        this.value = new PeriodMovement();
        this.addInfo(true, "saved");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doUpdate() {
        this.value = this.periodMovementService.update(this.value);
        this.addInfo(true, "updated");
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public String doDelete() {
        this.periodMovementService.delete(this.value);
        this.addInfoAndKeep("deleted");
        return this.changeToListing();
    }

    /**
     * Change to the payment view
     *
     * @param idMovement the {@link PeriodMovement} id
     * @return the payment page
     */
    public String changeToPay(long idMovement) {
        return "";
    }

    /**
     * Use this method to update and show the {@link Contact} dialog
     */
    public void showContactDialog() {

    }

    /**
     * Use this method to update and show the {@link Apportionment} dialog
     */
    public void showApportionmentDialog() {
        this.apportionment = new Apportionment();
        this.updateAndOpenDialog("apportionmentDialog", "dialogApportionment");
    }

    /**
     * Add the {@link Apportionment} to the {@link PeriodMovement}
     */
    public void addApportionment() {
        this.value.add(this.apportionment);
    }
}
