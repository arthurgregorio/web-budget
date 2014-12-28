package br.com.webbudget.domain.repository.movement;

import br.com.webbudget.domain.entity.movement.CostCenter;
import br.com.webbudget.domain.repository.IGenericRepository;
import java.util.List;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 04/03/2013
 */
public interface ICostCenterRepository extends IGenericRepository<CostCenter, Long> {

    /**
     * 
     * @param isBlocked
     * @return 
     */
    public List<CostCenter> listByStatus(Boolean isBlocked);
    
    /**
     * 
     * @param name
     * @param parent
     * @return 
     */
    public CostCenter findByNameAndParent(String name, CostCenter parent);
}
