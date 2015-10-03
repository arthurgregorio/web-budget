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
package br.com.webbudget.domain.entity.wallet;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 03/04/2014
 */
public enum WalletBalanceType {

    PAYMENT("e1410a","beans.wallet-balance.payment"),
    REVENUE("66cdaa","beans.wallet-balance.revenue"), // 2aa668
    ADJUSTMENT("d8ae60","beans.wallet-balance.adjustment"),
    TRANSFERENCE("59484f","beans.wallet-balance.transference"),
    BALANCE_RETURN("66cccc","beans.wallet-balance.balance-return"),
    TRANSFER_ADJUSTMENT("ffa535","beans.wallet-balance.transfer-adjustment");

    private final String color;
    private final String i18nKey;

    /**
     *
     * @param i18nKey
     */
    private WalletBalanceType(String color, String i18nKey) {
        this.color = color;
        this.i18nKey = i18nKey;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return this.i18nKey;
    }
    
    /**
     * @return a cor para este balanco
     */
    public String getStyleForType() {
        return "border-left: 10px solid #" + this.color;
    }
}
