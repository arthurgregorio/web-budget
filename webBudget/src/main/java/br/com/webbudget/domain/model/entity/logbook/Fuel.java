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
package br.com.webbudget.domain.model.entity.logbook;

import br.com.webbudget.domain.model.entity.PersistentEntity;
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

/**
 * Classe que representa o combustivel utilizado no abastecimento
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.3.0, 23/07/2016
 */
@Entity
@Table(name = "fuels")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
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
    @Column(name = "fuel_type", nullable = false)
    private FuelType fuelType;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_refueling")
    private Refueling refueling;
    
    /**
     * 
     */
    public Fuel() {
        this.liters = BigDecimal.ZERO;
        this.fuelType = FuelType.GASOLINE;
        this.valuePerLiter = BigDecimal.ZERO;
    }
    
    /**
     * @return o custo deste combustivel
     */
    public BigDecimal getCost() {
        return this.valuePerLiter.multiply(this.liters);
    }
}
