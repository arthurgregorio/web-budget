package br.com.webbudget.domain.repositories.tools;

import br.com.webbudget.domain.entities.security.Group;
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
public interface GroupRepository extends EntityRepository<Group, Long> {

    /**
     * 
     * @param name
     * @return 
     */
    Optional<Group> findOptionalByName(String name);

    /**
     * 
     * @return 
     */
    @Query("FROM Group g WHERE g.blocked = false")
    List<Group> findAllActive();
}
