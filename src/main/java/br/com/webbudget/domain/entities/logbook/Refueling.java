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

import br.com.webbudget.infrastructure.utils.RandomCode;
import br.com.webbudget.domain.entities.PersistentEntity;
import br.com.webbudget.domain.entities.registration.CostCenter;
import br.com.webbudget.domain.entities.registration.MovementClass;
import br.com.webbudget.domain.entities.miscellany.FinancialPeriod;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static javax.persistence.CascadeType.REMOVE;
import javax.persistence.Column;
import javax.persistence.Entity;
import static javax.persistence.FetchType.EAGER;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
    @Min(value = 1, message = "{refueling.odometer}")
    @Column(name = "odometer", nullable = false)
    private int odometer;
    @Getter
    @Setter
    @Column(name = "distance", nullable = false)
    private int distance;
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

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "refueling", fetch = EAGER, cascade = REMOVE)
    private List<Fuel> fuels;
    
    /**
     *
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
     * @return uma lista nao modificavel dos combustiveis
     */
    public List<Fuel> getFuels() {
        return Collections.unmodifiableList(this.fuels);
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
                    .orElse(null);
            valid = (fuel == null);
        }
        return valid;
    }

    /**
     * Totaliza os valores referentes aos combustiveis
     */
    public void totalize() {
        
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
        } else {
            this.costPerLiter = BigDecimal.ZERO;
        }
    }

    /**
     * Metodo utilizado para calcular a media de consumo baseado na distancia
     * do abastecimento atual
     */
    public void calculateAverageComsumption() {
        this.calculateAverageComsumption(this.distance, this.liters);
    }
    
    /**
     * Metodo utilizado para calcular a media de consumo ate o abastecimento 
     * quando temos abastecimentos anteriores em estado parcial, sem media
     * 
     * @param totalDistance o total de distancia percorrida
     * @param liters a quantidade total de litros
     */
    public void calculateAverageComsumption(int totalDistance, BigDecimal liters) {
        if (!this.firstRefueling && totalDistance > 0) {
            this.averageConsumption = 
                    new BigDecimal(totalDistance / liters.doubleValue());
        }
    }

    /**
     * @return a descricao para o movimento
     */
    public String createMovementDescription() {
        
        final StringBuilder builder = new StringBuilder();
        
        builder.append(this.vehicle.getIdentification());
        builder.append(" - ");
        builder.append(this.movementClass.getName());
        builder.append(",  ");
        builder.append(NumberFormat.getNumberInstance().format(this.liters));
        builder.append("lts");
        
        return builder.toString();
    }

    /**
     * Atualiza o odometro do veiculo vinculado de acordo com o odometro atual
     */
    public void updateVehicleOdometer() {
        this.vehicle.setOdometer(this.odometer);
    }

    /**
     * Calcula a distancia percorrida pelo ultimo odometro infomado
     * 
     * @param lastOdometer o ultimo odometro registrado por um abastecimento
     */
    public void calculateDistance(int lastOdometer) {
        this.distance = this.firstRefueling ? 0 : this.odometer - lastOdometer;
    }
}
