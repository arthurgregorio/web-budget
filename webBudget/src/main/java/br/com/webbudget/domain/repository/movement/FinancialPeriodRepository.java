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

import br.com.webbudget.domain.entity.movement.FinancialPeriod;
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
