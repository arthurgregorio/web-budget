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
 * The type of a {@link Movement}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 15/05/2014
 */
public enum MovementType {

    MOVEMENT("movement-type.movement"),
    CARD_INVOICE("movement-type.card-invoice");

    private final String description;

    /**
     * Constructor
     *
     * @param description the description and also the i18n key
     */
    MovementType(String description) {
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
