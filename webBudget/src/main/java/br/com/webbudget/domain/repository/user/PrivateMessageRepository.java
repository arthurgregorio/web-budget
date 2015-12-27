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
package br.com.webbudget.domain.repository.user;

import br.com.webbudget.domain.entity.message.PrivateMessage;
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
 * @version 1.1.0
 * @since 1.0.0, 18/10/2013
 */
public class PrivateMessageRepository extends GenericRepository<PrivateMessage, Long> implements IPrivateMessageRepository {

    /**
     * 
     * @param userId
     * @return 
     */
    @Override
    public List<PrivateMessage> listSent(String userId) {

        final Criteria criteria = this.createCriteria();

        criteria.add(Restrictions.eq("sender", userId));

        // nao mostra mensagens deletadas nunca
        criteria.add(Restrictions.eq("deleted", false));

        criteria.addOrder(Order.desc("inclusion"));

        return criteria.list();
    }

    /**
     * 
     * @param userId
     * @param pageRequest
     * @return 
     */
    @Override
    public Page<PrivateMessage> listSentLazily(String userId, PageRequest pageRequest) {
        
        final Criteria criteria = this.createCriteria();

        criteria.add(Restrictions.eq("sender", userId));

        // nao mostra mensagens deletadas nunca
        criteria.add(Restrictions.eq("deleted", false));

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
