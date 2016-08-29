/*
 * Copyright (C) 2016 Arthur Gregorio, AG.Software
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
package br.com.webbudget.domain.model.repository.logbook;

import br.com.webbudget.application.component.table.Page;
import br.com.webbudget.application.component.table.PageRequest;
import br.com.webbudget.domain.model.entity.logbook.Refueling;
import br.com.webbudget.domain.model.entity.logbook.Vehicle;
import br.com.webbudget.domain.model.repository.GenericRepository;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.3.0, 05/06/2016
 */
public class RefuelingRepository extends GenericRepository<Refueling, Long> implements IRefuelingRepository {

    /**
     * 
     * @param refueling
     * @return 
     */
    @Override
    public boolean isLast(Refueling refueling) {
        
        Criteria criteria = this.createCriteria();
        
        criteria.createAlias("vehicle", "ve");
        criteria.add(Restrictions.eq("ve.id", refueling.getVehicle().getId()));
        criteria.setProjection(Projections.max("id"));
        
        Object maxId = criteria.uniqueResult();
        
        if (maxId != null) {
            return refueling.getId().equals((Long) maxId);
        }
        return true;
    }
    
    /**
     * 
     * @param code
     * @return 
     */
    @Override
    public List<Refueling> listAccountedsBy(String code) {
       
        final Criteria criteria = this.createCriteria();
        
        criteria.add(Restrictions.eq("accountedBy", code));
        
        return criteria.list();
    }
    
    /**
     *
     * @return
     */
    @Override
    public int findLastOdometerForVehicle(Vehicle vehicle) {

        Criteria criteria = this.createCriteria();

        criteria.createAlias("vehicle", "ve");
        criteria.add(Restrictions.eq("ve.id", vehicle.getId()));
        criteria.setProjection(Projections.max("id"));

        Object maxId = criteria.uniqueResult();

        if (maxId != null) {
            criteria = this.createCriteria();
            criteria.add(Restrictions.eq("id", (Long) maxId));
            return ((Refueling) criteria.uniqueResult()).getOdometer();
        }
        return 0;
    }

    /**
     * 
     * @param vehicle
     * @return 
     */
    @Override
    public List<Refueling> findUnaccountedsForVehicle(Vehicle vehicle) {
       
        final Criteria criteria = this.createCriteria();
        
        criteria.createAlias("vehicle", "ve");
        criteria.add(Restrictions.eq("ve.id", vehicle.getId()));
        criteria.add(Restrictions.eq("accounted", false));
        
        return criteria.list();
    }
    
    /**
     *
     * @param filter
     * @param pageRequest
     * @return
     */
    @Override
    public Page<Refueling> listLazily(String filter, PageRequest pageRequest) {

        final Criteria criteria = this.createCriteria();

        if (StringUtils.isNotBlank(filter)) {
            // TODO colocar os filtros aqui
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
}
