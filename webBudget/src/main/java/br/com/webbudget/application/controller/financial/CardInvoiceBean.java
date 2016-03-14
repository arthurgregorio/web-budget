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
import br.com.webbudget.domain.entity.card.CardInvoice;
import br.com.webbudget.domain.entity.movement.FinancialPeriod;
import br.com.webbudget.domain.misc.ex.InternalServiceError;
import br.com.webbudget.domain.service.CardService;
import br.com.webbudget.domain.service.FinancialPeriodService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import org.omnifaces.util.Faces;

/**
 * Controller para a tela de faturas de cartao e historico das faturas
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 1.0.0, 28/04/2014
 */
@Named
@ViewScoped
public class CardInvoiceBean extends AbstractBean {

    @Getter
    private CardInvoice cardInvoice;

    @Getter
    private List<Card> cards;
    @Getter
    private List<CardInvoice> cardInvoices;
    @Getter
    private List<FinancialPeriod> financialPeriods;

    @Inject
    private CardService cardService;
    @Inject
    private FinancialPeriodService financialPeriodService;

    /**
     * 
     */
    public void initialize() {
        
        this.cardInvoice = new CardInvoice();

        this.cards = this.cardService.listCreditCards(false);
        this.financialPeriods = this.financialPeriodService.listOpenFinancialPeriods();
    }

    /**
     * @return cancela e volta para a listagem
     */
    public String doCancel() {
        return "cardInvoiceView.xhtml?faces-redirect=true";
    }

    /**
     * Gera a fatura para o cartao e periodo selecionado
     */
    public void viewInvoice() {

        if (this.cardInvoice.getCard() == null 
                || this.cardInvoice.getFinancialPeriod() == null) {
            this.addError(true, "error.card-invoice.select-period-card");
            return;
        }

        try {
            this.cardInvoice = this.cardService.fillCardInvoice(this.cardInvoice);

            if (!this.cardInvoice.hasMovements()) {
                this.addError(true, "error.card-invoice.no-movements");
                this.cardInvoice = new CardInvoice();
            } 
        } catch (InternalServiceError ex) {
            this.addError(true, ex.getMessage(), ex.getParameters());
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ex);
            this.addError(true, "error.undefined-error", ex.getMessage());
        } finally {
            this.updateComponent("invoiceBox");
        }
    }
    
    /**
     * Invoca a criacao do movimento para a fatura
     */
    public void moveInvoice() {
        
        try {
            this.cardService.createMovement(this.cardInvoice);
            this.updateAndOpenDialog("moveInvoiceDialog", "dialogMoveInvoice");
        } catch (InternalServiceError ex) {
            this.addError(true, ex.getMessage(), ex.getParameters());
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ex);
            this.addError(true, "error.undefined-error", ex.getMessage());
        } 
    }

    /**
     * @return a pagina do historico de faturas
     */
    public String changeToHistoric() {
        return "cardInvoiceHistoric.xhtml?faces-redirect=true";
    }
    
    /**
     * Redireciona o usuario para ver o movimento referente a fatura
     * 
     * @param movementId o id do movimento
     * @return a URL para visualizar o movimento
     */
    public String changeToViewMovement(long movementId) {
        return "../movement/formMovement.xhtml?faces-redirect=true"
                + "&movementId=" + movementId + "&detailing=true";
    }
    
    /**
     * @return muda para a impressao da fatura
     */
    public String changeToPrintInvoice() {
        Faces.setFlashAttribute("cardInvoice", this.cardInvoice);
        return "cardInvoicePrint.xhtml?faces-redirect=true";
    }

    /**
     * @return a data atual em formato string
     */
    public String getActualDateAsString() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy")
                .format(LocalDate.now());
    }
}
