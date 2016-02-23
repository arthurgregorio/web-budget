/*
 * Copyright (C) 2016 Arthur Gregorio, AG.Software
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
package br.com.webbudget.domain.service;

import br.com.webbudget.domain.entity.movement.FinancialPeriod;
import br.com.webbudget.domain.entity.movement.MovementClass;
import br.com.webbudget.domain.entity.movement.MovementClassType;
import br.com.webbudget.domain.repository.movement.IApportionmentRepository;
import br.com.webbudget.domain.repository.movement.IMovementClassRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.2.0, 22/02/2016
 */
@ApplicationScoped
public class PeriodDetailService {

    @Inject
    private IApportionmentRepository apportionmentRepository;
    @Inject
    private IMovementClassRepository movementClassRepository;
    
    /**
     * 
     * @param period
     * @param direction
     * @return 
     */
    public List<MovementClass> fetchTopClassesAndValues(
            FinancialPeriod period, MovementClassType direction) {
        
        final List<MovementClass> withValues = new ArrayList<>();
        
        // lista as classes sem pegar as bloqueadas
        final List<MovementClass> classes = this.movementClassRepository
                .listByTypeAndStatus(direction, Boolean.FALSE);
        
        classes.stream().forEach(clazz -> {
                    
            final BigDecimal total = 
                    this.apportionmentRepository.totalMovementsPerClass(clazz);
                    
            if (total != null) {
                clazz.setTotalMovements(total);
                withValues.add(clazz);
            }
        });
        
        // ordena do maior para o menor
        withValues.sort((c1, c2) -> 
                c2.getTotalMovements().compareTo(c1.getTotalMovements()));
        
        return withValues.size() > 10 ? withValues.subList(0, 10) : withValues;
    }
}
