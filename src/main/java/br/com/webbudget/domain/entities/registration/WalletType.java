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
package br.com.webbudget.domain.entities.registration;

/**
 * The enum with the possible types of a {@link Wallet}
 *
 * @author Arthur Gregorio
 *
 * @version 1.1.0
 * @since 1.0.0, 03/04/2014
 */
public enum WalletType {

    PERSONAL("wallet-type.personal"),
    BANK_ACCOUNT("wallet-type.bank-account");

    private final String description;

    /**
     * Default constructor
     *
     * @param description the description for this enum, also is the i18n key
     */
    WalletType(String description) {
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
