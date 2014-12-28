package br.com.webbudget.domain.repository.user;

import br.com.webbudget.domain.entity.users.PrivateMessage;
import br.com.webbudget.domain.entity.users.User;
import br.com.webbudget.domain.entity.users.UserPrivateMessage;
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
public class UserPrivateMessageRepository extends GenericRepository<UserPrivateMessage, Long> implements IUserPrivateMessageRepository  {

    /**
     * 
     * @param user
     * @param showUnread
     * @return 
     */
    @Override
    public List<UserPrivateMessage> listByUser(User user, Boolean showUnread) {
        
        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());

        criteria.createAlias("user", "u");
        criteria.add(Restrictions.eq("u.id", user.getId()));
        
        if (showUnread != null) {
            criteria.add(Restrictions.eq("wasRead", false));
        }
        
        // nao mostra mensagens deletadas nunca
        criteria.add(Restrictions.eq("deleted", false));
        
        criteria.addOrder(Order.desc("inclusion"));
        
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        
        return criteria.list();
    }

    @Override
    public List<UserPrivateMessage> listReceipts(PrivateMessage privateMessage) {
        
        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());

        criteria.createAlias("privateMessage", "pm");
        criteria.add(Restrictions.eq("pm.id", privateMessage.getId()));
        
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        
        return criteria.list();
    }
}
