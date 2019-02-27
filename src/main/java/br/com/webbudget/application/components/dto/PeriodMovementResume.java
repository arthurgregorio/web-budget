/*
 * Copyright (C) 2019 Arthur Gregorio, AG.Software
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
package br.com.webbudget.application.components.dto;

import br.com.webbudget.domain.entities.financial.PeriodMovement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * This DTO is used in the listing of {@link PeriodMovement} to create a summary of the current movements in the list
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 18/02/2019
 */
@ToString
@EqualsAndHashCode
public final class PeriodMovementResume implements Serializable {

    @Getter
    private BigDecimal totalOpen;
    @Getter
    private BigDecimal totalPaid;
    @Getter
    private BigDecimal totalRevenue;
    @Getter
    private BigDecimal totalExpense;

    /**
     * Constructor...
     */
    public PeriodMovementResume() {
        this.totalOpen = BigDecimal.ZERO;
        this.totalPaid = BigDecimal.ZERO;
        this.totalRevenue = BigDecimal.ZERO;
        this.totalExpense = BigDecimal.ZERO;
    }

    /**
     * Constructor...
     *
     * Internally this constructor call the {@link #update(List)} method
     *
     * @param periodMovements the list of {@link PeriodMovement} to use for calculate the values
     */
    public PeriodMovementResume(List<PeriodMovement> periodMovements) {
        this.update(periodMovements);
    }

    /**
     * Sum the values and update this value holder
     *
     * @param periodMovements the list of {@link PeriodMovement} to update the model
     */
    public void update(List<PeriodMovement> periodMovements) {

        this.totalOpen = periodMovements.stream()
                .filter(PeriodMovement::isOpen)
                .map(PeriodMovement::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalPaid = periodMovements.stream()
                .filter(PeriodMovement::isPaid)
                .map(PeriodMovement::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalRevenue = periodMovements.stream()
                .filter(PeriodMovement::isRevenue)
                .map(PeriodMovement::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalExpense = periodMovements.stream()
                .filter(PeriodMovement::isExpense)
                .map(PeriodMovement::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
