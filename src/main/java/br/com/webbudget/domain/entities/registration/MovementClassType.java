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
 * The enum with the possible types of a {@link MovementClass}
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 1.0.0, 13/03/2014
 */
public enum MovementClassType {

    REVENUE("movement-class-type.revenue"),
    EXPENSE("movement-class-type.expense");

    private final String description;

    /**
     * Constructor
     *
     * @param description the description for this enum, also is the i18n key
     */
    MovementClassType(String description) {
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
