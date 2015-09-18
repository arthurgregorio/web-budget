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
package br.com.webbudget.domain.entity.movement;

import br.com.webbudget.domain.entity.PersistentEntity;
import br.com.webbudget.domain.entity.wallet.Wallet;
import br.com.webbudget.domain.entity.card.Card;
import br.com.webbudget.domain.misc.ex.WbDomainException;
import java.time.LocalDate;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 03/04/2014
 */
@Entity
@Table(name = "payments")
@ToString(callSuper = true, of = "code")
@EqualsAndHashCode(callSuper = true, of = "code")
public class Payment extends PersistentEntity {

    @Getter
    @Setter
    @Column(name = "code", nullable = false, length = 8, unique = true)
    private String code;
    @Getter
    @Setter
    @NotNull(message = "{payment.payment-date}")
    @Temporal(TemporalType.DATE)
    @Column(name = "payment_date", nullable = false)
    private Date paymentDate;
    @Getter
    @Setter
    @Enumerated
    @NotNull(message = "{payment.payment-method}")
    @Column(name = "payment_method_type", nullable = false)
    private PaymentMethodType paymentMethodType;

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
     *
     */
    public Payment() {
        this.paymentDate = new Date();
        this.code = this.createPaymentCode();
    }

    /**
     * @return
     */
    private String createPaymentCode() {

        long decimalNumber = System.nanoTime();

        String generated = "";
        final String digits = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        synchronized (this.getClass()) {

            int mod;
            int authCodeLength = 0;

            while (decimalNumber != 0 && authCodeLength < 5) {

                mod = (int) (decimalNumber % 36);
                generated = digits.substring(mod, mod + 1) + generated;
                decimalNumber = decimalNumber / 36;
                authCodeLength++;
            }
        }
        return generated;
    }

    /**
     * Com este metodo construimos a data de vencimendo do cartao caso seja um
     * pagamento via cartao de credito para entao setar no movimento a fim de
     * que ele esteja no vencimento do cartao
     *
     * @param period o periodo que esperamos que a data compreenda
     * @return a data de vencimento do cartao
     */
    public LocalDate getCreditCardInvoiceDueDate(FinancialPeriod period) {

        if (this.card == null) {
            throw new WbDomainException("movement.validate.payment-not-credit-card");
        }
        
        final int expiration = this.card.getExpirationDay();

        if (expiration != 0) {
            return period.getEnd().withDayOfMonth(expiration).plusMonths(1);
        } else {
            return period.getEnd();
        }
    }
}
