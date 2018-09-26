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
import br.com.webbudget.domain.entities.registration.MovementClassType;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
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
    @Column(name = "code", nullable = false, length = 8, unique = true)
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
     *
     */
    public Apportionment() {
        this.code = RandomCode.alphanumeric(5);
    }

    /**
     *
     * @param costCenter
     * @param movementClass
     * @param value
     */
    public Apportionment(CostCenter costCenter, MovementClass movementClass, BigDecimal value) {
        this();
        if (!movementClass.getCostCenter().equals(costCenter)) {
            throw new BusinessLogicException("error.apportionment.invalid-class-for-cc");
        } else {
            this.value = value;
            this.costCenter = costCenter;
            this.movementClass = movementClass;
        }
    }

    /**
     * @return se este e um rateio de receita
     */
    public boolean isForRevenues() {
        return this.movementClass.getMovementClassType() == MovementClassType.REVENUE;
    }

    /**
     * @return se este e um rateio de despesa
     */
    public boolean isForExpenses() {
        return this.movementClass.getMovementClassType() == MovementClassType.EXPENSE;
    }

    /**
     * @return uma copia deste reateio com um novo codigo
     */
    public Apportionment copy() {

        final Apportionment apportionment = new Apportionment();

        apportionment.setValue(this.value);
        apportionment.setCostCenter(this.costCenter);
        apportionment.setMovementClass(this.movementClass);

        return apportionment;
    }
}
