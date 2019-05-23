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
package br.com.webbudget.domain.logics.financial.payment;

import br.com.webbudget.application.components.dto.PaymentWrapper;
import br.com.webbudget.domain.exceptions.BusinessLogicException;

import javax.enterprise.context.Dependent;

/**
 * Simple validator logic to check if we have a valid payment with method and wallet
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.2, 22/05/2019
 */
@Dependent
public class PaymentMethodValidator implements PaymentSavingLogic {

    /**
     * {@inheritDoc}
     *
     * @param value
     */
    @Override
    public void run(PaymentWrapper value) {

        final var payment = value.getPayment();

        if ((payment.isPaidWithCreditCard() || payment.isPaidWithDebitCard()) && payment.getCard() == null) {
            throw new BusinessLogicException("error.payment.no-card");
        } else if (payment.isPaidWithCash() && payment.getWallet() == null) {
            throw new BusinessLogicException("error.payment.no-wallet");
        }
    }
}
