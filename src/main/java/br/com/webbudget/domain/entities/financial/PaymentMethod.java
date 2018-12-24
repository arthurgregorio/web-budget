/*
 * Copyright (C) 2014 Arthur Gregorio, AG.Software
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
 * The {@link Payment} method for a {@link Movement}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 03/04/2014
 */
public enum PaymentMethod {

    CASH("payment-method.cash"),
    DEBIT_CARD("payment-method.debit-card"),
    CREDIT_CARD("payment-method.credit-card");

    private final String description;

    /**
     * Constructor
     *
     * @param description the description and also the i18n key
     */
    PaymentMethod(String description) {
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
