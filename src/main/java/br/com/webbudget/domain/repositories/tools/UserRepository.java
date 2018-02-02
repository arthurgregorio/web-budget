package br.com.webbudget.domain.repositories.tools;

import br.com.webbudget.domain.entities.security.User;
import java.util.List;
import java.util.Optional;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 28/12/2017
 */
@Repository
public interface UserRepository extends EntityRepository<User, Long> {

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
    @Query("FROM User u WHERE u.blocked = false")
    List<User> findAllActive();
}
