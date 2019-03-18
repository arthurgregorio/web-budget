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
package br.com.webbudget.domain.repositories;

import br.com.webbudget.application.components.ui.table.Page;
import br.com.webbudget.domain.entities.PersistentEntity;
import br.com.webbudget.domain.entities.PersistentEntity_;
import org.apache.deltaspike.data.api.criteria.Criteria;

import java.util.List;

/**
 * Implementation for {@link DefaultRepository} with lazy load support
 *
 * @param <T> the type of this repository
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 06/01/2019
 */
public interface LazyDefaultRepository<T extends PersistentEntity> extends DefaultRepository<T> {

    /**
     * Generic search method with lazy pagination support. To use this method you must implement
     * {@link #getRestrictions(String)} and {@link #getEntityStateProperty()}
     *
     * @param filter the filter to be used to find the objects
     * @param active the object state in the database, null means all states
     * @param start the starting page
     * @param pageSize size of the page
     * @return {@link Page} filled with the objects found
     */
    default Page<T> findAllBy(String filter, Boolean active, int start, int pageSize) {

        final int totalRows = this.countPages(filter, active);

        final Criteria<T, T> criteria = this.buildCriteria(filter, active);

        this.setOrder(criteria);

        final List<T> data = criteria.createQuery()
                .setFirstResult(start)
                .setMaxResults(pageSize)
                .getResultList();

        return Page.of(data, totalRows);
    }

    /**
     * Count the pages for pagination purpose
     *
     * @param filter the filter to use in count process
     * @param active if consider only active or inactive entities
     * @return the total of pages
     */
    @SuppressWarnings("unchecked")
    default int countPages(String filter, Boolean active) {
        return this.buildCriteria(filter, active)
                .select(Long.class, count(PersistentEntity_.id))
                .getSingleResult()
                .intValue();
    }
}