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
package br.com.webbudget.domain.misc.dto;

import br.com.webbudget.domain.entity.wallet.Wallet;
import br.com.webbudget.domain.entity.wallet.WalletBalance;
import br.com.webbudget.domain.entity.wallet.WalletBalanceType;
import java.math.BigDecimal;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.1.0, 27/08/2015
 */
public class BalanceBuilder {

    private final WalletBalance walletBalance;

    /**
     * 
     */
    public BalanceBuilder() {
        this.walletBalance = new WalletBalance();
    }
    
    /**
     * 
     * @param wallet
     * @return 
     */
    public BalanceBuilder forWallet(Wallet wallet) {
        this.walletBalance.setTargetWallet(wallet);
        return this;
    }
    
    /**
     * 
     * @param value
     * @return 
     */
    public BalanceBuilder withMovementValue(BigDecimal value) {
        this.walletBalance.setMovementedValue(value);
        return this;
    }
    
    /**
     * 
     * @param value
     * @return 
     */
    public BalanceBuilder withOldBalance(BigDecimal value) {
        this.walletBalance.setOldBalance(value);
        return this;
    }
    
    /**
     * 
     * @param value
     * @return 
     */
    public BalanceBuilder withActualBalance(BigDecimal value) {
        this.walletBalance.setActualBalance(value);
        return this;
    }
    
    /**
     * 
     * @param value
     * @return 
     */
    public BalanceBuilder byTheReason(BigDecimal value) {
        this.walletBalance.setMovementedValue(value);
        return this;
    }
    
    /**
     * 
     * @param walletBalanceType
     * @return 
     */
    public BalanceBuilder andType(WalletBalanceType walletBalanceType) {
        this.walletBalance.setWalletBalanceType(walletBalanceType);
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
