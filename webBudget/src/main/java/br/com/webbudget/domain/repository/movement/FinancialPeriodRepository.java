package br.com.webbudget.domain.repository.movement;

import br.com.webbudget.domain.entity.movement.FinancialPeriod;
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
public class FinancialPeriodRepository extends GenericRepository<FinancialPeriod, Long> implements IFinancialPeriodRepository {

    /**
     * 
     * @return 
     */
    @Override
    public List<FinancialPeriod> listOpen() {
        
        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());
        
        criteria.add(Restrictions.eq("closed", false));
        criteria.add(Restrictions.isNull("closing"));
        
        criteria.addOrder(Order.asc("end"));
        
        return criteria.list();
    }

    /**
     * 
     * @param isClosed
     * @return 
     */
    @Override
    public List<FinancialPeriod> listByStatus(Boolean isClosed) {
       
        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());
        
        if (isClosed != null) {
            criteria.add(Restrictions.eq("closed", isClosed));
        }
        
        return criteria.list();
    }
    
    /**
     * 
     * @param identification
     * @return 
     */
    @Override
    public FinancialPeriod findByIdentification(String identification) {

        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());
        
        criteria.add(Restrictions.eq("identification", identification));
        
        return (FinancialPeriod) criteria.uniqueResult();
    }
}
