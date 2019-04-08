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
package br.com.webbudget.domain.entities.registration;

import br.com.webbudget.domain.entities.PersistentEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.temporal.ChronoField;

import static br.com.webbudget.infrastructure.utils.DefaultSchemes.REGISTRATION;
import static br.com.webbudget.infrastructure.utils.DefaultSchemes.REGISTRATION_AUDIT;

/**
 * The representation of a vehicle in the application
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.3.0, 16/05/2016
 */
@Entity
@Audited
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "vehicles", schema = REGISTRATION)
@AuditTable(value = "vehicles", schema = REGISTRATION_AUDIT)
public class Vehicle extends PersistentEntity {

    @Getter
    @Setter
    @NotBlank(message = "{vehicle.identification}")
    @Column(name = "identification", nullable = false, length = 90)
    private String identification;
    @Getter
    @Setter
    @NotBlank(message = "{vehicle.brand}")
    @Column(name = "brand", nullable = false, length = 90)
    private String brand;
    @Getter
    @Setter
    @NotBlank(message = "{vehicle.model}")
    @Column(name = "model", nullable = false, length = 90)
    private String model;
    @Getter
    @Setter
    @NotBlank(message = "{vehicle.license-plate}")
    @Column(name = "license_plate", nullable = false, length = 11)
    private String licensePlate;
    @Getter
    @Setter
    @Column(name = "model_year", length = 4)
    private Integer modelYear;
    @Getter
    @Setter
    @Column(name = "manufacturing_year", length = 4)
    private Integer manufacturingYear;
    @Getter
    @Setter
    @NotNull(message = "{vehicle.odometer}")
    @Column(name = "odometer", length = 11, nullable = false)
    private Long odometer;
    @Getter
    @Setter
    @NotNull(message = "{vehicle.fuel-capacity}")
    @Column(name = "fuel_capacity", length = 4, nullable = false)
    private Integer fuelCapacity;
    @Getter
    @Setter
    @Column(name = "active", nullable = false)
    private boolean active;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @NotNull(message = "{vehicle.vehicle-type}")
    @Column(name = "vehicle_type", nullable = false, length = 45)
    private VehicleType vehicleType;

    @Getter
    @Setter
    @ManyToOne
    @NotNull(message = "{vehicle.cost-center}")
    @JoinColumn(name = "id_cost_center", nullable = false)
    private CostCenter costCenter;

    /**
     * Default constructor
     */
    public Vehicle() {
        this.active = true;
        this.odometer = 0L;

        final int year = LocalDate.now().get(ChronoField.YEAR);

        this.modelYear = year;
        this.manufacturingYear = year;
    }
}
