/*
 * Copyright (C) 2016 Arthur Gregorio, AG.Software
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
package br.com.webbudget.domain.entities.journal;

/**
 * The possible types of {@link Fuel}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.3.0, 24/07/2016
 */
public enum FuelType {

    DIESEL("fuel-type.diesel"),
    ETHANOL("fuel-type.ethanol"),
    GASOLINE("fuel-type.gasoline");
    
    private final String description;

    /**
     * Constructor...
     *
     * @param description the description and also the i18n key
     */
    FuelType(String description) {
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
