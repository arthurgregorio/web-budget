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
import br.com.webbudget.domain.model.entity.movement.CostCenter;
import br.com.webbudget.domain.misc.ex.InternalServiceError;
import br.com.webbudget.application.component.table.AbstractLazyModel;
import br.com.webbudget.application.component.table.Page;
import br.com.webbudget.application.component.table.PageRequest;
import br.com.webbudget.domain.model.service.MovementService;
import java.util.List;
import java.util.Map;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.model.SortOrder;

/**
 * Controller da view de centros de custo
 *
 * @author Arthur Gregorio
 *
 * @version 1.3.0
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
    
    @Getter
    private final AbstractLazyModel<CostCenter> costCentersModel;

    /**
     * 
     */
    public CostCenterBean() {
        
        this.costCentersModel = new AbstractLazyModel<CostCenter>() {
            @Override
            public List<CostCenter> load(int first, int pageSize, String sortField, 
                    SortOrder sortOrder, Map<String, Object> filters) {
                
                final PageRequest pageRequest = new PageRequest();
                
                pageRequest
                        .setFirstResult(first)
                        .withPageSize(pageSize)
                        .sortingBy(sortField, "inclusion")
                        .withDirection(sortOrder.name());
                
                final Page<CostCenter> page = movementService.listCostCentersLazily(null, pageRequest);
                
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
     * @return
     */
    public String changeToAdd() {
        return "formCostCenter.xhtml?faces-redirect=true";
    }

    /**
     * @return
     */
    public String changeToListing() {
        return "listCostCenters.xhtml?faces-redirect=true";
    }

    /**
     * @param costCenterId
     * @return
     */
    public String changeToEdit(long costCenterId) {
        return "formCostCenter.xhtml?faces-redirect=true&costCenterId=" + costCenterId;
    }

    /**
     * @param costCenterId
     */
    public void changeToDelete(long costCenterId) {
        this.costCenter = this.movementService.findCostCenterById(costCenterId);
        this.updateAndOpenDialog("deleteCostCenterDialog", "dialogDeleteCostCenter");
    }

    /**
     * @return
     */
    public String doCancel() {
        return "listCostCenters.xhtml?faces-redirect=true";
    }

    /**
     *
     */
    public void doSave() {

        try {
            this.movementService.saveCostCenter(this.costCenter);
            this.costCenter = new CostCenter();

            // busca novamente os centros de custo para atualizar a lista de parentes
            this.costCenters = this.movementService.listCostCenters(false);

            this.addInfo(true, "cost-center.saved");
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
            this.costCenter = this.movementService.updateCostCenter(this.costCenter);
            
            // busca novamente os centros de custo para atualizar a lista de parentes
            this.costCenters = this.movementService.listCostCenters(false);
            
            this.addInfo(true, "cost-center.updated");
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
            this.movementService.deleteCostCenter(this.costCenter);
            this.addInfo(true, "cost-center.deleted");
        } catch (InternalServiceError ex) {
            this.addError(true, ex.getMessage(), true, ex.getParameters());
        } catch (Exception ex) {
            if (this.containsException(ConstraintViolationException.class, ex)) {
                this.addError(true, "error.cost-center.integrity-violation", 
                        this.costCenter.getName());
            } else {
                this.logger.error(ex.getMessage(), ex);
                this.addError(true, "error.undefined-error", ex.getMessage());
            }
        } finally {
            this.closeDialog("dialogDeleteCostCenter");
            this.updateComponent("costCentersList");
        }
    }
}
