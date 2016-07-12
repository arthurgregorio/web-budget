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

import br.com.webbudget.application.controller.AbstractBean;
import br.com.webbudget.domain.misc.ex.InternalServiceError;
import br.com.webbudget.domain.model.entity.entries.MovementClass;
import br.com.webbudget.domain.model.entity.logbook.Refueling;
import br.com.webbudget.domain.model.entity.logbook.Vehicle;
import br.com.webbudget.domain.model.entity.miscellany.FinancialPeriod;
import br.com.webbudget.domain.model.service.FinancialPeriodService;
import br.com.webbudget.domain.model.service.LogbookService;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.3.0, 27/06/2016
 */
@Named
@ViewScoped
public class RefuelingBean extends AbstractBean {

    @Getter
    private Refueling refueling;

    @Getter
    private List<FinancialPeriod> openPeriods;
    @Getter
    private List<MovementClass> movementClasses;
    
    @Inject
    private LogbookService logbookService;
    @Inject
    private FinancialPeriodService periodService;
    

    /**
     *
     * @param vehicleId
     */
    public void initializeForm(long vehicleId) {

        final Vehicle vehicle = this.logbookService.findVehicleById(vehicleId);
        
        // busca as classes do CC do veiculo
        this.movementClasses
                = this.logbookService.listClassesForVehicle(vehicle);

        // pegamos os periodos financeiros em aberto
        this.openPeriods = this.periodService.listOpenFinancialPeriods();

        this.refueling = new Refueling(vehicle);
    }

    /**
     * @return o metodo para compor a navegacao quando voltamos do form
     */
    public String changeToList() {
        return "listEntries.xhtml?faces-redirect=true&vehicleId=" 
                + this.refueling.getVehicle().getId();
    }

    /**
     *
     */
    public void doSave() {
        try {

            this.addInfo(true, "refueling.saved");
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

            this.addInfo(true, "refueling.deleted");
        } catch (InternalServiceError ex) {
            this.addError(true, ex.getMessage(), ex.getParameters());
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ex);
            this.addError(true, "error.undefined-error", ex.getMessage());
        } finally {
            this.updateComponent("entriesBox");
            this.closeDialog("dialogDeleteEntry");
        }
    }
}
