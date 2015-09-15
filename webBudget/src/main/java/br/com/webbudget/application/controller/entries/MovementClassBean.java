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
import br.com.webbudget.domain.entity.movement.MovementClass;
import br.com.webbudget.domain.entity.movement.MovementClassType;
import br.com.webbudget.domain.misc.ex.WbDomainException;
import br.com.webbudget.domain.misc.model.AbstractLazyModel;
import br.com.webbudget.domain.misc.model.Page;
import br.com.webbudget.domain.misc.model.PageRequest;
import br.com.webbudget.domain.service.MovementService;
import java.util.List;
import java.util.Map;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.model.SortOrder;

/**
 * Controller da view de classes de movimento
 *
 * @author Arthur Gregorio
 *
 * @version 1.2.0
 * @since 1.0.0, 04/03/2014
 */
@Named
@ViewScoped
public class MovementClassBean extends AbstractBean {

    @Getter
    private MovementClass movementClass;

    @Getter
    private List<CostCenter> costCenters;

    @Inject
    private MovementService movementService;
    
    @Getter
    private final AbstractLazyModel<MovementClass> MovementClassesModel;

    /**
     * 
     */
    public MovementClassBean() {

        this.MovementClassesModel = new AbstractLazyModel<MovementClass>() {
            @Override
            public List<MovementClass> load(int first, int pageSize, String sortField, 
                    SortOrder sortOrder, Map<String, Object> filters) {
                
                final PageRequest pageRequest = new PageRequest();
                
                pageRequest
                        .setFirstResult(first)
                        .withPageSize(pageSize)
                        .sortingBy(sortField, "inclusion")
                        .withDirection(sortOrder.name());
                
                final Page<MovementClass> page = movementService.listMovementClassesLazily(null, pageRequest);
                
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
     * @param movementClassId
     */
    public void initializeForm(long movementClassId) {

        this.costCenters = this.movementService.listCostCenters(false);

        if (movementClassId == 0) {
            this.viewState = ViewState.ADDING;
            this.movementClass = new MovementClass();
        } else {
            this.viewState = ViewState.EDITING;
            this.movementClass = this.movementService.findMovementClassById(movementClassId);
        }
    }

    /**
     * @return o form de inclusao
     */
    public String changeToAdd() {
        return "formMovementClass.xhtml?faces-redirect=true";
    }

    /**
     * @return
     */
    public String changeToListing() {
        return "formMovementClass.xhtml?faces-redirect=true";
    }

    /**
     * @param movementClassId
     * @return
     */
    public String changeToEdit(long movementClassId) {
        return "formMovementClass.xhtml?faces-redirect=true&movementClassId=" + movementClassId;
    }

    /**
     * @param movementClassId
     */
    public void changeToDelete(long movementClassId) {
        this.movementClass = this.movementService.findMovementClassById(movementClassId);
        this.openDialog("deleteMovementClassDialog", "dialogDeleteMovementClass");
    }

    /**
     * @return
     */
    public String doCancel() {
        return "listMovementClasses.xhtml?faces-redirect=true";
    }

    /**
     *
     */
    public void doSave() {

        try {
            this.movementService.saveMovementClass(this.movementClass);
            this.movementClass = new MovementClass();

            this.info("movement-class.action.saved", true);
        } catch (WbDomainException ex) {
            this.logger.error("MovementClassBean#doSave found erros", ex);
            this.fixedError(ex.getMessage(), true, ex.getParameters());
        } catch (Exception ex) {
            this.logger.error("MovementClassBean#doSave found erros", ex);
            this.fixedError("generic.operation-error", true, ex.getMessage());
        }
    }

    /**
     *
     */
    public void doUpdate() {

        try {
            this.movementClass = this.movementService.updateMovementClass(this.movementClass);

            this.info("movement-class.action.updated", true);
        } catch (WbDomainException ex) {
            this.logger.error("MovementClassBean#doUpdate found erros", ex);
            this.fixedError(ex.getMessage(), true, ex.getParameters());
        } catch (Exception ex) {
            this.logger.error("MovementClassBean#doUpdate found erros", ex);
            this.fixedError("generic.operation-error", true, ex.getMessage());
        }
    }

    /**
     *
     */
    public void doDelete() {

        try {
            this.movementService.deleteMovementClass(this.movementClass);
            this.info("movement-class.action.deleted", true);
        } catch (WbDomainException ex) {
            this.logger.error("MovementClassBean#doDelete found erros", ex);
            this.fixedError(ex.getMessage(), true, ex.getParameters());
        } catch (Exception ex) {
            if (this.containsException(ConstraintViolationException.class, ex)) {
                this.logger.error("MovementClassBean#doDelete found erros", ex);
                this.fixedError("movement-class.action.delete-used", true);
            } else {
                this.logger.error("MovementClassBean#doDelete found erros", ex);
                this.fixedError("generic.operation-error", true, ex.getMessage());
            }
        } finally {
            this.update("movementClassesList");
            this.closeDialog("dialogDeleteMovementClass");
        }
    }

    /**
     * A lista com os tipos de movimento para preencher a view
     *
     * @return a lista dos valores do enum
     */
    public MovementClassType[] getAvailableMovementsTypes() {
        return MovementClassType.values();
    }
}
