package br.com.webbudget.domain.repositories.tools;

import br.com.webbudget.domain.entities.tools.Authorization;
import br.com.webbudget.domain.repositories.DefaultRepository;
import org.apache.deltaspike.data.api.Repository;

import java.util.Optional;

/**
 * The {@link Authorization} repository
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 28/12/2017
 */
@Repository
public interface AuthorizationRepository extends DefaultRepository<Authorization> {

    /**
     * Find an {@link Authorization} by the functionality and the permission
     * 
     * @param functionality the functionality of the {@link Authorization}
     * @param permission the permission of the {@link Authorization}
     * @return an {@link Optional} of the {@link Authorization}
     */
    Optional<Authorization> findOptionalByFunctionalityAndPermission(String functionality, String permission);
}
