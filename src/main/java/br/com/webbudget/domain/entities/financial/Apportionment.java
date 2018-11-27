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
package br.com.webbudget.domain.entities.financial;

import br.com.webbudget.domain.entities.PersistentEntity;
import br.com.webbudget.domain.entities.registration.CostCenter;
import br.com.webbudget.domain.entities.registration.MovementClass;
import br.com.webbudget.infrastructure.utils.RandomCode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

import static br.com.webbudget.infrastructure.utils.DefaultSchemes.FINANCIAL;

/**
 * The representation of an apportionment of a {@link Movement}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 02/02/2015
 */
@Entity
@Audited
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "apportionments", schema = FINANCIAL)
@AuditTable(value = "apportionments", schema = FINANCIAL)
public class Apportionment extends PersistentEntity {

    @Getter
    @Column(name = "code", nullable = false, length = 6, unique = true)
    private String code;
    @Getter
    @Setter
    @NotNull(message = "{apportionment.value}")
    @Column(name = "value", nullable = false)
    private BigDecimal value;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_movement")
    private Movement movement;
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_fixed_movement")
    private FixedMovement fixedMovement;
    @Getter
    @Setter
    @ManyToOne
    @NotNull(message = "{apportionment.cost-center}")
    @JoinColumn(name = "id_cost_center", nullable = false)
    private CostCenter costCenter;
    @Getter
    @Setter
    @ManyToOne
    @NotNull(message = "{apportionment.movement-class}")
    @JoinColumn(name = "id_movement_class", nullable = false)
    private MovementClass movementClass;

    /**
     * Constructor...
     */
    public Apportionment() {
        this.code = RandomCode.alphanumeric(6);
    }

    /**
     * Constructor...
     *
     * @param value the value of this apportionment
     * @param movementClass the apportionment {@link MovementClass} we use this to take the {@link CostCenter}
     */
    public Apportionment(BigDecimal value, MovementClass movementClass) {
        this();
        this.value = value;
        this.movementClass = movementClass;
        this.costCenter = movementClass.getCostCenter();
    }

    /**
     * To check if this apportionment is an income
     *
     * @return true if it is a revenue
     */
    public boolean isRevenue() {
        return this.movementClass.isRevenue();
    }

    /**
     * To check if this apportionment is an expense
     *
     * @return true if it is a expense
     */
    public boolean isExpense() {
        return this.movementClass.isExpense();
    }

    /**
     * FIXME remove it is not necessary!
     *
     * Copy this apportionment to a new instance
     *
     * @return the new apportionment instance
     */
//    public Apportionment copy() {
//        return new Apportionment(this.getValue(), this.getMovementClass());
//    }
}
