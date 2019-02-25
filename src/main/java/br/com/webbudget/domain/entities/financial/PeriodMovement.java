/*
 * Copyright (C) 2018 Arthur Gregorio, AG.Software
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

import br.com.webbudget.domain.entities.registration.Card;
import br.com.webbudget.domain.entities.registration.CardInvoice;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static javax.persistence.CascadeType.REMOVE;

/**
 * This is a period movement, this movement can appear only one time in the period, this one is connected directly with
 * the {@link FinancialPeriod} and is used to all the accounting in the closing process
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 04/12/2018
 */
@Entity
@Audited
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("PERIOD_MOVEMENT") // never change this!
public class PeriodMovement extends Movement {

    @Getter
    @Setter
    @Column(name = "due_date")
    @NotNull(message = "{period-movement.due-date}")
    private LocalDate dueDate;
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "period_movement_state", length = 45)
    private PeriodMovementState periodMovementState;
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", length = 45)
    private MovementType movementType;

    @Getter
    @Setter
    @OneToOne(cascade = REMOVE)
    @JoinColumn(name = "id_payment")
    private Payment payment;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_financial_period")
    @NotNull(message = "{period-movement.financial-period}")
    private FinancialPeriod financialPeriod;

    /**
     * Constructor...
     */
    public PeriodMovement() {
        super();
        this.dueDate = LocalDate.now();
        this.movementType = MovementType.MOVEMENT;
        this.periodMovementState = PeriodMovementState.OPEN;
    }

    /**
     * Get the payment date of this movement
     *
     * @return the payment date
     */
    public LocalDate getPaymentDate() {
        return this.payment != null ? this.payment.getPaidOn() : null;
    }

    /**
     * To check if this movement is paid with a credit {@link Card}
     *
     * @return true if is, false if not
     */
    public boolean isPaidWithCreditCard() {
        return this.payment.isPaidWithCreditCard();
    }

    /**
     * To check if this movement is paid with a debit {@link Card}
     *
     * @return true if is, false if not
     */
    public boolean isPaidWithDebitCard() {
        return this.payment.isPaidWithDebitCard();
    }

    /**
     * To check if this movement is a {@link CardInvoice}
     *
     * @return true if is, false otherwise
     */
    public boolean isCardInvoice() {
        return this.movementType == MovementType.CARD_INVOICE;
    }

    /**
     * To check if this movement is paid or not
     *
     * @return true if paid, false otherwise
     */
    public boolean isPaid() {
        return this.periodMovementState == PeriodMovementState.PAID;
    }

    /**
     * To check if this movement is open or not
     *
     * @return true if paid, false otherwise
     */
    public boolean isOpen() {
        return this.periodMovementState == PeriodMovementState.OPEN;
    }

    /**
     * To check if this movement is finalized or not
     *
     * @return true if paid, false otherwise
     */
    public boolean isAccounted() {
        return this.periodMovementState == PeriodMovementState.ACCOUNTED;
    }

    /**
     * To check if this movement is overdue
     *
     * @return true if is, false otherwise
     */
    public boolean isOverdue() {
        return this.dueDate.isBefore(LocalDate.now());
    }
}
