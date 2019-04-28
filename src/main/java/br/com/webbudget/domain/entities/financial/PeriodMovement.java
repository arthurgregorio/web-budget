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
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.entities.registration.Wallet;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
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
    @Column(name = "period_movement_type", length = 45)
    private PeriodMovementType periodMovementType;

    @Getter
    @Setter
    @OneToOne(cascade = REMOVE)
    @JoinColumn(name = "id_payment")
    private Payment payment;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_credit_card_invoice")
    private CreditCardInvoice creditCardInvoice;
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_financial_period")
    @NotNull(message = "{period-movement.financial-period}")
    private FinancialPeriod financialPeriod;

    /**
     * Used at the credit card invoice to mark the movement and help the user to check the invoice
     */
    @Getter
    @Setter
    @Transient
    private boolean checked;

    /**
     * Constructor...
     */
    public PeriodMovement() {
        super();
        this.dueDate = LocalDate.now();
        this.periodMovementType = PeriodMovementType.MOVEMENT;
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
        return this.payment != null && this.payment.isPaidWithCreditCard();
    }

    /**
     * To check if this movement is paid with a debit {@link Card}
     *
     * @return true if is, false if not
     */
    public boolean isPaidWithDebitCard() {
        return this.payment != null && this.payment.isPaidWithDebitCard();
    }

    /**
     * To check if this movement is paid with a cash
     *
     * @return true if is, false if not
     */
    public boolean isPaidWithCash() {
        return this.payment != null && this.payment.isPaidWithCash();
    }

    /**
     * To check if this movement is a {@link CreditCardInvoice}
     *
     * @return true if is, false otherwise
     */
    public boolean isCreditCardInvoice() {
        return this.periodMovementType == PeriodMovementType.CARD_INVOICE;
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

    /**
     * Helper method to get only the discount value
     *
     * @return the payment discount value
     */
    public BigDecimal getValueWithDiscount() {
        return this.payment != null ? this.payment.getPaidValue() : this.getValue();
    }

    /**
     * Helper method to check if this movement had discount at the payment
     *
     * @return true if yes, false otherwise
     */
    public boolean isDiscountPresent() {
        return this.payment != null && this.payment.getDiscount().compareTo(BigDecimal.ZERO) != 0;
    }

    /**
     * Get the payment wallet for this movement. If no wallet is used to pay for this movement, an {@link IllegalStateException}
     * is thrown with some information
     *
     * @return the payment wallet for this movement
     */
    public Wallet getPaymentWallet() {
        if (this.isPaidWithCash()) {
            return this.payment.getWallet();
        } else if (this.isPaidWithDebitCard()) {
            return this.payment.getDebitCardWallet();
        }
        throw new IllegalStateException("This movement is in a inconsistent state of payment, contact Odin God.");
    }

    /**
     * Method to prepare this movement to be paid
     *
     * @param payment the {@link Payment} to pay this movement
     * @return this {@link PeriodMovement}
     */
    public PeriodMovement prepareToPay(Payment payment) {
        this.payment = payment;
        this.periodMovementState = PeriodMovementState.PAID;

        if (payment.isPaidWithCreditCard()) {
            final int expirationDay = payment.getCard().getExpirationDay();
            this.dueDate = this.financialPeriod.getEnd().plusMonths(1).withDayOfMonth(expirationDay);
        }

        return this;
    }

    /**
     * Method used to prepare this period movement to be accounted
     *
     * @return this object
     */
    public PeriodMovement prepareToAccount() {
        this.periodMovementState = PeriodMovementState.ACCOUNTED;
        return this;
    }

    /**
     * Method used when we need to reopen the {@link FinancialPeriod} and rollback the status of this movement to be
     * Paid again
     *
     * @return this object
     */
    public PeriodMovement prepareToReopenPeriod() {
        this.periodMovementState = PeriodMovementState.PAID;
        return this;
    }
}
