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
package br.com.webbudget.application.components.ui.filter;

import br.com.webbudget.domain.entities.financial.BalanceType;
import br.com.webbudget.domain.entities.financial.ReasonType;
import br.com.webbudget.domain.entities.financial.WalletBalance;
import br.com.webbudget.domain.entities.registration.Wallet;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * Filter used on the {@link WalletBalance} historic listing
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.1.0, 01/09/2019
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class WalletBalanceFilter extends BasicFilter {

    @Getter
    @Setter
    private ReasonType reasonType;
    @Getter
    @Setter
    private BalanceType balanceType;

    @Getter
    @Setter
    private LocalDate operationDate;

    @Getter
    @Setter
    private Wallet wallet;

    /**
     * Constructor...
     *
     * @param wallet to be used to search for {@link WalletBalance}
     */
    public WalletBalanceFilter(Wallet wallet) {
        this.wallet = wallet;
    }

    /**
     * All possible {@link ReasonType} to filter
     *
     * @return an array of {@link ReasonType}
     */
    public ReasonType[] getReasonTypes() {
        return ReasonType.values();
    }

    /**
     * All possible {@link BalanceType} to filter
     *
     * @return an array of {@link BalanceType}
     */
    public BalanceType[] getBalanceTypes() {
        return BalanceType.values();
    }

    /**
     * Clear this filter
     */
    public void clear() {
        this.reasonType = null;
        this.balanceType = null;
        this.operationDate = null;
    }
}
