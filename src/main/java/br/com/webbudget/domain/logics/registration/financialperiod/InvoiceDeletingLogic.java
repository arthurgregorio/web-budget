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
package br.com.webbudget.domain.logics.registration.financialperiod;

import br.com.webbudget.domain.entities.financial.CreditCardInvoice;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.logics.BusinessLogic;
import br.com.webbudget.domain.repositories.financial.CreditCardInvoiceRepository;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

/**
 * {@link BusinessLogic} to check and delete the {@link CreditCardInvoice} before delete a {@link FinancialPeriod}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 07/04/2019
 */
@Dependent
public class InvoiceDeletingLogic implements PeriodDeletingLogic {

    @Inject
    private CreditCardInvoiceRepository creditCardInvoiceRepository;

    /**
     * {@inheritDoc}
     *
     * @param value
     */
    @Override
    public void run(FinancialPeriod value) {

        final List<CreditCardInvoice> invoices = this.creditCardInvoiceRepository.findByFinancialPeriod(value);

        invoices.forEach(invoice -> {
            if (!invoice.isOpen() && invoice.getTotalValue().compareTo(BigDecimal.ZERO) != 0) {
                throw new BusinessLogicException("error.financial-period.invoice-is-used");
            }
            this.creditCardInvoiceRepository.attachAndRemove(invoice);
        });
    }
}
