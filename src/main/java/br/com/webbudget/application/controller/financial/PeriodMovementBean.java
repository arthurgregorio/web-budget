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

import br.com.webbudget.application.components.dto.PeriodMovementResume;
import br.com.webbudget.application.components.ui.FormBean;
import br.com.webbudget.application.components.ui.NavigationManager;
import br.com.webbudget.application.components.ui.ViewState;
import br.com.webbudget.application.components.ui.filter.PeriodMovementFilter;
import br.com.webbudget.application.components.ui.table.LazyDataProvider;
import br.com.webbudget.application.components.ui.table.LazyModel;
import br.com.webbudget.application.components.ui.table.Page;
import br.com.webbudget.application.validator.apportionment.ApportionmentValidator;
import br.com.webbudget.domain.entities.financial.Apportionment;
import br.com.webbudget.domain.entities.financial.PeriodMovement;
import br.com.webbudget.domain.entities.registration.Contact;
import br.com.webbudget.domain.entities.registration.CostCenter;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.entities.registration.MovementClass;
import br.com.webbudget.domain.repositories.financial.PeriodMovementRepository;
import br.com.webbudget.domain.repositories.registration.ContactRepository;
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
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.webbudget.application.components.ui.NavigationManager.PageType.*;
import static br.com.webbudget.application.components.ui.NavigationManager.Parameter.of;

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
    private String contactFilter;

    @Getter
    @Setter
    private Apportionment apportionment;

    @Getter
    private PeriodMovementFilter filter;
    @Getter
    private LazyDataModel<PeriodMovement> dataModel;

    @Getter
    private PeriodMovementResume periodMovementResume;

    @Getter
    private FinancialPeriod currentPeriod;

    @Getter
    private List<Contact> contacts;
    @Getter
    private List<CostCenter> costCenters;
    @Getter
    private List<MovementClass> movementClasses;
    @Getter
    private List<FinancialPeriod> financialPeriods;

    @Inject
    private ContactRepository contactRepository;
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
        super();
        this.filter = new PeriodMovementFilter();
        this.dataModel = new LazyModel<>(this);
        this.periodMovementResume = new PeriodMovementResume();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() {
        super.initialize();

        this.costCenters = this.costCenterRepository.findAll();
        this.financialPeriods = this.financialPeriodRepository.findAll();

        this.filter.setSelectedFinancialPeriods(this.financialPeriodRepository
                .findByClosedOrderByIdentificationAsc(false));
    }

    /**
     * This initializer method take as parameter the filters to search all {@link PeriodMovement} by a given
     * {@link FinancialPeriod}, {@link CostCenter} and {@link MovementClass}
     *
     * @param periodId to use as {@link PeriodMovement} filter
     * @param costCenterId to use as {@link CostCenter} filter
     * @param movementClassId to use as {@link MovementClass} filter
     */
    public void initialize(long periodId, long costCenterId, long movementClassId) {
        this.initialize();

        // apply filters
        this.financialPeriodRepository.findById(periodId)
                .ifPresent(period -> this.filter.setSelectedFinancialPeriods(List.of(period)));
        this.costCenterRepository.findById(costCenterId)
                .ifPresent(costCenter -> this.filter.setCostCenter(costCenter));

        if (movementClassId != 0) {
            this.movementClasses = this.movementClassRepository
                    .findByCostCenterOrderByNameAsc(this.filter.getCostCenter());
            this.movementClassRepository.findById(movementClassId)
                    .ifPresent(movementClass -> this.filter.setMovementClass(movementClass));
        }

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

        if (viewState.isEditable()) {
            this.costCenters = this.costCenterRepository.findAllActive();
            this.financialPeriods = this.financialPeriodRepository.findByClosedOrderByIdentificationAsc(false);
        } else {
            this.financialPeriods = this.financialPeriodRepository.findAll();
        }

        this.currentPeriod = this.financialPeriods.stream()
                .filter(FinancialPeriod::isCurrent)
                .findFirst()
                .orElse(null);

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
        this.loadResume();
        return this.periodMovementRepository.findAllBy(this.filter, first, pageSize);
    }

    /**
     * Load the current resume for all selected {@link FinancialPeriod}
     */
    public void loadResume() {

        final List<Long> periods = this.filter.getSelectedFinancialPeriods().stream()
                .map(FinancialPeriod::getId)
                .collect(Collectors.toList());

        final BigDecimal totalOpen;
        final BigDecimal totalPaidReceived;
        final BigDecimal totalRevenues;
        final BigDecimal totalExpenses;

        if (periods.isEmpty()) {
            totalOpen = this.periodMovementRepository.calculateTotalOpen();
            totalPaidReceived = this.periodMovementRepository.calculateTotalPaidAndReceived();
            totalRevenues = this.periodMovementRepository.calculateTotalRevenues();
            totalExpenses = this.periodMovementRepository.calculateTotalExpenses();
        } else {
            totalOpen = this.periodMovementRepository.calculateTotalOpen(periods);
            totalPaidReceived = this.periodMovementRepository.calculateTotalPaidAndReceived(periods);
            totalRevenues = this.periodMovementRepository.calculateTotalRevenues(periods);
            totalExpenses = this.periodMovementRepository.calculateTotalExpenses(periods);
        }

        this.periodMovementResume.update(totalPaidReceived, totalOpen, totalRevenues, totalExpenses);
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
     * Method to be used when the user wants to save the {@link PeriodMovement} and after that pay for this
     *
     * @return the outcome to the payment window
     */
    public String doSaveAndPay() {
        final PeriodMovement saved = this.periodMovementService.save(this.value);
        return this.changeToPay(saved.getId(), ViewState.ADDING);
    }

    /**
     * Method to be used when the user wants to update the {@link PeriodMovement} and after that pay for this
     *
     * @return the outcome to the payment window
     */
    public String doUpdateAndPay() {
        final PeriodMovement saved = this.periodMovementService.update(this.value);
        return this.changeToPay(saved.getId(), ViewState.ADDING);
    }

    /**
     * Change to the payment view
     *
     * @param idMovement the {@link PeriodMovement} id
     * @return the payment page
     */
    public String changeToPay(long idMovement) {
        return this.changeToPay(idMovement, ViewState.EDITING);
    }

    /**
     * Change to the payment view
     *
     * @param idMovement the {@link PeriodMovement} id
     * @param viewState of the payment form, if we are coming from listing view, is editing
     * @return the payment page
     */
    public String changeToPay(long idMovement, ViewState viewState) {
        return NavigationManager.to("formPayment.xhtml", of("id", idMovement),
                of("viewState", viewState));
    }

    /**
     * Show the dialog with the payment details
     */
    public void showPaymentDetailDialog() {
        this.updateAndOpenDialog("detailPaymentDialog", "dialogDetailPayment");
    }

    /**
     * Open the dialog to search for a {@link Contact} to link with this {@link PeriodMovement}
     */
    public void showSearchContactDialog() {
        this.contactFilter = null;
        this.contacts = new ArrayList<>();
        this.updateAndOpenDialog("searchContactDialog", "dialogSearchContact");
    }

    /**
     * Find the {@link Contact} by the given filter
     */
    public void searchContacts() {
        this.contacts = this.contactRepository.findAllBy(this.contactFilter, true);
    }

    /**
     * On the selection is made, call this method to close the dialog and update de UI
     */
    public void onContactSelect() {
        this.updateComponent("contactBox");
        this.closeDialog("dialogSearchContact");
    }

    /**
     * Clear the selected contact
     */
    public void removeContact() {
        this.value.setContact(null);
        this.updateComponent("contactBox");
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
     *
     * FIXME remove this dup code
     */
    public void addApportionment() {
        this.apportionmentValidators.forEach(validator -> validator.validate(this.apportionment, this.value));
        this.value.add(this.apportionment);
        this.updateComponent("inValue");
        this.updateComponent("apportionmentBox");
        this.closeDialog("dialogApportionment");
    }

    /**
     * Event to find {@link MovementClass} filtering by the selected {@link CostCenter}. Used on the form UI
     */
    public void onCostCenterSelectAtForm() {
        this.movementClasses = this.movementClassRepository
                .findByActiveAndCostCenterOrderByNameAsc(true, this.apportionment.getCostCenter());
    }

    /**
     * Event to find {@link MovementClass} filtering by the selected {@link CostCenter}. Used on the listing UI
     */
    public void onCostCenterSelectedAtListing() {
        this.movementClasses = this.movementClassRepository
                .findByActiveAndCostCenterOrderByNameAsc(true, this.filter.getCostCenter());
    }

    /**
     * Autocomplete method used by the auto complete input on the filters area
     *
     * @param query text to search for the financial period
     * @return a {@link List} with the {@link FinancialPeriod} found
     */
    public List<FinancialPeriod> completeFinancialPeriod(String query) {
        return this.financialPeriodRepository.findByIdentificationLikeIgnoreCaseOrderByCreatedOnDesc(query + "%");
    }

    /**
     * Clear all filters applied to the current search
     */
    public void clearFilters() {
        this.filter.clear();
        this.filter.setSelectedFinancialPeriods(this.financialPeriodRepository
                .findByClosedOrderByIdentificationAsc(false));
        this.updateComponent("periodMovementGrid");
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