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
import br.com.webbudget.application.exceptions.ApplicationException;
import br.com.webbudget.domain.entity.card.Card;
import br.com.webbudget.domain.entity.contact.Contact;
import br.com.webbudget.domain.entity.movement.Apportionment;
import br.com.webbudget.domain.entity.movement.CostCenter;
import br.com.webbudget.domain.entity.movement.FinancialPeriod;
import br.com.webbudget.domain.entity.movement.Movement;
import br.com.webbudget.domain.entity.movement.MovementClass;
import br.com.webbudget.domain.entity.movement.MovementClassType;
import br.com.webbudget.domain.entity.movement.MovementType;
import br.com.webbudget.domain.entity.movement.Payment;
import br.com.webbudget.domain.entity.movement.PaymentMethodType;
import br.com.webbudget.domain.entity.wallet.Wallet;
import br.com.webbudget.domain.service.CardService;
import br.com.webbudget.domain.service.ContactService;
import br.com.webbudget.domain.service.FinancialPeriodService;
import br.com.webbudget.domain.service.MovementService;
import br.com.webbudget.domain.service.WalletService;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import org.omnifaces.util.Faces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 18/03/2014
 */
@ViewScoped
@ManagedBean
public class MovementBean extends AbstractBean {

    @Getter
    @Setter
    private String filter;
    @Getter
    @Setter
    private boolean filterPaid;

    @Getter
    @Setter
    private Movement movement;
    @Getter
    private Payment payment;
    @Getter
    @Setter
    private Apportionment apportionment;
    @Getter
    private FinancialPeriod financialPeriod;

    @Getter
    private List<Wallet> wallets;
    @Getter
    private List<Card> debitCards;
    @Getter
    private List<Card> creditCards;
    @Getter
    private List<Contact> contacts;
    @Getter
    private List<Movement> movements;
    @Getter
    private List<CostCenter> costCenters;
    @Getter
    private List<MovementClass> movementClasses;
    @Getter
    private List<FinancialPeriod> financialPeriods;
    @Getter
    private List<FinancialPeriod> openFinancialPeriods;

    @Setter
    @ManagedProperty("#{cardService}")
    private CardService cardService;
    @Setter
    @ManagedProperty("#{walletService}")
    private WalletService walletService;
    @Setter
    @ManagedProperty("#{contactService}")
    private ContactService contactService;
    @Setter
    @ManagedProperty("#{movementService}")
    private MovementService movementService;
    @Setter
    @ManagedProperty("#{financialPeriodService}")
    private FinancialPeriodService financialPeriodService;

    /**
     *
     * @return
     */
    @Override
    protected Logger initializeLogger() {
        return LoggerFactory.getLogger(MovementBean.class);
    }

    /**
     *
     */
    public void initializeListing() {
        this.viewState = ViewState.LISTING;
        this.movements = this.movementService.listMovementsByActiveFinancialPeriod();

        // preenche os campos de filtro
        this.costCenters = this.movementService.listCostCenters(false);
        this.financialPeriods = this.financialPeriodService.listFinancialPeriods(null);
    }

    /**
     *
     * @param movementId
     * @param detailing
     */
    public void initializeForm(long movementId, boolean detailing) {

        // buscamos o periodo financeiro atual
        this.costCenters = this.movementService.listCostCenters(false);
        this.financialPeriod = this.financialPeriodService.findActiveFinancialPeriod();
        this.openFinancialPeriods = this.financialPeriodService.listOpenFinancialPeriods();
        
        if (movementId == 0 && !detailing) {
            this.viewState = ViewState.ADDING;

            this.movement = new Movement();

            // setamos o periodo financeiro atual no movimento a ser incluido
            this.movement.setFinancialPeriod(this.financialPeriod);
        } else if (movementId != 0 && !detailing) {
            this.viewState = ViewState.EDITING;
            this.movement = this.movementService.findMovementById(movementId);
        } else {
            this.viewState = ViewState.DETAILING;
            this.movement = this.movementService.findMovementById(movementId);
        }
    }

    /**
     *
     * @param movementId
     */
    public void initializePayment(long movementId) {

        this.movement = this.movementService.findMovementById(movementId);

        this.payment = new Payment();

        // tipos entrada, pagamento somente em carteira
        if (this.movement.getDirection() == MovementClassType.IN) {
            this.payment.setPaymentMethodType(PaymentMethodType.IN_CASH);
        } else {
            // se for fatura de cartao, so permite pagar em carteira
            if (this.movement.getMovementType() == MovementType.CARD_INVOICE) {
                this.payment.setPaymentMethodType(PaymentMethodType.IN_CASH);
            } else {
                this.debitCards = this.cardService.listDebitCards(false);
                this.creditCards = this.cardService.listCreditCards(false);
            }
        }

        // lista as fontes para preencher os combos
        this.wallets = this.walletService.listWallets(false);
    }

    /**
     * Pesquisa com filtro
     */
    public void filterMovementsList() {
        this.movements = this.movementService
                .listMovementsByFilter(this.filter, this.filterPaid);
        this.update("movementsList");
    }
    
    /**
     * Pesquisa os contatos pelo filtro
     */
    public void filterContactsList() {
        this.contacts = this.contactService.listContactsByFilter(this.filter, false);
        this.update("contactsList");
    }

    /**
     *
     * @return o form de inclusao
     */
    public String changeToAdd() {
        return "formMovement.xhtml?faces-redirect=true";
    }

    /**
     *
     * @return
     */
    public String changeToListing() {
        return "listMovements.xhtml?faces-redirect=true";
    }

    /**
     *
     * @param movementId
     * @return
     */
    public String changeToEdit(long movementId) {
        return "formMovement.xhtml?faces-redirect=true&movementId=" + movementId;
    }

    /**
     *
     * @return
     */
    public String changeToOpenFinancialPeriod() {
        return "../../miscellany/financialPeriod/formFinancialPeriod.xhtml?faces-redirect=true";
    }

    /**
     *
     * @param movementId
     * @return
     */
    public String changeToPay(long movementId) {
        return "formPayment.xhtml?faces-redirect=true&movementId=" + movementId;
    }

    /**
     * Da um redirect para os detalhes do movimento
     */
    public void changeToDetails() {
        try {
            String url = FacesContext.getCurrentInstance()
                    .getExternalContext().getRequestContextPath();

            url += "/main/financial/movement/formMovement.xhtml?movementId="
                    + this.movement.getId() + "&detailing=true";

            Faces.redirect(url);
        } catch (Exception ex) {
            this.logger.error("Cannot redirect user", ex);
        }
    }

    /**
     *
     * @param movementId
     */
    public void changeToDelete(long movementId) {
        this.movement = this.movementService.findMovementById(movementId);
        this.openDialog("deleteMovementDialog", "dialogDeleteMovement");
    }

    /**
     * Cancela e volta para a listagem
     *
     * @return
     */
    public String doCancel() {
        return "listMovements.xhtml?faces-redirect=true";
    }

    /**
     *
     */
    public void doSave() {

        try {
            this.movementService.saveMovement(this.movement);

            // reiniciamos o form
            this.movement = new Movement();
            this.movement.setFinancialPeriod(this.financialPeriod);

            this.info("movement.action.saved", true);
        } catch (ApplicationException ex) {
            this.logger.error("MovementBean#doSave found erros", ex);
            this.fixedError(ex.getMessage(), true, ex.getParameters());
        }
    }

    /**
     *
     */
    public void doSaveAndPay() {

        try {
            this.movement = this.movementService.saveMovement(this.movement);

            // invoca os metodos para mostrar a popup de pagamento
            this.displayPaymentPopup();
        } catch (ApplicationException ex) {
            this.logger.error("MovementBean#doSaveAndPay found erros", ex);
            this.fixedError(ex.getMessage(), true, ex.getParameters());
        }
    }

    /**
     *
     */
    public void doPayment() {

        // setamos o pagamento
        this.movement.setPayment(this.payment);

        try {
            if (this.payment.getCard() == null && this.payment.getWallet() == null) {
                this.error("movement.validate.payment-font", true);
            } else {
                this.movementService.payAndUpdateMovement(this.movement);

                this.openDialog("confirmPaymentDialog", "dialogConfirmPayment");

                this.movement = new Movement();
                this.movement.setFinancialPeriod(this.financialPeriod);
            }
        } catch (ApplicationException ex) {
            this.logger.error("MovementBean#doPayment found erros", ex);
            this.fixedError(ex.getMessage(), true);
        } finally {
            this.update("paymentForm");
        }
    }

    /**
     *
     */
    public void doPaymentAfterSave() {

        // setamos o pagamento
        this.movement.setPayment(this.payment);

        try {
            if (this.payment.getCard() == null && this.payment.getWallet() == null) {
                this.error("movement.validate.payment-font", true);
            } else {
                this.movementService.payAndUpdateMovement(this.movement);

                this.movement = new Movement();
                this.movement.setFinancialPeriod(this.financialPeriod);

                this.closeDialog("dialogPayment");
                this.info("movement.action.saved-and-paid", true);
            }
        } catch (ApplicationException ex) {
            this.logger.error("MovementBean#doPayment found erros", ex);
            this.fixedError(ex.getMessage(), true);
        } finally {
            this.update("movementForm");
        }
    }

    /**
     *
     */
    public void doUpdate() {

        try {
            this.movement = this.movementService.saveMovement(this.movement);
            this.info("movement.action.updated", true);
        } catch (ApplicationException ex) {
            this.logger.error("MovementBean#doUpdate found erros", ex);
            this.fixedError(ex.getMessage(), true, ex.getParameters());
        }
    }

    /**
     *
     */
    public void doDelete() {

        try {
            final MovementType movementType = this.movement.getMovementType();

            // fazemos a selecao do tipo de delecao a ser executado
            if (movementType == MovementType.MOVEMENT) {
                this.movementService.deleteMovement(this.movement);
            } else if (movementType == MovementType.CARD_INVOICE) {
                this.movementService.deleteCardInvoiceMovement(this.movement);
            }

            this.movements = this.movementService.listMovementsByActiveFinancialPeriod();

            this.info("movement.action.deleted", true);
        } catch (ApplicationException ex) {
            this.logger.error("MovementBean#doDelete found erros", ex);
            this.fixedError(ex.getMessage(), true);
        } finally {
            this.update("movementsList");
            this.closeDialog("dialogDeleteMovement");
        }
    }

    /**
     *
     */
    public void addApportionment() {
        try {
            this.movement.addApportionment(this.apportionment);
        
            this.update("valuePanel");
            this.update("apportionmentList");
            this.closeDialog("dialogApportionment");
        } catch (ApplicationException ex) {
            this.fixedError(ex.getMessage(), false);
        }
    }

    /**
     *
     * @param id
     */
    public void deleteApportionment(String id) {
        this.movement.removeApportionment(id);
        this.update("valuePanel");
        this.update("apportionmentList");
    }

    /**
     * Limpa os filtros e listas para entao buscar do zero
     */
    public void showContactDialog() {
        
        this.filter = null;
        this.contacts = new ArrayList<>();
        
        this.openDialog("contactDialog", "dialogContact");
    }
    
    /**
     * Quando selecionar o contato, fecha a dialog e atualiza a view
     */
    public void onContactSelect() {
        this.update("movementForm");
        this.closeDialog("dialogContact");
    }
    
    /**
     *
     */
    public void showApportionmentDialog() {
        
        // se o valor do rateio for igual ao total do movimento nem deixa exibir
        // a tela de rateios para que nao seja feito cagada
        if (this.movement.isApportionmentsValid()) {
            this.error("movement.validate.no-value-divide", true);
            return;
        }
        
        this.apportionment = new Apportionment();
        this.apportionment.setValue(this.movement.getValueToDivide());
        
        this.openDialog("apportionmentDialog", "dialogApportionment");
    }

    /**
     * Atualiza o combo de classes quando o usuário selecionar o centro de custo
     */
    public void loadMovementClasses() {
        this.movementClasses = this.movementService.listMovementClassesByCostCenterAndType(
                this.apportionment.getCostCenter(), null);
        this.update("inMovementClass");
    }

    /**
     * Atualiza a view para mostrar o combo de acordo com o tipo de pagamento
     */
    public void loadPaymentMethodFont() {
        this.update("inWallet");
        this.update("inDebitCard");
        this.update("inCreditCard");
    }

    /**
     *
     */
    public void displayPaymentPopup() {

        this.payment = new Payment();

        // tipos entrada, pagamento somente em carteira
        if (this.movement.getDirection() == MovementClassType.IN) {
            this.payment.setPaymentMethodType(PaymentMethodType.IN_CASH);
        }

        // lista as fontes para preencher os combos
        this.wallets = this.walletService.listWallets(false);
        this.debitCards = this.cardService.listDebitCards(false);
        this.creditCards = this.cardService.listCreditCards(false);

        this.openDialog("paymentDialog", "dialogPayment");
    }

    /**
     * Invocado quando cancelamos o pagamento apos salvar
     */
    public void cancelPayment() {

        // reiniciamos o form
        this.movement = new Movement();
        this.movement.setFinancialPeriod(this.financialPeriod);

        // atualizamos tudo na tela
        this.update("movementForm");
        this.closeDialog("dialogPayment");

        this.warn("movement.action.saved-not-paid", true);
    }

    /**
     *
     */
    public void showPaymentDetails() {

        if (this.movement.getPayment() == null) {
            this.warn("movement.not-paid", true);
        } else {
            this.openDialog("detailPaymentDialog", "dialogDetailPayment");
        }
    }

    /**
     * Se existe ou nao um periodo aberto para lancamentos
     *
     * @return
     */
    public boolean haveOpenPeriod() {
        return this.financialPeriod != null;
    }

    /**
     *
     * @return
     */
    public boolean isPayableWithCard() {
        return this.movement.getMovementType() != MovementType.CARD_INVOICE
                && this.movement.getDirection() != MovementClassType.IN;
    }

    /**
     *
     * @return a lista dos valores do enum
     */
    public PaymentMethodType[] getAvailablePaymentMethods() {
        return PaymentMethodType.values();
    }
}
