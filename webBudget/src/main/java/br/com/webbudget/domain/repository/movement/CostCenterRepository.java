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
