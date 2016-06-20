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
import br.com.webbudget.domain.model.entity.entries.MovementClass;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Convert;
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
import org.hibernate.validator.constraints.NotBlank;

/**
 * Representacao das ocorrencias de um registro do logbook
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.3.0, 09/05/2016
 */
@Entity
@Table(name = "entries")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Entry extends PersistentEntity {

    @Getter
    @Setter
    @NotBlank(message = "{entry.title}")
    @Column(name = "title", nullable = false)
    private String title;
    @Getter
    @Setter
    @Column(name = "odometer")
    private Integer odometer;
    @Getter
    @Setter
    @Column(name = "cost")
    private BigDecimal cost;
    @Getter
    @Setter
    @Column(name = "place")
    private String place;
    @Getter
    @Setter
    @Column(name = "description")
    private String description;
    @Getter
    @Setter
    @NotNull(message = "{entry.event-date}")
    @Column(name = "event_date", nullable = false)
    @Convert(converter = JPALocalDateConverter.class)
    private LocalDate eventDate;
    @Getter
    @Setter
    @Column(name = "financial", nullable = false)
    private boolean financial;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @NotNull(message = "{entry.entry-type}")
    @Column(name = "entry_type", nullable = false)
    private EntryType entryType;

    @Getter
    @Setter
    @ManyToOne
    @NotNull(message = "{entry.vehicle}")
    @JoinColumn(name = "id_vehicle", nullable = false)
    private Vehicle vehicle;
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_movement_class")
    private MovementClass movementClass;

    /**
     *
     */
    public Entry() {
        this.financial = false;
        this.eventDate = LocalDate.now();
        this.entryType = EntryType.MAINTENANCES;
    }

    /**
     *
     * @param entryType
     * @param vehicle
     */
    public Entry(EntryType entryType, Vehicle vehicle) {
        this();
        this.vehicle = vehicle;
        this.entryType = entryType;
    }

    /**
     * @return a identificacao do veiculo vinculado ao registro
     */
    public String getVehicleIdentification() {
        return this.vehicle.getIdentification();
    }
}
