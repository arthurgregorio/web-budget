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
package br.com.webbudget.domain.services.misc;

import br.com.webbudget.domain.entities.registration.Wallet;
import br.com.webbudget.domain.entities.registration.WalletBalance;
import br.com.webbudget.domain.entities.registration.BalanceType;
import java.math.BigDecimal;

/**
 * 
 * @author Arthur Gregorio
 *
 * @version 1.1.0
 * @since 2.1.0, 27/08/2015
 */
public final class WalletBalanceBuilder {

    private final WalletBalance walletBalance;

    /**
     * 
     */
    private WalletBalanceBuilder() {
        this.walletBalance = new WalletBalance();
    }
    
    /**
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
        this.walletBalance.setWallet(target);
        return this;
    }
    
    /**
     * The value movemented by this builder
     * 
     * @param value the value to be credited or debited from the wallets
     * @return this builder
     */
    public WalletBalanceBuilder withValue(BigDecimal value) {
        this.walletBalance.setMovementedValue(value);
        return this;
    }
    
    /**
     * 
     * @param reason
     * @return this builder
     */
    public WalletBalanceBuilder withReason(String reason) {
        this.walletBalance.setReason(reason);
        return this;
    }
    
    /**
     * 
     * @param balanceType
     * @return this builder
     */
    public WalletBalanceBuilder withType(BalanceType balanceType) {
        this.walletBalance.setBalanceType(balanceType);
        return this;
    }
    
    /**
     * 
     * @param movementCode
     * @return this builder
     */
    public WalletBalanceBuilder forMovement(String movementCode) {
        this.walletBalance.setMovementCode(movementCode);
        return this;
    }
    
    /**
     * Build the balance
     * 
     * @return the builded {@link WalletBalance} for the wallet
     */
    public WalletBalance build() {
        this.walletBalance.processBalances();
        return this.walletBalance;
    }
}
