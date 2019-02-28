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
package br.com.webbudget.domain.entities.financial;

/**
 * The enum with the possible reasons for a {@link WalletBalance} update transaction
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 19/08/2018
 */
public enum ReasonType {

    RETURN("reason-type.return"),
    REVENUE("reason-type.revenue"),
    EXPENSE("reason-type.expense"),
    ADJUSTMENT("reason-type.adjustment"),
    DEBIT_CARD("reason-type.debit-card"),
    TRANSFERENCE("reason-type.transference"),;

    private final String description;

    /**
     * Default constructor
     *
     * @param description the description for this enum, also is the i18n key
     */
    ReasonType(String description) {
        this.description = description;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public String toString() {
        return this.description;
    }
}
