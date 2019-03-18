/*
 * Copyright (C) 2018 Arthur Gregorio, AG.Software
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
package br.com.webbudget.application.controller.registration;

import br.com.webbudget.application.components.ui.AbstractBean;
import br.com.webbudget.domain.entities.registration.Wallet;
import br.com.webbudget.domain.entities.financial.WalletBalance;
import br.com.webbudget.domain.repositories.registration.WalletRepository;
import br.com.webbudget.domain.services.WalletService;
import lombok.Getter;
import lombok.Setter;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;

/**
 * Controller to manage the adjustment operation of the {@link WalletBalance}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 22/08/2018
 */
@Named
@ViewScoped
public class BalanceAdjustmentBean extends AbstractBean {

    @Getter
    @Setter
    private String adjustmentReason;
    @Getter
    @Setter
    private BigDecimal adjustmentValue;

    @Getter
    private Wallet wallet;

    @Inject
    private WalletService walletService;

    @Inject
    private WalletRepository walletRepository;

    /**
     * Initialize this bean with the selected {@link Wallet}
     *
     * @param walletId the id of the {@link Wallet} to be loaded
     */
    public void initialize(long walletId) {
        this.wallet = this.walletRepository.findById(walletId)
                .orElseGet(Wallet::new);
    }

    /**
     * Invoke the adjustment operation
     */
    public void doAdjustment() {
        this.walletService.adjustBalance(this.wallet, this.adjustmentValue, this.adjustmentReason);
        this.updateAndOpenDialog("confirmAdjustmentDialog", "dialogConfirmAdjustment");
    }

    /**
     * Change the view back to the listing
     *
     * @return the route to the listing view
     */
    public String changeToListing() {
        return "listWallets.xhtml?faces-redirect=true";
    }
}
