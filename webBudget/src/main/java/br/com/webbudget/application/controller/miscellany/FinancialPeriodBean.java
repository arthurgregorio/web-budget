package br.com.webbudget.application.controller.miscellany;

import br.com.webbudget.application.ViewState;
import br.com.webbudget.application.components.MessagesFactory;
import br.com.webbudget.domain.entity.closing.Closing;
import br.com.webbudget.domain.entity.movement.FinancialPeriod;
import br.com.webbudget.domain.service.FinancialPeriodService;
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

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 23/03/2014
 */
@ViewScoped
@ManagedBean
public class FinancialPeriodBean implements Serializable {

    @Getter
    private ViewState viewState;
    @Getter
    private boolean hasOpenPeriod;
    
    @Getter
    private Closing closing;
    @Getter
    private FinancialPeriod financialPeriod;
    
    @Getter
    private List<FinancialPeriod> financialPeriods;
    
    @Setter
    @ManagedProperty("#{messagesFactory}")
    private transient MessagesFactory messages;
    @Setter
    @ManagedProperty("#{financialPeriodService}")
    private transient FinancialPeriodService financialPeriodService;
    
    private final Logger LOG = LoggerFactory.getLogger(FinancialPeriodBean.class);
    
    /**
     * 
     */
    public void initializeListing(){
        if (!FacesContext.getCurrentInstance().isPostback()) {
            this.viewState = ViewState.LISTING;
            this.financialPeriods = this.financialPeriodService.listFinancialPeriods(null);
        }
    }
    
    /**
     * 
     */
    public void initializeForm(){
        if (!FacesContext.getCurrentInstance().isPostback()) {

            // diz que pode abrir um periodo
            this.hasOpenPeriod = false; 
            
            // validamos se tem periodo em aberto
            this.validateOpenPeriods();

            this.viewState = ViewState.ADD;
            this.financialPeriod = new FinancialPeriod();
        }
    }
    
    /**
     * 
     * @return 
     */
    public String changeToAdd() {
        return "formFinancialPeriod.xhtml?faces-redirect=true";
    }
    
    /**
     * 
     * @param financialPeriodId
     * @return 
     */
    public String changeToDetails(long financialPeriodId) {
        return "detailFinancialPeriod.xhtml?faces-redirect=true&financialPeriodId=" + financialPeriodId;
    }
   
    /**
     * 
     * @param financialPeriodId
     * @return 
     */
    public String changeToClosing(long financialPeriodId) {
        return "../closing/closeFinancialPeriod.xhtml?faces-redirect=true&financialPeriodId=" + financialPeriodId;
    }
    
    /**
     * 
     */
    public void doSave() {
        
        try {
            this.financialPeriodService.openPeriod(this.financialPeriod);
            
            this.financialPeriod = new FinancialPeriod();
            
            // validamos se tem periodo em aberto
            this.validateOpenPeriods();
            
            Messages.addInfo(null, this.messages.getMessage("financial-period.action.saved"));
        } catch (Exception ex) {
            LOG.error("FinancialPeriodBean#doSave found errors", ex);
            Messages.addError(null, this.messages.getMessage(ex.getMessage()));
        } finally {
            RequestContext.getCurrentInstance().update("financialPeriodForm");
            RequestContext.getCurrentInstance().execute("setTimeout(\"$(\'#messages\').slideUp(300)\", 5000)");
        }
    }
    
    /**
     * Cancela e volta para a listagem
     *
     * @return
     */
    public String doCancel() {
        return "listFinancialPeriods.xhtml?faces-redirect=true";
    }
    
    /**
     * valida se tem algum periodo em aberto, se houver avisa ao usuario que 
     * ja tem e se ele tem certeza que quer abrir um novo
     */
    public void validateOpenPeriods() {

        // validamos se ha algum periodo em aberto
        final List<FinancialPeriod> periods = this.financialPeriodService.listOpenFinancialPeriods();

        for (FinancialPeriod open : periods) {
            if (open != null && (!open.isClosed() || !open.isExpired())) {
                // se ja houver aberto, nega o que foi dito antes
                this.hasOpenPeriod = true;
                break;
            }
        }
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
