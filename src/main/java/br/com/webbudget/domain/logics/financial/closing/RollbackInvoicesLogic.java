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
package br.com.webbudget.domain.logics.financial.closing;

import br.com.webbudget.domain.entities.financial.CreditCardInvoice;
import br.com.webbudget.domain.entities.registration.Card;
import br.com.webbudget.domain.entities.registration.CardType;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.repositories.financial.CreditCardInvoiceRepository;
import br.com.webbudget.domain.repositories.registration.CardRepository;
import br.com.webbudget.domain.services.CreditCardInvoiceService;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.List;

/**
 * Logic used to recreate the {@link CreditCardInvoice} when we want to reopen a {@link FinancialPeriod}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 27/04/2019
 */
@Dependent
public class RollbackInvoicesLogic implements ReopenPeriodLogic {

    @Inject
    private CreditCardInvoiceService creditCardInvoiceService;

    @Inject
    private CardRepository cardRepository;
    @Inject
    private CreditCardInvoiceRepository creditCardInvoiceRepository;

    /**
     * {@inheritDoc}
     *
     * @param value
     */
    @Override
    public void run(FinancialPeriod value) {

        final List<Card> cards = this.cardRepository.findByCardTypeAndActive(CardType.CREDIT, true);

        cards.forEach(card -> this.creditCardInvoiceRepository.findByCardAndFinancialPeriod(card, value)
                .ifPresentOrElse(invoice -> {/* do nothing */}, () -> this.recreate(card, value)));
    }

    /**
     *
     * @param card
     * @param financialPeriod
     */
    private void recreate(Card card, FinancialPeriod financialPeriod) {
        this.creditCardInvoiceService.createInvoice(card, financialPeriod);
    }
}
