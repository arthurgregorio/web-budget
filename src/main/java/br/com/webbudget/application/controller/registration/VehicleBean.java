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

import static br.com.webbudget.application.components.NavigationManager.PageType.ADD_PAGE;
import static br.com.webbudget.application.components.NavigationManager.PageType.DELETE_PAGE;
import static br.com.webbudget.application.components.NavigationManager.PageType.DETAIL_PAGE;
import static br.com.webbudget.application.components.NavigationManager.PageType.LIST_PAGE;
import static br.com.webbudget.application.components.NavigationManager.PageType.UPDATE_PAGE;
import br.com.webbudget.application.components.ViewState;
import br.com.webbudget.application.components.table.Page;
import br.com.webbudget.application.controller.FormBean;
import br.com.webbudget.domain.entities.registration.CostCenter;
import br.com.webbudget.domain.entities.registration.Vehicle;
import br.com.webbudget.domain.entities.registration.VehicleType;
import br.com.webbudget.domain.repositories.entries.CostCenterRepository;
import br.com.webbudget.domain.repositories.entries.VehicleRepository;
import br.com.webbudget.domain.services.VehicleService;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import org.primefaces.model.SortOrder;

/**
 * Controller para a view de manutencao dos veiculos do diario de bordo
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 2.3.0, 09/05/2016
 */
@Named
@ViewScoped
public class VehicleBean extends FormBean<Vehicle> {

    @Getter
    private List<CostCenter> costCenters;

    @Inject
    private VehicleService vehicleService;
    
    @Inject
    private VehicleRepository vehicleRepository;
    @Inject
    private CostCenterRepository costCenterRepository;

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
        this.costCenters = this.costCenterRepository.findAllUnblocked();
        this.value = this.vehicleRepository.findOptionalById(id)
                .orElseGet(Vehicle::new);
    }

    /**
     * 
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
     *
     */
    @Override
    public void doSave() {
        this.vehicleService.save(this.value);
        this.value = new Vehicle();
        this.addInfo(true, "vehicle.saved");
    }

    /**
     *
     */
    @Override
    public void doUpdate() {
        this.value = this.vehicleService.update(this.value);
        this.addInfo(true, "vehicle.updated");
    }

    /**
     *
     * @return
     */
    @Override
    public String doDelete() {
        this.vehicleService.delete(this.value);
        this.addInfoAndKeep("vehicle.deleted");
        return this.changeToListing();
    }

    /**
     * @return os tipos de veiculo para selecao
     */
    public VehicleType[] getVehicleTypes() {
        return VehicleType.values();
    }
}
