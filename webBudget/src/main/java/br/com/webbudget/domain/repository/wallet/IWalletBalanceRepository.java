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
package br.com.webbudget.domain.repository.wallet;

import br.com.webbudget.domain.entity.wallet.Wallet;
import br.com.webbudget.domain.entity.wallet.WalletBalance;
import br.com.webbudget.domain.entity.wallet.WalletBalanceType;
import br.com.webbudget.domain.repository.IGenericRepository;
import java.util.List;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 04/03/2013
 */
public interface IWalletBalanceRepository extends IGenericRepository<WalletBalance, Long> {

    /**
     *
     * @param wallet
     * @return
     */
    public WalletBalance findLastWalletBalance(Wallet wallet);

    /**
     *
     * @param walletBalanceType
     * @return
     */
    public List<WalletBalance> listByType(WalletBalanceType walletBalanceType);

    /**
     *
     * @param wallet
     * @param walletBalanceTypes
     * @return
     */
    public List<WalletBalance> listByWallet(Wallet wallet, WalletBalanceType... walletBalanceTypes);

    /**
     *
     * @param source
     * @param target
     * @param walletBalanceTypes
     * @return
     */
    public List<WalletBalance> listByWallet(Wallet source, Wallet target, WalletBalanceType... walletBalanceTypes);
}
