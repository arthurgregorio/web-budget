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
import javax.faces.view.ViewScoped;
import javax.inject.Named;

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

//    @Getter
//    @Setter
//    private WalletBalance walletBalance;
//
//    @Getter
//    private List<Wallet> wallets;
//    @Getter
//    private List<WalletBalance> transferences;
//
//    @Inject
//    private WalletService walletService;
//
//    /**
//     * Inicializa a listagem de alimentos
//     */
//    public void initialize() {
//        
//        this.walletBalance = new WalletBalance();
//        
//        // lista a carteiras
//        this.wallets = this.walletService.listWallets(false);
//        this.transferences = this.walletService.listTransferences(null, null);
//    }
//
//    /**
//     * Transfere a grana
//     */
//    public void doTransference() {
//        try {
//            this.walletService.transfer(this.walletBalance);
//
//            this.walletBalance = new WalletBalance();
//            
//            this.wallets = this.walletService.listWallets(false);
//            this.transferences = this.walletService.listTransferences(null, null);
//
//            this.addInfo(true, "transference.transfered");
//        } catch (InternalServiceError ex) {
//            this.addError(true, ex.getMessage(), ex.getParameters());
//        } catch (Exception ex) {
//            this.logger.error(ex.getMessage(), ex);
//            this.addError(true, "error.undefined-error", ex.getMessage());
//        }
//    }
//
//    /**
//     * Atualiza a lista do historico
//     */
//    public void updateHistoric() {
//        try {
//            this.transferences = this.walletService.listTransferences(null, null);
//        } catch (InternalServiceError ex) {
//            this.addError(true, ex.getMessage(), ex.getParameters());
//        } catch (Exception ex) {
//            this.logger.error(ex.getMessage(), ex);
//            this.addError(true, "error.undefined-error", ex.getMessage());
//        } finally {
//            this.updateComponent("transferencesList");
//        }
//    }
//    
//    /**
//     * Monta uma lista somente com as transferencias daquela data especifica
//     * 
//     * @param inclusion a data de inclusao
//     * @return a lista de saldos daquela data
//     */
//    public List<WalletBalance> transferencesByInclusion(LocalDate inclusion) {
//        return this.transferences.stream()
//                .filter(balance -> balance.getInclusion().toLocalDate().equals(inclusion))
//                .collect(Collectors.toList());
//    }
//    
//    /**
//     * @return as transferencias agrupadas por data
//     */
//    public List<LocalDate> groupTransferencesByInclusion() {
//        
//        final List<LocalDate> grouped = new ArrayList<>();
//        
//        this.transferences.stream().forEach(balance -> {
//            if (!grouped.contains(balance.getInclusion().toLocalDate())) {
//                grouped.add(balance.getInclusion().toLocalDate());
//            }
//        });
//        return grouped;
//    }
//    
//    /**
//     * Exibe a dialog com o motivo da transferencia
//     */
//    public void showTransferReasonDialog() {
//        this.updateAndOpenDialog("transferenceReasonDialog", "dialogTransferenceReason");
//    }
}
