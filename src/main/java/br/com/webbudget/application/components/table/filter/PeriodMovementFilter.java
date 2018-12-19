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
package br.com.webbudget.application.components.table.filter;

import br.com.webbudget.domain.entities.financial.MovementState;
import br.com.webbudget.domain.entities.financial.MovementType;
import br.com.webbudget.domain.entities.registration.Contact;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Helper class to
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 10/12/2018
 */
@ToString
public final class PeriodMovementFilter {

    @Setter
    @Getter
    private String text;

    @Setter
    @Getter
    private MovementType movementType;
    @Setter
    @Getter
    private MovementState movementState;

    @Setter
    @Getter
    private Contact contact;

    private List<FinancialPeriod> financialPeriods;

    /**
     * Constructor...
     */
    private PeriodMovementFilter() {
        this.financialPeriods = new ArrayList<>();
    }

    /**
     * To get a instance for this filter
     *
     * @return the instance to be used
     */
    public static PeriodMovementFilter getInstance() {
        return new PeriodMovementFilter();
    }

    /**
     * Get the list of {@link FinancialPeriod}
     *
     * @return an unmodifiable version of the {@link FinancialPeriod} list
     */
    public List<FinancialPeriod> getFinancialPeriods() {
        return Collections.unmodifiableList(this.financialPeriods);
    }

    /**
     * Add a new financial period to the list o periods to text
     *
     * @param financialPeriod the {@link FinancialPeriod} to search
     */
    public void add(FinancialPeriod financialPeriod) {
        this.financialPeriods.add(financialPeriod);
    }
}
