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
package br.com.webbudget.domain.entities.journal;

import br.com.webbudget.domain.entities.PersistentEntity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import static br.com.webbudget.infrastructure.utils.DefaultSchemes.JOURNAL;
import static br.com.webbudget.infrastructure.utils.DefaultSchemes.JOURNAL_AUDIT;

/**
 * Class to represent a fuel used on the {@link Refueling}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.3.0, 23/07/2016
 */
@Entity
@Audited
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "fuels", schema = JOURNAL)
@AuditTable(value = "fuels", schema = JOURNAL_AUDIT)
public class Fuel extends PersistentEntity {

    @Getter
    @Setter
    @Column(name = "liters", nullable = false)
    private BigDecimal liters;
    @Getter
    @Setter
    @Column(name = "value_per_liter", nullable = false)
    private BigDecimal valuePerLiter;
    
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "fuel_type", nullable = false, length = 45)
    private FuelType fuelType;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_refueling", nullable = false)
    private Refueling refueling;
    
    /**
     * Constructor...
     */
    public Fuel() {
        this.liters = BigDecimal.ZERO;
        this.fuelType = FuelType.GASOLINE;
        this.valuePerLiter = BigDecimal.ZERO;
    }
    
    /**
     * Constructor...
     * 
     * @param refueling the linked {@link Refueling}
     */
    public Fuel(Refueling refueling) {
        this();
        this.refueling = refueling;
    }
    
    /**
     * Get the cost of this fuel
     *
     * @return the fuel cost, liters multiplied by the value per liter
     */
    public BigDecimal getCost() {
        return this.valuePerLiter.multiply(this.liters);
    }
    
    /**
     * To check if this fuel is valid or not
     *
     * @return if this fuel is valid or not
     */
    protected boolean isValid() {
        return this.liters != null && this.liters.compareTo(BigDecimal.ZERO) > 0
                && this.valuePerLiter != null && this.valuePerLiter.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * The inversion of {@link #isValid()} to use in streams
     *
     * @return if this fuel is invalid or not
     */
    protected boolean isInvalid() {
        return !this.isValid();
    }
}
