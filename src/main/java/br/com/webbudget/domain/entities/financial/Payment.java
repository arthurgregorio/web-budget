/*
 * Copyright (C) 2014 Arthur Gregorio, AG.Software
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
import br.com.webbudget.domain.entities.registration.Wallet;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

import static br.com.webbudget.infrastructure.utils.DefaultSchemes.FINANCIAL;
import static br.com.webbudget.infrastructure.utils.DefaultSchemes.FINANCIAL_AUDIT;

/**
 * This class represents the payment of any {@link PeriodMovement}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 03/04/2014
 */
@Entity
@Audited
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "payments", schema = FINANCIAL)
@AuditTable(value = "payments", schema = FINANCIAL_AUDIT)
public class Payment extends PersistentEntity {

    @Getter
    @Setter
    @NotNull(message = "{payment.paid-on}")
    @Column(name = "paid_on", nullable = false)
    private LocalDate paidOn;
    @Getter
    @Setter
    @Column(name = "discount")
    private BigDecimal discount;
    @Getter
    @Setter
    @Column(name = "paid_value", nullable = false)
    private BigDecimal paidValue;
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @NotNull(message = "{payment.payment-method}")
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_card")
    private Card card;
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_wallet")
    private Wallet wallet;

    /**
     * Constructor
     */
    public Payment() {
        this.paidOn = LocalDate.now();
        this.discount = BigDecimal.ZERO;
        this.paymentMethod = PaymentMethod.CASH;
    }

    /**
     * To check if this payment is paid with cash
     *
     * @return true if is, false if not
     */
    public boolean isPaidWithCash() {
        return this.paymentMethod == PaymentMethod.CASH;
    }

    /**
     * To check if this payment is paid with a credit {@link Card}
     *
     * @return true if is, false if not
     */
    public boolean isPaidWithCreditCard() {
        return this.paymentMethod == PaymentMethod.CREDIT_CARD;
    }

    /**
     * To check if this payment is paid with a debit {@link Card}
     *
     * @return true if is, false if not
     */
    public boolean isPaidWithDebitCard() {
        return this.paymentMethod == PaymentMethod.DEBIT_CARD;
    }

    /**
     * Helper method to get the {@link Wallet} linked with the {@link Card} if is a debit {@link Card}
     *
     * @return the {@link Wallet}
     */
    public Wallet getDebitCardWallet() {
        return this.card.getWallet();
    }
}
