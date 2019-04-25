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
package br.com.webbudget.application.components.dto;

import br.com.webbudget.domain.entities.financial.CreditCardInvoice;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

/**
 * Resume of all {@link CreditCardInvoice} for all {@link FinancialPeriod}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 25/04/2019
 */
@ToString
@EqualsAndHashCode
public final class CreditCardInvoiceResume {

    @Getter
    private BigDecimal highestValue;
    @Getter
    private BigDecimal lowestValue;
    @Getter
    private BigDecimal averageValue;
    @Getter
    private BigDecimal actualValue;

    /**
     * Constructor...
     */
    public CreditCardInvoiceResume() {
        this.highestValue = BigDecimal.ZERO;
        this.lowestValue = BigDecimal.ZERO;
        this.averageValue = BigDecimal.ZERO;
        this.actualValue = BigDecimal.ZERO;
    }

    /**
     * Load this DTO and calculate the values
     *
     * @param invoices {@link List} of {@link CreditCardInvoice} to calculate the values
     */
    public void load(List<CreditCardInvoice> invoices) {

        this.highestValue = invoices.stream()
                .map(CreditCardInvoice::getTotalValue)
                .max(Comparator.naturalOrder())
                .orElse(BigDecimal.ZERO);

        this.lowestValue = invoices.stream()
                .map(CreditCardInvoice::getTotalValue)
                .min(Comparator.naturalOrder())
                .orElse(BigDecimal.ZERO);

        this.actualValue = invoices.stream()
                .filter(CreditCardInvoice::isOpen)
                .map(CreditCardInvoice::getTotalValue)
                .min(Comparator.naturalOrder())
                .orElse(BigDecimal.ZERO);

        final BigDecimal totalValue = invoices.stream()
                .map(CreditCardInvoice::getTotalValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.averageValue = totalValue.divide(BigDecimal.valueOf(invoices.size()), RoundingMode.CEILING);
    }
}
