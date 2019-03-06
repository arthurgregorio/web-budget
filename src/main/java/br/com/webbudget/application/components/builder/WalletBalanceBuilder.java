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
package br.com.webbudget.application.components.builder;

import br.com.webbudget.domain.entities.financial.BalanceType;
import br.com.webbudget.domain.entities.financial.ReasonType;
import br.com.webbudget.domain.entities.financial.WalletBalance;
import br.com.webbudget.domain.entities.registration.Wallet;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Builder to work with the {@link WalletBalance}
 *
 * @author Arthur Gregorio
 *
 * @version 1.1.0
 * @since 2.1.0, 27/08/2015
 */
public final class WalletBalanceBuilder extends AbstractBuilder<WalletBalance> {

    /**
     * Private constructor to force working with the {@link #getInstance()} method
     */
    private WalletBalanceBuilder() {
        this.instance = new WalletBalance();
        this.instance.setMovementDateTime(LocalDateTime.now());
    }

    /**
     * Get a new instance of the builder
     *
     * @return this builder
     */
    public static WalletBalanceBuilder getInstance() {
        return new WalletBalanceBuilder();
    }

    /**
     * Set the wallet to be the target of the transaction
     *
     * @param target the target wallet
     * @return this builder
     */
    public WalletBalanceBuilder to(Wallet target) {
        this.instance.setWallet(target);
        return this;
    }

    /**
     * The instance to be debited or credited to the {@link Wallet}
     *
     * @param value the instance
     * @return this builder
     */
    public WalletBalanceBuilder value(BigDecimal value) {

        // determine which type of financial movement we are doing
        if (value.signum() < 0) {
            this.instance.setBalanceType(BalanceType.DEBIT);
        } else {
            this.instance.setBalanceType(BalanceType.CREDIT);
        }

        this.instance.setTransactionValue(value);
        return this;
    }

    /**
     * The observations for this balance
     *
     * @param observations the observations
     * @return this builder
     */
    public WalletBalanceBuilder withObservations(String observations) {
        this.instance.setObservations(observations);
        return this;
    }

    /**
     * Indicate this balance is linked with some movement
     *
     * @param movementCode the movement code
     * @return this builder
     */
    public WalletBalanceBuilder forMovement(String movementCode) {
        this.instance.setMovementCode(movementCode);
        return this;
    }

    /**
     * Indicate the movement date of this balance
     *
     * @param movementDate the movement date
     * @return this builder
     */
    public WalletBalanceBuilder withMovementDate(LocalDateTime movementDate) {
        this.instance.setMovementDateTime(movementDate);
        return this;
    }

    /**
     * Add the reason to be doing this manipulation of the balance
     *
     * @param reason the reason
     * @return this builder
     */
    public WalletBalanceBuilder withReason(ReasonType reason) {
        this.instance.setReasonType(reason);
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public WalletBalance build() {
        this.instance.processBalances();
        return this.instance;
    }
}
