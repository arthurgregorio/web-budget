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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * This class represents the movements used on each period or for a short time, all movements of this type can be
 * repeated across the periods turning into a {@link PeriodMovement}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.1.0, 18/09/2015
 */
@Entity
@Audited
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("FIXED_MOVEMENT") // never change this!
public class FixedMovement extends Movement {

    @Getter
    @Setter
    @Column(name = "quotes")
    private Integer quotes;
    @Getter
    @Setter
    @Column(name = "auto_launch")
    private boolean autoLaunch;
    @Getter
    @Setter
    @Column(name = "undetermined")
    private boolean undetermined;
    @Getter
    @Setter
    @Column(name = "start_date")
    @NotNull(message = "{fixed-movement.start-date}")
    private LocalDate startDate;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "movement_state")
    private MovementState movementState;

    @Getter
    @Setter
    @Transient
    private boolean alreadyLaunched;

    /**
     * Constructor...
     */
    public FixedMovement() {
        super();
        this.autoLaunch = true;
        this.movementState = MovementState.OPEN;
    }

    /**
     * To check if this fixed movement is finalized or not
     *
     * @return true if is, false if not
     */
    boolean isFinalized() {
        return this.movementState == MovementState.FINALIZED;
    }
}
