package br.com.webbudget.domain.repositories.tools;

import br.com.webbudget.domain.entities.tools.Grant;
import br.com.webbudget.domain.entities.tools.Group;
import br.com.webbudget.domain.repositories.DefaultRepository;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

/**
 * The {@link Grant} repository
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 28/12/2017
 */
@Repository
public interface GrantRepository extends DefaultRepository<Grant> {

    /**
     * Find a list o {@link Grant} from a given {@link Group}
     * 
     * @param group the {@link Group} to list his {@link Grant}
     * @return a {@link List} of {@link Grant}
     */
    List<Grant> findByGroup(Group group);
}
