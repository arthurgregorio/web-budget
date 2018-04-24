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

import static br.com.webbudget.application.components.NavigationManager.PageType.ADD_PAGE;
import static br.com.webbudget.application.components.NavigationManager.PageType.DELETE_PAGE;
import static br.com.webbudget.application.components.NavigationManager.PageType.DETAIL_PAGE;
import static br.com.webbudget.application.components.NavigationManager.PageType.LIST_PAGE;
import static br.com.webbudget.application.components.NavigationManager.PageType.UPDATE_PAGE;
import br.com.webbudget.application.components.ViewState;
import br.com.webbudget.application.components.table.Page;
import br.com.webbudget.application.controller.FormBean;
import br.com.webbudget.domain.entities.entries.Wallet;
import br.com.webbudget.domain.entities.entries.WalletType;
import br.com.webbudget.domain.repositories.entries.WalletRepository;
import br.com.webbudget.domain.services.WalletService;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.3.0
 * @since 1.0.0, 04/03/2014
 */
@Named
@ViewScoped
public class WalletBean extends FormBean<Wallet> {

    @Inject
    private WalletService walletService;
    
    @Inject
    private WalletRepository walletRepository;

    /**
     * 
     */
    @Override
    public void initialize() {
        super.initialize();
        this.temporizeHiding(this.getDefaultMessagesComponentId());
    }

    /**
     * 
     * @param id
     * @param viewState 
     */
    @Override
    public void initialize(long id, ViewState viewState) {
       this.viewState = viewState;
       
    }

    /**
     * 
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
     * 
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @return 
     */
    @Override
    public Page<Wallet> load(int first, int pageSize, String sortField, SortOrder sortOrder) {
        return this.walletRepository.findAllBy(this.filter.getValue(), 
                this.filter.getEntityStatusValue(), first, pageSize);
    }
    
    /**
     *
     */
    @Override
    public void doSave() {
        this.walletService.save(this.value);
        this.value = new Wallet();
        this.addInfo(true, "wallet.saved");
    }

    /**
     *
     */
    @Override
    public void doUpdate() {
        this.value = this.walletService.update(this.value);
        this.addInfo(true, "wallet.updated");
    }

    /**
     * 
     * @return 
     */
    @Override
    public String doDelete() {
        this.walletService.delete(this.value);
        this.addInfoAndKeep("wallet.deleted");
        return this.changeToListing();
    }

//    /**
//     *
//     */
//    public void doAdjustment() {
//        try {
//            this.walletService.adjustBalance(this.wallet);
//            this.updateAndOpenDialog("confirmAdjustmentDialog", "dialogConfirmAdjustment");
//        } catch (InternalServiceError ex) {
//            this.addError(true, ex.getMessage(), ex.getParameters());
//        } catch (Exception ex) {
//            this.logger.error(ex.getMessage(), ex);
//            this.addError(true, "error.undefined-error", ex.getMessage());
//        } 
//    }
//    /**
//     * Monta uma lista somente com os saldos daquela data especifica
//     * 
//     * @param inclusion a data de inclusao
//     * @return a lista de saldos daquela data
//     */
//    public List<WalletBalance> balancesByInclusion(LocalDate inclusion) {
//        return this.walletBalances.stream()
//                .filter(balance -> balance.getInclusion().toLocalDate().equals(inclusion))
//                .collect(Collectors.toList());
//    }
//    
//    /**
//     * @return os saldos agrupados por data
//     */
//    public List<LocalDate> groupBalancesByInclusion() {
//        
//        final List<LocalDate> grouped = new ArrayList<>();
//        
//        this.walletBalances.stream().forEach(balance -> {
//            if (!grouped.contains(balance.getInclusion().toLocalDate())) {
//                grouped.add(balance.getInclusion().toLocalDate());
//            }
//        });
//        return grouped;
//    }
    
    /**
     * @return os tipos de carteira disponiveis para cadastro
     */
    public WalletType[] getWalletTypes() {
        return WalletType.values();
    }
}
