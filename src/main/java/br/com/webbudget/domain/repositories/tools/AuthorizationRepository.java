package br.com.webbudget.domain.repositories.tools;

import br.com.webbudget.domain.entities.security.Authorization;
import java.util.Optional;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 28/12/2017
 */
@Repository
public interface AuthorizationRepository extends EntityRepository<Authorization, Long> {

    /**
     * 
     * @param functionality
     * @param permission
     * @return 
     */
    Optional<Authorization> findOptionalByFunctionalityAndPermission(String functionality, String permission);
}
