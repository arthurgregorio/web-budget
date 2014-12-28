package br.com.webbudget.domain.repository.user;

import br.com.webbudget.domain.entity.users.Permission;
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
public class PermissionRepository extends GenericRepository<Permission, Long> implements IPermissionRepository  {

    /**
     * 
     * @param user
     * @return 
     */
    @Override
    public List<Permission> listByUser(User user) {

        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());
                
        criteria.createAlias("user", "us");
        criteria.add(Restrictions.eq("us.id", user.getId()));
        
        return criteria.list();
    }
}
