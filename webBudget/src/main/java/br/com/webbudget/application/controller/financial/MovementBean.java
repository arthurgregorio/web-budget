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
import br.com.webbudget.domain.misc.dto.MovementFilter;
import br.com.webbudget.domain.misc.ex.WbDomainException;
import br.com.webbudget.domain.misc.table.AbstractLazyModel;
import br.com.webbudget.domain.misc.table.MovementsListModel;
import br.com.webbudget.domain.service.CardService;
import br.com.webbudget.domain.service.ContactService;
import br.com.webbudget.domain.service.FinancialPeriodService;
import br.com.webbudget.domain.service.MovementService;
import br.com.webbudget.domain.service.WalletService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.omnifaces.util.Faces;

/**
 * Controle da tela de movimentos do sistema
 *
 * @author Arthur Gregorio
 *
 * @version 1.3.0
 * @since 1.0.0, 18/03/2014
 */
@Named
@ViewScoped
public class MovementBean extends AbstractBean {

    @Getter
    @Setter
    private String contactFilter;
    @Getter
    @Setter
    private MovementFilter movementFilter;

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
    private List<CostCenter> costCenters;
    @Getter
    private List<MovementClass> movementClasses;
    @Getter
    private List<FinancialPeriod> financialPeriods;
    @Getter
    private List<FinancialPeriod> openFinancialPeriods;

    @Inject
    private CardService cardService;
    @Inject
    private WalletService walletService;
    @Inject
    private ContactService contactService;
    @Inject
    private MovementService movementService;
    @Inject
    private FinancialPeriodService financialPeriodService;

    @Getter
    private AbstractLazyModel<Movement> movementsModel;

    /**
     * Inicializamos os objetos necessarios
     */
    @PostConstruct
    public void initialize() {
        
        // inicializa o model customizado
        this.movementsModel = new MovementsListModel(
                this.movementService, () -> this.getMovementFilter());
        
        // inicializa o DTO de filtro
        this.movementFilter = new MovementFilter();
    }

    /**
     *
     */
    public void initializeListing() {
        this.viewState = ViewState.LISTING;

        // inicializa o DTO de filtro
        this.movementFilter = new MovementFilter();

        // preenche os campos de filtro
        this.costCenters = this.movementService.listCostCenters(false);
        this.financialPeriods = this.financialPeriodService.listFinancialPeriods(null);
    }

    /**
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
            this.openFinancialPeriods = this.financialPeriodService.listFinancialPeriods(true);
        }
    }

    /**
     * @param movementId
     */
    public void initializePayment(long movementId) {

        this.movement = this.movementService.findMovementById(movementId);

        this.payment = new Payment();

        // se ele for parte de um lancamento de movimento fixo, entao pegamos
        // a data de inicio para compor a data de pagamento
        final LocalDate startDate
                = this.movementService.findStartDateByMovement(this.movement);

        if (startDate != null) {
            this.payment.setPaymentDate(startDate);
        }

        // tipos entrada, pagamento somente em carteira
        if (this.movement.getDirection() == MovementClassType.IN) {
            this.payment.setPaymentMethodType(PaymentMethodType.IN_CASH);
        } else // se for fatura de cartao, so permite pagar em carteira
        if (this.movement.getMovementType() == MovementType.CARD_INVOICE) {
            this.payment.setPaymentMethodType(PaymentMethodType.IN_CASH);
        } else {
            this.debitCards = this.cardService.listDebitCards(false);
            this.creditCards = this.cardService.listCreditCards(false);
        }

        // lista as fontes para preencher os combos
        this.wallets = this.walletService.listWallets(false);
    }

    /**
     *
     */
    public void filterList() {
        this.update("movementsList");
    }

    /**
     * Pesquisa os contatos pelo filtro
     */
    public void filterContactsList() {

        try {
            this.contacts = this.contactService.listContactsByFilter(this.contactFilter, false);
        } catch (Exception ex) {
            this.logger.error("MovementBean#filterContactsList found erros", ex);
            this.fixedError("generic.operation-error", true, ex.getMessage());
        } finally {
            this.update("contactsList");
        }
    }

    /**
     * @return
     */
    public String changeToAdd() {
        return "formMovement.xhtml?faces-redirect=true";
    }

    /**
     * @return
     */
    public String changeToListing() {
        return "listMovements.xhtml?faces-redirect=true";
    }

    /**
     * @param movementId
     * @return
     */
    public String changeToEdit(long movementId) {
        return "formMovement.xhtml?faces-redirect=true&movementId=" + movementId;
    }

    /**
     * @return
     */
    public String changeToOpenFinancialPeriod() {
        return "/main/miscellany/financialPeriod/formFinancialPeriod.xhtml";
    }

    /**
     * @param movementId
     * @return
     */
    public String changeToPay(long movementId) {
        return "formPayment.xhtml?faces-redirect=true&movementId=" + movementId;
    }

    /**
     * @param movementId
     * @return
     */
    public String changeToDetail(long movementId) {
        return "formMovement.xhtml?faces-redirect=true&movementId=" + movementId + "&detailing=true";
    }

    /**
     * Da um redirect para os detalhes do movimento
     */
    public void changeToDetail() {
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
     * @param movementId
     */
    public void changeToDelete(long movementId) {
        this.movement = this.movementService.findMovementById(movementId);
        this.openDialog("deleteMovementDialog", "dialogDeleteMovement");
    }

    /**
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
        } catch (WbDomainException ex) {
            this.logger.error("MovementBean#doSave found erros", ex);
            this.fixedError(ex.getMessage(), true, ex.getParameters());
        } catch (Exception ex) {
            this.logger.error("MovementBean#doSave found erros", ex);
            this.fixedError("generic.operation-error", true, ex.getMessage());
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
        } catch (WbDomainException ex) {
            this.logger.error("MovementBean#doSaveAndPay found erros", ex);
            this.fixedError(ex.getMessage(), true, ex.getParameters());
        } catch (Exception ex) {
            this.logger.error("MovementBean#doSaveAndPay found erros", ex);
            this.fixedError("generic.operation-error", true, ex.getMessage());
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
        } catch (WbDomainException ex) {
            this.logger.error("MovementBean#doPayment found erros", ex);
            this.fixedError(ex.getMessage(), true);
        } catch (Exception ex) {
            this.logger.error("MovementBean#doPayment found erros", ex);
            this.fixedError("generic.operation-error", true, ex.getMessage());
        } finally {
            this.update("paymentForm");
        }
    }

    /**
     * Pagamos o movimento depois de salvar, caso seja uma edicao com pagamento
     * entao devolvemos a pagina de listagem
     * 
     * @return a pagina de listagem caso seja uma pagamento apos edicao
     */
    public String doPaymentAfterSave() {

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
        } catch (Exception ex) {
            this.logger.error("MovementBean#doPaymentAfterSave found erros", ex);
            this.fixedError("generic.operation-error", true, ex.getMessage());
        } finally {
            this.update("movementForm");
        }

        // se estamos editando, voltamos para a listagem
        if (this.viewState == ViewState.EDITING) {
            return this.doCancel();
        }

        // se nao permanecemos aqui
        return null;
    }

    /**
     *
     */
    public void doUpdate() {

        try {
            this.movement = this.movementService.saveMovement(this.movement);
            this.info("movement.action.updated", true);
        } catch (WbDomainException ex) {
            this.logger.error("MovementBean#doUpdate found erros", ex);
            this.fixedError(ex.getMessage(), true, ex.getParameters());
        } catch (Exception ex) {
            this.logger.error("MovementBean#doUpdate found erros", ex);
            this.fixedError("generic.operation-error", true, ex.getMessage());
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
            this.info("movement.action.deleted", true);
        } catch (WbDomainException ex) {
            this.logger.error("MovementBean#doDelete found erros", ex);
            this.fixedError(ex.getMessage(), true, ex.getParameters());
        } catch (Exception ex) {
            this.logger.error("MovementBean#doDelete found erros", ex);
            this.fixedError("generic.operation-error", true, ex.getMessage());
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
            this.update("inValue");
            this.update("apportionmentList");
            this.closeDialog("dialogApportionment");
        } catch (WbDomainException ex) {
            this.logger.error("MovementBean#addApportionment found erros", ex);
            this.fixedError(ex.getMessage(), false, ex.getParameters());
        } catch (Exception ex) {
            this.logger.error("MovementBean#addApportionment found erros", ex);
            this.fixedError("generic.operation-error", false, ex.getMessage());
        }
    }

    /**
     *
     * @param id
     */
    public void deleteApportionment(String id) {
        this.movement.removeApportionment(id);
        this.update("inValue");
        this.update("apportionmentList");
    }

    /**
     * Limpa os filtros e listas para entao buscar do zero
     */
    public void showContactDialog() {

        this.contactFilter = null;
        this.contacts = new ArrayList<>();

        this.openDialog("contactDialog", "dialogContact");
    }

    /**
     * Quando selecionar o contato, fecha a dialog e atualiza a view
     */
    public void onContactSelect() {
        this.update("contactPanel");
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
     * @return se existe ou nao um periodo financeiro em aberto
     */
    public boolean haveOpenPeriod() {
        return this.financialPeriod != null;
    }

    /**
     * @return se este movimento pode ou nao ser pago com cartao
     */
    public boolean isPayableWithCard() {
        return this.movement.getMovementType() != MovementType.CARD_INVOICE
                && this.movement.getDirection() != MovementClassType.IN;
    }

    /**
     * @return
     */
    public boolean canSaveAndPay() {
        return this.haveOpenPeriod() && (this.viewState == ViewState.ADDING
                || this.viewState == ViewState.EDITING);
    }

    /**
     * @return
     */
    public boolean canSave() {
        return this.haveOpenPeriod() && this.viewState == ViewState.ADDING;
    }

    /**
     * @return os tipso de pagamento disponiveis para uso
     */
    public PaymentMethodType[] getAvailablePaymentMethods() {
        return PaymentMethodType.values();
    }
}
