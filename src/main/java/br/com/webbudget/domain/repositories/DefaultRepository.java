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
import java.util.List;
import java.util.Optional;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.FirstResult;
import org.apache.deltaspike.data.api.MaxResults;
import org.apache.deltaspike.data.api.criteria.Criteria;
import org.apache.deltaspike.data.api.criteria.CriteriaSupport;

/**
 * Implementacao default de um repositorio para uma entidade do sistema
 * 
 * @param <T> o tipo que nosso repositorio ira representar
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 20/03/2018
 */
public interface DefaultRepository<T extends PersistentEntity> 
        extends EntityRepository<T, Long>, CriteriaSupport<T> {

    /**
     * 
     * @param filter
     * @param blocked
     * @param start
     * @param pageSize
     * @return 
     */
    default List<T> findAllBy(String filter, Boolean blocked, 
            @FirstResult int start, @MaxResults int pageSize) {
        
        final Criteria<T, T> criteria = criteria()
                .or(this.getRestrictions(filter));
        
        if (blocked != null) {
            criteria.eq(this.getBlockedProperty(), blocked);
        }
        
        this.applyOrder(criteria);
                
        return criteria.createQuery()
                .setFirstResult(start)
                .setMaxResults(pageSize)
                .getResultList();
    }

    /**
     * 
     * @return 
     */
    default List<T> findAllUnblocked() {
        
        final Criteria<T, T> criteria = criteria()
                .eq(this.getBlockedProperty(), false);
        
        this.applyOrder(criteria);              

        return criteria.getResultList();
    }
    
    /**
     * 
     * @param criteria 
     */
    default void applyOrder(Criteria<T, T> criteria) {
        criteria.orderDesc(PersistentEntity_.createdOn);
    }
    
    /**
     * 
     * @param id
     * @return 
     */
    Optional<T> findOptionalById(Long id);
    
    /**
     * 
     * @param filter
     * @return 
     */
    Criteria<T, T> getRestrictions(String filter);
    
    /**
     * 
     * @return 
     */
    SingularAttribute<T, Boolean> getBlockedProperty();
}
