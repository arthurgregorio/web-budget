/*
 * Copyright (C) 2014 Arthur Gregorio, AG.Software
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
import br.com.webbudget.domain.entities.financial.Transference;
import br.com.webbudget.domain.entities.registration.Wallet;
import br.com.webbudget.domain.repositories.financial.TransferenceRepository;
import br.com.webbudget.domain.repositories.registration.WalletRepository;
import lombok.Getter;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Controller for the balance transfers between two wallets
 *
 * @author Arthur Gregorio
 *
 * @version 3.0.0
 * @since 1.0.0, 20/05/2014
 */
@Named
@ViewScoped
public class TransferenceBean extends AbstractBean {

    @Getter
    private Transference transference;

    @Getter
    private List<Wallet> wallets;

    @Inject
    private WalletRepository walletRepository;
    @Inject
    private TransferenceRepository transferenceRepository;

    /**
     * Initialize the UI
     */
    public void initialize() {
        this.transference = new Transference();
        this.wallets = this.walletRepository.findAllActive();
    }

    /**
     * Use this method to validate and save the transference
     */
    public void doTransference() {
        this.transferenceRepository.save(this.transference);
        this.addInfo(true, "info.transference.done");
    }
}
