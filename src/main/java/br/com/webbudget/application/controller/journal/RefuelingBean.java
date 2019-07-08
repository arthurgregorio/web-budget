/*
 * Copyright (C) 2016 Arthur Gregorio, AG.Software
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
package br.com.webbudget.application.controller.journal;

import br.com.webbudget.application.components.ui.ViewState;
import br.com.webbudget.application.components.ui.table.Page;
import br.com.webbudget.application.components.ui.LazyFormBean;
import br.com.webbudget.domain.entities.journal.FuelType;
import br.com.webbudget.domain.entities.journal.Refueling;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.entities.registration.MovementClass;
import br.com.webbudget.domain.entities.registration.MovementClassType;
import br.com.webbudget.domain.entities.registration.Vehicle;
import br.com.webbudget.domain.repositories.journal.RefuelingRepository;
import br.com.webbudget.domain.repositories.registration.FinancialPeriodRepository;
import br.com.webbudget.domain.repositories.registration.MovementClassRepository;
import br.com.webbudget.domain.repositories.registration.VehicleRepository;
import br.com.webbudget.domain.services.RefuelingService;
import lombok.Getter;
import org.primefaces.model.SortOrder;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

import static br.com.webbudget.application.components.ui.NavigationManager.PageType.*;

/**
 * The of the {@link Refueling} view
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 2.3.0, 27/06/2016
 */
@Named
@ViewScoped
public class RefuelingBean extends LazyFormBean<Refueling> {

    @Getter
    private List<Vehicle> vehicles;
    @Getter
    private List<MovementClass> movementClasses;
    @Getter
    private List<FinancialPeriod> financialPeriods;

    @Inject
    private RefuelingService refuelingService;
    
    @Inject
    private VehicleRepository vehicleRepository;
    @Inject
    private RefuelingRepository refuelingRepository;
    @Inject
    private MovementClassRepository movementClassRepository;
    @Inject
    private FinancialPeriodRepository financialPeriodRepository;

    /**
     * {@inheritDoc}
     * 
     * @param
     * @param viewState 
     */
    @Override
    public void initialize(long id, ViewState viewState) {
        this.viewState = viewState;
        
        this.vehicles = this.vehicleRepository.findAllActive();
        this.financialPeriods = this.financialPeriodRepository.findByClosedOrderByIdentificationAsc(false);
        
        this.value = this.refuelingRepository.findById(id)
                .orElseGet(Refueling::new);
        
        // if detailing or deleting, list the classes to fill up the field
        if (this.viewState != ViewState.ADDING) {
            this.onVehicleSelect();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void initializeNavigationManager() {
        this.navigation.addPage(LIST_PAGE, "listRefuelings.xhtml");
        this.navigation.addPage(ADD_PAGE, "formRefueling.xhtml");
        this.navigation.addPage(UPDATE_PAGE, "formRefueling.xhtml");
        this.navigation.addPage(DETAIL_PAGE, "detailRefueling.xhtml");
        this.navigation.addPage(DELETE_PAGE, "detailRefueling.xhtml");
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
    public Page<Refueling> load(int first, int pageSize, String sortField, SortOrder sortOrder) {
        // FIXME the search for refuelings is not working well... fix this later before the release of v3.0
        return this.refuelingRepository.findAllBy(this.filter.getValue(), null, first, pageSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doSave() {
        this.refuelingService.save(this.value);
        this.value = new Refueling();
        this.addInfo(true, "saved");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doUpdate() {
        // No update for refueling, if you saved wrong, delete it and try again
    }

    /**
     * {@inheritDoc}
     * 
     * @return 
     */
    @Override
    public String doDelete() {
        this.refuelingService.delete(this.value);
        this.addInfoAndKeep("deleted");
        return this.changeToListing();
    }

    /**
     * When vehicle is selected, show the cost center linked to it
     */
    public void onVehicleSelect() {
        this.movementClasses = this.movementClassRepository
                .findByMovementClassTypeAndCostCenter(MovementClassType.EXPENSE, this.value.getCostCenter());
    }
    
    /**
     * Use this method to list all the types of fuel
     *
     * @return a list of {@link FuelType}
     */
    public FuelType[] getFuelTypes() {
        return FuelType.values();
    }
}
