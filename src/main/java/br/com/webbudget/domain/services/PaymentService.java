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
package br.com.webbudget.domain.services;

import br.com.webbudget.application.components.dto.PaymentWrapper;
import br.com.webbudget.domain.entities.financial.Payment;
import br.com.webbudget.domain.entities.financial.PeriodMovement;
import br.com.webbudget.domain.entities.financial.ReasonType;
import br.com.webbudget.domain.entities.financial.WalletBalance;
import br.com.webbudget.domain.entities.registration.Wallet;
import br.com.webbudget.domain.events.PeriodMovementPaid;
import br.com.webbudget.domain.events.UpdateWalletBalance;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.logics.financial.payment.PaymentSavingLogic;
import br.com.webbudget.domain.repositories.financial.PaymentRepository;
import br.com.webbudget.domain.repositories.financial.PeriodMovementRepository;
import br.com.webbudget.application.components.builder.WalletBalanceBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 * The {@link Payment} service
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 23/02/2019
 */
@ApplicationScoped
public class PaymentService {

    @Inject
    private PaymentRepository paymentRepository;
    @Inject
    private PeriodMovementRepository periodMovementRepository;

    @Inject
    @UpdateWalletBalance
    private Event<WalletBalance> updateWalletBalanceEvent;
    @Inject
    @PeriodMovementPaid
    private Event<PeriodMovement> periodMovementPaidEvent;

    @Any
    @Inject
    private Instance<PaymentSavingLogic> paymentSavingLogics;

    /**
     * Service method to pay a given {@link PeriodMovement}
     *
     * @param periodMovement to be paid
     * @param payment object with the information about the payment
     */
    @Transactional
    public void pay(PeriodMovement periodMovement, Payment payment) {

        this.paymentSavingLogics.forEach(logic -> logic.run(new PaymentWrapper(payment, periodMovement)));

        // save the paid value for easy viewing at the database
        payment.setPaidValue(periodMovement.getValue().subtract(payment.getDiscount()));

        final PeriodMovement saved = this.periodMovementRepository.saveAndFlushAndRefresh(periodMovement.prepareToPay(
                this.paymentRepository.save(payment)));

        // now, if we are paying with cash or debit card, update the wallet balance
        if (payment.isPaidWithCash()) {
            this.afterPaymentWithCash(payment, periodMovement);
        } else if (payment.isPaidWithDebitCard()) {
            this.afterPaymentWithDebitCard(payment, periodMovement);
        }

        this.periodMovementPaidEvent.fire(saved);
    }

    /**
     * Method to build the new {@link WalletBalance} and fire the {@link Event} to update if we are paying with a debit
     * card
     *
     * @param payment the {@link Payment}
     * @param periodMovement the {@link PeriodMovement} we are paying
     */
    private void afterPaymentWithDebitCard(Payment payment, PeriodMovement periodMovement) {

        final Wallet wallet = payment.getCard().getWallet();

        // debit card payments are always a expense and we have a specific reason this
        final WalletBalanceBuilder builder = WalletBalanceBuilder.getInstance()
                .to(wallet)
                .withReason(ReasonType.DEBIT_CARD)
                .forMovement(periodMovement.getCode())
                .value(payment.getPaidValue().negate());

        this.updateWalletBalanceEvent.fire(builder.build());
    }

    /**
     * Method to build the new {@link WalletBalance} and fire the {@link Event} to update if we are paying with cash
     *
     * @param payment the {@link Payment}
     * @param periodMovement the {@link PeriodMovement} we are paying
     */
    private void afterPaymentWithCash(Payment payment, PeriodMovement periodMovement) {

        final WalletBalanceBuilder builder = WalletBalanceBuilder.getInstance()
                .to(payment.getWallet())
                .forMovement(periodMovement.getCode())
                .withReason(periodMovement.isRevenue() ? ReasonType.REVENUE : ReasonType.EXPENSE)
                .value(periodMovement.isRevenue() ? payment.getPaidValue() : payment.getPaidValue().negate());

        this.updateWalletBalanceEvent.fire(builder.build());
    }
}