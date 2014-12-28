package br.com.webbudget.domain.repository.movement;

import br.com.webbudget.domain.entity.closing.Closing;
import br.com.webbudget.domain.repository.GenericRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 22/04/2014
 */
@Repository
public class ClosingRepository extends GenericRepository<Closing, Long> implements IClosingRepository {

}
