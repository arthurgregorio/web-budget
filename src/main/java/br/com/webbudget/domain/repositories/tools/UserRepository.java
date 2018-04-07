package br.com.webbudget.domain.repositories.tools;

import br.com.webbudget.domain.entities.security.StoreType;
import br.com.webbudget.domain.entities.security.User;
import br.com.webbudget.domain.entities.security.User_;
import br.com.webbudget.domain.repositories.DefaultRepository;
import java.util.Optional;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.Criteria;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 28/12/2017
 */
@Repository
public interface UserRepository extends DefaultRepository<User> {

    /**
     * 
     * @param id
     * @return 
     */
    Optional<User> findOptionalById(long id);
    
    /**
     * 
     * @param email
     * @return 
     */
    Optional<User> findOptionalByEmail(String email);
    
    /**
     * 
     * @param email
     * @param storeType
     * @return 
     */
    Optional<User> findOptionalByEmailAndStoreType(String email, StoreType storeType);
    
    /**
     * 
     * @param username
     * @return 
     */
    Optional<User> findOptionalByUsername(String username);
    
    /**
     * 
     * @return 
     */
    @Override
    default SingularAttribute<User, Boolean> getBlockedProperty() {
        return User_.blocked;
    }

    /**
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
