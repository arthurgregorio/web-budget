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
package br.com.webbudget.application.controller.financial;

import br.com.webbudget.application.components.ui.AbstractBean;
import br.com.webbudget.application.components.ui.filter.TransferenceFilter;
import br.com.webbudget.domain.entities.financial.Transference;
import br.com.webbudget.domain.entities.registration.Wallet;
import br.com.webbudget.domain.repositories.financial.TransferenceRepository;
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
 * The {@link Transference} historic controller
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 07/10/2018
 */
@Named
@ViewScoped
public class TransferenceHistoricBean extends AbstractBean {

    private List<Transference> transfers;

    @Getter
    private TransferenceFilter filter;

    @Getter
    private List<Wallet> wallets;
    @Getter
    private List<LocalDate> transferenceDates;

    @Inject
    private WalletRepository walletRepository;
    @Inject
    private TransferenceRepository transferenceRepository;

    /**
     * Initialize this controller
     */
    public void initialize() {
        this.filter = new TransferenceFilter();
        this.wallets = this.walletRepository.findAll();
        this.filterList();
    }

    /**
     * Filter all transference according to the filter selection
     */
    public void filterList() {
        this.transfers = this.transferenceRepository.findByFilter(this.filter);
        this.processTransferenceDates();
    }

    /**
     * Clear the filter selection on the UI
     */
    public void clearFilter() {
        this.filter.clear();
        this.filterList();
    }

    /**
     * Process the dates contained in the list of transfers and return a distinct list of dates for the timeline
     */
    private void processTransferenceDates() {
        this.transferenceDates = this.transfers.stream()
                .sorted(Comparator.comparing(Transference::getTransferDate))
                .map(Transference::getTransferDate)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    /**
     * By a given date, return only the transfers for this date
     *
     * @param transferenceDate the date
     * @return the transfers
     */
    public List<Transference> transfersByDate(LocalDate transferenceDate) {
        return this.transfers.stream()
                .filter(balance -> balance.getTransferDate().equals(transferenceDate))
                .sorted(Comparator.comparing(Transference::getTransferDate).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Change back to the transference form
     *
     * @return outcome to the transference form
     */
    public String changeToForm() {
        return "formTransference.xhtml?faces-redirect=true";
    }
}
