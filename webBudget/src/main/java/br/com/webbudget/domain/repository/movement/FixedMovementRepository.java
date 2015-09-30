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
import br.com.webbudget.domain.entity.movement.FixedMovementStatusType;
import br.com.webbudget.domain.misc.model.Page;
import br.com.webbudget.domain.misc.model.PageRequest;
import br.com.webbudget.domain.repository.GenericRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
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
public class FixedMovementRepository extends GenericRepository<FixedMovement, Long> implements IFixedMovementRepository {

    /**
     *
     * @return
     */
    @Override
    public List<FixedMovement> listAutoLaunch() {

        final Criteria criteria = this.createCriteria();

        criteria.add(
                Restrictions.and(Restrictions.eq("autoLaunch", true), 
                Restrictions.eq("fixedMovementStatusType", FixedMovementStatusType.ACTIVE)));

        return criteria.list();
    }

    /**
     *
     * @param filter
     * @param pageRequest
     * @return
     */
    @Override
    public Page<FixedMovement> listByFilter(String filter, PageRequest pageRequest) {

        final Criteria criteria = this.createCriteria();

        final List<Criterion> criterions = new ArrayList<>();

        // filtramos
        if (filter != null && !filter.isEmpty()) {

            criterions.add(Restrictions.ilike("description", "%" + filter + "%"));
            criterions.add(Restrictions.ilike("identification", "%" + filter + "%"));

            // se conseguir castar para bigdecimal trata como um filtro
            try {
                criterions.add(Restrictions.eq("value", new BigDecimal(filter)));
            } catch (NumberFormatException ex) {
            }
        }

        criteria.add(Restrictions.or(criterions.toArray(new Criterion[]{})));

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
