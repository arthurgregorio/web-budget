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

import br.com.webbudget.application.components.builder.PeriodMovementBuilder;
import br.com.webbudget.domain.entities.PersistentEntity;
import br.com.webbudget.domain.entities.registration.Card;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.entities.registration.MovementClass;
import br.com.webbudget.infrastructure.i18n.MessageSource;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static br.com.webbudget.infrastructure.utils.DefaultSchemes.FINANCIAL;
import static br.com.webbudget.infrastructure.utils.DefaultSchemes.FINANCIAL_AUDIT;
import static javax.persistence.FetchType.EAGER;

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
    @Column(name = "closing_date")
    private LocalDate closingDate;
    @Getter
    @Setter
    @Column(name = "payment_date")
    private LocalDate paymentDate;
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "invoice_state", nullable = false, length = 45)
    private InvoiceState invoiceState;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_card")
    private Card card;
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_financial_period")
    public FinancialPeriod financialPeriod;

    @Getter
    @Setter
    @OneToOne
    @JoinColumn(name = "id_period_movement")
    private PeriodMovement periodMovement;

    @OneToMany(mappedBy = "creditCardInvoice", fetch = EAGER)
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
     * Method used to check if the invoice is empty or not
     *
     * @return true if is, false otherwise
     */
    public boolean isEmpty() {
        return this.totalValue.compareTo(BigDecimal.ZERO) == 0 && this.periodMovements.isEmpty();
    }

    /**
     * Get the {@link FinancialPeriod} start date
     *
     * @return start date as {@link String}
     */
    public String getPeriodStart() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.financialPeriod.getStart());
    }

    /**
     * Get the {@link FinancialPeriod} end date
     *
     * @return end date as {@link String}
     */
    public String getPeriodEnd() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.financialPeriod.getEnd());
    }

    /**
     * Convert this credit card invoice into a {@link PeriodMovement}
     *
     * @param movementClass to be used at the {@link Apportionment}
     * @return the {@link PeriodMovement} for this invoice
     */
    public PeriodMovement toPeriodMovement(MovementClass movementClass) {

        final String dueDateString = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.dueDate);
        final String valueString = NumberFormat.getCurrencyInstance().format(this.totalValue);

        final String description = MessageSource.get("credit-card-invoice.invoice-description",
                this.card.getReadableName(), dueDateString, valueString, this.financialPeriod.getIdentification());

        return new PeriodMovementBuilder()
                .type(PeriodMovementType.CARD_INVOICE)
                .financialPeriod(this.financialPeriod)
                .dueDate(this.dueDate)
                .identification(this.identification)
                .description(description)
                .value(this.totalValue)
                .addApportionment(new Apportionment(this.totalValue, movementClass))
                .build();
    }

    /**
     * Prepare this invoice to be closed
     *
     * @param periodMovement to link with this invoice
     * @return this invoice to be saved with the new state
     */
    public CreditCardInvoice prepareToClose(PeriodMovement periodMovement) {
        this.periodMovement = periodMovement;
        this.invoiceState = InvoiceState.CLOSED;
        this.closingDate = LocalDate.now();
        return this;
    }

    /**
     * Prepare this invoice to be paid
     *
     * @return this invoice to be saved with the new state
     */
    public CreditCardInvoice prepareToPay() {
        this.invoiceState = InvoiceState.PAID;
        this.paymentDate = LocalDate.now();
        return this;
    }

    /**
     * Prepare this invoice to open again
     *
     * @return this invoice to be saved with the new status
     */
    public CreditCardInvoice prepareToReopen() {
        this.closingDate = null;
        this.paymentDate = null;
        this.periodMovement = null;
        this.invoiceState = InvoiceState.OPEN;
        return this;
    }

    /**
     * Method used to apply the correct order to the {@link PeriodMovement} list
     */
    public void orderPeriodMovements() {
        this.periodMovements.sort(Comparator.comparing(PeriodMovement::getPaymentDate));
    }
}