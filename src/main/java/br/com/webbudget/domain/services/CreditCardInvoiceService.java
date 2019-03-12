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
package br.com.webbudget.domain.services;

import br.com.webbudget.application.components.builder.CreditCardInvoiceBuilder;
import br.com.webbudget.domain.entities.financial.CreditCardInvoice;
import br.com.webbudget.domain.entities.financial.Payment;
import br.com.webbudget.domain.entities.financial.PeriodMovement;
import br.com.webbudget.domain.entities.registration.Card;
import br.com.webbudget.domain.entities.registration.CardType;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.events.CardCreated;
import br.com.webbudget.domain.events.FinancialPeriodOpened;
import br.com.webbudget.domain.events.PeriodMovementPaid;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.repositories.financial.CreditCardInvoiceRepository;
import br.com.webbudget.domain.repositories.financial.PeriodMovementRepository;
import br.com.webbudget.domain.repositories.registration.CardRepository;
import br.com.webbudget.domain.repositories.registration.FinancialPeriodRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

/**
 * The {@link CreditCardInvoice} service
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 10/03/2019
 */
@ApplicationScoped
public class CreditCardInvoiceService {

    @Inject
    private CardRepository cardRepository;
    @Inject
    private PeriodMovementRepository periodMovementRepository;
    @Inject
    private FinancialPeriodRepository financialPeriodRepository;
    @Inject
    private CreditCardInvoiceRepository creditCardInvoiceRepository;

    /**
     * This method observes for events about the {@link FinancialPeriod} opening action and create the
     * {@link CreditCardInvoice} for all active credit {@link Card}
     *
     * @param financialPeriod to create the {@link CreditCardInvoice}
     */
    @Transactional
    public void createInvoices(@Observes @FinancialPeriodOpened FinancialPeriod financialPeriod) {
        final List<Card> cards = this.cardRepository.findByCardTypeAndActive(CardType.CREDIT, true);
        cards.forEach(card -> this.createInvoice(card, financialPeriod));
    }

    /**
     * This method observes for events about the creation of a new {@link Card} and create the {@link CreditCardInvoice}
     * for all open {@link FinancialPeriod}
     *
     * @param card we will open the {@link CreditCardInvoice}
     */
    @Transactional
    public void createInvoices(@Observes @CardCreated Card card) {

        if (card.getCardType() != CardType.CREDIT) {
            return;
        }

        final List<FinancialPeriod> financialPeriods = this.financialPeriodRepository.findByClosed(false);
        financialPeriods.forEach(financialPeriod -> this.createInvoice(card, financialPeriod));
    }

    /**
     * This method listen to {@link Event} about the {@link Payment} of any {@link PeriodMovement} and if we are paying
     * with a credit {@link Card} update the {@link CreditCardInvoice} with this {@link PeriodMovement}
     *
     * @param periodMovement the {@link PeriodMovement} to be linked with the {@link CreditCardInvoice} if the
     * {@link Payment} are made with a credit {@link Card}
     */
    @Transactional
    public void updateInvoiceAfterPayment(@Observes @PeriodMovementPaid PeriodMovement periodMovement) {

        if (!periodMovement.isPaidWithCreditCard()) {
            return;
        }

        final Card card = periodMovement.getPayment().getCard();
        final FinancialPeriod financialPeriod = periodMovement.getFinancialPeriod();

        final CreditCardInvoice invoice = this.creditCardInvoiceRepository
                .findByCardAndFinancialPeriod(card, financialPeriod)
                .orElseThrow(() -> new BusinessLogicException("error.credit-card-invoice.no-invoice",
                        card.getReadableName(), financialPeriod.getIdentification()));

        // link the period movement
        periodMovement.setCreditCardInvoice(invoice);
        this.periodMovementRepository.saveAndFlushAndRefresh(periodMovement);

        // update the invoice total
        invoice.setTotalValue(invoice.getTotalValue().add(periodMovement.getValue()));
        this.creditCardInvoiceRepository.saveAndFlushAndRefresh(invoice);
    }

    /**
     * Generic method to create a {@link CreditCardInvoice} for a given {@link Card}
     *
     * @param card to create the invoice
     * @param financialPeriod to be used for the invoice
     */
    private void createInvoice(Card card, FinancialPeriod financialPeriod) {

        final CreditCardInvoice creditCardInvoice = new CreditCardInvoiceBuilder()
                .card(card)
                .financialPeriod(financialPeriod)
                .build();

        this.creditCardInvoiceRepository.save(creditCardInvoice);
    }
}