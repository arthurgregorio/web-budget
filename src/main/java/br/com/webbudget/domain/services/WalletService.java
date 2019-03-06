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
package br.com.webbudget.domain.services;

import br.com.webbudget.domain.entities.financial.ReasonType;
import br.com.webbudget.domain.entities.financial.WalletBalance;
import br.com.webbudget.domain.entities.registration.Wallet;
import br.com.webbudget.domain.events.UpdateWalletBalance;
import br.com.webbudget.domain.logics.registration.wallet.WalletSavingLogic;
import br.com.webbudget.domain.logics.registration.wallet.WalletUpdatingLogic;
import br.com.webbudget.domain.repositories.registration.WalletBalanceRepository;
import br.com.webbudget.domain.repositories.registration.WalletRepository;
import br.com.webbudget.application.components.builder.WalletBalanceBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

/**
 * The service responsible for the business operations with {@link Wallet}
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 1.0.0, 12/03/2014
 */
@ApplicationScoped
public class WalletService {

    @Inject
    private WalletRepository walletRepository;
    @Inject
    private WalletBalanceRepository walletBalanceRepository;

    @Any
    @Inject
    private Instance<WalletSavingLogic> savingBusinessLogics;
    @Any
    @Inject
    private Instance<WalletUpdatingLogic> updatingBusinessLogics;

    /**
     * Use this method to persist a {@link Wallet}
     *
     * @param wallet the {@link Wallet} to be persisted
     */
    @Transactional
    public void save(Wallet wallet) {

        this.savingBusinessLogics.forEach(logic -> logic.run(wallet));

        // get the actual balance
        final BigDecimal actualBalance = wallet.getActualBalance();

        // set it to zero before save the wallet
        wallet.setActualBalance(BigDecimal.ZERO);

        final Wallet saved = this.walletRepository.save(wallet);

        // now put the balance by the standard way, with the builder
        final WalletBalanceBuilder builder = WalletBalanceBuilder.getInstance();

        builder.to(saved)
                .value(actualBalance)
                .withReason(ReasonType.ADJUSTMENT);

        this.updateWalletBalance(builder.build());
    }

    /**
     * Use this method to update a persisted {@link Wallet}
     *
     * @param wallet the {@link Wallet} to be updated
     * @return the update {@link Wallet}
     */
    @Transactional
    public Wallet update(Wallet wallet) {
        this.updatingBusinessLogics.forEach(logic -> logic.run(wallet));
        return this.walletRepository.save(wallet);
    }

    /**
     * Use this method to delete a persisted {@link Wallet}
     *
     * @param wallet the {@link Wallet} to be deleted
     */
    @Transactional
    public void delete(Wallet wallet) {
        final List<WalletBalance> balances = this.walletBalanceRepository.findByWallet_id(wallet.getId());
        balances.forEach(balance -> this.walletBalanceRepository.removeAndFlush(balance));
        this.walletRepository.attachAndRemove(wallet);
    }

    /**
     * Build the new wallet balance and call the method to update it
     *
     * @param wallet the wallet to be updated
     * @param value the value to be adjusted
     * @param observations the justification of adjustment to be placed as observation
     */
    @Transactional
    public void adjustBalance(Wallet wallet, BigDecimal value, String observations) {

        final WalletBalanceBuilder builder = WalletBalanceBuilder.getInstance()
                .to(wallet)
                .value(value)
                .withReason(ReasonType.ADJUSTMENT)
                .withObservations(observations);

        this.updateWalletBalance(builder.build());
    }

    /**
     * Update the {@link WalletBalance} for a given wallet
     *
     * @param walletBalance the builder with the balance historic
     */
    @Transactional
    private void updateWalletBalance(@Observes @UpdateWalletBalance WalletBalance walletBalance) {

        // update the actual balance on the wallet
        this.walletRepository.save(walletBalance.getWallet());

        // save the new balance history
        this.walletBalanceRepository.save(walletBalance);
    }
}
