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

import br.com.webbudget.application.components.ui.ViewState;
import br.com.webbudget.application.components.ui.table.Page;
import br.com.webbudget.application.components.ui.LazyFormBean;
import br.com.webbudget.domain.entities.registration.Wallet;
import br.com.webbudget.domain.entities.registration.WalletType;
import br.com.webbudget.domain.repositories.registration.WalletRepository;
import br.com.webbudget.domain.services.WalletService;
import org.primefaces.model.SortOrder;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import static br.com.webbudget.application.components.ui.NavigationManager.PageType.*;
import static br.com.webbudget.application.components.ui.NavigationManager.Parameter;

/**
 * The {@link Wallet} maintenance routine controller
 *
 * @author Arthur Gregorio
 *
 * @version 1.3.0
 * @since 1.0.0, 04/03/2014
 */
@Named
@ViewScoped
public class WalletBean extends LazyFormBean<Wallet> {

    @Inject
    private WalletService walletService;

    @Inject
    private WalletRepository walletRepository;

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param viewState
     */
    @Override
    public void initialize(long id, ViewState viewState) {
        this.viewState = viewState;
        this.value = this.walletRepository.findById(id).orElseGet(Wallet::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initializeNavigationManager() {
        this.navigation.addPage(LIST_PAGE, "listWallets.xhtml");
        this.navigation.addPage(ADD_PAGE, "formWallet.xhtml");
        this.navigation.addPage(UPDATE_PAGE, "formWallet.xhtml");
        this.navigation.addPage(DETAIL_PAGE, "detailWallet.xhtml");
        this.navigation.addPage(DELETE_PAGE, "detailWallet.xhtml");
    }

    /**
     * {@inheritDoc}
     *
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @return
     */
    @Override
    public Page<Wallet> load(int first, int pageSize, String sortField, SortOrder sortOrder) {
        return this.walletRepository.findAllBy(this.filter.getValue(), this.filter.getEntityStatusValue(), first, pageSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doSave() {
        this.walletService.save(this.value);
        this.value = new Wallet();
        this.addInfo(true, "saved");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doUpdate() {
        this.value = this.walletService.update(this.value);
        this.addInfo(true, "updated");
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public String doDelete() {
        this.walletService.delete(this.value);
        this.addInfoAndKeep("deleted");
        return this.changeToListing();
    }

    /**
     * Method to navigate to the balance history page
     *
     * @param walletId the wallet to show the balance history
     * @return the navigation rule
     */
    public String changeToBalanceHistory(long walletId) {
        return this.navigation.to("balanceHistoric.xhtml", Parameter.of("id", walletId));
    }

    /**
     * Method to navigate to the balance adjustment page
     *
     * @param walletId the wallet to have your balance adjusted
     * @return the navigation rule
     */
    public String changeToBalanceAdjustment(long walletId) {
        return this.navigation.to("balanceAdjustment.xhtml", Parameter.of("id", walletId));
    }

    /**
     * Helper method to get the types defined in the {@link WalletType} enum
     *
     * @return an array of types from {@link WalletType}
     */
    public WalletType[] getWalletTypes() {
        return WalletType.values();
    }
}
