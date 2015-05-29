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
package br.com.webbudget.application.controller.entries;

import br.com.webbudget.application.controller.AbstractBean;
import br.com.webbudget.domain.entity.movement.CostCenter;
import br.com.webbudget.domain.service.MovementService;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.1.0
 * @since 1.0.0, 04/03/2014
 */
@Named
@ViewScoped
public class CostCenterBean extends AbstractBean {

    @Getter
    private CostCenter costCenter;
    @Getter
    private List<CostCenter> costCenters;

    @Inject
    private MovementService movementService;

    /**
     *
     */
    public void initializeListing() {
        this.viewState = ViewState.LISTING;
        this.costCenters = this.movementService.listCostCenters(null);
    }

    /**
     *
     * @param costCenterId
     */
    public void initializeForm(long costCenterId) {

        this.costCenters = this.movementService.listCostCenters(false);

        if (costCenterId == 0) {
            this.viewState = ViewState.ADDING;
            this.costCenter = new CostCenter();
        } else {
            this.viewState = ViewState.EDITING;
            this.costCenter = this.movementService.findCostCenterById(costCenterId);
        }
    }

    /**
     *
     * @return
     */
    public String changeToAdd() {
        return "formCostCenter.xhtml?faces-redirect=true";
    }

    /**
     *
     * @return
     */
    public String changeToListing() {
        return "listCostCenters.xhtml?faces-redirect=true";
    }

    /**
     *
     * @param costCenterId
     * @return
     */
    public String changeToEdit(long costCenterId) {
        return "formCostCenter.xhtml?faces-redirect=true&costCenterId=" + costCenterId;
    }

    /**
     *
     * @param costCenterId
     */
    public void changeToDelete(long costCenterId) {
        this.costCenter = this.movementService.findCostCenterById(costCenterId);
        this.openDialog("deleteCostCenterDialog", "dialogDeleteCostCenter");
    }

    /**
     * Cancela e volta para a listagem
     *
     * @return
     */
    public String doCancel() {
        return "listCostCenters.xhtml?faces-redirect=true";
    }

    /**
     *
     */
    public void doSave() {

//        try {
//            this.movementService.saveCostCenter(this.costCenter);
//            this.costCenter = new CostCenter();
//
//            // busca novamente os centros de custo para atualizar a lista de parentes
//            this.costCenters = this.movementService.listCostCenters(false);
//
//            this.info("cost-center.action.saved", true);
//        } catch (ApplicationException ex) {
//            this.logger.error("CostCenterBean#doSave found erros", ex);
//            this.fixedError(ex.getMessage(), true);
//        }
    }

    /**
     *
     */
    public void doUpdate() {

//        try {
//            this.costCenter = this.movementService.updateCostCenter(this.costCenter);
//
//            this.info("cost-center.action.updated", true);
//        } catch (ApplicationException ex) {
//            this.logger.error("CostCenterBean#doUpdate found erros", ex);
//            this.fixedError(ex.getMessage(), true);
//        }
    }

    /**
     *
     */
    public void doDelete() {

        try {
            this.movementService.deleteCostCenter(this.costCenter);
            this.costCenters = this.movementService.listCostCenters(false);

            this.info("cost-center.action.deleted", true);
//        } catch (DataIntegrityViolationException ex) {
//            this.logger.error("CostCenterBean#doDelete found erros", ex);
//            this.fixedError("cost-center.action.delete-used", true);
        } catch (Exception ex) {
            this.logger.error("CostCenterBean#doDelete found erros", ex);
            this.fixedError(ex.getMessage(), true);
        } finally {
            this.closeDialog("dialogDeleteCostCenter");
            this.update("costCentersList");
        }
    }
}
