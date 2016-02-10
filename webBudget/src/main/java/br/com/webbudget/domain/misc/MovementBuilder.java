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
package br.com.webbudget.domain.misc;

import br.com.webbudget.domain.entity.movement.Apportionment;
import br.com.webbudget.domain.entity.movement.FinancialPeriod;
import br.com.webbudget.domain.entity.movement.Movement;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Builder padrao para construcao de movimentos dentro do sistema, utilize ele
 * para acessar as funcionalidades de criacao de movimentos atraves dos metodos
 * desta classe
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.1.0, 27/09/2015
 */
public final class MovementBuilder {

    private final Movement movement;

    /**
     * Inicializa o movimento a ser construido
     */
    public MovementBuilder() {
        this.movement = new Movement();
    }

    /**
     * @param value seu valor
     * @return este builder
     */
    public MovementBuilder withValue(BigDecimal value) {
        this.movement.setValue(value);
        return this;
    }
    
    /**
     * @param description a sua descricao
     * @return este builder
     */
    public MovementBuilder withDescription(String description) {
        this.movement.setDescription(description);
        return this;
    }
    
    /**
     * @param dueDate a sua data de vencimento
     * @return este builder
     */
    public MovementBuilder withDueDate(LocalDate dueDate) {
        this.movement.setDueDate(dueDate);
        return this;
    }
    
    /**
     * @param financialPeriod o periodo que vamos usar no moviemento
     * @return este builder
     */
    public MovementBuilder inTheFinancialPeriod(FinancialPeriod financialPeriod) {
        this.movement.setFinancialPeriod(financialPeriod);
        return this;
    }
    
    /**
     * @param apportionments os rateios 
     * @return este builder
     */
    public MovementBuilder andDividedAmong(List<Apportionment> apportionments) {
        apportionments
                .stream()
                .forEach(apportionment -> {
            this.movement.addApportionment(apportionment.copy());
        });
        return this;
    }
    
    /**
     * @return uma instancia de movimento com os itens preenchidos
     */
    public Movement build() {
        return this.movement;
    }
}