package br.com.webbudget.application.controller.financial;

import br.com.webbudget.application.components.MessagesFactory;
import br.com.webbudget.domain.entity.wallet.Wallet;
import br.com.webbudget.domain.entity.wallet.WalletBalance;
import br.com.webbudget.domain.service.WalletService;
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
 * @since 1.0, 20/05/2014
 */
@ViewScoped
@ManagedBean
public class TransferenceBean implements Serializable {

    @Getter
    @Setter
    private Wallet selectedWallet;
    
    @Getter
    private WalletBalance walletBalance;
    
    @Getter
    private List<Wallet> wallets;
    @Getter
    private List<WalletBalance> transferences;
    
    @Setter
    @ManagedProperty("#{messagesFactory}")
    private transient MessagesFactory messages;
    @Setter
    @ManagedProperty("#{walletService}")
    private transient WalletService walletService;
    
    private final Logger LOG = LoggerFactory.getLogger(TransferenceBean.class);
    
    /**
     * 
     */
    public void initializeListing() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
//            this.viewState = ViewState.LISTING;
            
            this.wallets = this.walletService.listWallets(false);
        }
    }    
    
    /**
     * 
     */
    public void initializeForm() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
//            this.viewState = ViewState.ADD;
            
            this.walletBalance = new WalletBalance();
            
            this.wallets = this.walletService.listWallets(false);
        }
    }    
    
    /**
     * 
     */
    public void doTransference() {
        
        try {
            this.walletService.transfer(this.walletBalance);
            
            this.walletBalance = new WalletBalance();
            this.wallets = this.walletService.listWallets(false);
            
            Messages.addInfo(null, messages.getMessage("transfer.action.transfered"));
        }  catch (Exception ex) {
            LOG.error("TransferenceBean#doTransference found erros", ex);
            Messages.addError(null, messages.getMessage(ex.getMessage()));
        } finally {
            RequestContext.getCurrentInstance().update("transferForm");
            RequestContext.getCurrentInstance().execute("setTimeout(\"$(\'#messages\').slideUp(300)\", 5000)");
        }
    }
    
    /**
     * 
     */
    public void loadTransfers() {
        this.transferences = this.walletService.listTransfersByWallet(this.selectedWallet);
        
        if (this.transferences.isEmpty()) {
            Messages.addError(null, this.messages.getMessage("transfer.no-trasfers"));
            RequestContext.getCurrentInstance().update("messages");
            RequestContext.getCurrentInstance().execute("setTimeout(\"$(\'#messages\').slideUp(300)\", 5000)");
        } else {
            RequestContext.getCurrentInstance().update("transferencesList");
        }
    }
    
    /**
     * 
     * @return 
     */
    public String changeToAdd() {
        return "formTransfer.xhtml?faces-redirect=true";
    }
    
    /**
     * 
     * @return 
     */
    public String doCancel() {
        return "listTransfers.xhtml?faces-redirect=true";
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
