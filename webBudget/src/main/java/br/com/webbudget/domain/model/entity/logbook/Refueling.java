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

import br.com.webbudget.application.converter.JPALocalDateConverter;
import br.com.webbudget.domain.model.entity.PersistentEntity;
import br.com.webbudget.domain.model.entity.entries.CostCenter;
import br.com.webbudget.domain.model.entity.entries.MovementClass;
import br.com.webbudget.domain.model.entity.miscellany.FinancialPeriod;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Classe que representa o abastecimento de um veiculo
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.3.0, 27/06/2016
 */
@Entity
@Table(name = "refuelings")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Refueling extends PersistentEntity {

    @Getter
    @Setter
    @Column(name = "full_tank", nullable = false)
    private boolean fullTank;
    @Getter
    @Setter
    @Min(value = 1, message = "{refueling.odometer}")
    @Column(name = "odometer", nullable = false)
    private int odometer;
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
    @Column(name = "cost_per_litre", nullable = false)
    private BigDecimal costPerLiter;
    @Getter
    @Setter
    @Column(name = "place", length = 90)
    private String place;
    @Getter
    @Setter
    @NotNull(message = "{refueling.event-date}")
    @Column(name = "event_date", nullable = false)
    @Convert(converter = JPALocalDateConverter.class)
    private LocalDate eventDate;
    @Getter
    @Setter
    @Column(name = "movement_code", length = 6)
    private String movementCode;

    @Getter
    @Setter
    @ManyToOne
    @NotNull(message = "{refueling.vehicle}")
    @JoinColumn(name = "id_vehicle", nullable = false)
    private Vehicle vehicle;
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_movement_class")
    @NotNull(message = "{refueling.movement-class}")
    private MovementClass movementClass;
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_financial_period")
    @NotNull(message = "{refueling.financial-period}")
    private FinancialPeriod financialPeriod;

    @Getter
    @Setter
    @Transient
    @NotEmpty(message = "{refueling.fuels}")
    private List<Fuel> fuels;

    /**
     *
     */
    public Refueling() {
        
        this.fullTank = true;
        
        this.eventDate = LocalDate.now();
        
        this.cost = BigDecimal.ZERO;
        this.liters = BigDecimal.ZERO;
        this.costPerLiter = BigDecimal.ZERO;
        
        this.fuels = new ArrayList<>();
    }

    /**
     *
     * @param vehicle
     */
    public Refueling(Vehicle vehicle) {
        this();
        this.vehicle = vehicle;
    }

    /**
     * Adiciona um novo combustivel a este abastecimento
     */
    public void addFuel() {
        this.fuels.add(new Fuel());
        this.totalize();
    }

    /**
     * Deleta um combustivel da lista de combustiveis
     *
     * @param fuel o combustivel
     */
    public void deleteFuel(Fuel fuel) {
        this.fuels.remove(fuel);
        this.totalize();
    }

    /**
     * @return a identificacao do veiculo vinculado ao registro
     */
    public String getVehicleIdentification() {
        return this.vehicle.getIdentification();
    }

    /**
     * @return se temos ou nao uma entrada financeira valida
     */
    public boolean isFinancialValid() {
        return this.movementClass != null && this.getCost() != null;
    }

    /**
     * @return o centro de custo do veiculo vinculado ao registro
     */
    public CostCenter getCostCenter() {
        return this.getVehicle().getCostCenter();
    }
    
    /**
     * @return se temos ou nao combustiveis
     */
    public boolean isFuelsValid() {
        
        boolean valid = this.fuels != null && !this.fuels.isEmpty();
        
        if (valid) {
            final Fuel fuel = this.fuels
                    .stream()
                    .filter(Fuel::isInvalid)
                    .findAny()
                    .get();
            valid = (fuel == null);
        }
        return valid;
    }

    /**
     * Totaliza os valores referentes aos combustiveis
     */
    private void totalize() {
        
        // calcula o total em reais 
        this.cost = this.fuels
                .stream()
                .map(Fuel::getCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // calcula o total em litros abastecido
        this.liters = this.fuels
                .stream()
                .map(Fuel::getLiters)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // calcula o custo por litro
        if (this.cost != BigDecimal.ZERO && this.liters != BigDecimal.ZERO) {
            this.costPerLiter = this.cost.divide(this.liters, RoundingMode.CEILING);
        }
    }
}
