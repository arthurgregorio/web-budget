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

import br.com.webbudget.application.components.table.Page;
import br.com.webbudget.domain.entities.PersistentEntity;
import br.com.webbudget.domain.entities.PersistentEntity_;
import org.apache.deltaspike.data.api.criteria.Criteria;

import javax.persistence.metamodel.SingularAttribute;
import java.util.Collection;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
     * Same function as {@link #findAllBy(String, Boolean, int, int)} but in simpler way, without the pagination
     *
     * @param filter the filter to be used to find the objects
     * @param active the object state in the database, null means all states
     * @return {@link List} with the objects found
     */
    default List<T> findAllBy(String filter, Boolean active) {

        final Criteria<T, T> criteria = criteria();

        if (isNotBlank(filter)) {
            criteria.or(this.getRestrictions(filter));
        }

        criteria.eq(this.getEntityStateProperty(), active);

        this.setOrder(criteria);

        return criteria.createQuery().getResultList();
    }

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
     * Generic method to find all inactive entities
     *
     * @return a {@link List} of all inactive entities
     */
    default List<T> findAllInactive() {

        final Criteria<T, T> criteria = criteria()
                .eq(this.getEntityStateProperty(), false);

        this.setOrder(criteria);

        return criteria.getResultList();
    }

    /**
     * Generic method to find all active entities
     *
     * @return the {@link List} of all active entities
     */
    default List<T> findAllActive() {

        final Criteria<T, T> criteria = criteria()
                .eq(this.getEntityStateProperty(), true);

        this.setOrder(criteria);

        return criteria.getResultList();
    }

    /**
     * Helper method to create {@link Criteria} instances, do not override this method or if you do this, keep in mind
     * that you are change a core behavior and problems here means problems in all queries inside de the application
     *
     * @param filter the filters provided by the {@link #getRestrictions(String)}
     * @param active the active property provided by the {@link #getEntityStateProperty()}
     * @return a new criteria ready to query
     */
    default Criteria<T, T> buildCriteria(String filter, Boolean active) {

        final Criteria<T, T> criteria = criteria();

        if (isNotBlank(filter)) {
            criteria.or(this.getRestrictions(filter));
        }

        if (active != null) {
            criteria.eq(this.getEntityStateProperty(), active);
        }

        return criteria;
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

    /**
     * Use this method to set the default order to all the queries using the default repository
     *
     * @param criteria the criteria to be used
     */
    default void setOrder(Criteria<T, T> criteria) {
        criteria.orderAsc(PersistentEntity_.id);
    }

    /**
     * This method should be implemented if the user needs to use the generic type search with the
     * {@link #findAllBy(String, Boolean, int, int)} method
     *
     * With this we can detect all the restrictions to build the criteria
     *
     * @param filter the generic filter in {@link String} format
     * @return the criteria for the type of the repository
     */
    default Collection<Criteria<T, T>> getRestrictions(String filter) {
        throw new RuntimeException("getRestrictions not implemented for query");
    }

    /**
     * This method should be implemented if the user needs to use the generic type search with the
     * {@link #findAllBy(String, Boolean, int, int)} method
     *
     * @return the attribute responsible for representing the entity state
     */
    default SingularAttribute<? super T, Boolean> getEntityStateProperty() {
        throw new RuntimeException("getBlockProperty not implemented for query");
    }
}