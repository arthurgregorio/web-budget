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
     * @param walletId 
     */
    public void initializeAdjustment(long walletId) {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            this.wallet = this.walletService.findWalletById(walletId);            
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
     * @return 
     */
    public String changeToAdjustment(long walletId) {
        return "formAdjustment.xhtml?faces-redirect=true&walletId=" + walletId;
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
            
            this.wallets = this.walletService.listWallets(null);
            
            this.openDialog("adjustmentDialog","dialogAdjustment");
        }  catch (Exception ex) {
            this.logger.error("WalletBean#doAdjustment found erros", ex);
            this.fixedError(ex.getMessage(), true);
        } finally {
            this.update("walletsList");
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
