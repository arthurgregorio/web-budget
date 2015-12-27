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

import br.com.webbudget.domain.entity.movement.FixedMovement;
import br.com.webbudget.domain.entity.movement.Launch;
import br.com.webbudget.domain.entity.movement.Movement;
import br.com.webbudget.domain.misc.table.Page;
import br.com.webbudget.domain.misc.table.PageRequest;
import br.com.webbudget.domain.repository.GenericRepository;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.1.0, 20/09/2015
 */
public class LaunchRepository extends GenericRepository<Launch, Long> implements ILaunchRepository {

    /**
     * 
     * @param movement
     * @return 
     */
    @Override
    public Launch findByMovement(Movement movement) {
        
        final Criteria criteria = this.createCriteria();
        
        criteria.createAlias("movement", "m");
        criteria.add(Restrictions.eq("m.id", movement.getId()));
        
        return (Launch) criteria.uniqueResult();
    }
    
    /**
     * 
     * @param fixedMovement
     * @return 
     */
    @Override
    public Long countByFixedMovement(FixedMovement fixedMovement) {
        
        final Criteria criteria = this.createCriteria();
        
        criteria.createAlias("fixedMovement", "fm");
        criteria.add(Restrictions.eq("fm.id", fixedMovement.getId()));
        
        // projetamos para pegar o total de paginas possiveis
        criteria.setProjection(Projections.count("id"));

        return (Long) criteria.uniqueResult();
    }

    /**
     * 
     * @param fixedMovement
     * @return 
     */
    @Override
    public List<Launch> listByFixedMovement(FixedMovement fixedMovement) {
       
        final Criteria criteria = this.createCriteria();
        
        criteria.createAlias("fixedMovement", "fm");
        criteria.add(Restrictions.eq("fm.id", fixedMovement.getId()));
        
        return criteria.list();
    }

    /**
     * 
     * @param fixedMovement
     * @param pageRequest
     * @return 
     */
    @Override
    public Page<Launch> listByFixedMovement(FixedMovement fixedMovement, PageRequest pageRequest) {
        
        final Criteria criteria = this.createCriteria();

        criteria.createAlias("fixedMovement", "fm");
        criteria.add(Restrictions.eq("fm.id", fixedMovement.getId()));

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
