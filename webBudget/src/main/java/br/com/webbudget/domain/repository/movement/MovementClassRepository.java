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
import br.com.webbudget.domain.entity.movement.MovementClass;
import br.com.webbudget.domain.entity.movement.MovementClassType;
import br.com.webbudget.domain.repository.GenericRepository;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 18/10/2013
 */
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

        criteria.addOrder(Order.asc("costCenter"));

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
