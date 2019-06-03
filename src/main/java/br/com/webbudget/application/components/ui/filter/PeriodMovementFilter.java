/*
 * Copyright (C) 2018 Arthur Gregorio, AG.Software
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
package br.com.webbudget.application.components.ui.filter;

import br.com.webbudget.domain.entities.financial.PeriodMovement;
import br.com.webbudget.domain.entities.financial.PeriodMovementState;
import br.com.webbudget.domain.entities.financial.PeriodMovementType;
import br.com.webbudget.domain.entities.registration.CostCenter;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.entities.registration.MovementClass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A specific implementation of the {@link BasicFilter} to be used at the {@link PeriodMovement} controller
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 10/12/2018
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class PeriodMovementFilter extends BasicFilter {

    @Setter
    @Getter
    private CostCenter costCenter;
    @Setter
    @Getter
    private MovementClass movementClass;
    @Setter
    @Getter
    private PeriodMovementType periodMovementType;
    @Setter
    @Getter
    private PeriodMovementState periodMovementState;

    @Setter
    private List<FinancialPeriod> selectedFinancialPeriods;

    /**
     * Constructor...
     */
    public PeriodMovementFilter() {
        this.selectedFinancialPeriods = new ArrayList<>();
    }

    /**
     * Clear this filter to default values
     */
    public void clear() {
        this.value = null;
        this.costCenter = null;
        this.movementClass = null;
        this.periodMovementType = null;
        this.periodMovementState = null;
        this.selectedFinancialPeriods = new ArrayList<>();
    }

    /**
     * Method used to cast the text value of this filter to a {@link BigDecimal} value so this can be used as a filter
     *
     * @return an {@link Optional} of the value of the filter as {@link BigDecimal}
     */
    public Optional<BigDecimal> valueToBigDecimal() {
        try {
            return Optional.of(new BigDecimal(this.value.replace(",", ".")));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    /**
     * Getter for the {@link FinancialPeriod} selected
     *
     * @return a {@link List} with the selected {@link FinancialPeriod}
     */
    public List<FinancialPeriod> getSelectedFinancialPeriods() {
        return this.selectedFinancialPeriods == null ? List.of() : this.selectedFinancialPeriods;
    }

    /**
     * All possible values to filter a {@link PeriodMovement} by the {@link PeriodMovementType}
     *
     * @return the list of {@link PeriodMovementType}
     */
    public PeriodMovementType[] getMovementTypes() {
        return PeriodMovementType.values();
    }

    /**
     * All possible values to filter a {@link PeriodMovement} by the {@link PeriodMovementState}
     *
     * @return the list of {@link PeriodMovementState}
     */
    public PeriodMovementState[] getPeriodMovementStates() {
        return PeriodMovementState.values();
    }

    /**
     * Transform the selected {@link FinancialPeriod} into an array of {@link String} to be used on the query
     *
     * @return the selected {@link FinancialPeriod} as {@link String} array
     */
    public String[] getSelectedFinancialPeriodsAsStringArray() {
        return this.selectedFinancialPeriods.stream()
                .map(FinancialPeriod::getIdentification)
                .collect(Collectors.toList())
                .toArray(String[]::new);
    }
}