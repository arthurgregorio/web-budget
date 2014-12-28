package br.com.webbudget.domain.repository.wallet;

import br.com.webbudget.domain.entity.wallet.Wallet;
import br.com.webbudget.domain.entity.wallet.WalletBalance;
import br.com.webbudget.domain.entity.wallet.WalletBalanceType;
import br.com.webbudget.domain.repository.GenericRepository;
import java.util.List;
import org.hibernate.Criteria;
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
     * @param walletBalanceType
     * @return 
     */
    @Override
    public List<WalletBalance> listByWallet(Wallet wallet, WalletBalanceType walletBalanceType) {
       
        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());
        
        criteria.createAlias("wallet", "wl");
        criteria.add(Restrictions.eq("wl.id", wallet.getId()));

        criteria.add(Restrictions.eq("walletBalanceType", walletBalanceType));
        
        return criteria.list();
    }
}
