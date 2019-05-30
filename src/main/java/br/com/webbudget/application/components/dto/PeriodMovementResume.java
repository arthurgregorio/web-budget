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

/**
 * This DTO is used in the listing of {@link PeriodMovement} to create a summary of the current movements in the list
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
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
     * Update this resume
     *
     * @param totalOpen represents the amount open on selected periods
     * @param totalPaidReceived represents the amount paid or received on selected periods
     * @param totalRevenues represents the revenues of the period
     * @param totalExpenses represents the expenses of the period
     */
    public void update(BigDecimal totalPaidReceived, BigDecimal totalOpen, BigDecimal totalRevenues, BigDecimal totalExpenses) {
        this.totalOpen = totalOpen;
        this.totalPaid = totalPaidReceived;
        this.totalRevenue = totalRevenues;
        this.totalExpense = totalExpenses;
    }
}
