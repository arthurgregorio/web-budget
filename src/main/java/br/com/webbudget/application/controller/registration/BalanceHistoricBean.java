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
import br.com.webbudget.application.components.ui.filter.WalletBalanceFilter;
import br.com.webbudget.domain.entities.financial.WalletBalance;
import br.com.webbudget.domain.entities.registration.Wallet;
import br.com.webbudget.domain.repositories.registration.WalletBalanceRepository;
import br.com.webbudget.domain.repositories.registration.WalletRepository;
import lombok.Getter;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for the balance historic of the {@link Wallet}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 11/07/2018
 */
@Named
@ViewScoped
public class BalanceHistoricBean extends AbstractBean {

    private List<WalletBalance> walletBalances;

    @Getter
    private WalletBalanceFilter filter;

    @Getter
    private Wallet wallet;
    @Getter
    private List<LocalDate> walletBalanceDates;

    @Inject
    private WalletRepository walletRepository;
    @Inject
    private WalletBalanceRepository walletBalanceRepository;

    /**
     * Initialize the view with the balances of the give wallet
     */
    public void initialize(long walletId) {
        this.wallet = this.walletRepository.findById(walletId).orElseGet(Wallet::new);
        this.filter = new WalletBalanceFilter(this.wallet);
        this.filterList();
    }

    /**
     * Filter the balance list
     */
    public void filterList() {
        this.walletBalances = this.walletBalanceRepository.findByFilter(this.filter);
        this.processBalanceDates();
    }

    /**
     * Clear the filter selection on the UI
     */
    public void clearFilter() {
        this.filter.clear();
        this.filterList();
    }

    /**
     * Process all the {@link WalletBalance} and give only the dates where we have balances
     */
    private void processBalanceDates() {
        this.walletBalanceDates = this.walletBalances.stream()
                .sorted(Comparator.comparing(WalletBalance::getMovementDateTime))
                .map(WalletBalance::getMovementDate)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    /**
     * Called by the view to get only the balances from a given date
     *
     * @param movementDate the date where we want the balances
     * @return the list of balances
     */
    public List<WalletBalance> balancesByDate(LocalDate movementDate) {
        return this.walletBalances.stream()
                .filter(balance -> balance.getMovementDate().equals(movementDate))
                .sorted(Comparator.comparing(WalletBalance::getMovementDateTime).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Return to the {@link Wallet} listing
     *
     * @return the outcome to the {@link Wallet} listing
     */
    public String changeToWalletsListing() {
        return "listWallets.xhtml?faces-redirect=true";
    }
}
