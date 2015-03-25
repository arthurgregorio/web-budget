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
import br.com.webbudget.domain.entity.card.CardInvoice;
import br.com.webbudget.domain.entity.movement.FinancialPeriod;
import br.com.webbudget.domain.entity.movement.MovementClass;
import br.com.webbudget.domain.entity.wallet.Wallet;
import br.com.webbudget.domain.service.CardService;
import br.com.webbudget.domain.service.FinancialPeriodService;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller para a tela de faturas de cartao e historico das faturas
 *
 * @author Arthur Gregorio
 *
 * @version 1.1.0
 * @since 1.0.0, 28/04/2014
 */
@ViewScoped
@ManagedBean
public class CardInvoiceBean extends AbstractBean {

    @Getter
    private CardInvoice cardInvoice;
    
    @Getter
    @Setter
    private Card selectedCard;
    @Getter
    @Setter
    private FinancialPeriod selectedFinancialPeriod;

    @Getter
    public List<Card> cards;
    @Getter
    public List<Wallet> wallets;
    @Getter
    public List<CardInvoice> cardInvoices;
    @Getter
    public List<MovementClass> movementClasses;
    @Getter
    public List<FinancialPeriod> financialPeriods;

    @Setter
    @ManagedProperty("#{cardService}")
    private CardService cardService;
    @Setter
    @ManagedProperty("#{financialPeriodService}")
    private FinancialPeriodService financialPeriodService;

    /**
     *
     * @return
     */
    @Override
    protected Logger initializeLogger() {
        return LoggerFactory.getLogger(CardInvoiceBean.class);
    }

    /**
     * Inicializa o formulario listando os cartoes de credito e o periodo para 
     * entao o usuario realizar a geracao de uma nova fatura
     */
    public void initialize() {
        this.cardInvoice = new CardInvoice();

        this.cards = this.cardService.listCreditCards(false);
        this.financialPeriods = this.financialPeriodService.listOpenFinancialPeriods();
    }

    /**
     * Inicializa a tela de historico das faturas
     */
    public void initializeHistory() {

        this.cardInvoices = new ArrayList<>();

        this.cards = this.cardService.listCreditCards(false);
        this.financialPeriods = this.financialPeriodService.listFinancialPeriods(null);
    }

    /**
     * @return cancela e volta para a listagem
     */
    public String doCancel() {
        return "generateCardInvoice.xhtml?faces-redirect=true";
    }

    /**
     * Gera a fatura para o cartao e periodo selecionado
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
                this.cardInvoice = new CardInvoice();
            } else {
                this.info("card-invoice.action.generated", true);
            }
        } catch (ApplicationException ex) {
            this.logger.error("CardInvoiceBean#generateInvoice found errors", ex);
            this.fixedError(ex.getMessage(), true);
        } finally {
            this.update("detailsPanel");
        }
    }

    /**
     *
     */
    public void loadHistory() {

        
    }

    /**
     * 
     * @param id 
     */
    public void detailInvoice(long id) {
       
        
    }
    
    /**
     * Invoca a criacao do movimento para a fatura
     */
    public void createInvoiceMovement() {

        try {
            this.cardService.createMovement(this.cardInvoice,
                    this.translate("card-invoice.identification"));

            this.openDialog("moveInvoiceDialog", "dialogMoveInvoice");
        } catch (ApplicationException ex) {
            this.logger.error("CardInvoiceBean#payInvoice found errors", ex);
            this.fixedError(ex.getMessage(), true);
        }
    }

    /**
     * @return a pagina do historico de faturas
     */
    public String changeToHistory() {
        return "invoiceHistory.xhtml?faces-redirect=true";
    }

    /**
     * @return se pode ou nao pagar esta fatura
     */
    public boolean canPay() {
        return (this.cardInvoice != null && this.cardInvoice.getMovements() != null
                && !this.cardInvoice.getMovements().isEmpty());
    }
}
