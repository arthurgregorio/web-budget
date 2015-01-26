package br.com.webbudget.application.controller.entries;

import br.com.webbudget.application.controller.AbstractBean;
import br.com.webbudget.domain.entity.wallet.Wallet;
import br.com.webbudget.domain.entity.wallet.WalletType;
import br.com.webbudget.domain.service.WalletService;
import java.math.BigDecimal;
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
 * @since 1.0, 04/03/2014
 */
@ViewScoped
@ManagedBean
public class WalletBean extends AbstractBean {

    @Getter
    private Wallet wallet;
    @Getter
    private List<Wallet> wallets;
    
    @Setter
    @ManagedProperty("#{walletService}")
    private WalletService walletService;

    /**
     * 
     * @return 
     */
    @Override
    protected Logger initializeLogger() {
        return LoggerFactory.getLogger(WalletBean.class);
    }
    
    /**
     * 
     */
    public void initializeListing(){
        if (!FacesContext.getCurrentInstance().isPostback()) {
            this.viewState = ViewState.LISTING;
            this.wallets = this.walletService.listWallets(null);
        }
    }

    /**
     * 
     * @param walletId 
     */
    public void initializeForm(long walletId) {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            if (walletId == 0) {
                this.viewState = ViewState.ADDING;
                this.wallet = new Wallet();
            } else {
                this.viewState = ViewState.EDITING;
                this.wallet = this.walletService.findWalletById(walletId);
            }
        }
    }
    
    /**
     * 
     * @return o form de inclusao
     */
    public String changeToAdd() {
        return "formWallet.xhtml?faces-redirect=true";
    }
    
    /**
     * 
     * @return 
     */
    public String changeToListing() {
        return "listWallets.xhtml?faces-redirect=true";
    }
    
    /**
     * 
     * @param walletId
     * @return 
     */
    public String changeToEdit(long walletId) {
        return "formWallet.xhtml?faces-redirect=true&walletId=" + walletId;
    }
    
    /**
     * 
     * @param walletId 
     */
    public void changeToDelete(long walletId) {
        this.wallet = this.walletService.findWalletById(walletId);
        this.openDialog("deleteWalletDialog","dialogDeleteWallet");
    }
    
    /**
     * Cancela e volta para a listagem
     * 
     * @return 
     */
    public String doCancel() {
        return "listWallets.xhtml?faces-redirect=true";
    }
    
    /**
     * 
     */
    public void doSave() {
        
        // zeramos o saldo se nao for informado
        if (this.wallet.getBalance() == null) {
            this.wallet.setBalance(BigDecimal.ZERO);
        }
        
        try {
            this.walletService.saveWallet(this.wallet);
            this.wallet = new Wallet();
            
            this.info("wallet.action.saved", true);
        }  catch (Exception ex) {
            this.logger.error("WalletBean#doSave found erros", ex);
            this.fixedError(ex.getMessage(), true);
        } 
    }
    
    /**
     * 
     */
    public void doAdjustment() {
        
        try {
            this.walletService.adjustBalance(this.wallet);
            this.wallet = new Wallet();
            
            this.wallets = this.walletService.listWallets(null);
            
            this.info("wallet.action.adjusted", true);
        }  catch (Exception ex) {
            this.logger.error("WalletBean#doAdjustment found erros", ex);
            this.fixedError(ex.getMessage(), true);
        } finally {
            this.update("walletsList");
            this.closeDialog("dialogAdjustBalance");
        }
    }
    
    /**
     * 
     */
    public void doUpdate() {
        
        try {
            this.wallet = this.walletService.updateWallet(this.wallet);
            
            this.info("wallet.action.updated", true);
        } catch (Exception ex) {
            this.logger.error("WalletBean#doUpdate found erros", ex);
            this.fixedError(ex.getMessage(), true);
        } 
    }
    
    /**
     * 
     */
    public void doDelete() {
        
        try {
            this.walletService.deleteWallet(this.wallet);
            this.wallets = this.walletService.listWallets(false);
            
            this.info("wallet.action.deleted", true);
        } catch (Exception ex) {
            this.logger.error("WalletBean#doDelete found erros", ex);
            this.fixedError(ex.getMessage(), true);
        } finally {
            this.update("walletsList");
            this.closeDialog("dialogDeleteWallet");
        }
    }
    
    /**
     * 
     * @param walletId 
     */
    public void displayAdjustment(long walletId) {
        this.wallet = this.walletService.findWalletById(walletId);
        this.openDialog("adjustBalanceDialog", "dialogAdjustBalance");
    }
    
    /**
     * 
     */
    public void cancelAdjustment() {
        this.wallet = null;
        this.closeDialog("dialogAdjustBalance");
    }
    
    /**
     * 
     */
    public void loadBankData() {
        this.update("inBank");
        this.update("inDigit");
        this.update("inAgency");
        this.update("inAccount");
    }
        
    /**
     * 
     * @return 
     */
    public WalletType[] getAvailableWalletTypes() {
        return WalletType.values();
    }
}