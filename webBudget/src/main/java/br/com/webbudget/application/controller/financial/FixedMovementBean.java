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

import br.com.webbudget.application.controller.AbstractBean;
import br.com.webbudget.domain.entity.movement.FixedMovement;
import br.com.webbudget.domain.misc.ex.WbDomainException;
import br.com.webbudget.domain.misc.model.AbstractLazyModel;
import br.com.webbudget.domain.service.MovementService;
import java.util.List;
import java.util.Map;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.1.0, 18/09/2015
 */
@Named
@ViewScoped
public class FixedMovementBean extends AbstractBean {

    @Getter
    private String filter;
    
    @Getter
    private FixedMovement fixedMovement;
    
    @Inject
    private MovementService movementService;
    
    @Getter
    private final AbstractLazyModel<FixedMovement> fixedMovementsModel;
    
    /**
     * 
     */
    public FixedMovementBean(){

        this.fixedMovementsModel = new AbstractLazyModel<FixedMovement>() {
            @Override
            public List<FixedMovement> load(int first, int pageSize, String sortField, 
                    SortOrder sortOrder, Map<String, Object> filters) {
                return null;
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
     * 
     * @param fixedMovementId 
     */
    public void initializeForm(long fixedMovementId) {

        if (fixedMovementId == 0) {
            this.viewState = ViewState.ADDING;
            this.fixedMovement = new FixedMovement();
        } else {
            this.viewState = ViewState.EDITING;
            this.fixedMovement = this.movementService.findFixedMovementById(fixedMovementId);
        }
    }
    
    /**
     * 
     */
    public void filterList() {
        this.update("fixedMovementsList");
    }

    /**
     * @return 
     */
    public String changeToAdd() {
        return "formFixedMovement.xhtml?faces-redirect=true";
    }

    /**
     * @return 
     */
    public String changeToListing() {
        return "listFixedMovements.xhtml?faces-redirect=true";
    }

    /**
     * @param fixedMovementId 
     * @return 
     */
    public String changeToEdit(long fixedMovementId) {
        return "formFixedMovement.xhtml?faces-redirect=true&fixedMovementId=" + fixedMovementId;
    }

    /**
     * @param fixedMovementId 
     */
    public void changeToDelete(long fixedMovementId) {
        this.fixedMovement = this.movementService.findFixedMovementById(fixedMovementId);
        this.openDialog("deleteFixedMovementDialog", "dialogDeleteFixedMovement");
    }

    /**
     * @return 
     */
    public String doCancel() {
        return "listFixedMovements.xhtml?faces-redirect=true";
    }

    /**
     * 
     */
    public void doSave() {

        try {
            this.movementService.saveFixedMovement(this.fixedMovement);
            this.fixedMovement = new FixedMovement();
            this.info("fixed-movement.action.saved", true);
        } catch (WbDomainException ex) {
            this.logger.error("FixedMovementBean#doSave found erros", ex);
            this.fixedError(ex.getMessage(), true, ex.getParameters());
        } catch (Exception ex) {
            this.logger.error("FixedMovementBean#doSave found erros", ex);
            this.fixedError("generic.operation-error", true, ex.getMessage());
        }
    }

    /**
     *
     */
    public void doUpdate() {

        try {
            this.fixedMovement = this.movementService.updateFixedMovement(this.fixedMovement);
            this.info("fixed-movement.action.updated", true);
        } catch (WbDomainException ex) {
            this.logger.error("FixedMovementBean#doUpdate found erros", ex);
            this.fixedError(ex.getMessage(), true, ex.getParameters());
        } catch (Exception ex) {
            this.logger.error("FixedMovementBean#doUpdate found erros", ex);
            this.fixedError("generic.operation-error", true, ex.getMessage());
        }
    }

    /**
     *
     */
    public void doDelete() {

        try {
            this.movementService.deleteFixedMovement(this.fixedMovement);
            this.info("fixed-movement.action.deleted", true);
        } catch (WbDomainException ex) {
            this.logger.error("FixedMovementBean#doDelete found erros", ex);
            this.fixedError(ex.getMessage(), true, ex.getParameters());
        } catch (Exception ex) {
            this.logger.error("FixedMovementBean#doDelete found erros", ex);
            this.fixedError("generic.operation-error", true, ex.getMessage());
        } finally {
            this.update("fixedMovementsList");
            this.closeDialog("dialogDeleteFixedMovement");
        }
    }
}
