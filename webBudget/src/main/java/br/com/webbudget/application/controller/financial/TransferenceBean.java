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
package br.com.webbudget.application.controller.financial;

import br.com.webbudget.application.controller.AbstractBean;
import br.com.webbudget.domain.entity.wallet.Wallet;
import br.com.webbudget.domain.entity.wallet.WalletBalance;
import br.com.webbudget.domain.misc.ex.WbDomainException;
import br.com.webbudget.domain.service.WalletService;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;

/**
 * Bean que realiza as operacoes de transferencia de valores entre as carteiras
 *
 * @author Arthur Gregorio
 *
 * @version 1.2.0
 * @since 1.0.0, 20/05/2014
 */
@Named
@ViewScoped
public class TransferenceBean extends AbstractBean {

    @Getter
    @Setter
    private Wallet sourceWallet;
    @Getter
    @Setter
    private Wallet targetWallet;
    @Getter
    @Setter
    private WalletBalance selectedTransfer;

    @Getter
    private WalletBalance walletBalance;

    @Getter
    private List<Wallet> wallets;
    @Getter
    private List<WalletBalance> transferences;

    @Inject
    private WalletService walletService;

    /**
     * Inicializa a listagem de alimentos
     */
    public void initializeListing() {
        this.viewState = ViewState.LISTING;
        this.wallets = this.walletService.listWallets(false);
    }

    /**
     * Inicializa o form de transferencias
     */
    public void initializeForm() {
        this.viewState = ViewState.ADDING;
        
        this.walletBalance = new WalletBalance();
        
        // lista a carteiras
        this.wallets = this.walletService.listWallets(false);
    }

    /**
     * @return envia para o form de adicionar transferencias
     */
    public String changeToAdd() {
        return "formTransfer.xhtml?faces-redirect=true";
    }

    /**
     * @return cancela e volta
     */
    public String doCancel() {
        return "listTransfers.xhtml?faces-redirect=true";
    }

    /**
     * Transfere a grana
     */
    public void doTransference() {

//        try {
//            this.walletService.transfer(this.walletBalance);
//
//            this.walletBalance = new WalletBalance();
//            this.wallets = this.walletService.listWallets(false);
//
//            this.info("transfer.action.transfered", true);
//        } catch (WbDomainException ex) {
//            this.logger.error("TransferenceBean#doTransference found erros", ex);
//            this.fixedError(ex.getMessage(), true, ex.getParameters());
//        } catch (Exception ex) {
//            this.logger.error("TransferenceBean#doTransference found erros", ex);
//            this.fixedError("generic.operation-error", true, ex.getMessage());
//        }
    }

    /**
     * Filtra as transferencias e lista
     */
    public void filterTransfers() {

//        if (this.sourceWallet == null && this.targetWallet == null) {
//            this.transferences = this.walletService.listTransferences();
//        } else {
//            this.transferences = this.walletService
//                    .listTransfersByWallet(this.sourceWallet, this.targetWallet);
//        }
//
//        if (this.transferences.isEmpty()) {
//            this.error("transfer.no-trasfers", true);
//        } else {
//            this.update("transferencesList");
//        }
    }
    
    /**
     * Exibe a dialog com o motivo da transferencia
     */
    public void showTransferReasonDialog() {
//        this.openDialog("transferReasonDialog", "dialogTransferReason");
    }
    
    /**
     * Atualiza o fragment que tem os saldos
     */
    public void updateBalances() {
//        this.update("balancesPanel");
    }
}
