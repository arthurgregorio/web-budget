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
package br.com.webbudget.domain.model.entity.movement;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 03/04/2014
 */
public enum PaymentMethodType {

    IN_CASH("beans.payment-method-type.in-cash"),
    DEBIT_CARD("beans.payment-method-type.debit-card"),
    CREDIT_CARD("beans.payment-method-type.credit-card");
//    INSTALLMENT("beans.payment-method-type.installment");

    private final String i18nKey;

    /**
     *
     * @param i18nKey
     */
    private PaymentMethodType(String i18nKey) {
        this.i18nKey = i18nKey;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return this.i18nKey;
    }
}
