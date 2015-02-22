/*
 * Copyright (C) 2015 Arthur
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

package br.com.webbudget.domain.components;

import br.com.webbudget.domain.entity.card.CardType;
import br.com.webbudget.domain.entity.movement.FinancialPeriod;
import br.com.webbudget.domain.entity.movement.Movement;
import br.com.webbudget.domain.entity.movement.MovementClass;
import br.com.webbudget.domain.entity.movement.MovementClassType;
import br.com.webbudget.domain.repository.movement.IMovementClassRepository;
import br.com.webbudget.domain.repository.movement.IMovementRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 22/02/2015
 */
@Component
public class MovementsCalculator {
    
    @Autowired
    private IMovementRepository movementRepository;
    @Autowired
    private IMovementClassRepository movementClassRepository;

    /**
     * Calcula o total de movimentos contidos em cada classe de movimento
     * 
     * @param period o periodo
     * @param direction a direcao da classe, se e entrada ou saida
     * 
     * @return as classes com sesus valores de movimentos do periodo
     */
    public List<MovementClass> calculateTotalByMovementClass(FinancialPeriod period, MovementClassType direction) {
       
        final List<MovementClass> classes = 
                this.movementClassRepository.listByTypeAndStatus(direction, false);
        
        final List<MovementClass> onlyValidClasses = new ArrayList<>();
        
        for (MovementClass movementClass : classes) {
            
            final BigDecimal total = this.movementRepository
                    .countTotalByPeriodAndMovementClass(period, movementClass);
            
            if (total != null) {
                movementClass.setTotalMovements(total);
                onlyValidClasses.add(movementClass);
            }
        }
        
        return onlyValidClasses;
    }
    
    /**
     * Calcula o total dos movimentos de mesmo tipo
     * 
     * @param period o periodo de busca
     * @param direction a direcao do movimento (receita ou despesa)
     * 
     * @return o total para aquele tipo
     */
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalByDirection(FinancialPeriod period, 
            MovementClassType direction) {
        
        final List<Movement> movements = this.movementRepository
                .listByPeriodAndDirection(period, direction);
        
        BigDecimal total = BigDecimal.ZERO;
        
        for (Movement movement : movements) {
            total = total.add(movement.getValue());
        }
        
        return total;
    }
    
    /**
     * Lista os movimentos do perido totalizando os cosnsumos em cartao de 
     * debito e credito
     * 
     * @param period o periodo a pesquisar
     * @param type o tipo do cartao
     * 
     * @return o total de consumo para aquele tipo
     */
    @Transactional(readOnly = true)
    public BigDecimal calculateCardExpenses(FinancialPeriod period, CardType type) {
       
        final List<Movement> movements = this.movementRepository
                .listByPeriodAndCardType(period, type);
        
        BigDecimal total = BigDecimal.ZERO;
        
        for (Movement movement : movements) {
            total = total.add(movement.getValue());
        }
        
        return total;
    }
}
