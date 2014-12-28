package br.com.webbudget.domain.repository.movement;

import br.com.webbudget.domain.entity.movement.CostCenter;
import br.com.webbudget.domain.entity.movement.MovementClass;
import br.com.webbudget.domain.entity.movement.MovementClassType;
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
public class MovementClassRepository extends GenericRepository<MovementClass, Long> implements IMovementClassRepository {

    /**
     * 
     * @param isBlocked
     * @return 
     */
    @Override
    public List<MovementClass> listByStatus(Boolean isBlocked) {
       
        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());
        
        if (isBlocked != null) {
            criteria.add(Restrictions.eq("blocked", isBlocked));
        }
        
        criteria.addOrder(Order.asc("name"));
        
        return criteria.list();
    }
   
    /**
     * 
     * @param type
     * @param blocked
     * @return 
     */
    @Override
    public List<MovementClass> listByTypeAndStatus(MovementClassType type, Boolean blocked) {
        
        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());
        
        criteria.add(Restrictions.eq("movementClassType", type));
        
        if (blocked != null) {
            criteria.add(Restrictions.eq("blocked", blocked));
        }
        
        return criteria.list(); 
    }

    /**
     * 
     * @param costCenter
     * @return 
     */
    @Override
    public List<MovementClass> listByCostCenterAndType(CostCenter costCenter, MovementClassType type) {
    
        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());
        
        criteria.createAlias("costCenter", "cc");
        criteria.add(Restrictions.eq("cc.id", costCenter.getId()));
    
        if (type != null) {
            criteria.add(Restrictions.eq("movementClassType", type));
        }
        
        criteria.add(Restrictions.eq("blocked", false));
        
        criteria.addOrder(Order.asc("name"));
        
        return criteria.list(); 
    }
    
    /**
     * 
     * @param name
     * @param type
     * @param costCenter
     * @return 
     */
    @Override
    public MovementClass findByNameAndTypeAndCostCenter(String name, MovementClassType type, CostCenter costCenter) {
        
        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());
        
        criteria.createAlias("costCenter", "cc");
        criteria.add(Restrictions.eq("cc.id", costCenter.getId()));
        
        criteria.add(Restrictions.eq("name", name));
        criteria.add(Restrictions.eq("movementClassType", type));
        
        return (MovementClass) criteria.uniqueResult();        
    }
}
