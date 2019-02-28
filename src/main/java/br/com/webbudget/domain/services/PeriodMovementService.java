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

import br.com.webbudget.domain.entities.financial.PeriodMovement;
import br.com.webbudget.domain.entities.financial.ReasonType;
import br.com.webbudget.domain.entities.financial.WalletBalance;
import br.com.webbudget.domain.entities.registration.Wallet;
import br.com.webbudget.domain.events.UpdateWalletBalance;
import br.com.webbudget.domain.logics.financial.periodmovement.PeriodMovementDeletingLogic;
import br.com.webbudget.domain.logics.financial.periodmovement.PeriodMovementSavingLogic;
import br.com.webbudget.domain.logics.financial.periodmovement.PeriodMovementUpdatingLogic;
import br.com.webbudget.domain.repositories.financial.ApportionmentRepository;
import br.com.webbudget.domain.repositories.financial.PeriodMovementRepository;
import br.com.webbudget.domain.services.misc.WalletBalanceBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
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
    @UpdateWalletBalance
    private Event<WalletBalance> updateWalletBalanceEvent;

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
        this.periodMovementRepository.attachAndRemove(periodMovement);

        // if the movement is paid with cash or debit, return the balance
        if (periodMovement.isPaidWithCash() || periodMovement.isPaidWithDebitCard()) {
            this.returnBalance(periodMovement, periodMovement.getPaymentWallet());
        }
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
                .withReason(ReasonType.RETURN)
                .value(periodMovement.getPayment().getPaidValue());

        this.updateWalletBalanceEvent.fire(builder.build());
    }
}