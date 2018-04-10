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
package br.com.webbudget.domain.entities.entries;

import br.com.webbudget.application.components.Color;
import br.com.webbudget.infrastructure.jpa.ColorConverter;
import br.com.webbudget.domain.entities.PersistentEntity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.1.0
 * @since 1.0.0, 28/03/2014
 */
@Entity
@Table(name = "cost_centers")
@ToString(callSuper = true, exclude = {"color","totalMovements","percentage"})
@EqualsAndHashCode(callSuper = true, exclude = {"color","totalMovements","percentage"})
public class CostCenter extends PersistentEntity {

    @Getter
    @Setter
    @NotEmpty(message = "{cost-center.name}")
    @Column(name = "name", nullable = false, length = 90)
    private String name;
    @Getter
    @Setter
    @Convert(converter = ColorConverter.class)
    @Column(name = "color", nullable = false, length = 20)
    private Color color;
    @Getter
    @Setter
    @NotNull(message = "{cost-center.expenses-budget}")
    @Column(name = "expenses_budget", nullable = false)
    private BigDecimal expensesBudget;
    @Getter
    @Setter
    @NotNull(message = "{cost-center.revenues-budget}")
    @Column(name = "revenues_budget", nullable = false)
    private BigDecimal revenuesBudget;
    @Getter
    @Setter
    @Column(name = "blocked")
    private boolean blocked;
    @Getter
    @Setter
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "id_parent")
    private CostCenter parent;

    @Setter
    @Getter
    @Transient
    private BigDecimal percentage;
    @Setter
    @Getter
    @Transient
    private BigDecimal totalMovements;

    /**
     *
     */
    public CostCenter() {
        this.color = Color.randomize();
        this.percentage = BigDecimal.ZERO;
        this.totalMovements = BigDecimal.ZERO;
        this.revenuesBudget = BigDecimal.ZERO;
        this.expensesBudget = BigDecimal.ZERO;
    }
    
    /**
     * @param classType a direcao do tipo de classe que estamos checando se
     * controla o valor de orcamento
     * @return true se houver orcamento para controlar, false se nao
     */
    public boolean controlBudget(MovementClassType classType) {
        if (classType == MovementClassType.IN) {
            return this.revenuesBudget.compareTo(BigDecimal.ZERO) > 0;
        } else {
            return this.expensesBudget.compareTo(BigDecimal.ZERO) > 0;
        }
    }
}
