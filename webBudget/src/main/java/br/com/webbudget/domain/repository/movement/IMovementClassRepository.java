package br.com.webbudget.domain.repository.movement;

import br.com.webbudget.domain.entity.movement.CostCenter;
import br.com.webbudget.domain.entity.movement.MovementClass;
import br.com.webbudget.domain.entity.movement.MovementClassType;
import br.com.webbudget.domain.repository.IGenericRepository;
import java.util.List;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 04/03/2013
 */
public interface IMovementClassRepository extends IGenericRepository<MovementClass, Long> {

    /**
     * 
     * @param isBlocked
     * @return 
     */
    public List<MovementClass> listByStatus(Boolean isBlocked);
    
    /**
     * 
     * @param type
     * @param blocked
     * @return 
     */
    public List<MovementClass> listByTypeAndStatus(MovementClassType type, Boolean blocked);

    /**
     * 
     * @param costCenter
     * @param type
     * @return 
     */
    public List<MovementClass> listByCostCenterAndType(CostCenter costCenter, MovementClassType type);
    
    /**
     * 
     * @param name
     * @param type
     * @param costCenter
     * @return 
     */
    public MovementClass findByNameAndTypeAndCostCenter(String name, MovementClassType type, CostCenter costCenter);
}
