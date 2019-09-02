/*
 * Copyright (C) 2013 Arthur Gregorio, AG.Software
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
package br.com.webbudget.domain.repositories.registration;

import br.com.webbudget.application.components.ui.filter.WalletBalanceFilter;
import br.com.webbudget.domain.entities.financial.WalletBalance;
import br.com.webbudget.domain.entities.financial.WalletBalance_;
import br.com.webbudget.domain.entities.registration.Wallet;
import br.com.webbudget.domain.entities.registration.Wallet_;
import br.com.webbudget.domain.repositories.DefaultRepository;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.Criteria;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@link WalletBalance} repository
 *
 * @author Arthur Gregorio
 *
 * @version 2.2.0
 * @since 1.0.0, 04/03/2013
 */
@Repository
public interface WalletBalanceRepository extends DefaultRepository<WalletBalance> {

    /**
     * Method to find all the {@link WalletBalance} of the given {@link Wallet}
     *
     * @param walletId the id of the {@link Wallet} to search
     * @return the list of {@link WalletBalance} for this wallet
     */
    List<WalletBalance> findByWallet_id(long walletId);

    /**
     * Find the {@link WalletBalance} by a given filter
     *
     * @param filter to be used on the search process
     * @return a {@link List} of the {@link WalletBalance} found
     */
    default List<WalletBalance> findByFilter(WalletBalanceFilter filter) {

        final Criteria<WalletBalance, WalletBalance> criteria = this.criteria();

        final List<Criteria<WalletBalance, WalletBalance>> restrictions = new ArrayList<>();

        if (filter.getReasonType() != null) {
            restrictions.add(this.criteria().eq(WalletBalance_.reasonType, filter.getReasonType()));
        }

        if (filter.getBalanceType() != null) {
            restrictions.add(this.criteria().eq(WalletBalance_.balanceType, filter.getBalanceType()));
        }

        if (filter.getOperationDate() != null) {

            final var start = filter.getOperationDate().atTime(0, 0);
            final var end = filter.getOperationDate().atTime(23, 59);

            restrictions.add(this.criteria().between(WalletBalance_.movementDateTime, start, end));
        }

        if (!restrictions.isEmpty()) {
            criteria.or(restrictions);
        }

        criteria.join(WalletBalance_.wallet,
                where(Wallet.class).eq(Wallet_.id, filter.getWallet().getId()));

        return criteria.getResultList();
    }
}
