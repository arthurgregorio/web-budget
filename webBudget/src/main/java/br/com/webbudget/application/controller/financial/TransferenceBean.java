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
import br.com.webbudget.domain.service.WalletService;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 20/05/2014
 */
@ViewScoped
@ManagedBean
public class TransferenceBean extends AbstractBean {

    @Getter
    @Setter
    private Wallet sourceWallet;
    @Getter
    @Setter
    private Wallet targetWallet;

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
        this.viewState = ViewState.LISTING;
        this.wallets = this.walletService.listWallets(false);
    }

    /**
     *
     */
    public void initializeForm() {
        this.viewState = ViewState.ADDING;
        this.walletBalance = new WalletBalance();
        this.wallets = this.walletService.listWallets(false);
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
        } catch (Exception ex) {
            this.logger.error("TransferenceBean#doTransference found erros", ex);
            this.fixedError(ex.getMessage(), true);
        }
    }

    /**
     *
     */
    public void filterTransfers() {

        if (this.sourceWallet == null && this.targetWallet == null) {
            this.transferences = this.walletService.listTransferences();
        } else {
            this.transferences = this.walletService
                    .listTransfersByWallet(this.sourceWallet, this.targetWallet);
        }

        if (this.transferences.isEmpty()) {
            this.error("transfer.no-trasfers", true);
        } else {
            this.update("transferencesList");
        }
    }
}
