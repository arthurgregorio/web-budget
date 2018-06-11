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
package br.com.webbudget.domain.services.misc;

import br.com.webbudget.domain.entities.financial.Apportionment;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.entities.financial.Movement;
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
 * @version 2.0.0
 * @since 2.1.0, 27/09/2015
 */
public final class MovementBuilder extends Builder<Movement> {

    /**
     * Inicializa o movimento a ser construido
     */
    public MovementBuilder() {
        this.value = new Movement();
    }

    /**
     * @param value seu valor
     * @return este builder
     */
    public MovementBuilder withValue(BigDecimal value) {
        this.value.setValue(value);
        return this;
    }

    /**
     * @param description a sua descricao
     * @return este builder
     */
    public MovementBuilder describedBy(String description) {
        this.value.setDescription(description);
        return this;
    }

    /**
     * @param dueDate a sua data de vencimento
     * @return este builder
     */
    public MovementBuilder onDueDate(LocalDate dueDate) {
        this.value.setDueDate(dueDate);
        return this;
    }

    /**
     * @param financialPeriod o periodo que vamos usar no moviemento
     * @return este builder
     */
    public MovementBuilder inThePeriodOf(FinancialPeriod financialPeriod) {
        this.value.setFinancialPeriod(financialPeriod);
        return this;
    }

    /**
     * @param builder o builder de rateios
     * @return este builder
     */
    public MovementBuilder dividedAmong(ApportionmentBuilder builder) {
        this.value.addApportionment(builder.build());
        return this;
    }

    /**
     * @param apportionments os rateios
     * @return este builder
     */
    public MovementBuilder dividedAmong(List<Apportionment> apportionments) {
        apportionments
                .stream()
                .forEach(apportionment -> {
                    this.value.addApportionment(apportionment.copy());
                });
        return this;
    }
    
    /**
     * @return o codigo do movimento gerado
     */
    public String getMovementCode() {
        return this.value.getCode();
    }
}
