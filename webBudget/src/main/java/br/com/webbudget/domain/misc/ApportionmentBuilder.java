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
package br.com.webbudget.domain.misc;

import br.com.webbudget.domain.model.entity.entries.CostCenter;
import br.com.webbudget.domain.model.entity.entries.MovementClass;
import br.com.webbudget.domain.model.entity.financial.Apportionment;
import java.math.BigDecimal;

/**
 * Um builder para construcao de objetos de rateio de movimentos
 * 
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.3.0, 29/08/2016
 */
public final class ApportionmentBuilder extends Builder<Apportionment> {

    /**
     * Inicializa o builder
     */
    public ApportionmentBuilder() { 
        this.value = new Apportionment();
    }
    
    /**
     * @param value o valor do rateado
     * @return este builder
     */
    public ApportionmentBuilder withValue(BigDecimal value) {
        this.value.setValue(value);
        return this;
    }
    
    /**
     * @param costCenter o CC de rateio
     * @return este builder
     */
    public ApportionmentBuilder onCostCenter(CostCenter costCenter) {
        this.value.setCostCenter(costCenter);
        return this;
    }
    
    /**
     * @param movementClass a classe do CC a ratear
     * @return este builder
     */
    public ApportionmentBuilder withMovementClass(MovementClass movementClass) {
        this.value.setMovementClass(movementClass);
        return this;
    }
}
