package br.com.webbudget.application.controller.miscellany;

import br.com.webbudget.application.components.MessagesFactory;
import br.com.webbudget.application.exceptions.ApplicationException;
import br.com.webbudget.domain.entity.closing.Closing;
import br.com.webbudget.domain.entity.movement.FinancialPeriod;
import br.com.webbudget.domain.service.ClosingService;
import br.com.webbudget.domain.service.FinancialPeriodService;
import java.io.Serializable;
import java.util.List;
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
 * MBean que contem os metodos para encerramento dos periodos financeiros e <br/>
 * calculo do fechamento
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 14/04/2014
 */
@ViewScoped
@ManagedBean
public class ClosingBean implements Serializable {

    @Getter
    @Setter
    private FinancialPeriod financialPeriod;
    
    @Getter
    private Closing closing;
    
    @Getter
    private List<FinancialPeriod> financialPeriods;
    
    @Setter
    @ManagedProperty("#{messagesFactory}")
    private transient MessagesFactory messages;
    @Setter
    @ManagedProperty("#{closingService}")
    private transient ClosingService closingService;
    @Setter
    @ManagedProperty("#{financialPeriodService}")
    private transient FinancialPeriodService financialPeriodService;
    
    private final Logger LOG = LoggerFactory.getLogger(ClosingBean.class);
    
    /**
     * Inicializa o form do fechamento com os periodos disponiveis para encerramento
     * 
     * @param financialPeriodId se informado, apos a pesquisa por periodos
     * disponiveis selecione o periodo passado por parametro para fechamento
     */
    public void initializeClosing(long financialPeriodId){
        if (!FacesContext.getCurrentInstance().isPostback()) {

            this.financialPeriods = this.financialPeriodService.listOpenFinancialPeriods();
            
            if (financialPeriodId > 0) {
                this.financialPeriod = this.financialPeriodService
                        .findFinancialPeriodById(financialPeriodId); 
            }      
        }
    }
    
    /**
     * Processa o periodo financeiro selecionado e habilita ou nao a funcao de
     * fechamento
     */
    public void process() {

        if (this.financialPeriod == null) {
            Messages.addError(null, messages.getMessage("closing.validate.null-period"));
            RequestContext.getCurrentInstance().update("controlsForm");
            RequestContext.getCurrentInstance().execute("setTimeout(\"$(\'#messages\').slideUp(300)\", 5000)");
            return;
        }

        try {
            this.closing = this.closingService.process(this.financialPeriod, this.closing);
            Messages.addInfo(null, messages.getMessage("closing.action.processed"));
        } catch (ApplicationException ex) {
            LOG.error("ClosingBean#process found errors", ex);
            Messages.addError(null, this.messages.getMessage(ex.getMessage()));
        } finally {
            RequestContext.getCurrentInstance().update("controlsForm");
            RequestContext.getCurrentInstance().update("movementsList");
            RequestContext.getCurrentInstance().update("messages");
            RequestContext.getCurrentInstance().execute("setTimeout(\"$(\'#messages\').slideUp(300)\", 5000)");
        }
    }
    
    /**
     * Dependendo da selecao do usuario este metodo calcula e encerra o periodo
     */
    public void close() {
        
        if (this.financialPeriod == null) {
            Messages.addError(null, messages.getMessage("closing.validate.null-period"));
            RequestContext.getCurrentInstance().update("controlsForm");
            RequestContext.getCurrentInstance().update("messages");
            RequestContext.getCurrentInstance().execute("setTimeout(\"$(\'#messages\').slideUp(300)\", 5000)");
            return;
        }
        
        try {
            this.closingService.close(this.financialPeriod);
            
            // listamos novamente os periodos para fechamento e limpamos os objetos
            this.financialPeriods = this.financialPeriodService.listOpenFinancialPeriods();
            
            this.closing = null;
            this.financialPeriod = null;
            
            Messages.addInfo(null, this.messages.getMessage("closing.action.closed"));
        } catch (ApplicationException ex) {
            LOG.error("ClosingBean#close found errors", ex);
            Messages.addError(null, this.messages.getMessage(ex.getMessage()));
        } finally {
            RequestContext.getCurrentInstance().update("controlsForm");
            RequestContext.getCurrentInstance().update("movementsList");
            RequestContext.getCurrentInstance().execute("PF('popupConfirmClosing').hide()");
            RequestContext.getCurrentInstance().update("messages");
            RequestContext.getCurrentInstance().execute("setTimeout(\"$(\'#messages\').slideUp(300)\", 5000)");
        }
    }
    
    /**
     * Faz popup de confirmacao do fechamento aparecer na tela apos ter processado
     * o periodo
     */
    public void changeToClose() {
        RequestContext.getCurrentInstance().execute("PF('popupConfirmClosing').show()");
    }
    
    /**
     * @return se true, renderiza o botao para fechamento
     */
    public boolean canClosePeriod() {
        return this.closing != null && this.closing.getOpenMovements().isEmpty();
    }
    
    /**
     * @return se true, renderiza o botao de processamento do periodo
     */
    public boolean canProcessPeriod() {
        return this.closing == null || (this.closing.getOpenMovements() != null 
                && !this.closing.getOpenMovements().isEmpty());
    }
}
