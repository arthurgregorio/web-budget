package br.com.webbudget.application.controller.entries;

import br.com.webbudget.application.ViewState;
import br.com.webbudget.application.components.MessagesFactory;
import br.com.webbudget.application.exceptions.ApplicationException;
import br.com.webbudget.domain.entity.movement.CostCenter;
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
public class CostCenterBean implements Serializable {

    @Getter
    private ViewState viewState;
    
    @Getter
    private CostCenter costCenter;
    @Getter
    private List<CostCenter> costCenters;
    
    @Setter
    @ManagedProperty("#{messagesFactory}")
    private transient MessagesFactory messages;
    @Setter
    @ManagedProperty("#{movementService}")
    private transient MovementService movementService;
    
    private final Logger LOG = LoggerFactory.getLogger(CostCenterBean.class);
    
    /**
     * 
     */
    public void initializeListing(){
        if (!FacesContext.getCurrentInstance().isPostback()) {
            this.viewState = ViewState.LISTING;
            this.costCenters = this.movementService.listCostCenters(null);
        }
    }

    /**
     * 
     * @param costCenterId 
     */
    public void initializeForm(long costCenterId) {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            
            this.costCenters = this.movementService.listCostCenters(false);
            
            if (costCenterId == 0) {
                this.viewState = ViewState.ADD;
                this.costCenter = new CostCenter();
            } else {
                this.viewState = ViewState.EDIT;
                this.costCenter = this.movementService.findCostCenterById(costCenterId);
            }
        }
    }
    
    /**
     * 
     * @return o form de inclusao
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
        RequestContext.getCurrentInstance().execute("PF('popupDeleteCostCenter').show()");
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
            
            Messages.addInfo(null, messages.getMessage("cost-center.action.saved"));
        }  catch (ApplicationException ex) {
            LOG.error("CostCenterBean#doSave found erros", ex);
            Messages.addError(null, messages.getMessage(ex.getMessage()));
        } finally {
            RequestContext.getCurrentInstance().update("costCenterForm");
            RequestContext.getCurrentInstance().execute("setTimeout(\"$(\'#messages\').slideUp(300)\", 5000)");
        }
    }
    
    /**
     * 
     */
    public void doUpdate() {
        
        try {
            this.costCenter = this.movementService.updateCostCenter(this.costCenter);
            
            Messages.addInfo(null, messages.getMessage("cost-center.action.updated"));
        } catch (ApplicationException ex) {
            LOG.error("CostCenterBean#doUpdate found erros", ex);
            Messages.addError(null, messages.getMessage(ex.getMessage()));
        } finally {
            RequestContext.getCurrentInstance().update("costCenterForm");
            RequestContext.getCurrentInstance().execute("setTimeout(\"$(\'#messages\').slideUp(300)\", 5000)");
        }
    }
    
    /**
     * 
     */
    public void doDelete() {
        
        try {
            this.movementService.deleteCostCenter(this.costCenter);
            this.costCenters = this.movementService.listCostCenters(false);
            
            Messages.addWarn(null, messages.getMessage("cost-center.action.deleted"));
        } catch (DataIntegrityViolationException ex) {
            LOG.error("CostCenterBean#doDelete found erros", ex);
            Messages.addError(null, messages.getMessage("cost-center.action.delete-used"));
        } catch (Exception ex) {
            LOG.error("CostCenterBean#doDelete found erros", ex);
            Messages.addError(null, messages.getMessage(ex.getMessage()));
        } finally {
            RequestContext.getCurrentInstance().execute("PF('popupDeleteCostCenter').hide()");
            RequestContext.getCurrentInstance().update("messages");
            RequestContext.getCurrentInstance().execute("setTimeout(\"$(\'#messages\').slideUp(300)\", 5000)");
            RequestContext.getCurrentInstance().update("costCentersList");
        }
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
}
