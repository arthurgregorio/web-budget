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
package br.com.webbudget.application.controller.registration;

import br.com.webbudget.application.controller.AbstractBean;
import br.com.webbudget.domain.entities.registration.Wallet;
import br.com.webbudget.domain.entities.registration.WalletBalance;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.repositories.registration.WalletRepository;
import br.com.webbudget.domain.services.WalletService;
import lombok.Getter;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * The {@link WalletBalance} view controller
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 11/07/2018
 */
@Named
@ViewScoped
public class WalletBalanceBean extends AbstractBean {

    @Getter
    private Wallet wallet;

    @Inject
    private WalletService walletService;
    
    @Inject
    private WalletRepository walletRepository;

    /**
     * Initialize the view
     */
    public void initialize(long id) {
        this.wallet = this.walletRepository.findOptionalById(id)
                .orElseThrow(() -> new BusinessLogicException("error.wallet.not-found"));
    }
}
