/*
 * Copyright (C) 2017 Arthur Gregorio, AG.Software
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
package br.com.webbudget.domain.repositories.tools;

import br.com.webbudget.domain.entities.tools.StoreType;
import br.com.webbudget.domain.entities.tools.User;
import br.com.webbudget.domain.entities.tools.User_;
import br.com.webbudget.domain.repositories.DefaultRepository;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.Criteria;

import javax.persistence.metamodel.SingularAttribute;
import java.util.Optional;

/**
 * The {@link User} repository
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 28/12/2017
 */
@Repository
public interface UserRepository extends DefaultRepository<User> {

    /**
     * Find an {@link User} by the email address
     * 
     * @param email the {@link User} email address to find
     * @return an {@link Optional} of the {@link User}
     */
    Optional<User> findOptionalByEmail(String email);
    
    /**
     * Find an {@link User} by the email address and the {@link StoreType}
     * 
     * @param email the {@link User} email address to find
     * @param storeType the enum value of {@link StoreType}
     * @return an {@link Optional} of the {@link User}
     */
    Optional<User> findOptionalByEmailAndStoreType(String email, StoreType storeType);
    
    /**
     * Find an {@link User} by the username
     * 
     * @param username the username to find the {@link User} object
     * @return an {@link Optional} of the {@link User}
     */
    Optional<User> findOptionalByUsername(String username);
    
    /**
     * {@inheritDoc}
     * 
     * @return 
     */
    @Override
    default SingularAttribute<User, Boolean> getEntityStateProperty() {
        return User_.active;
    }

    /**
     * {@inheritDoc}
     * 
     * @param filter
     * @return 
     */
    @Override
    default Criteria<User, User> getRestrictions(String filter) {
        return criteria()
                .likeIgnoreCase(User_.name, filter)
                .likeIgnoreCase(User_.username, filter)
                .likeIgnoreCase(User_.email, filter);
    }
}