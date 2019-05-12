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
package br.com.webbudget.domain.logics.registration.card;

import br.com.webbudget.domain.entities.financial.CreditCardInvoice;
import br.com.webbudget.domain.entities.registration.Card;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.repositories.financial.CreditCardInvoiceRepository;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.List;

/**
 * Validator logic to check and delete the invoices before deleting a {@link Card}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.1, 11/05/2019
 */
@Dependent
public class InvoiceValidator implements CardDeletingLogic {

    @Inject
    private CreditCardInvoiceRepository creditCardInvoiceRepository;

    /**
     * {@inheritDoc}
     *
     * @param value
     */
    @Override
    public void run(Card value) {

        final List<CreditCardInvoice> invoices = this.creditCardInvoiceRepository.findByCard(value);

        invoices.forEach(invoice -> {
            if (invoice.isClosed() || invoice.isPaid() || !invoice.isEmpty()) {
                throw new BusinessLogicException("error.card.not-deletable");
            }
            this.creditCardInvoiceRepository.attachAndRemove(invoice);
        });
    }
}
