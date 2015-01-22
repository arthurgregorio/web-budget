package br.com.webbudget.application.controller.financial;

import br.com.webbudget.application.components.MessagesFactory;
import br.com.webbudget.application.controller.AbstractBean;
import br.com.webbudget.application.exceptions.ApplicationException;
import br.com.webbudget.domain.entity.card.Card;
import br.com.webbudget.domain.entity.card.CardInvoice;
import br.com.webbudget.domain.entity.movement.CostCenter;
import br.com.webbudget.domain.entity.movement.FinancialPeriod;
import br.com.webbudget.domain.entity.movement.MovementClass;
import br.com.webbudget.domain.entity.movement.MovementClassType;
import br.com.webbudget.domain.entity.wallet.Wallet;
import br.com.webbudget.domain.service.CardService;
import br.com.webbudget.domain.service.FinancialPeriodService;
import br.com.webbudget.domain.service.MovementService;
import br.com.webbudget.domain.service.WalletService;
import java.io.Serializable;
import java.util.List;
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
 * @since 1.0, 28/04/2014
 */
@ViewScoped
@ManagedBean
public class CardInvoiceBean extends AbstractBean {

    @Getter
    private CardInvoice cardInvoice;
    
    @Getter
    public List<Card> cards;
    @Getter
    public List<Wallet> wallets;
    @Getter
    public List<CostCenter> costCenters;
    @Getter
    public List<MovementClass> movementClasses;
    @Getter
    public List<FinancialPeriod> financialPeriods;
    
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
        return LoggerFactory.getLogger(CardInvoiceBean.class);
    }
    
    /**
     * Inicializa o formulario listando os cartoes de credito e o periodo
     */
    public void initialize() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            
            // FIXME arrumar isso! passar o code da fatura
            this.cardInvoice = new CardInvoice("");
            
            this.cards = this.cardService.listCreditCards(false);
            this.costCenters = this.movementService.listCostCenters(false);
            this.financialPeriods = this.financialPeriodService.listOpenFinancialPeriods();
        }
    }
    
    /**
     * 
     */
    public void generateInvoice() {
        
        if (this.cardInvoice.getCard() == null || this.cardInvoice.getFinancialPeriod() == null) {
            this.error("card-invoice.validate.null-period-card", true);
            return;
        }

        try {
            this.cardInvoice = this.cardService.fillCardInvoice(this.cardInvoice);
            
            if (this.cardInvoice.getMovements().isEmpty()) {
                this.info("card-invoice.action.no-movements-to-pay", true);
                this.cardInvoice = new CardInvoice("");
            } else {
                this.info("card-invoice.action.generated", true);
            }
        } catch (ApplicationException ex) {
            this.logger.error("CardInvoiceBean#generateInvoice found errors", ex);
            this.fixedError(ex.getMessage(), true);
        } finally {
            this.update("controlsForm");
            this.update("movementsList");
        }
    }
    
    /**
     * 
     */
    public void payInvoice() {
        
        if (this.cardInvoice.getWallet() == null) {
            this.error("card-invoice.validate.null-wallet", true);
            return;
        }

        try {
            this.cardService.payInvoice(this.cardInvoice);
            
            // limpa o form
            this.cardInvoice = new CardInvoice("");
                    
            this.info("card-invoice.action.paid", true);
        } catch (ApplicationException ex) {
            this.logger.error("CardInvoiceBean#payInvoice found errors", ex);
            this.fixedError(ex.getMessage(), true);
        } finally {
            this.update("controlsForm");
            this.update("movementsList");
            this.closeDialog("dialogPayInvoice");
        }
    }
    
    /**
     * Atualiza o combo de classes quando o usuário selecionar o centro de custo
     */
    public void loadMovementClasses() {
        this.movementClasses = this.movementService.listMovementClassesByCostCenterAndType(
                this.cardInvoice.getCostCenter(), MovementClassType.OUT);
        this.update("inMovementClass");
    }
    
    /**
     * Muda para a popup de pagamento da fatura
     */
    public void changeToPay() {
        
        // listas as carteiras para pagamento
        this.wallets = this.walletService.listWallets(false);
        
        // atualiza e chama a popup de pagamento
        this.openDialog("payInvoiceDialog","dialogPayInvoice");
    }
    
    /**
     * 
     * @return 
     */
    public boolean canPay() {
        return (this.cardInvoice != null && this.cardInvoice.getMovements() != null 
                && !this.cardInvoice.getMovements().isEmpty());
    }
}
