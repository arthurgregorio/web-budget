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
import br.com.webbudget.domain.entities.registration.Card;
import br.com.webbudget.domain.entities.registration.CardType;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.events.FinancialPeriodOpened;
import br.com.webbudget.domain.repositories.financial.CreditCardInvoiceRepository;
import br.com.webbudget.domain.repositories.registration.CardRepository;

import javax.enterprise.context.ApplicationScoped;
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
    private CreditCardInvoiceRepository creditCardInvoiceRepository;

    /**
     * This method observes for events about the {@link FinancialPeriod} opening action and create the
     * {@link CreditCardInvoice} for all active {@link Card}
     *
     * @param financialPeriod the {@link FinancialPeriod} to create the {@link CreditCardInvoice}
     */
    @Transactional
    public void createInvoicesFor(@Observes @FinancialPeriodOpened FinancialPeriod financialPeriod) {

        final List<Card> cards = this.cardRepository.findByCardTypeAndActive(CardType.CREDIT, true);

        cards.forEach(card -> {
            final CreditCardInvoice creditCardInvoice = new CreditCardInvoiceBuilder()
                    .card(card)
                    .financialPeriod(financialPeriod)
                    .build();
            this.creditCardInvoiceRepository.save(creditCardInvoice);
        });
    }
}
