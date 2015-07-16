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
import br.com.webbudget.domain.entity.wallet.WalletBalance;
import br.com.webbudget.domain.entity.wallet.WalletType;
import br.com.webbudget.domain.service.WalletService;
import java.math.BigDecimal;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;

/**
 * Controller para a view do manutencao de carteiras
 *
 * @author Arthur Gregorio
 *
 * @version 1.2.0
 * @since 1.0.0, 04/03/2014
 */
@Named
@ViewScoped
public class WalletBean extends AbstractBean {

    @Getter
    @Setter
    private WalletBalance selectedBalance;
    
    @Getter
    private Wallet wallet;

    @Getter
    private List<Wallet> wallets;
    @Getter
    private List<WalletBalance> walletBalances;

    @Inject
    private WalletService walletService;

    /**
     *
     */
    public void initializeListing() {
        this.viewState = ViewState.LISTING;
        this.wallets = this.walletService.listWallets(null);
    }

    /**
     * @param walletId
     */
    public void initializeForm(long walletId) {

        if (walletId == 0) {
            this.viewState = ViewState.ADDING;
            this.wallet = new Wallet();
        } else {
            this.viewState = ViewState.EDITING;
            this.wallet = this.walletService.findWalletById(walletId);
        }
    }

    /**
     * @param walletId
     */
    public void initializeAdjustment(long walletId) {
        this.wallet = this.walletService.findWalletById(walletId);
    }

    /**
     * @return o form de inclusao
     */
    public String changeToAdd() {
        return "formWallet.xhtml?faces-redirect=true";
    }

    /**
     * @return
     */
    public String changeToListing() {
        return "listWallets.xhtml?faces-redirect=true";
    }

    /**
     * @param walletId
     * @return
     */
    public String changeToEdit(long walletId) {
        return "formWallet.xhtml?faces-redirect=true&walletId=" + walletId;
    }

    /**
     * @param walletId
     * @return
     */
    public String changeToAdjustment(long walletId) {
        return "formAdjustment.xhtml?faces-redirect=true&walletId=" + walletId;
    }

    /**
     * @param walletId
     */
    public void changeToDelete(long walletId) {
        this.wallet = this.walletService.findWalletById(walletId);
        this.openDialog("deleteWalletDialog", "dialogDeleteWallet");
    }

    /**
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
        } catch (Exception ex) {
            this.logger.error("WalletBean#doSave found erros", ex);
            this.fixedError("generic.operation-error", true, ex.getMessage());
        }
    }

    /**
     *
     */
    public void doAdjustment() {

        try {
            this.walletService.adjustBalance(this.wallet);

            this.wallets = this.walletService.listWallets(null);

            this.openDialog("adjustmentDialog", "dialogAdjustment");
        } catch (Exception ex) {
            this.logger.error("WalletBean#doAdjustment found erros", ex);
            this.fixedError("generic.operation-error", true, ex.getMessage());
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
            this.fixedError("generic.operation-error", true, ex.getMessage());
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
            this.fixedError("generic.operation-error", true, ex.getMessage());
        } finally {
            this.update("walletsList");
            this.closeDialog("dialogDeleteWallet");
        }
    }
    
    /**
     * @param walletId 
     */
    public void showBalance(long walletId) {

        this.wallet = this.walletService.findWalletById(walletId);
        this.walletBalances = this.walletService.listBalancesByWallet(this.wallet);
        
        // se nao tem saldo, nao mostra a popup
        if (this.walletBalances.isEmpty()) {
            this.warn("wallet.action.no-balances", true);
            return;
        }

        // se tem saldos, ja pega o primeiro e sai mostrando
        this.selectedBalance = this.getWalletBalances().get(0);
        
        this.openDialog("balanceHistoryDialog", "dialogBalanceHistory");
    }

    /**
     * Atualiza os dados do saldo na tela
     */
    public void updateBalanceDetails() {
        this.update("detailsPanel");
    }
    
    /**
     * Carrega os dados referentes a conta bancaria quando o tipo a ser cadast-
     * rado e uma conta bancaria
     */
    public void loadBankData() {
        this.update("inBank");
        this.update("inDigit");
        this.update("inAgency");
        this.update("inAccount");
    }

    /**
     * @return os tipos de carteira disponiveis para cadastro
     */
    public WalletType[] getAvailableWalletTypes() {
        return WalletType.values();
    }
}
