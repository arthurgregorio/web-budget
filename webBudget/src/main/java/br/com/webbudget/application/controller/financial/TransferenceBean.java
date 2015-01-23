package br.com.webbudget.application.controller.financial;

import br.com.webbudget.application.controller.AbstractBean;
import br.com.webbudget.domain.entity.wallet.Wallet;
import br.com.webbudget.domain.entity.wallet.WalletBalance;
import br.com.webbudget.domain.service.WalletService;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
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
public class TransferenceBean extends AbstractBean {

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
    @ManagedProperty("#{walletService}")
    private WalletService walletService;

    /**
     * 
     * @return 
     */
    @Override
    protected Logger initializeLogger() {
        return LoggerFactory.getLogger(TransferenceBean.class);
    }
    
    /**
     * 
     */
    public void initializeListing() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            this.viewState = ViewState.LISTING;
            this.wallets = this.walletService.listWallets(false);
        }
    }    
    
    /**
     * 
     */
    public void initializeForm() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            
            this.viewState = ViewState.ADDING;
            
            this.walletBalance = new WalletBalance();
            
            this.wallets = this.walletService.listWallets(false);
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
     */
    public void doTransference() {
        
        try {
            this.walletService.transfer(this.walletBalance);
            
            this.walletBalance = new WalletBalance();
            this.wallets = this.walletService.listWallets(false);
            
            this.info("transfer.action.transfered", true);
        }  catch (Exception ex) {
            this.logger.error("TransferenceBean#doTransference found erros", ex);
            this.fixedError(ex.getMessage(), true);
        }
    }
    
    /**
     * 
     */
    public void loadTransfers() {
        
        this.transferences = this.walletService.listTransfersByWallet(this.selectedWallet);
        
        if (this.transferences.isEmpty()) {
            this.error("transfer.no-trasfers", true);
        } else {
            this.update("transferencesList");
        }
    }
}
