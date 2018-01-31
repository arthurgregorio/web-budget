/*
 * Copyright (C) 2015 Arthur Gregorio, AG.Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.com.webbudget.domain.repositories.financial;

import br.com.webbudget.domain.entities.financial.Apportionment;
import br.com.webbudget.domain.entities.miscellany.FinancialPeriod;
import br.com.webbudget.domain.entities.financial.FixedMovement;
import br.com.webbudget.domain.entities.financial.Movement;
import br.com.webbudget.domain.entities.entries.MovementClass;
import java.math.BigDecimal;
import java.util.List;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 1.0.0, 22/04/2014
 */
@Repository
public interface IApportionmentRepository extends EntityRepository<Apportionment, Long> {

    /**
     *
     * @param movement
     * @return
     */
    public List<Apportionment> listByMovement(Movement movement);
    
    /**
     * 
     * @param movementClass
     * @return 
     */
    public BigDecimal totalMovementsPerClass(MovementClass movementClass);
    
    /**
     * 
     * @param fixedMovement
     * @return 
     */
    public List<Apportionment> listByFixedMovement(FixedMovement fixedMovement);
    
    /**
     * 
     * @param period
     * @param movementClass
     * @return 
     */
    public BigDecimal totalMovementsPerClassAndPeriod(FinancialPeriod period, MovementClass movementClass);
}
