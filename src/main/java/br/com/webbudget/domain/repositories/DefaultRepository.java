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

import br.com.webbudget.domain.entities.PersistentEntity;
import br.com.webbudget.domain.entities.PersistentEntity_;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.criteria.Criteria;
import org.apache.deltaspike.data.api.criteria.CriteriaSupport;

import javax.persistence.metamodel.SingularAttribute;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * The default repository, every repository inside this application should implement this interface
 *
 * @param <T> the type of this repository
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 20/03/2018
 */
public interface DefaultRepository<T extends PersistentEntity> extends EntityRepository<T, Long>, CriteriaSupport<T> {

    /**
     * Generic method to find a entity by Id
     *
     * @param id the id to search
     * @return the entity in a optional state
     */
    Optional<T> findById(Long id);

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
     * Same function as {@link LazyDefaultRepository#findAllBy(String, Boolean, int, int)} but in simpler way, without the pagination
     *
     * @param filter the filter to be used to find the objects
     * @param active the object state in the database, null means all states
     * @return {@link List} with the objects found
     */
    default List<T> findAllBy(String filter, Boolean active) {

        final Criteria<T, T> criteria = this.buildCriteria(filter, active);

        this.setOrder(criteria);

        return criteria.createQuery().getResultList();
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
     * Use this method to set the default order to all the queries using the default repository
     *
     * @param criteria the criteria to be used
     */
    default void setOrder(Criteria<T, T> criteria) {
        criteria.orderAsc(PersistentEntity_.id);
    }

    /**
     * This method should be implemented if the user needs to use the generic search provided by the methods
     * {@link #findAllBy(String, Boolean)} and {@link LazyDefaultRepository#findAllBy(String, Boolean)}
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
     * This method should be implemented if the user needs to use the generic type search provided by the methods
     * {@link #findAllBy(String, Boolean)} and {@link LazyDefaultRepository#findAllBy(String, Boolean)}
     *
     * @return the attribute responsible for representing the entity state
     */
    default SingularAttribute<? super T, Boolean> getEntityStateProperty() {
        throw new RuntimeException("getBlockProperty not implemented for query");
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
}