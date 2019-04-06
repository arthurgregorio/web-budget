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
package br.com.webbudget.application.components.ui.filter;

import br.com.webbudget.domain.entities.financial.FixedMovement;
import br.com.webbudget.domain.entities.financial.FixedMovementState;
import lombok.*;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * DTO used to transport the filtering data for the listing of {@link FixedMovement} through the view to the backend
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 22/03/2019
 */
@ToString
@EqualsAndHashCode
public final class FixedMovementFilter {

    @Setter
    @Getter
    private String value;

    @Setter
    @Getter
    private FixedMovementState fixedMovementState;

    /**
     * Constructor...
     */
    public FixedMovementFilter() {
        this.clear();
    }

    /**
     * Clear this filter to default values
     */
    public void clear() {
        this.value = null;
        this.fixedMovementState = FixedMovementState.ACTIVE;
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
     * All possible values to filter a {@link FixedMovement} by the {@link FixedMovementState}
     *
     * @return the list of {@link FixedMovementState}
     */
    public FixedMovementState[] getFixedMovementStates() {
        return FixedMovementState.values();
    }
}