/*
 * Copyright (C) 2019 Arthur Gregorio, AG.Software
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
package br.com.webbudget.application.components.builder;

import br.com.webbudget.domain.entities.financial.CreditCardInvoice;
import br.com.webbudget.domain.entities.registration.Card;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.infrastructure.i18n.MessageSource;

import java.time.LocalDate;

import static br.com.webbudget.infrastructure.utils.RandomCode.numeric;

/**
 * Builder implementation for the {@link CreditCardInvoice}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 10/03/2019
 */
public class CreditCardInvoiceBuilder extends AbstractBuilder<CreditCardInvoice> {

    /**
     * Constructor
     */
    public CreditCardInvoiceBuilder() {
        this.instance = new CreditCardInvoice();
    }

    /**
     * Set the {@link Card}
     *
     * @param card to be set
     * @return this builder
     */
    public CreditCardInvoiceBuilder card(Card card) {
        this.instance.setCard(card);
        return this;
    }

    /**
     * Set the {@link FinancialPeriod}
     *
     * @param financialPeriod to be set
     * @return this builder
     */
    public CreditCardInvoiceBuilder financialPeriod(FinancialPeriod financialPeriod) {
        this.instance.setFinancialPeriod(financialPeriod);
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public CreditCardInvoice build() {
        this.instance.setIdentification(this.defineIdentification());
        this.instance.setDueDate(this.defineDueDate());
        return this.instance;
    }

    /**
     * Define the identification for this {@link CreditCardInvoice}
     *
     * @return the identification text
     */
    private String defineIdentification() {
        final Card card = this.instance.getCard();
        return MessageSource.get("credit-card-invoice.invoice-title", numeric(4), card.getName(),
                card.getNumber().substring(card.getNumber().length() - 4));
    }

    /**
     * Define the due date for this {@link CreditCardInvoice}
     *
     * @return the due date
     */
    private LocalDate defineDueDate() {
        return this.instance.getFinancialPeriod().getEnd().plusMonths(1).withDayOfMonth(
                this.instance.getCard().getExpirationDay());
    }
}
