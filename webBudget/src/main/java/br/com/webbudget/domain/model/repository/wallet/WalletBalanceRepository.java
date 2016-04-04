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
package br.com.webbudget.domain.model.repository.wallet;

import br.com.webbudget.domain.model.entity.wallet.Wallet;
import br.com.webbudget.domain.model.entity.wallet.WalletBalance;
import br.com.webbudget.domain.model.entity.wallet.WalletBalanceType;
import br.com.webbudget.domain.model.repository.GenericRepository;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.1
 * @since 1.0.0, 18/10/2013
 */
public class WalletBalanceRepository extends GenericRepository<WalletBalance, Long> implements IWalletBalanceRepository {

    /**
     *
     * @param wallet
     * @return
     */
    @Override
    public WalletBalance findLastWalletBalance(Wallet wallet) {

        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());

        criteria.createAlias("wallet", "wl");
        criteria.add(Restrictions.eq("wl.id", wallet.getId()));

        criteria.setProjection(Projections.max("id"));

        final Object id = criteria.uniqueResult();

        if (id != null) {
            return this.findById((Long) id, false);
        } else {
            return null;
        }
    }

    /**
     *
     * @param walletBalanceType
     * @return
     */
    @Override
    public List<WalletBalance> listByType(WalletBalanceType walletBalanceType) {

        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());

        criteria.add(Restrictions.eq("walletBalanceType", walletBalanceType));

        criteria.addOrder(Order.desc("inclusion"));
        
        return criteria.list();
    }

    /**
     *
     * @param source
     * @param target
     * @param types
     * @return
     */
    @Override
    public List<WalletBalance> listByWallet(Wallet source, Wallet target, WalletBalanceType... types) {

        final Criteria criteria = this.createCriteria();

        if (target != null) {
            criteria.createAlias("targetWallet", "tgt");
            criteria.add(Restrictions.eq("tgt.id", target.getId()));
        }

        if (source != null) {
            criteria.createAlias("sourceWallet", "src");
            criteria.add(Restrictions.eq("src.id", source.getId()));
        }

        if (types != null) {
            for (WalletBalanceType type : types) {
                criteria.add(Restrictions.eq("walletBalanceType", type));
            }
        }

        criteria.addOrder(Order.desc("inclusion"));

        return criteria.list();
    }
}
