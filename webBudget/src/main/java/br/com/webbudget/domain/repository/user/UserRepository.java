package br.com.webbudget.domain.repository.user;

import br.com.webbudget.domain.entity.users.User;
import br.com.webbudget.domain.repository.GenericRepository;
import java.util.List;
import org.hibernate.Criteria;
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
public class UserRepository extends GenericRepository<User, Long> implements IUserRepository  {

    /**
     * 
     * @param login
     * @return 
     */
    @Override
    public User findByUsername(String login) {
        
        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());
                
        criteria.add(Restrictions.eq("username", login));
        
        return (User) criteria.uniqueResult();
    }

    /**
     * 
     * @param blocked
     * @return 
     */
    @Override
    public List<User> listByStatus(Boolean blocked) {
        
        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());
                
        if (blocked != null) {
            criteria.add(Restrictions.eq("blocked", blocked));
        }
        
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        
        return criteria.list();
    }

    /**
     * 
     * @param blocked
     * @param authenticated
     * @return 
     */
    @Override
    public List<User> listByStatusAndRemoveAuthenticated(Boolean blocked, User authenticated) {
        
        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());
                
        if (blocked != null) {
            criteria.add(Restrictions.eq("blocked", blocked));
        }
        
        criteria.add(Restrictions.ne("id", authenticated.getId()));
        
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        
        return criteria.list();
    }
}
