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
     * Generic search method with lazy pagination support. To use this method you must implement
     * {@link #getRestrictions(String)} and {@link #getEntityStateProperty()}
     *
     * @param filter the string filter to use
     * @param active the object status of the entity, null means all states
     * @param start the start page
     * @param pageSize the size of the page
     * @return the list of objects found
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
     * @return a list of all inactive entities
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
     * @return the list of all active entities
     */
    default List<T> findAllActive() {

        final Criteria<T, T> criteria = this.criteria()
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
    @SuppressWarnings("unchecked")
    default Criteria<T,T> buildCriteria(String filter, Boolean active) {

        final Criteria<T, T> criteria = this.criteria();

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
     * Helper method to make a simple LIKE clause look in both ways (begin and end) of the sentence.
     *
     * Example: if the filter is 'John' the result after calling this method should be '%John%'
     *
     * @param filter the filter to put the wildcard '%'
     * @return the string filter with 'any' style
     */
    default String likeAny(String filter) {
        return "%" + filter + "%";
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
    default SingularAttribute<T, Boolean> getEntityStateProperty() {
        throw new RuntimeException("getBlockProperty not implemented for query");
    }
}