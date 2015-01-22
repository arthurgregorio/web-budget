package br.com.webbudget.application.controller.financial;

import br.com.webbudget.application.controller.AbstractBean;
import br.com.webbudget.application.exceptions.ApplicationException;
import br.com.webbudget.domain.entity.card.Card;
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
import br.com.webbudget.domain.service.FinancialPeriodService;
import br.com.webbudget.domain.service.MovementService;
import br.com.webbudget.domain.service.WalletService;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 18/03/2014
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
    private Payment payment;
    @Getter
    private Movement movement;
    @Getter
    private FinancialPeriod financialPeriod;
    
    @Getter
    @Setter
    private Movement selectedMovement;
    
    @Getter
    private List<Wallet> wallets;
    @Getter
    private List<Card> debitCards;
    @Getter
    private List<Card> creditCards;
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
    private transient CardService cardService;
    @Setter
    @ManagedProperty("#{walletService}")
    private transient WalletService walletService;
    @Setter
    @ManagedProperty("#{movementService}")
    private transient MovementService movementService;
    @Setter
    @ManagedProperty("#{financialPeriodService}")
    private transient FinancialPeriodService financialPeriodService;

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
    public void initializeListing(){
        if (!FacesContext.getCurrentInstance().isPostback()) {
            this.viewState = ViewState.LISTING;
            this.movements = this.movementService.listMovementsByActiveFinancialPeriod();
            
            // preenche os campos de filtro
            this.costCenters = this.movementService.listCostCenters(false);
            this.financialPeriods = this.financialPeriodService.listFinancialPeriods(null);
        }
    }

    /**
     * 
     * @param movementId 
     */
    public void initializeForm(long movementId) {
        if (!FacesContext.getCurrentInstance().isPostback()) {

            // buscamos o periodo financeiro atual
            this.costCenters = this.movementService.listCostCenters(false);
            this.financialPeriod = this.financialPeriodService.findActiveFinancialPeriod();
            this.openFinancialPeriods = this.financialPeriodService.listOpenFinancialPeriods();

            if (movementId == 0) {
                this.viewState = ViewState.ADDING;

                this.movement = new Movement();

                // setamos o periodo financeiro atual no movimento a ser incluido
                this.movement.setFinancialPeriod(this.financialPeriod);
            } else {
                this.viewState = ViewState.EDITING;

                this.movement = this.movementService.findMovementById(movementId);

                // seta o centro de custo para setar a classe
                this.movement.setCostCenter(this.movement.getMovementClass().getCostCenter());
                this.movementClasses = this.movementService.listMovementClassesByCostCenterAndType(
                        this.movement.getMovementClass().getCostCenter(), null);
            }
        }
    }
    
    /**
     * 
     * @param movementId 
     */
    public void initializePayment(long movementId) {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            this.movement = this.movementService.findMovementById(movementId);

            this.payment = new Payment();

            // tipos entrada, pagamento somente em carteira
            if (this.movement.getMovementClass().getMovementClassType() == MovementClassType.IN) {
                this.payment.setPaymentMethodType(PaymentMethodType.IN_CASH);
            }

            // lista as fontes para preencher os combos
            this.wallets = this.walletService.listWallets(false);
            this.debitCards = this.cardService.listDebitCards(false);
            this.creditCards = this.cardService.listCreditCards(false);
        }
    }
    
    /**
     * Pesquisa com filtro
     */
    public void filterList() {
        this.movements = this.movementService.listByFilter(this.filter, this.filterPaid);
        this.update("movementsList");
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
     * 
     * @param movementId 
     */
    public void changeToDelete(long movementId) {
        this.movement = this.movementService.findMovementById(movementId);
        this.openDialog("deleteMovementDialog","dialogDeleteMovement");
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
            this.fixedError(ex.getMessage(), true);
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
            this.fixedError(ex.getMessage(), true);
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
                this.movementService.payAndSaveMovement(this.movement);
                
                this.openDialog("confirmPaymentDialog","dialogConfirmPayment");
                
                this.movement = new Movement();
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
                this.movementService.payAndSaveMovement(this.movement);
                
                this.movement = new Movement();
                this.movement.setFinancialPeriod(this.financialPeriod);

                this.closeDialog("popupPayment");
                this.info("movement.action.saved-and-paid", true);
            }
        } catch (ApplicationException ex) {
            this.logger.error("MovementBean#doPayment found erros", ex);
            this.fixedError(ex.getMessage(), true);
        } 
    }
    
    /**
     * 
     */
    public void doUpdate() {
        
        try {
            this.movement = this.movementService.updateMovement(this.movement);
            
            // seta o centro de custo para nao ficar em branco a view
            this.movement.setCostCenter(this.movement.getMovementClass().getCostCenter());
            
            this.info("movement.action.updated", true);
        } catch (ApplicationException ex) {
            this.logger.error("MovementBean#doUpdate found erros", ex);
            this.fixedError(ex.getMessage(), true);
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
     * Atualiza o combo de classes quando o usuário selecionar o centro de custo
     */
    public void loadMovementClasses() {
        this.movementClasses = this.movementService
                .listMovementClassesByCostCenterAndType(this.movement.getCostCenter(), null);
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
        if (this.movement.getMovementClass().getMovementClassType() == MovementClassType.IN) {
            this.payment.setPaymentMethodType(PaymentMethodType.IN_CASH);
        }

        // lista as fontes para preencher os combos
        this.wallets = this.walletService.listWallets(false);
        this.debitCards = this.cardService.listDebitCards(false);
        this.creditCards = this.cardService.listCreditCards(false);
        
        this.openDialog("paymentDialog","dialogPayment");
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
    public void displayDetailsPopup() {
        this.openDialog("detailMovementDialog","dialogDetailMovement");
    }
    
    /**
     * 
     */
    public void closeDetailsPopup() {
        this.selectedMovement = null;
        this.update("movementsList");
        this.closeDialog("dialogDetailMovement");
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
     * @return a lista dos valores do enum
     */
    public PaymentMethodType[] getAvailablePaymentMethods() {
        return PaymentMethodType.values();
    }
}
