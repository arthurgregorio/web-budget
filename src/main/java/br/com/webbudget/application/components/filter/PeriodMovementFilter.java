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
package br.com.webbudget.application.components.filter;

import br.com.webbudget.domain.entities.financial.PeriodMovementType;
import br.com.webbudget.domain.entities.financial.PeriodMovement;
import br.com.webbudget.domain.entities.financial.PeriodMovementState;
import lombok.*;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * DTO to use for filter the {@link PeriodMovement} list. This class hold the possible filter options and serve as a
 * place to retrieve some possible values to use as a filter
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 10/12/2018
 */
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public final class PeriodMovementFilter {

    @Setter
    @Getter
    private String value;

    @Setter
    @Getter
    private PeriodMovementType periodMovementType;
    @Setter
    @Getter
    private PeriodMovementState periodMovementState;

    /**
     * Clear this filter to default values
     */
    public void clear() {
        this.value = null;
        this.periodMovementType = null;
        this.periodMovementState = null;
    }

    /**
     * Method used to cast the text value of this filter to a {@link BigDecimal} value so this can be used as a filter
     *
     * @return an {@link Optional} of the value of the filter as {@link BigDecimal}
     */
    public Optional<BigDecimal> valueToBigDecimal() {
        try {
            return Optional.of(new BigDecimal(this.value));
        } catch (Exception ex) {
            return Optional.empty();
        }
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
}