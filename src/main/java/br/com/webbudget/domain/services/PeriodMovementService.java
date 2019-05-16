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
package br.com.webbudget.domain.services;

import br.com.webbudget.application.components.builder.WalletBalanceBuilder;
import br.com.webbudget.domain.entities.financial.CreditCardInvoice;
import br.com.webbudget.domain.entities.financial.PeriodMovement;
import br.com.webbudget.domain.entities.financial.ReasonType;
import br.com.webbudget.domain.entities.financial.WalletBalance;
import br.com.webbudget.domain.entities.registration.Wallet;
import br.com.webbudget.domain.events.*;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.logics.financial.movement.period.PeriodMovementDeletingLogic;
import br.com.webbudget.domain.logics.financial.movement.period.PeriodMovementSavingLogic;
import br.com.webbudget.domain.logics.financial.movement.period.PeriodMovementUpdatingLogic;
import br.com.webbudget.domain.repositories.financial.ApportionmentRepository;
import br.com.webbudget.domain.repositories.financial.CreditCardInvoiceRepository;
import br.com.webbudget.domain.repositories.financial.PeriodMovementRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 * The {@link PeriodMovement} service
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 04/12/2018
 */
@ApplicationScoped
public class PeriodMovementService {

    @Inject
    private ApportionmentRepository apportionmentRepository;
    @Inject
    private PeriodMovementRepository periodMovementRepository;
    @Inject
    private CreditCardInvoiceRepository creditCardInvoiceRepository;

    @Inject
    @UpdateWalletBalance
    private Event<WalletBalance> updateWalletBalanceEvent;
    @Inject
    @PeriodMovementDeleted
    private Event<PeriodMovement> periodMovementDeletedEvent;
    @Inject
    @PeriodMovementUpdated
    private Event<PeriodMovement> periodMovementUpdatedEvent;

    @Any
    @Inject
    private Instance<PeriodMovementSavingLogic> periodMovementSavingLogics;
    @Any
    @Inject
    private Instance<PeriodMovementUpdatingLogic> periodMovementUpdatingLogics;
    @Any
    @Inject
    private Instance<PeriodMovementDeletingLogic> periodMovementDeletingLogics;

    /**
     * Create a new {@link PeriodMovement}
     *
     * @param periodMovement the {@link PeriodMovement} to be saved
     * @return the {@link PeriodMovement} saved
     */
    @Transactional
    public PeriodMovement save(PeriodMovement periodMovement) {

        this.periodMovementSavingLogics.forEach(logic -> logic.run(periodMovement));

        final PeriodMovement saved = this.periodMovementRepository.save(periodMovement);

        periodMovement.getApportionments().forEach(apportionment -> {
            apportionment.setMovement(saved);
            this.apportionmentRepository.save(apportionment);
        });

        return saved;
    }

    /**
     * Update the {@link PeriodMovement}
     *
     * @param periodMovement the {@link PeriodMovement} to be updated
     * @return the updated {@link PeriodMovement}
     */
    @Transactional
    public PeriodMovement update(PeriodMovement periodMovement) {

        this.periodMovementUpdatingLogics.forEach(logic -> logic.run(periodMovement));

        // delete all removed apportionments
        periodMovement.getDeletedApportionments()
                .forEach(apportionment -> this.apportionmentRepository.attachAndRemove(apportionment));

        final PeriodMovement saved = this.periodMovementRepository.saveAndFlushAndRefresh(periodMovement);

        // save all current apportionments
        periodMovement.getApportionments().forEach(apportionment -> {
            apportionment.setMovement(saved);
            this.apportionmentRepository.save(apportionment);
        });

        // fire an event telling about the update
        this.periodMovementUpdatedEvent.fire(saved);

        return saved;
    }

    /**
     * Attach and delete a given {@link PeriodMovement}
     *
     * @param periodMovement the {@link PeriodMovement} to be deleted
     */
    @Transactional
    public void delete(PeriodMovement periodMovement) {

        this.periodMovementDeletingLogics.forEach(logic -> logic.run(periodMovement));

        // if is a invoice movement, remove the link first
        if (periodMovement.isCreditCardInvoice()) {

            final CreditCardInvoice invoice = this.creditCardInvoiceRepository.findByPeriodMovement(periodMovement)
                    .orElseThrow(() -> new BusinessLogicException("error.credit-card-invoice.not-found"));

            this.creditCardInvoiceRepository.saveAndFlushAndRefresh(invoice.prepareToReopen());
        }

        this.periodMovementRepository.attachAndRemove(periodMovement);

        // if the movement is paid with cash or debit, return the balance
        if (periodMovement.isPaidWithCash() || periodMovement.isPaidWithDebitCard()) {
            this.returnBalance(periodMovement, periodMovement.getPaymentWallet());
        }

        this.periodMovementDeletedEvent.fire(periodMovement);
    }

    /**
     * Catch {@link Event} for {@link CreatePeriodMovement}
     *
     * @param periodMovement to be created
     */
    @Transactional
    public void listenFor(@Observes @CreatePeriodMovement PeriodMovement periodMovement) {
        this.save(periodMovement);
    }

    /**
     * Catch the {@link Event} for {@link DeletePeriodMovement}
     *
     * @param movementCode of the {@link PeriodMovement} to be deleted
     */
    @Transactional
    public void listenFor(@Observes @DeletePeriodMovement String movementCode) {
        this.periodMovementRepository.findByCode(movementCode).ifPresent(this::delete);
    }

    /**
     * Method used to return the balance of the given {@link PeriodMovement} to the given {@link Wallet}
     *
     * @param periodMovement to be returned
     * @param wallet to be credited
     */
    private void returnBalance(PeriodMovement periodMovement, Wallet wallet) {

        final WalletBalanceBuilder builder = WalletBalanceBuilder.getInstance()
                .to(wallet)
                .forMovement(periodMovement.getCode())
                .withReason(ReasonType.RETURN);

        // if is revenue, subtract, if not, add
        if (periodMovement.isRevenue()) {
            builder.value(periodMovement.getPayment().getPaidValue().negate());
        } else {
            builder.value(periodMovement.getPayment().getPaidValue());
        }

        this.updateWalletBalanceEvent.fire(builder.build());
    }
}