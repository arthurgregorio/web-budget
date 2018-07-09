/*
 * Copyright (C) 2015 Arthur Gregorio, AG.Software
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
package br.com.webbudget.domain.entities.registration;

import br.com.webbudget.domain.entities.PersistentEntity;
import br.com.webbudget.domain.entities.financial.Movement;
import br.com.webbudget.infrastructure.utils.RandomCode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * The representation of a card invoice in the application
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 01/05/2014
 */
@Entity
@Audited
@Table(name = "card_invoices")
@AuditTable(value = "audit_card_invoices")
@ToString(callSuper = true, of = {"identification", "total"})
@EqualsAndHashCode(callSuper = true, of = {"identification", "total"})
public class CardInvoice extends PersistentEntity {

    @Getter
    @Column(name = "identification", nullable = false, length = 45, unique = true)
    private String identification;
    @Getter
    @Setter
    @Column(name = "total", nullable = false)
    private BigDecimal total;

    @Getter
    @ManyToOne
    @JoinColumn(name = "id_card")
    private Card card;
    @Getter
    @Setter
    @OneToOne
    @JoinColumn(name = "id_movement")
    private Movement movement;
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_financial_period")
    public FinancialPeriod financialPeriod;

    @Getter
    @Transient
    private List<Movement> movements;

    /**
     * Default constructor
     */
    public CardInvoice() {
        this.movements = new ArrayList<>();
    }

    /**
     * Setter for the {@link Card} and also the method to create the invoice unique code
     *
     * @param card the {@link Card} to be set in this invoice
     */
    public void setCard(Card card) {
        this.card = card;

        if (card != null) {
            final StringBuilder builder = new StringBuilder();

            builder.append(card.getName());
            builder.append(" - ");
            builder.append(RandomCode.numeric(6));

            this.identification = builder.toString();
        }
    }

    /**
     * Setter method for the {@link Movement} of the invoice
     *
     * @param movements the list of invoice {@link Movement}
     */
    public void setMovements(List<Movement> movements) {
        this.movements = movements;
        this.total = this.calculateTotal();
    }

    /**
     * Method to calculate the total of the invoice
     *
     * @return the sum of all the {@link Movement} for this invoice
     */
    public BigDecimal calculateTotal() { // FIXME verify utility
        return this.movements.stream()
                .map(Movement::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Method to determine by the {@link Card} the due date of this invoice
     *
     * @return the due date provided by the {@link Card} or the last day of the current {@link FinancialPeriod}
     */
    public LocalDate getInvoiceDueDate() { // FIXME verify utility

        int dueDate = this.card.getExpirationDay();

        if (dueDate != 0) {
            return this.financialPeriod.getEnd()
                    .withDayOfMonth(dueDate)
                    .plusMonths(1);
        } else {
            return this.financialPeriod.getEnd();
        }
    }

    /**
     * Method to check if the invoice contains {@link Movement}
     *
     * @return <code>true</code> if has, <code>false</code> if not
     */
    public boolean hasMovements() {
        return !this.movements.isEmpty();
    } // FIXME verify utility
    
    /**
     * Method to check if the invoice can be payd
     *
     * @return <code>true</code> if can be, <code>false</code> otherwise
     */
    public boolean isPayable() { // FIXME verify utility
        return this.hasMovements() 
                && this.movement != null && this.movement.isPaid();
    }

    /**
     * Method to get the start day of the invoice
     *
     * @return the start day of the invoice
     */
    public String getInvoicePeriodStart() { // FIXME verify utility
        return DateTimeFormatter.ofPattern("dd/MM/yyyy")
                .format(this.financialPeriod.getStart());
    }

    /**
     * Method to get the last day of the invoice
     *
     * @return the last day of the invoice
     */
    public String getInvoicePeriodEnd() { // FIXME verify utility
        return DateTimeFormatter.ofPattern("dd/MM/yyyy")
                .format(this.financialPeriod.getEnd());
    }
}
