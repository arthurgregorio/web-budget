package br.com.webbudget.domain.repository.movement;

import br.com.webbudget.domain.entity.movement.CostCenter;
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
public class CostCenterRepository extends GenericRepository<CostCenter, Long> implements ICostCenterRepository {

    /**
     * 
     * @param isBlocked
     * @return 
     */
    @Override
    public List<CostCenter> listByStatus(Boolean isBlocked) {
       
        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());
        
        if (isBlocked != null) {
            criteria.add(Restrictions.eq("blocked", isBlocked));
        }
        
        criteria.addOrder(Order.asc("name"));
        
        return criteria.list();
    }

    /**
     * 
     * @param name
     * @param parent
     * @return 
     */
    @Override
    public CostCenter findByNameAndParent(String name, CostCenter parent) {
        
        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());

        if (parent != null) {
            criteria.createAlias("parentCostCenter", "pcc");
            criteria.add(Restrictions.eq("pcc.name", parent.getName()));
        } else {
            criteria.add(Restrictions.isNull("parentCostCenter"));
        }
        
        criteria.add(Restrictions.eq("name", name));
        
        return (CostCenter) criteria.uniqueResult();        
    }
}
