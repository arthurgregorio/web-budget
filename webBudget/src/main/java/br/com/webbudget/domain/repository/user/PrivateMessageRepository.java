package br.com.webbudget.domain.repository.user;

import br.com.webbudget.domain.entity.users.PrivateMessage;
import br.com.webbudget.domain.entity.users.User;
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
public class PrivateMessageRepository extends GenericRepository<PrivateMessage, Long> implements IPrivateMessageRepository  {

    /**
     * 
     * @param user
     * @return 
     */
    @Override
    public List<PrivateMessage> listSent(User user) {
        
        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());

        criteria.createAlias("owner", "ow");
        
        criteria.add(Restrictions.eq("ow.id", user.getId()));
        
        // nao mostra mensagens deletadas nunca
        criteria.add(Restrictions.eq("deleted", false));

        criteria.addOrder(Order.desc("inclusion"));
        
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        
        return criteria.list();
    }
    
    /**
     * 
     * @param user
     * @param showUnread
     * @return 
     */
    @Override
    public List<PrivateMessage> listByUser(User user, boolean showUnread) {
        
        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());

        criteria.createAlias("addressees", "adr");
        criteria.add(Restrictions.eq("adr.id", user.getId()));
        
        if (!showUnread) {
            criteria.add(Restrictions.eq("wasRead", false));
        }
        
        // nao mostra mensagens deletadas nunca
        criteria.add(Restrictions.eq("deleted", false));
        
        criteria.addOrder(Order.desc("inclusion"));
        
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        return criteria.list();
    }
}
