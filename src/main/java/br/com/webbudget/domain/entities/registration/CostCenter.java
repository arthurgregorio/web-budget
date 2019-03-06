/*
 * Copyright (C) 2014 Arthur Gregorio, AG.Software
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
package br.com.webbudget.domain.entities.registration;

import br.com.webbudget.application.components.dto.Color;
import br.com.webbudget.domain.entities.PersistentEntity;
import br.com.webbudget.infrastructure.jpa.ColorConverter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

import static br.com.webbudget.infrastructure.utils.DefaultSchemes.REGISTRATION;
import static br.com.webbudget.infrastructure.utils.DefaultSchemes.REGISTRATION_AUDIT;

/**
 * The representation of a cost center in the application
 *
 * @author Arthur Gregorio
 *
 * @version 1.1.0
 * @since 1.0.0, 28/03/2014
 */
@Entity
@Audited
@Table(name = "cost_centers", schema = REGISTRATION)
@AuditTable(value = "cost_centers", schema = REGISTRATION_AUDIT)
@ToString(callSuper = true, exclude = {"percentage", "totalMovements"})
@EqualsAndHashCode(callSuper = true, exclude = {"percentage", "totalMovements"})
public class CostCenter extends PersistentEntity {

    @Getter
    @Setter
    @NotBlank(message = "{cost-center.name}")
    @Column(name = "name", nullable = false, length = 90)
    private String name;
    @Getter
    @Setter
    @Convert(converter = ColorConverter.class)
    @Column(name = "color", nullable = false, length = 21)
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
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @Getter
    @Setter
    @Column(name = "active", nullable = false)
    private boolean active;

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
     * Default constructor
     */
    public CostCenter() {
        this.active = true;
        this.color = Color.randomize();
        this.percentage = BigDecimal.ZERO;
        this.totalMovements = BigDecimal.ZERO;
        this.revenuesBudget = BigDecimal.ZERO;
        this.expensesBudget = BigDecimal.ZERO;
    }

    /**
     * Get the parent cost center name
     *
     * @return the name of the parent cost center
     */
    public String getParentName() {
        return this.parent != null ? this.parent.getName() : null;
    }

    /**
     * Method used to check if this cost center control the budget by the type of a {@link MovementClass}
     *
     * @param classType the {@link MovementClassType} to determine which budget we should consume
     * @return true for a cost center that control budget or false otherwise
     */
    public boolean controlBudget(MovementClassType classType) {
        if (classType == MovementClassType.REVENUE) {
            return this.revenuesBudget.compareTo(BigDecimal.ZERO) > 0;
        } else {
            return this.expensesBudget.compareTo(BigDecimal.ZERO) > 0;
        }
    }
}
