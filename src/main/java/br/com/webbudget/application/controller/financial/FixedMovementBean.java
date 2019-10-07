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
package br.com.webbudget.application.controller.financial;

import br.com.webbudget.application.components.ui.FormBean;
import br.com.webbudget.application.components.ui.ViewState;
import br.com.webbudget.application.components.ui.filter.FixedMovementFilter;
import br.com.webbudget.application.components.ui.table.LazyDataProvider;
import br.com.webbudget.application.components.ui.table.LazyModel;
import br.com.webbudget.application.components.ui.table.Page;
import br.com.webbudget.application.validator.apportionment.ApportionmentValidator;
import br.com.webbudget.domain.entities.financial.Apportionment;
import br.com.webbudget.domain.entities.financial.FixedMovement;
import br.com.webbudget.domain.entities.financial.FixedMovementState;
import br.com.webbudget.domain.entities.financial.Launch;
import br.com.webbudget.domain.entities.registration.Contact;
import br.com.webbudget.domain.entities.registration.CostCenter;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.entities.registration.MovementClass;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.repositories.financial.FixedMovementRepository;
import br.com.webbudget.domain.repositories.financial.LaunchRepository;
import br.com.webbudget.domain.repositories.registration.ContactRepository;
import br.com.webbudget.domain.repositories.registration.CostCenterRepository;
import br.com.webbudget.domain.repositories.registration.FinancialPeriodRepository;
import br.com.webbudget.domain.repositories.registration.MovementClassRepository;
import br.com.webbudget.domain.services.FixedMovementService;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

import static br.com.webbudget.application.components.ui.NavigationManager.PageType.*;
import static br.com.webbudget.application.components.ui.NavigationManager.Parameter.of;

/**
 * Controller for the {@link FixedMovement} view
 *
 * @author Arthur Gregorio
 *
 * @version 3.0.0
 * @since 2.1.0, 18/09/2015
 */
@Named
@ViewScoped
public class FixedMovementBean extends FormBean<FixedMovement> implements LazyDataProvider<FixedMovement> {

    @Getter
    @Setter
    private String contactFilter;

    @Getter
    @Setter
    private Apportionment apportionment;
    @Getter
    @Setter
    private FinancialPeriod selectedFinancialPeriod;

    @Getter
    @Setter
    private List<FixedMovement> selectedFixedMovements;

    @Getter
    private FixedMovementFilter filter;
    @Getter
    private LazyDataModel<FixedMovement> dataModel;

    @Getter
    private List<Launch> launches;
    @Getter
    private List<Contact> contacts;
    @Getter
    private List<CostCenter> costCenters;
    @Getter
    private List<MovementClass> movementClasses;
    @Getter
    private List<FinancialPeriod> openFinancialPeriods;

    @Inject
    private FixedMovementService fixedMovementService;

    @Inject
    private LaunchRepository launchRepository;
    @Inject
    private ContactRepository contactRepository;
    @Inject
    private CostCenterRepository costCenterRepository;
    @Inject
    private MovementClassRepository movementClassRepository;
    @Inject
    private FixedMovementRepository fixedMovementRepository;
    @Inject
    private FinancialPeriodRepository financialPeriodRepository;

    @Any
    @Inject
    private Instance<ApportionmentValidator> apportionmentValidators;

    /**
     * Constructor...
     */
    public FixedMovementBean() {
        this.filter = new FixedMovementFilter();
        this.dataModel = new LazyModel<>(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() {
        super.initialize();
        this.selectedFixedMovements = new ArrayList<>();
        this.openFinancialPeriods = this.financialPeriodRepository.findByClosedOrderByIdentificationAsc(false);
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

        this.costCenters = this.costCenterRepository.findAllActive();
        this.value = this.fixedMovementRepository.findById(id).orElseGet(FixedMovement::new);

        if (viewState.isDetailing()) {
            this.launches = this.launchRepository.findByFixedMovement(this.value);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initializeNavigationManager() {
        this.navigation.addPage(LIST_PAGE, "listFixedMovements.xhtml");
        this.navigation.addPage(ADD_PAGE, "formFixedMovement.xhtml");
        this.navigation.addPage(UPDATE_PAGE, "formFixedMovement.xhtml");
        this.navigation.addPage(DETAIL_PAGE, "detailFixedMovement.xhtml");
        this.navigation.addPage(DELETE_PAGE, "detailFixedMovement.xhtml");
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
    public Page<FixedMovement> load(int first, int pageSize, String sortField, SortOrder sortOrder) {

        final Page<FixedMovement> page = this.fixedMovementRepository.findAllBy(this.filter, first, pageSize);

        // check to see if the fixed movement is already launched at the current active period
        page.getContent()
                .stream()
                .filter(FixedMovement::isActive)
                .forEach(fixedMovement -> fixedMovement.setAlreadyLaunched(
                        this.launchRepository.countByFixedMovementAtCurrentFinancialPeriod(fixedMovement) > 0));

        return page;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doSave() {
        this.fixedMovementService.save(this.value);
        this.value = new FixedMovement();
        this.addInfo(true, "saved");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doUpdate() {
        this.value = this.fixedMovementService.update(this.value);
        this.addInfo(true, "updated");
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public String doDelete() {
        this.fixedMovementService.delete(this.value);
        this.addInfoAndKeep("deleted");
        return this.changeToListing();
    }

    /**
     * Execute the launching of the {@link FixedMovement}
     */
    public void doLaunch() {
        this.fixedMovementService.launch(this.selectedFixedMovements, this.selectedFinancialPeriod);
        this.closeDialog("dialogPeriodSelect");
        this.addInfo(false, "fixed-movement.launch", this.selectedFinancialPeriod.getIdentification());
        this.updateComponent("messages");
        this.updateComponent("itemsList");
    }

    /**
     * Clear the listing view filters
     */
    public void clearFilters() {
        this.filter = new FixedMovementFilter();
    }

    /**
     * Same as {@link #changeToDetail()} but in this case the navigations is made by the JSF Outcome because this UI
     * uses multi-selection and row selection in this cases are useless
     *
     * @param id the id of the {@link FixedMovement} to detail
     * @return the outcome to the detailing page
     */
    public String changeToDetail(Long id) {
        return this.navigation.to(DETAIL_PAGE, of("id", id));
    }

    /**
     * When the user change the quotes at the UI, this method will be triggered to change the fields behavior
     */
    public void onQuotesChanged() {
        this.value.setTotalQuotes(null);
        this.value.setStartingQuote(null);
        this.updateComponent("quotesBox");
    }

    /**
     * Specific method to display the launches of a {@link FixedMovement}
     */
    public void showLaunchesDialog() {
        this.updateAndOpenDialog("launchesDialog", "dialogLaunches");
    }

    /**
     * Method used to display the search {@link Contact} dialog
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
     * Add the {@link Apportionment} to the {@link FixedMovement}
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
                .findByActiveAndCostCenterOrderByNameAsc(true, this.apportionment.getCostCenter());
    }

    /**
     * Method used to display the {@link FinancialPeriod} select window before the launch process
     */
    public void showPeriodSelectDialog() {

        if (this.selectedFixedMovements.isEmpty()) {
            throw new BusinessLogicException("error.fixed-movement.list-is-empty");
        }

        this.updateAndOpenDialog("periodSelectDialog", "dialogPeriodSelect");
    }

    /**
     * Get the {@link FixedMovementState} list
     *
     * @return array with all possible {@link FixedMovement} states
     */
    public FixedMovementState[] getFixedMovementStates() {
        return FixedMovementState.values();
    }
}