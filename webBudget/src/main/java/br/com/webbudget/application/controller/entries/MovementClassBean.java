package br.com.webbudget.application.controller.entries;

import br.com.webbudget.application.ViewState;
import br.com.webbudget.application.components.MessagesFactory;
import br.com.webbudget.application.exceptions.ApplicationException;
import br.com.webbudget.domain.entity.movement.CostCenter;
import br.com.webbudget.domain.entity.movement.MovementClass;
import br.com.webbudget.domain.entity.movement.MovementClassType;
import br.com.webbudget.domain.service.MovementService;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import org.omnifaces.util.Messages;
import org.primefaces.context.RequestContext;
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
public class MovementClassBean implements Serializable {

    @Getter
    private ViewState viewState;
    
    @Getter
    private MovementClass movementClass;
    
    @Getter
    private List<CostCenter> costCenters;
    @Getter
    private List<MovementClass> movementClasses;
    
    @Setter
    @ManagedProperty("#{movementService}")
    private transient MovementService movementService;
    @Setter
    @ManagedProperty("#{messagesFactory}")
    private transient MessagesFactory messages;
    
    private final Logger LOG = LoggerFactory.getLogger(MovementClassBean.class);
    
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
                this.viewState = ViewState.ADD;
                this.movementClass = new MovementClass();
            } else {
                this.viewState = ViewState.EDIT;
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
        RequestContext.getCurrentInstance().execute("PF('popupDeleteMovementClass').show()");
    }
    
    /**
     * 
     */
    public void doSave() {
        
        try {
            this.movementService.saveMovementClass(this.movementClass);
            this.movementClass = new MovementClass();
            
            Messages.addInfo(null, messages.getMessage("movement-class.action.saved"));
        } catch (ApplicationException ex) {
            LOG.error("MovementClassBean#doSave found erros", ex);
            Messages.addError(null, messages.getMessage(ex.getMessage()));
        } finally {
            RequestContext.getCurrentInstance().update("movementClassForm");
            RequestContext.getCurrentInstance().execute("setTimeout(\"$(\'#messages\').slideUp(300)\", 5000)");
        }
    }
    
    /**
     * 
     */
    public void doUpdate() {
        
        try {
            this.movementClass = this.movementService.updateMovementClass(this.movementClass);
            
            Messages.addInfo(null, messages.getMessage("movement-class.action.updated"));
        } catch (ApplicationException ex) {
            LOG.error("MovementClassBean#doUpdate found erros", ex);
            Messages.addError(null, messages.getMessage(ex.getMessage()));
        } finally {
            RequestContext.getCurrentInstance().update("movementClassForm");
            RequestContext.getCurrentInstance().execute("setTimeout(\"$(\'#messages\').slideUp(300)\", 5000)");
        }
    }
    
    /**
     * 
     */
    public void doDelete() {
        
        try {
            this.movementService.deleteMovementClass(this.movementClass);
            this.movementClasses = this.movementService.listMovementClasses(false);
            
            Messages.addWarn(null, messages.getMessage("movement-class.action.deleted"));
        } catch (DataIntegrityViolationException ex) {
            LOG.error("MovementClassBean#doDelete found erros", ex);
            Messages.addError(null, messages.getMessage("movement-class.action.delete-used"));
        } catch (Exception ex) {
            LOG.error("MovementClassBean#doDelete found erros", ex);
            Messages.addError(null, messages.getMessage(ex.getMessage()));
        } finally {
            RequestContext.getCurrentInstance().execute("PF('popupDeleteMovementClass').hide()");
            RequestContext.getCurrentInstance().update("messages");
            RequestContext.getCurrentInstance().execute("setTimeout(\"$(\'#messages\').slideUp(300)\", 5000)");
            RequestContext.getCurrentInstance().update("movementClassesList");
        }
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
     * @param id
     * @return 
     */
    public String getErrorMessage(String id) {
    
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        final Iterator<FacesMessage> iterator = facesContext.getMessages(id);
        
        if (iterator.hasNext()) {
            return this.messages.getMessage(iterator.next().getDetail());
        }
        return "";
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
