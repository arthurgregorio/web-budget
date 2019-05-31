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
package br.com.webbudget.application.controller.registration;

import br.com.webbudget.application.components.ui.LazyFormBean;
import br.com.webbudget.application.components.ui.ViewState;
import br.com.webbudget.application.components.ui.table.Page;
import br.com.webbudget.domain.entities.registration.CostCenter;
import br.com.webbudget.domain.entities.registration.Vehicle;
import br.com.webbudget.domain.entities.registration.VehicleType;
import br.com.webbudget.domain.logics.registration.vehicle.VehicleSavingLogic;
import br.com.webbudget.domain.repositories.registration.CostCenterRepository;
import br.com.webbudget.domain.repositories.registration.VehicleRepository;
import lombok.Getter;
import org.primefaces.model.SortOrder;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.util.List;

import static br.com.webbudget.application.components.ui.NavigationManager.PageType.*;

/**
 * The {@link Vehicle} maintenance routine controller
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 2.3.0, 09/05/2016
 */
@Named
@ViewScoped
public class VehicleBean extends LazyFormBean<Vehicle> {

    @Getter
    private List<CostCenter> costCenters;

    @Inject
    private VehicleRepository vehicleRepository;
    @Inject
    private CostCenterRepository costCenterRepository;

    @Any
    @Inject
    private Instance<VehicleSavingLogic> savingBusinessLogics;

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param viewState
     */
    @Override
    public void initialize(long id, ViewState viewState) {
        this.viewState = viewState;

        if (viewState.isDetailing() || viewState.isDeleting()) {
            this.costCenters = this.costCenterRepository.findAll();
        } else {
            this.costCenters = this.costCenterRepository.findAllActive();
        }

        this.value = this.vehicleRepository.findById(id).orElseGet(Vehicle::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initializeNavigationManager() {
        this.navigation.addPage(LIST_PAGE, "listVehicles.xhtml");
        this.navigation.addPage(ADD_PAGE, "formVehicle.xhtml");
        this.navigation.addPage(UPDATE_PAGE, "formVehicle.xhtml");
        this.navigation.addPage(DETAIL_PAGE, "detailVehicle.xhtml");
        this.navigation.addPage(DELETE_PAGE, "detailVehicle.xhtml");
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
    public Page<Vehicle> load(int first, int pageSize, String sortField, SortOrder sortOrder) {
        return this.vehicleRepository.findAllBy(this.filter.getValue(),
                this.filter.getEntityStatusValue(), first, pageSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void doSave() {
        this.savingBusinessLogics.forEach(logic -> logic.run(this.value));
        this.vehicleRepository.save(this.value);
        this.value = new Vehicle();
        this.addInfo(true, "saved");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void doUpdate() {
        this.vehicleRepository.saveAndFlushAndRefresh(this.value);
        this.addInfo(true, "updated");
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    @Transactional
    public String doDelete() {
        this.vehicleRepository.attachAndRemove(this.value);
        this.addInfoAndKeep("deleted");
        return this.changeToListing();
    }

    /**
     * Helper method to get the types defined in the {@link VehicleType} enum
     *
     * @return an array of types from {@link VehicleType}
     */
    public VehicleType[] getVehicleTypes() {
        return VehicleType.values();
    }
}
