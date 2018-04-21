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
package br.com.webbudget.domain.entities.logbook;

import br.com.webbudget.domain.entities.PersistentEntity;
import br.com.webbudget.domain.entities.entries.CostCenter;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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
 * @version 1.0.0
 * @since 2.3.0, 16/05/2016
 */
@Entity
@Table(name = "vehicles")
@ToString(callSuper = true, exclude = {"costCenter", "odometer"})
@EqualsAndHashCode(callSuper = true, exclude = {"costCenter", "odometer"})
public class Vehicle extends PersistentEntity {

    @Getter
    @Setter
    @NotEmpty(message = "{vehicle.identification}")
    @Column(name = "identification", nullable = false, length = 90)
    private String identification;
    @Getter
    @Setter
    @NotEmpty(message = "{vehicle.brand}")
    @Column(name = "brand", nullable = false, length = 90)
    private String brand;
    @Getter
    @Setter
    @NotEmpty(message = "{vehicle.model}")
    @Column(name = "model", nullable = false, length = 90)
    private String model;
    @Getter
    @Setter
    @NotEmpty(message = "{vehicle.license-plate}")
    @Column(name = "license_plate", nullable = false, length = 11)
    private String licensePlate;
    @Getter
    @Setter
    @Column(name = "model_year", length = 4)
    private int modelYear;
    @Getter
    @Setter
    @Column(name = "manufacturing_year", length = 4)
    private int manufacturingYear;
    @Getter
    @Setter
    @Column(name = "odometer", length = 11)
    private int odometer;
    @Getter
    @Setter
    @Column(name = "fuel_capacity", length = 4)
    private int fuelCapacity;
    @Getter
    @Setter
    @Column(name = "blocked")
    private boolean blocked;
    
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @NotNull(message = "{vehicle.vehicle-type}")
    @Column(name = "vehicle_type", nullable = false)
    private VehicleType vehicleType;
    
    @Getter
    @Setter
    @ManyToOne
    @NotNull(message = "{vehicle.cost-center}")
    @JoinColumn(name = "id_cost_center", nullable = false)
    private CostCenter costCenter;

    /**
     * 
     */
    public Vehicle() {
        final int year = LocalDate.now().get(ChronoField.YEAR);
        this.modelYear = year;
        this.manufacturingYear = year;
    }
}
