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
package br.com.webbudget.domain.repositories.configuration;

import br.com.webbudget.domain.entities.configuration.StoreType;
import br.com.webbudget.domain.entities.configuration.User;
import br.com.webbudget.domain.entities.configuration.User_;
import br.com.webbudget.domain.repositories.LazyDefaultRepository;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.Criteria;

import javax.persistence.metamodel.SingularAttribute;
import java.util.Collection;
import java.util.List;
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
public interface UserRepository extends LazyDefaultRepository<User> {

    /**
     * Find an {@link User} by the email address
     * 
     * @param email the {@link User} email address to find
     * @return an {@link Optional} of the {@link User}
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Find an {@link User} by the email address and the {@link StoreType}
     * 
     * @param email the {@link User} email address to find
     * @param storeType the enum instance of {@link StoreType}
     * @return an {@link Optional} of the {@link User}
     */
    Optional<User> findByEmailAndStoreType(String email, StoreType storeType);
    
    /**
     * Find an {@link User} by the username
     * 
     * @param username the username to find the {@link User} object
     * @return an {@link Optional} of the {@link User}
     */
    Optional<User> findByUsername(String username);
    
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
    default Collection<Criteria<User, User>> getRestrictions(String filter) {
        return List.of(
                this.criteria().likeIgnoreCase(User_.name, this.likeAny(filter)),
                this.criteria().likeIgnoreCase(User_.username, this.likeAny(filter)),
                this.criteria().likeIgnoreCase(User_.email, this.likeAny(filter)));
    }
}