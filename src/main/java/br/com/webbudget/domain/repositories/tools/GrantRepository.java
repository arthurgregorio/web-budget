package br.com.webbudget.domain.repositories.tools;

import br.com.webbudget.domain.entities.security.Grant;
import br.com.webbudget.domain.entities.security.Group;
import java.util.List;
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
public interface GrantRepository extends EntityRepository<Grant, Long> {

    /**
     * 
     * @param group
     * @return 
     */
    List<Grant> findByGroup(Group group);
}
