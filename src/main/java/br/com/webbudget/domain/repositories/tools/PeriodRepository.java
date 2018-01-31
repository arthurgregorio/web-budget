package br.com.webbudget.domain.repositories.tools;

import br.com.webbudget.domain.entities.miscellany.FinancialPeriod;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

/**
 *
 * @author Willian Brecher
 *
 * @version 1.0.0
 * @since 1.0.0, 06/12/2017
 */
@Repository
public interface PeriodRepository extends EntityRepository<FinancialPeriod, Long> {

    /**
     * 
     * @param id
     * @return 
     */
    @Query("SELECT p FROM Period p WHERE p.id = ?1")
    FinancialPeriod findPeriodById(Long id);
}
