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
import br.com.webbudget.domain.repository.GenericRepository;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 18/10/2013
 */
@Repository
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

        return criteria.list();
    }
    
    /**
     * 
     * @param wallet
     * @param walletBalanceTypes
     * @return 
     */
    @Override
    public List<WalletBalance> listByWallet(Wallet wallet, WalletBalanceType... walletBalanceTypes) {
       
        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());
        
        criteria.createAlias("wallet", "wl");
        criteria.add(Restrictions.eq("wl.id", wallet.getId()));

        if (walletBalanceTypes != null) {
            for (WalletBalanceType type : walletBalanceTypes) {
                criteria.add(Restrictions.eq("walletBalanceType", type));
            }
        }
        
        criteria.addOrder(Order.desc("inclusion"));
        
        return criteria.list();
    }

    /**
     * 
     * @param source
     * @param target
     * @param walletBalanceTypes
     * @return 
     */
    @Override
    public List<WalletBalance> listByWallet(Wallet source, Wallet target, WalletBalanceType... walletBalanceTypes) {
        
        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());
        
        if (target != null) {
            criteria.createAlias("wallet", "tgt");
            criteria.add(Restrictions.eq("tgt.id", target.getId()));
        } 
        
        if (source != null) {
            criteria.createAlias("sourceWallet", "src");
            criteria.add(Restrictions.eq("src.id", source.getId()));
        }

        if (walletBalanceTypes != null) {
            for (WalletBalanceType type : walletBalanceTypes) {
                criteria.add(Restrictions.eq("walletBalanceType", type));
            }
        }
        
        criteria.addOrder(Order.desc("inclusion"));
        
        return criteria.list();
    }
}
