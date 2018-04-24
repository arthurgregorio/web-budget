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

import br.com.webbudget.domain.entities.entries.Wallet;
import br.com.webbudget.domain.entities.entries.WalletBalance;
import br.com.webbudget.domain.entities.entries.WalletBalanceType;
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

    // TODO make balance builder understand the old and the balance automatically
    
    /**
     * 
     */
    private WalletBalanceBuilder() {
        this.walletBalance = new WalletBalance();
    }
    
    /**
     * 
     * @return 
     */
    public static WalletBalanceBuilder getInstance() {
        return new WalletBalanceBuilder();
    }
    
    /**
     * 
     * @param wallet
     * @return 
     */
    public WalletBalanceBuilder to(Wallet wallet) {
        this.walletBalance.setTargetWallet(wallet);
        return this;
    }
    
    /**
     * 
     * @param wallet
     * @return 
     */
    public WalletBalanceBuilder from(Wallet wallet) {
        this.walletBalance.setSourceWallet(wallet);
        return this;
    }
    
    /**
     * 
     * @param value
     * @return 
     */
    public WalletBalanceBuilder withValue(BigDecimal value) {
        this.walletBalance.setMovementedValue(value);
        return this;
    }
    
    /**
     * 
     * @param reason
     * @return 
     */
    public WalletBalanceBuilder withReason(String reason) {
        this.walletBalance.setReason(reason);
        return this;
    }
    
    /**
     * 
     * @param walletBalanceType
     * @return 
     */
    public WalletBalanceBuilder andType(WalletBalanceType walletBalanceType) {
        this.walletBalance.setWalletBalanceType(walletBalanceType);
        return this;
    }
    
    /**
     * 
     * @param movementCode
     * @return 
     */
    public WalletBalanceBuilder forMovement(String movementCode) {
        this.walletBalance.setMovementCode(movementCode);
        return this;
    }
    
    /**
     * 
     * @return 
     */
    public WalletBalance build() {
        return this.walletBalance;
    }
}
