package br.com.webbudget.application.controller.movements;

import br.com.webbudget.application.ViewState;
import br.com.webbudget.application.components.MessagesFactory;
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
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import org.omnifaces.util.Messages;
import org.primefaces.context.RequestContext;
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
public class MovementBean implements Serializable {

    @Getter
    private ViewState viewState;
    
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
    @ManagedProperty("#{messagesFactory}")
    private transient MessagesFactory messages;
    @Setter
    @ManagedProperty("#{walletService}")
    private transient WalletService walletService;
    @Setter
    @ManagedProperty("#{movementService}")
    private transient MovementService movementService;
    @Setter
    @ManagedProperty("#{financialPeriodService}")
    private transient FinancialPeriodService financialPeriodService;
    
    private final Logger LOG = LoggerFactory.getLogger(MovementBean.class);
    
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
                this.viewState = ViewState.ADD;

                this.movement = new Movement();

                // setamos o periodo financeiro atual no movimento a ser incluido
                this.movement.setFinancialPeriod(this.financialPeriod);
            } else {
                this.viewState = ViewState.EDIT;

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
        RequestContext.getCurrentInstance().update("movementsList");
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
        RequestContext.getCurrentInstance().execute("PF('popupDeleteMovement').show()");
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
            
            Messages.addInfo(null, messages.getMessage("movement.action.saved"));
        } catch (ApplicationException ex) {
            LOG.error("MovementBean#doSave found erros", ex);
            Messages.addError(null, messages.getMessage(ex.getMessage()));
        } finally {
            RequestContext.getCurrentInstance().update("messages");
            RequestContext.getCurrentInstance().execute("setTimeout(\"$(\'#messages\').slideUp(300)\", 5000)");
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
            LOG.error("MovementBean#doSaveAndPay found erros", ex);
            Messages.addError(null, messages.getMessage(ex.getMessage()));
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
                Messages.addError(null, messages.getMessage("movement.validate.payment-font"));
            } else {
                this.movementService.payAndSaveMovement(this.movement);
                this.movement = new Movement();

                RequestContext.getCurrentInstance().execute("PF('popupConfirmPayment').show()");
            }
        } catch (ApplicationException ex) {
            LOG.error("MovementBean#doPayment found erros", ex);
            Messages.addError(null, messages.getMessage(ex.getMessage()));
        } finally {
            RequestContext.getCurrentInstance().update("paymentForm");
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
                Messages.addError(null, messages.getMessage("movement.validate.payment-font"));
            } else {
                this.movementService.payAndSaveMovement(this.movement);
                
                this.movement = new Movement();
                this.movement.setFinancialPeriod(this.financialPeriod);

                RequestContext.getCurrentInstance().execute("PF('popupPayment').hide()");
                Messages.addInfo(null, messages.getMessage("movement.action.saved-and-paid"));
            }
        } catch (ApplicationException ex) {
            LOG.error("MovementBean#doPayment found erros", ex);
            Messages.addError(null, messages.getMessage(ex.getMessage()));
        } finally {
            RequestContext.getCurrentInstance().update("movementForm");
            RequestContext.getCurrentInstance().execute("setTimeout(\"$(\'#messages\').slideUp(300)\", 5000)");
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
            
            Messages.addInfo(null, messages.getMessage("movement.action.updated"));
        } catch (ApplicationException ex) {
            LOG.error("MovementBean#doUpdate found erros", ex);
            Messages.addError(null, messages.getMessage(ex.getMessage()));
        } finally {
            RequestContext.getCurrentInstance().update("movementForm");
            RequestContext.getCurrentInstance().execute("setTimeout(\"$(\'#messages\').slideUp(300)\", 5000)");
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
            
            Messages.addWarn(null, messages.getMessage("movement.action.deleted"));
        } catch (ApplicationException ex) {
            LOG.error("MovementBean#doDelete found erros", ex);
            Messages.addError(null, messages.getMessage(ex.getMessage()));
        } finally {
            RequestContext.getCurrentInstance().execute("PF('popupDeleteMovement').hide()");
            RequestContext.getCurrentInstance().update("messages");
            RequestContext.getCurrentInstance().execute("setTimeout(\"$(\'#messages\').slideUp(300)\", 5000)");
            RequestContext.getCurrentInstance().update("movementsList");
        }
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
     * Atualiza o combo de classes quando o usuário selecionar o centro de custo
     */
    public void loadMovementClasses() {
        this.movementClasses = this.movementService
                .listMovementClassesByCostCenterAndType(this.movement.getCostCenter(), null);
        RequestContext.getCurrentInstance().update("inMovementClass");
    }
    
    /**
     * Atualiza a view para mostrar o combo de acordo com o tipo de pagamento
     */
    public void loadPaymentMethodFont() {
        RequestContext.getCurrentInstance().update("inWallet");
        RequestContext.getCurrentInstance().update("inDebitCard");
        RequestContext.getCurrentInstance().update("inCreditCard");
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
        
        RequestContext.getCurrentInstance().update("paymentPopup");
        RequestContext.getCurrentInstance().execute("PF('popupPayment').show()");
    }
    
    /**
     * Invocado quando cancelamos o pagamento apos salvar
     */
    public void cancelPayment() {

        // reiniciamos o form
        this.movement = new Movement();
        this.movement.setFinancialPeriod(this.financialPeriod);

        // atualizamos tudo na tela
        RequestContext.getCurrentInstance().execute("PF('popupPayment').hide()");
        RequestContext.getCurrentInstance().update("movementForm");
        
        Messages.addWarn(null, messages.getMessage("movement.action.saved-not-paid"));
        RequestContext.getCurrentInstance().update("messages");
        RequestContext.getCurrentInstance().execute("setTimeout(\"$(\'#messages\').slideUp(300)\", 5000)");
    }

    /**
     * 
     */
    public void displayDetailsPopup() {
        RequestContext.getCurrentInstance().update("detailMovementPopup");
        RequestContext.getCurrentInstance().execute("PF('popupDetailMovement').show()");
    }
    
    /**
     * 
     */
    public void closeDetailsPopup() {
        this.selectedMovement = null;
        RequestContext.getCurrentInstance().update("movementsList");
        RequestContext.getCurrentInstance().execute("PF('popupDetailMovement').hide()");
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
    
    /**
     * 
     * @param id
     * @return 
     */
    public String getErrorMessage(String id) {
    
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        final Iterator<FacesMessage> iterator = facesContext.getMessages(id);
        
        if (iterator.hasNext()) {
            return this.messages.getMessage(iterator.next().getDetail());
        }
        return "";
    }
}
