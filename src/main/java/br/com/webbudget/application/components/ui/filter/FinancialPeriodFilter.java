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

import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import lombok.*;

/**
 * A specific implementation of the {@link BasicFilter} to be used with the {@link FinancialPeriod} controller
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 04/04/2019
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FinancialPeriodFilter extends BasicFilter {

    @Getter
    @Setter
    private Boolean closed;

    /**
     * Constructor...
     */
    public FinancialPeriodFilter() {
        this.closed = false;
    }
}
