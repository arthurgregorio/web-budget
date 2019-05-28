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
package br.com.webbudget.domain.repositories.financial;

import br.com.webbudget.application.components.ui.table.Page;
import br.com.webbudget.domain.entities.financial.CreditCardInvoice;
import br.com.webbudget.domain.entities.financial.CreditCardInvoice_;
import br.com.webbudget.domain.entities.financial.InvoiceState;
import br.com.webbudget.domain.entities.financial.PeriodMovement;
import br.com.webbudget.domain.entities.registration.Card;
import br.com.webbudget.domain.entities.registration.Card_;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.entities.registration.FinancialPeriod_;
import br.com.webbudget.domain.repositories.DefaultRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.Criteria;

import java.util.List;
import java.util.Optional;

/**
 * The {@link CreditCardInvoice} repository
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 10/03/2019
 */
@Repository
public interface CreditCardInvoiceRepository extends DefaultRepository<CreditCardInvoice> {

    /**
     * Find a {@link CreditCardInvoice} by the {@link PeriodMovement} used to pay the invoice
     *
     * @param periodMovement linked with this invoice
     * @return an {@link Optional} of the {@link CreditCardInvoice}
     */
    Optional<CreditCardInvoice> findByPeriodMovement(PeriodMovement periodMovement);

    /**
     * Find a {@link CreditCardInvoice} by the given {@link Card} and {@link FinancialPeriod}
     *
     * @param card to use as filter
     * @param financialPeriod to use as filter
     * @return an {@link Optional} of the {@link CreditCardInvoice}
     */
    Optional<CreditCardInvoice> findByCardAndFinancialPeriod(Card card, FinancialPeriod financialPeriod);


    /**
     * Get a list of {@link CreditCardInvoice} for a given {@link Card}
     *
     * @param card to use as filter
     * @return the {@link List} of invoices
     */
    List<CreditCardInvoice> findByCard(Card card);

    /**
     * Find all {@link CreditCardInvoice} for a given {@link FinancialPeriod}
     *
     * @param financialPeriod to be used as filter
     * @return a {@link List} of the {@link CreditCardInvoice} found
     */
    List<CreditCardInvoice> findByFinancialPeriod(FinancialPeriod financialPeriod);

    /**
     * Lazy filter method to search for {@link CreditCardInvoice}
     *
     * @param filter value
     * @param invoiceState to filter
     * @param start of the page
     * @param pageSize for limiting the items
     * @return a {@link Page} with the {@link CreditCardInvoice} found
     */
    default Page<CreditCardInvoice> findAllBy(String filter, InvoiceState invoiceState, int start, int pageSize) {

        final int totalRows = this.countPages(filter, invoiceState);

        final Criteria<CreditCardInvoice, CreditCardInvoice> criteria = this.buildCriteria(filter, invoiceState);

        criteria.orderDesc(CreditCardInvoice_.createdOn);

        final List<CreditCardInvoice> data = criteria.createQuery()
                .setFirstResult(start)
                .setMaxResults(pageSize)
                .getResultList();

        return Page.of(data, totalRows);
    }

    /**
     * Count the pages for lazy loading
     *
     * @param filter value
     * @param invoiceState to filter
     * @return total of pages for this search
     */
    @SuppressWarnings("unchecked")
    default int countPages(String filter, InvoiceState invoiceState) {
        return this.buildCriteria(filter, invoiceState)
                .select(Long.class, count(CreditCardInvoice_.id))
                .getSingleResult()
                .intValue();
    }

    /**
     * Build the {@link Criteria} to search for {@link CreditCardInvoice}
     *
     * @param filter value
     * @param invoiceState to filter
     * @return the {@link Criteria} ready to search
     */
    @SuppressWarnings("unchecked")
    default Criteria<CreditCardInvoice, CreditCardInvoice> buildCriteria(String filter, InvoiceState invoiceState) {

        final Criteria<CreditCardInvoice, CreditCardInvoice> criteria = this.criteria();

        if (invoiceState != null) {
            criteria.eq(CreditCardInvoice_.invoiceState, invoiceState);
        }

        if (StringUtils.isNotBlank(filter)) {
            criteria.or(
                    this.criteria().join(CreditCardInvoice_.card, where(Card.class)
                            .likeIgnoreCase(Card_.name, this.likeAny(filter))),
                    this.criteria().join(CreditCardInvoice_.financialPeriod, where(FinancialPeriod.class)
                            .likeIgnoreCase(FinancialPeriod_.identification, this.likeAny(filter)))
            );
        }

        criteria.orderAsc(CreditCardInvoice_.financialPeriod);

        return criteria;
    }
}