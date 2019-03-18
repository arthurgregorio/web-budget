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

import br.com.webbudget.domain.entities.financial.Payment;
import br.com.webbudget.domain.entities.financial.PeriodMovement;
import br.com.webbudget.domain.entities.registration.Card;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.logics.BusinessLogic;
import lombok.Getter;

/**
 * A simple DTO to transport the {@link Payment} and the {@link PeriodMovement} in a single package to the
 * {@link BusinessLogic} layer
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 17/03/2019
 */
public final class PaymentWrapper {

    @Getter
    private Payment payment;
    @Getter
    private PeriodMovement periodMovement;

    /**
     * Constructor...
     *
     * @param payment to be wrapped
     * @param periodMovement to be wrapped
     */
    public PaymentWrapper(Payment payment, PeriodMovement periodMovement) {
        this.payment = payment;
        this.periodMovement = periodMovement;
    }

    /**
     * Get the {@link FinancialPeriod} for this payment
     *
     * @return the {@link FinancialPeriod}
     */
    public FinancialPeriod getFinancialPeriod() {
        return this.periodMovement.getFinancialPeriod();
    }

    /**
     * Get the {@link Card} for this payment
     *
     * @return the {@link Card}
     */
    public Card getCreditCard() {
        return this.payment.getCard();
    }

    /**
     * Check if this {@link Payment} is done with a credit {@link Card} or not
     *
     * @return true if is, false otherwise
     */
    public boolean isCreditCardPayment() {
        return this.payment.isPaidWithCreditCard();
    }
}
