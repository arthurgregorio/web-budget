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
package br.com.webbudget.application.controller.logbook;

import br.com.webbudget.application.component.table.AbstractLazyModel;
import br.com.webbudget.application.component.table.Page;
import br.com.webbudget.application.component.table.PageRequest;
import br.com.webbudget.application.controller.AbstractBean;
import br.com.webbudget.domain.misc.ex.InternalServiceError;
import br.com.webbudget.domain.model.entity.logbook.Vehicle;
import br.com.webbudget.domain.model.entity.logbook.VehicleType;
import br.com.webbudget.domain.model.service.LogbookService;
import java.util.List;
import java.util.Map;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.model.SortOrder;

/**
 * Controller para a view de manutencao dos veiculos do diario de bordo
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.3.0, 09/05/2016
 */
@Named
@ViewScoped
public class VehicleBean extends AbstractBean {

    @Getter
    private Vehicle vehicle;
    @Getter
    private List<Vehicle> vehicles;

    @Inject
    private LogbookService logbookService;

    @Getter
    private final AbstractLazyModel<Vehicle> vehiclesModel;

    /**
     *
     */
    public VehicleBean() {

        this.vehiclesModel = new AbstractLazyModel<Vehicle>() {
            @Override
            public List<Vehicle> load(int first, int pageSize, String sortField,
                    SortOrder sortOrder, Map<String, Object> filters) {

                final PageRequest pageRequest = new PageRequest();

                pageRequest
                        .setFirstResult(first)
                        .withPageSize(pageSize)
                        .sortingBy(sortField, "inclusion")
                        .withDirection(sortOrder.name());

                final Page<Vehicle> page = 
                        logbookService.listVehiclesLazily(null, pageRequest);

                this.setRowCount(page.getTotalPagesInt());

                return page.getContent();
            }
        };
    }

    /**
     *
     */
    public void initializeListing() {
        this.viewState = ViewState.LISTING;
    }

    /**
     * @param vehicleId
     */
    public void initializeForm(long vehicleId) {

        this.vehicles = this.logbookService.listVehicles(false);

        if (vehicleId == 0) {
            this.viewState = ViewState.ADDING;
            this.vehicle = new Vehicle();
        } else {
            this.viewState = ViewState.EDITING;
            this.vehicle = this.logbookService.findVehicleById(vehicleId);
        }
    }

    /**
     * @return
     */
    public String changeToAdd() {
        return "formVehicle.xhtml?faces-redirect=true";
    }

    /**
     * @return
     */
    public String changeToListing() {
        return "listVehicles.xhtml?faces-redirect=true";
    }

    /**
     * @param vehicleId
     * @return
     */
    public String changeToEdit(long vehicleId) {
        return "formVehicle.xhtml?faces-redirect=true&vehicleId=" + vehicleId;
    }

    /**
     * @param vehicleId
     */
    public void changeToDelete(long vehicleId) {
        this.vehicle = this.logbookService.findVehicleById(vehicleId);
        this.updateAndOpenDialog("deleteVehicleDialog", "dialogDeleteVehicle");
    }

    /**
     * @return
     */
    public String doCancel() {
        return "listVehicles.xhtml?faces-redirect=true";
    }

    /**
     *
     */
    public void doSave() {

        try {
            this.logbookService.saveVehicle(this.vehicle);
            this.vehicle = new Vehicle();
            this.addInfo(true, "vehicle.saved");
        } catch (InternalServiceError ex) {
            this.addError(true, ex.getMessage(), ex.getParameters());
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ex);
            this.addError(true, "error.undefined-error", ex.getMessage());
        }
    }

    /**
     *
     */
    public void doUpdate() {

        try {
            this.vehicle = this.logbookService.updateVehicle(this.vehicle);
            this.addInfo(true, "vehicle.updated");
        } catch (InternalServiceError ex) {
            this.addError(true, ex.getMessage(), ex.getParameters());
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ex);
            this.addError(true, "error.undefined-error", ex.getMessage());
        }
    }

    /**
     *
     */
    public void doDelete() {

        try {
            this.logbookService.deleteVehicle(this.vehicle);
            this.addInfo(true, "vehicle.deleted");
        } catch (InternalServiceError ex) {
            this.addError(true, ex.getMessage(), true, ex.getParameters());
        } catch (Exception ex) {
            if (this.containsException(ConstraintViolationException.class, ex)) {
                this.addError(true, "error.vehicle.integrity-violation",
                        this.vehicle.getIdentification());
            } else {
                this.logger.error(ex.getMessage(), ex);
                this.addError(true, "error.undefined-error", ex.getMessage());
            }
        } finally {
            this.closeDialog("dialogDeleteVehicle");
            this.updateComponent("vehiclesList");
        }
    }
    
    /**
     * @return os tipos de veiculo para selecao
     */
    public VehicleType[] getVehicleTypes() {
        return VehicleType.values();
    }
}
