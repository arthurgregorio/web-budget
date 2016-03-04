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
import br.com.webbudget.domain.misc.ex.InternalServiceError;
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
 * @version 2.0.0
 * @since 1.0.0, 20/05/2014
 */
@Named
@ViewScoped
public class TransferenceBean extends AbstractBean {

    @Getter
    @Setter
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
    public void initialize() {
        
        this.walletBalance = new WalletBalance();
        
        // lista a carteiras
        this.wallets = this.walletService.listWallets(false);
        this.transferences = this.walletService.listTransferences(null, null);
    }

    /**
     * Transfere a grana
     */
    public void doTransference() {

        try {
            this.walletService.transfer(this.walletBalance);

            this.walletBalance = new WalletBalance();
            this.wallets = this.walletService.listWallets(false);

            this.addInfo(true, "transference.transfered");
        } catch (InternalServiceError ex) {
            this.addError(true, ex.getMessage(), ex.getParameters());
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ex);
            this.addError(true, "error.undefined-error", ex.getMessage());
        }
    }

    /**
     * 
     */
    public void updateHistoric() {
        
    }
    
    /**
     * Exibe a dialog com o motivo da transferencia
     */
    public void showTransferReasonDialog() {
        this.updateAndOpenDialog("transferenceReasonDialog", "dialogTransferenceReason");
    }
}
