/*
 * Copyright (C) 2019 Arthur Gregorio, AG.Software
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

import br.com.webbudget.application.components.ui.filter.FixedMovementFilter;
import br.com.webbudget.application.components.ui.table.Page;
import br.com.webbudget.domain.entities.financial.FixedMovement;
import br.com.webbudget.domain.entities.financial.FixedMovementState;
import br.com.webbudget.domain.entities.financial.FixedMovement_;
import br.com.webbudget.domain.entities.financial.PeriodMovement_;
import br.com.webbudget.domain.repositories.DefaultRepository;
import org.apache.deltaspike.data.api.EntityGraph;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.Criteria;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * The {@link FixedMovement} repository
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 21/03/2019
 */
@Repository
public interface FixedMovementRepository extends DefaultRepository<FixedMovement> {

    /**
     * Find a {@link FixedMovement} by the ID
     *
     * @param id to search for
     * @return an {@link Optional} of the {@link FixedMovement}
     */
    @Override
    @EntityGraph(value = "Movement.full")
    Optional<FixedMovement> findById(Long id);

    /**
     * Find all {@link FixedMovement} by the auto launch flag
     *
     * @param autoLaunch true or false
     * @param state the state that we want to get
     * @return a {@link List} of the {@link FixedMovement} found
     */
    List<FixedMovement> findByAutoLaunchAndFixedMovementState(boolean autoLaunch, FixedMovementState state);

    /**
     * Method used to search for {@link FixedMovement} using pagination
     *
     * @param filter to be applied
     * @param start starting point
     * @param pageSize maximum size of the page
     * @return a {@link Page} with the {@link FixedMovement} found
     */
    default Page<FixedMovement> findAllBy(FixedMovementFilter filter, int start, int pageSize) {

        final int totalRows = this.countPages(filter);

        final Criteria<FixedMovement, FixedMovement> criteria = this.buildCriteria(filter);

        criteria.orderDesc(FixedMovement_.createdOn);

        final List<FixedMovement> data = criteria.createQuery()
                .setFirstResult(start)
                .setMaxResults(pageSize)
                .getResultList();

        return Page.of(data, totalRows);
    }

    /**
     * Method used to count the pages before the pagination process
     *
     * @param filter used to find and count the pages
     * @return number of pages for the given filter
     */
    @SuppressWarnings("unchecked")
    default int countPages(FixedMovementFilter filter) {
        return this.buildCriteria(filter)
                .select(Long.class, count(FixedMovement_.id))
                .getSingleResult()
                .intValue();
    }

    /**
     * Create and apply the filters to the {@link Criteria}
     *
     * @param filter to be used
     * @return the {@link Criteria} created to search for {@link FixedMovement}
     */
    default Criteria<FixedMovement, FixedMovement> buildCriteria(FixedMovementFilter filter) {

        final Criteria<FixedMovement, FixedMovement> criteria = this.criteria();

        if (filter.getFixedMovementState() != null) {
            criteria.eq(FixedMovement_.fixedMovementState, filter.getFixedMovementState());
        }

        if (isNotBlank(filter.getValue())) {

            final String anyFilter = this.likeAny(filter.getValue());

            final Set<Criteria<FixedMovement, FixedMovement>> restrictions = new HashSet<>();

            restrictions.add(this.criteria().likeIgnoreCase(FixedMovement_.identification, anyFilter));
            restrictions.add(this.criteria().likeIgnoreCase(FixedMovement_.description, anyFilter));

            filter.valueToBigDecimal()
                    .ifPresent(value -> restrictions.add(this.criteria().eq(PeriodMovement_.value, value)));

            criteria.or(restrictions);
        }

        return criteria;
    }
}
