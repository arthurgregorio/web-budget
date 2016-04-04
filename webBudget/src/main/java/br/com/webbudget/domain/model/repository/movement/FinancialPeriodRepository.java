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
package br.com.webbudget.domain.model.repository.movement;

import br.com.webbudget.domain.model.entity.closing.Closing;
import br.com.webbudget.domain.model.entity.movement.FinancialPeriod;
import br.com.webbudget.application.component.table.Page;
import br.com.webbudget.application.component.table.PageRequest;
import br.com.webbudget.domain.model.repository.GenericRepository;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
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

    /**
     * 
     * @param isClosed
     * @param pageRequest
     * @return 
     */
    @Override
    public Page<FinancialPeriod> listByStatusLazily(Boolean isClosed, PageRequest pageRequest) {
        
        final Criteria criteria = this.createCriteria();

        if (isClosed != null) {
            criteria.add(Restrictions.eq("closed", isClosed));
        }

        // projetamos para pegar o total de paginas possiveis
        criteria.setProjection(Projections.count("id"));

        final Long totalRows = (Long) criteria.uniqueResult();

        // limpamos a projection para que a criteria seja reusada
        criteria.setProjection(null);
        criteria.setResultTransformer(Criteria.ROOT_ENTITY);

        // paginamos
        criteria.setFirstResult(pageRequest.getFirstResult());
        criteria.setMaxResults(pageRequest.getPageSize());

        if (pageRequest.getSortDirection() == PageRequest.SortDirection.ASC) {
            criteria.addOrder(Order.asc(pageRequest.getSortField()));
        } else if (pageRequest.getSortDirection() == PageRequest.SortDirection.DESC) {
            criteria.addOrder(Order.desc(pageRequest.getSortField()));
        }

        // montamos o resultado paginado
        return new Page<>(criteria.list(), totalRows);
    }

    /**
     * 
     * @return 
     */
    @Override
    public FinancialPeriod findLatestClosed() {
        
        final Criteria criteria = this.createCriteria();
        
        DetachedCriteria maxId = DetachedCriteria
                .forClass(FinancialPeriod.class)
                .add(Restrictions.eq("closed", true))
                .setProjection(Projections.max("id"));
        
        criteria.add(Property.forName("id").eq(maxId));
        
        return (FinancialPeriod) criteria.uniqueResult();
    }

    /**
     * 
     * @return 
     */
    @Override
    public List<FinancialPeriod> listLastSixClosed() {
     
        final Criteria criteria = this.createCriteria();
        
        criteria.add(Restrictions.eq("closed", true));
        criteria.addOrder(Order.desc("id"));
        criteria.setMaxResults(6);        
        
        criteria.addOrder(Order.desc("inclusion"));
        
        return criteria.list();
    }
}
