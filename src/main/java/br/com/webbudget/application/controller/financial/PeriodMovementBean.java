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
import br.com.webbudget.application.validator.apportionment.ApportionmentValidator;
import br.com.webbudget.domain.entities.financial.Apportionment;
import br.com.webbudget.domain.entities.financial.PeriodMovement;
import br.com.webbudget.domain.entities.registration.Contact;
import br.com.webbudget.domain.entities.registration.CostCenter;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.entities.registration.MovementClass;
import br.com.webbudget.domain.repositories.financial.PeriodMovementRepository;
import br.com.webbudget.domain.repositories.registration.CostCenterRepository;
import br.com.webbudget.domain.repositories.registration.FinancialPeriodRepository;
import br.com.webbudget.domain.repositories.registration.MovementClassRepository;
import br.com.webbudget.domain.services.PeriodMovementService;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
    private PeriodMovementFilter filter;
    @Getter
    private LazyDataModel<PeriodMovement> dataModel;

    @Getter
    private FinancialPeriod currentPeriod;

    @Getter
    private List<CostCenter> costCenters;
    @Getter
    private List<MovementClass> movementClasses;
    @Getter
    private List<FinancialPeriod> financialPeriods;

    @Inject
    private CostCenterRepository costCenterRepository;
    @Inject
    private MovementClassRepository movementClassRepository;
    @Inject
    private PeriodMovementRepository periodMovementRepository;
    @Inject
    private FinancialPeriodRepository financialPeriodRepository;

    @Inject
    private PeriodMovementService periodMovementService;

    @Any
    @Inject
    private Instance<ApportionmentValidator> apportionmentValidators;

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

        if (viewState.isAdding()) {
            this.initializeForAdding();
        }

        this.value = this.periodMovementRepository.findById(id).orElseGet(PeriodMovement::new);
    }

    /**
     * Initialize the bean in the adding state
     */
    private void initializeForAdding() {

        this.costCenters = this.costCenterRepository.findAllActive();
        this.financialPeriods = this.financialPeriodRepository.findByClosed(false);

        this.currentPeriod = this.financialPeriods.stream()
                .filter(FinancialPeriod::isCurrent)
                .findFirst()
                .orElse(null);
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
        this.apportionment = new Apportionment(this.value.calculateRemainingTotal());
        this.updateAndOpenDialog("apportionmentDialog", "dialogApportionment");
    }

    /**
     * Add the {@link Apportionment} to the {@link PeriodMovement}
     */
    public void addApportionment() {
        this.apportionmentValidators.forEach(validator -> validator.validate(this.apportionment, this.value));
        this.value.add(this.apportionment);
        this.updateComponent("inValue");
        this.updateComponent("apportionmentBox");
        this.closeDialog("dialogApportionment");
    }

    /**
     * Event to find {@link MovementClass} filtering by the selected {@link CostCenter}
     */
    public void onCostCenterSelect() {
        this.movementClasses = this.movementClassRepository
                .findByActiveAndCostCenter(true, this.apportionment.getCostCenter());
    }

    /**
     * Get the current {@link FinancialPeriod} start date in string format
     *
     * @return the start date formatted in {@link String} type
     */
    public String getCurrentPeriodStart() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.currentPeriod.getStart());
    }

    /**
     * Get the current {@link FinancialPeriod} end date in string format
     *
     * @return the end date formatted in {@link String} type
     */
    public String getCurrentPeriodEnd() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.currentPeriod.getEnd());
    }
}