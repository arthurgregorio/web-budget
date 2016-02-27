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

import br.com.webbudget.domain.entity.movement.Movement;
import java.math.BigDecimal;
import java.util.List;

/**
 * A calculadora de movimentos, por este helper podemos calcular diversos valores
 * dos movimentos informados como parametro para a classe.
 * 
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.2.0, 08/02/2016
 */
public class MovementCalculator {

    private final List<Movement> movements;

    /**
     * 
     * @param movements 
     */
    public MovementCalculator(List<Movement> movements) {
        this.movements = movements;
    }
    
    /**
     * @return o total de receitas
     */
    public BigDecimal getRevenuesTotal() {
        return this.movements.stream()
                .filter(Movement::isRevenue)
                .map(Movement::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * @return o total de despesas
     */
    public BigDecimal getExpensesTotal() {
        return this.movements.stream()
                .filter(Movement::isExpense)
                .map(Movement::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * @return o total pago no cartao de credito
     */
    public BigDecimal getTotalPaidOnCreditCard() {
        return this.movements.stream()
                .filter(Movement::isPaidOnCreditCard)
                .map(Movement::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * @return o saldo, receitas menos despesas
     */
    public BigDecimal getBalance() {
        return this.getRevenuesTotal().subtract(this.getExpensesTotal());
    }
}
