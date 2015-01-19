package br.com.webbudget.application.controller.entries;

import br.com.webbudget.application.controller.AbstractBean;
import br.com.webbudget.application.exceptions.ApplicationException;
import br.com.webbudget.domain.entity.movement.CostCenter;
import br.com.webbudget.domain.entity.movement.MovementClass;
import br.com.webbudget.domain.entity.movement.MovementClassType;
import br.com.webbudget.domain.service.MovementService;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 04/03/2014
 */
@ViewScoped
@ManagedBean
public class MovementClassBean extends AbstractBean {

    @Getter
    private MovementClass movementClass;
    
    @Getter
    private List<CostCenter> costCenters;
    @Getter
    private List<MovementClass> movementClasses;
    
    @Setter
    @ManagedProperty("#{movementService}")
    private transient MovementService movementService;

    /**
     * 
     * @return 
     */
    @Override
    protected Logger initializeLogger() {
        return LoggerFactory.getLogger(MovementClassBean.class);
    }
    
    /**
     * 
     */
    public void initializeListing(){
        if (!FacesContext.getCurrentInstance().isPostback()) {
            this.viewState = ViewState.LISTING;
            this.movementClasses = this.movementService.listMovementClasses(null);
        }
    }

    /**
     * 
     * @param movementClassId 
     */
    public void initializeForm(long movementClassId) {
        if (!FacesContext.getCurrentInstance().isPostback()) {

            this.costCenters = this.movementService.listCostCenters(false);
            
            if (movementClassId == 0) {
                this.viewState = ViewState.ADDING;
                this.movementClass = new MovementClass();
            } else {
                this.viewState = ViewState.EDITING;
                this.movementClass = this.movementService.findMovementClassById(movementClassId);
            }
        }
    }
    
    /**
     * 
     * @return o form de inclusao
     */
    public String changeToAdd() {
        return "formMovementClass.xhtml?faces-redirect=true";
    }
    
    /**
     * 
     * @return 
     */
    public String changeToListing() {
        return "formMovementClass.xhtml?faces-redirect=true";
    }
    
    /**
     * 
     * @param movementClassId
     * @return 
     */
    public String changeToEdit(long movementClassId) {
        return "formMovementClass.xhtml?faces-redirect=true&movementClassId=" + movementClassId;
    }
    
    /**
     * 
     * @param movementClassId 
     */
    public void changeToDelete(long movementClassId) {
        this.movementClass = this.movementService.findMovementClassById(movementClassId);
        this.openDialog("deleteMovementClassDialog", "dialogDeleteMovementClass");
    }
    
    /**
     * Cancela e volta para a listagem
     * 
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
        } catch (ApplicationException ex) {
            this.logger.error("MovementClassBean#doSave found erros", ex);
            this.fixedError(ex.getMessage(), true);
        }
    }
    
    /**
     * 
     */
    public void doUpdate() {
        
        try {
            this.movementClass = this.movementService.updateMovementClass(this.movementClass);
            
            this.info("movement-class.action.updated", true);
        } catch (ApplicationException ex) {
            this.logger.error("MovementClassBean#doUpdate found erros", ex);
            this.fixedError(ex.getMessage(), true);
        } 
    }
    
    /**
     * 
     */
    public void doDelete() {
        
        try {
            this.movementService.deleteMovementClass(this.movementClass);
            this.movementClasses = this.movementService.listMovementClasses(false);
            
            this.info("movement-class.action.deleted", true);
        } catch (DataIntegrityViolationException ex) {
            this.logger.error("MovementClassBean#doDelete found erros", ex);
            this.fixedError("movement-class.action.delete-used", true);
        } catch (Exception ex) {
            this.logger.error("MovementClassBean#doDelete found erros", ex);
            this.fixedError(ex.getMessage(), true);
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
