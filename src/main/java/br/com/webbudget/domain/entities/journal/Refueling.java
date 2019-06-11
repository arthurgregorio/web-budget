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
import br.com.webbudget.domain.entities.financial.Movement;
import br.com.webbudget.domain.entities.financial.PeriodMovement;
import br.com.webbudget.domain.entities.registration.CostCenter;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.entities.registration.MovementClass;
import br.com.webbudget.domain.entities.registration.Vehicle;
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
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.webbudget.infrastructure.utils.DefaultSchemes.JOURNAL;
import static br.com.webbudget.infrastructure.utils.DefaultSchemes.JOURNAL_AUDIT;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.FetchType.EAGER;

/**
 * The representation of a {@link Vehicle} refueling
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.3.0, 27/06/2016
 */
@Entity
@Audited
@Table(name = "refuelings", schema = JOURNAL)
@ToString(callSuper = true, exclude = "fuels")
@EqualsAndHashCode(callSuper = true, exclude = "fuels")
@AuditTable(value = "refuelings", schema = JOURNAL_AUDIT)
public class Refueling extends PersistentEntity {

    @Getter
    @Column(name = "code", length = 6, unique = true)
    private String code;
    @Getter
    @Setter
    @Column(name = "accounted", nullable = false)
    private boolean accounted;
    @Getter
    @Setter
    @Column(name = "accounted_by")
    private String accountedBy;
    @Getter
    @Setter
    @Column(name = "first_refueling", nullable = false)
    private boolean firstRefueling;
    @Getter
    @Setter
    @Column(name = "full_tank", nullable = false)
    private boolean fullTank;
    @Getter
    @Setter
    @NotNull(message = "{refueling.odometer}")
    @Column(name = "odometer", nullable = false)
    private Long odometer;
    @Getter
    @Setter
    @Column(name = "distance", nullable = false)
    private Long distance;
    @Getter
    @Setter
    @Column(name = "average_consumption")
    private BigDecimal averageConsumption;
    @Getter
    @Setter
    @Column(name = "liters", nullable = false)
    private BigDecimal liters;
    @Getter
    @Setter
    @Column(name = "cost", nullable = false)
    private BigDecimal cost;
    @Getter
    @Setter
    @Column(name = "cost_per_liter", nullable = false)
    private BigDecimal costPerLiter;
    @Getter
    @Setter
    @Column(name = "place", length = 90)
    private String place;
    @Getter
    @Setter
    @NotNull(message = "{refueling.event-date}")
    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @Getter
    @Setter
    @OneToOne
    @JoinColumn(name = "id_period_movement")
    private PeriodMovement periodMovement;
    @Getter
    @Setter
    @ManyToOne(optional = false)
    @NotNull(message = "{refueling.vehicle}")
    @JoinColumn(name = "id_vehicle", nullable = false)
    private Vehicle vehicle;
    @Getter
    @Setter
    @ManyToOne(optional = false)
    @NotNull(message = "{refueling.movement-class}")
    @JoinColumn(name = "id_movement_class", nullable = false)
    private MovementClass movementClass;
    @Getter
    @Setter
    @ManyToOne(optional = false)
    @NotNull(message = "{refueling.financial-period}")
    @JoinColumn(name = "id_financial_period", nullable = false)
    private FinancialPeriod financialPeriod;

    @OneToMany(mappedBy = "refueling", orphanRemoval = true, fetch = EAGER, cascade = {PERSIST, REMOVE})
    private List<Fuel> fuels;

    /**
     * Default constructor
     */
    public Refueling() {

        this.code = RandomCode.alphanumeric(6);

        this.fullTank = true;
        this.accounted = false;

        this.eventDate = LocalDate.now();

        this.cost = BigDecimal.ZERO;
        this.liters = BigDecimal.ZERO;
        this.costPerLiter = BigDecimal.ZERO;

        this.fuels = new ArrayList<>();
    }

    /**
     * Get a unmodifiable list of {@link Fuel}
     *
     * @return unmodifiable list of {@link Fuel}
     */
    public List<Fuel> getFuels() {
        return Collections.unmodifiableList(this.fuels);
    }

    /**
     * Add a {@link Fuel} to the list
     */
    public void addFuel() {
        this.fuels.add(new Fuel(this));
        this.totalsFuels();
    }

    /**
     * Delete a {@link Fuel} from the {@link Fuel} list
     *
     * @param fuel to be deleted
     */
    public void deleteFuel(Fuel fuel) {
        this.fuels.remove(fuel);
        this.totalsFuels();
    }

    /**
     * Get the {@link Vehicle} identification
     *
     * @return the {@link Vehicle} identification
     */
    public String getVehicleIdentification() {
        return this.vehicle.getIdentification();
    }

    /**
     * Get the {@link Vehicle} {@link CostCenter}
     *
     * @return the {@link Vehicle} {@link CostCenter}
     */
    public CostCenter getCostCenter() {
        return this.getVehicle().getCostCenter();
    }

    /**
     * This is a helper method to check if the {@link Fuel} is ok the there are at least one informed
     *
     * @return true or false for a valid or invalid list of {@link Fuel}
     */
    public boolean isFuelsValid() {
        return this.fuels.stream()
                .filter(Fuel::isInvalid)
                .collect(Collectors.toList())
                .isEmpty();
    }

    /**
     * Totals the values of the fuels based on the total in liters multiplied by the cost by liter
     */
    public void totalsFuels() {

        // calculate the total cost
        this.cost = this.fuels
                .stream()
                .map(Fuel::getCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // calculate the total of liters stocked
        this.liters = this.fuels
                .stream()
                .map(Fuel::getLiters)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // calculate the cost per liter
        if (!this.cost.equals(BigDecimal.ZERO) && !this.liters.equals(BigDecimal.ZERO)) {
            this.costPerLiter = this.cost.divide(this.liters, RoundingMode.CEILING);
        } else {
            this.costPerLiter = BigDecimal.ZERO;
        }
    }

    /**
     * Same as the {@link #calculateAverageConsumption(long, BigDecimal)} but this method use the internal values of
     * this {@link Refueling}
     */
    public void calculateAverageConsumption() {
        this.calculateAverageConsumption(this.distance, this.liters);
    }

    /**
     * Use this method to calculate the average consumption of the fuel using the total travelled distance
     *
     * @param totalDistance the total distance travelled
     * @param liters total of fuel liters spent
     */
    public void calculateAverageConsumption(long totalDistance, BigDecimal liters) {
        if (!this.firstRefueling && totalDistance > 0) {
            this.averageConsumption = new BigDecimal(totalDistance / liters.doubleValue());
        }
    }

    /**
     * This method build a simples {@link Movement} description based on the {@link Refueling} content
     *
     * @return the {@link Movement} description
     */
    public String getMovementDescription() {
        return this.vehicle.getIdentification() + " - " + this.movementClass.getName() + ",  "
                + NumberFormat.getNumberInstance().format(this.liters) + "lts";
    }

    /**
     * Use this method to update the vehicle odometer with the value of this refueling
     */
    public void updateVehicleOdometer() {
        this.vehicle.setOdometer(this.odometer);
    }

    /**
     * Calculate the distance from the current odometer and the last one
     *
     * @param lastOdometer the last odometer registered
     */
    public void calculateDistance(long lastOdometer) {
        this.distance = this.firstRefueling ? 0 : this.odometer - lastOdometer;
    }
}