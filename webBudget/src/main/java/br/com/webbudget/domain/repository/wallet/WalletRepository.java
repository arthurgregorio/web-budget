package br.com.webbudget.domain.repository.wallet;

import br.com.webbudget.domain.entity.wallet.Wallet;
import br.com.webbudget.domain.entity.wallet.WalletType;
import br.com.webbudget.domain.repository.GenericRepository;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
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
public class WalletRepository extends GenericRepository<Wallet, Long> implements IWalletRepository {

    /**
     * 
     * @param isBlocked
     * @return 
     */
    @Override
    public List<Wallet> listByStatus(Boolean isBlocked) {
       
        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());
        
        if (isBlocked != null) {
            criteria.add(Restrictions.eq("blocked", isBlocked));
        }
        
        criteria.addOrder(Order.asc("bank"));
        
        return criteria.list();
    }

    /**
     * 
     * @param name
     * @param bank
     * @param walletType
     * @return 
     */
    @Override
    public Wallet findByNameAndBankAndType(String name, String bank, WalletType walletType) {
        
        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());

        criteria.add(Restrictions.eq("name", name));
        criteria.add(Restrictions.eq("walletType", walletType));
        
        if (walletType == WalletType.BANK_ACCOUNT) {
            criteria.add(Restrictions.eq("bank", bank));
        } 
        
        return (Wallet) criteria.uniqueResult();
    }
}
