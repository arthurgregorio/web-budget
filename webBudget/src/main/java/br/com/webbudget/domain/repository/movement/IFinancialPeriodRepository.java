package br.com.webbudget.domain.repository.movement;

import br.com.webbudget.domain.entity.movement.FinancialPeriod;
import br.com.webbudget.domain.repository.IGenericRepository;
import java.util.List;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 04/03/2013
 */
public interface IFinancialPeriodRepository extends IGenericRepository<FinancialPeriod, Long> {

    /**
     * 
     * @return 
     */
    public List<FinancialPeriod> listOpen();
    
    /**
     * 
     * @param isClosed
     * @return 
     */
    public List<FinancialPeriod> listByStatus(Boolean isClosed);
    
    /**
     * 
     * @param identification
     * @return 
     */
    public FinancialPeriod findByIdentification(String identification);
}
