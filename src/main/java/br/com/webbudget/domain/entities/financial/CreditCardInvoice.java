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
package br.com.webbudget.domain.entities.financial;

import br.com.webbudget.domain.entities.PersistentEntity;
import br.com.webbudget.domain.entities.registration.Card;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static br.com.webbudget.infrastructure.utils.DefaultSchemes.FINANCIAL;
import static br.com.webbudget.infrastructure.utils.DefaultSchemes.FINANCIAL_AUDIT;

/**
 * The representation of a credit {@link Card} invoice in the application
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 09/03/2019
 */
@Entity
@Audited
@ToString(callSuper = true, exclude = "periodMovements")
@Table(name = "credit_card_invoices", schema = FINANCIAL)
@EqualsAndHashCode(callSuper = true, exclude = "periodMovements")
@AuditTable(value = "credit_card_invoices", schema = FINANCIAL_AUDIT)
@NamedEntityGraph(
        name = "CreditCardInvoice.withPeriodMovements",
        attributeNodes = @NamedAttributeNode(value = "periodMovements"))
public class CreditCardInvoice extends PersistentEntity {

    @Getter
    @Setter
    @Column(name = "identification", nullable = false, length = 90, unique = true)
    private String identification;
    @Getter
    @Setter
    @Column(name = "total_value", nullable = false)
    private BigDecimal totalValue;
    @Getter
    @Setter
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;
    @Getter
    @Setter
    @Column(name = "invoice_state", nullable = false)
    private InvoiceState invoiceState;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_card")
    private Card card;
    @Getter
    @Setter
    @OneToOne
    @JoinColumn(name = "id_period_movement")
    private PeriodMovement periodMovement;
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_financial_period")
    public FinancialPeriod financialPeriod;

    @OneToMany(mappedBy = "creditCardInvoice")
    private List<PeriodMovement> periodMovements;

    /**
     * Constructor
     */
    public CreditCardInvoice() {
        this.totalValue = BigDecimal.ZERO;
        this.invoiceState = InvoiceState.OPEN;
    }

    /**
     * The {@link PeriodMovement} linked with this invoice
     *
     * @return a list with all {@link PeriodMovement} of this invoice
     */
    public List<PeriodMovement> getPeriodMovements() {
        return Collections.unmodifiableList(this.periodMovements);
    }

    /**
     * If this invoice is open or not
     *
     * @return true if is, false otherwise
     */
    public boolean isOpen() {
        return this.invoiceState == InvoiceState.OPEN;
    }

    /**
     * If this invoice is closed or not
     *
     * @return true if is, false otherwise
     */
    public boolean isClosed() {
        return this.invoiceState == InvoiceState.CLOSED;
    }

    /**
     * If this invoice is paid or not
     *
     * @return true if is, false otherwise
     */
    public boolean isPaid() {
        return this.invoiceState == InvoiceState.PAID;
    }

    /**
     * Method to calculate the total invoice amount
     *
     * @return the sum of the {@link PeriodMovement} values
     */
    public BigDecimal calculateTotal() {
        return this.periodMovements.stream()
                .map(Movement::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
