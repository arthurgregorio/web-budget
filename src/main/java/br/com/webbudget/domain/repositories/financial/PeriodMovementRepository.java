/*
 * Copyright (C) 2018 Arthur Gregorio, AG.Software
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
package br.com.webbudget.domain.repositories.financial;

import br.com.webbudget.application.components.table.Page;
import br.com.webbudget.application.components.table.filter.PeriodMovementFilter;
import br.com.webbudget.domain.entities.financial.PeriodMovement;
import br.com.webbudget.domain.entities.financial.PeriodMovement_;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.Criteria;
import org.apache.deltaspike.data.api.criteria.CriteriaSupport;

import java.util.List;
import java.util.Optional;

/**
 * The {@link PeriodMovement} repository
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 04/12/2018
 */
@Repository
public interface PeriodMovementRepository extends EntityRepository<PeriodMovement, Long>, CriteriaSupport<PeriodMovement> {

    /**
     * Find a {@link PeriodMovement} by the ID
     *
     * @param id the id of the {@link PeriodMovement}
     * @return an {@link Optional} of the {@link PeriodMovement}
     */
    Optional<PeriodMovement> findById(Long id);

    /**
     * Generic search method to find any {@link PeriodMovement} with a specific group of filters
     *
     * TODO make this filter by all properties defined on the period movement filter
     *
     * @param start the start position of the query cursor
     * @param pageSize the maximum amount of items per page
     * @return the {@link Page} implementation with a {@link List} of {@link PeriodMovement} inside
     */
    default Page<PeriodMovement> findAllBy(PeriodMovementFilter filter, int start, int pageSize) {

        final int totalRows = this.countPages(filter);

        final Criteria<PeriodMovement, PeriodMovement> criteria = criteria();

        criteria.orderAsc(PeriodMovement_.createdOn);

        final List<PeriodMovement> data = criteria.createQuery()
                .setFirstResult(start)
                .setMaxResults(pageSize)
                .getResultList();

        return Page.of(data, totalRows);
    }

    /**
     * Count the pages to the datatable so the pagination will work
     *
     * @param filter the {@link PeriodMovementFilter} to be used
     * @return the total of pages for this query
     */
    @SuppressWarnings("unchecked")
    default int countPages(PeriodMovementFilter filter) {

        final Criteria<PeriodMovement, PeriodMovement> criteria = criteria().or(this.getRestrictions(filter));

        return criteria.select(Long.class, count(PeriodMovement_.id))
                .getSingleResult()
                .intValue();
    }

    /**
     * Get all possible filters for the default query
     *
     * @param filter the {@link PeriodMovementFilter} to be used
     * @return the {@link Criteria} restrictions for this query
     */
    default Criteria<PeriodMovement, PeriodMovement> getRestrictions(PeriodMovementFilter filter) {
        return this.criteria()
                .likeIgnoreCase(PeriodMovement_.identification, filter.getText())
                .likeIgnoreCase(PeriodMovement_.code, filter.getText())
                .eq(PeriodMovement_.movementType, filter.getMovementType())
                .eq(PeriodMovement_.movementState, filter.getMovementState());
    }
}
