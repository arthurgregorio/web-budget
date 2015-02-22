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
package br.com.webbudget.application.controller.miscellany;

import br.com.webbudget.domain.entity.movement.FinancialPeriod;
import br.com.webbudget.domain.entity.movement.MovementClass;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO utilizado para transportar os dados do servico para a view ja que nao
 * existe nenhum objeto que detalhe o periodo e nao queremos ficar criando
 * objetos transientes na entidade do periodo financeiro
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 21/02/2015
 */
public class PeriodDetailsDTO implements Serializable {

    @Getter
    @Setter
    private BigDecimal expenses;
    @Getter
    @Setter
    private BigDecimal revenues;
    @Getter
    @Setter
    private BigDecimal debitCardExpenses;
    @Getter
    @Setter
    private BigDecimal creditCardExpenses;

    @Getter
    @Setter
    private FinancialPeriod financialPeriod;

    @Getter
    @Setter
    private List<MovementClass> expenseClasses;
    @Getter
    @Setter
    private List<MovementClass> revenueClasses;

    /**
     * Inicializa a parada toda...
     */
    public PeriodDetailsDTO() {

        this.expenses = BigDecimal.ZERO;
        this.revenues = BigDecimal.ZERO;
        this.debitCardExpenses = BigDecimal.ZERO;
        this.creditCardExpenses = BigDecimal.ZERO;

        this.expenseClasses = new ArrayList<>();
        this.revenueClasses = new ArrayList<>();
    }

    /**
     * @return o saldo dos movimentos, receitas - despesas
     */
    public BigDecimal getBalance() {
        return this.revenues.subtract(this.expenses);
    }

    /**
     * @return o total dos cartoes, cartao de debito + cartao de credito
     */
    public BigDecimal getCardsTotal() {
        return this.creditCardExpenses.add(this.debitCardExpenses);
    }
    
    /**
     * Metodo que realiza a organizacao das classes
     */
    public void sortMovementClasses() {
        Collections.sort(this.revenueClasses, new MovementClassComparator());
        Collections.sort(this.expenseClasses, new MovementClassComparator());
    }

    /**
     * Comparator para organizar as listas de classes
     */
    private class MovementClassComparator implements Comparator<MovementClass> {

        /**
         * @see Comparator#compare(java.lang.Object, java.lang.Object) 
         * 
         * @param c1
         * @param c2
         * @return 
         */
        @Override
        public int compare(MovementClass c1, MovementClass c2) {
            return c2.getTotalMovements().compareTo(c1.getTotalMovements());
        }
    }
}
