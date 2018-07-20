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
package br.com.webbudget.domain.entities.tools;

/**
 * Enum to represent the themes of the application
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 18/07/2018
 */
public enum ThemeType {

    BLACK("themes.black"), BLACK_LIGHT("themes.black-light"),
    BLUE("themes.blue"), BLUE_LIGHT("themes.blue-light"),
    GREEN("themes.green"), GREEN_LIGHT("themes.green-light"),
    PURPLE("themes.purple"), PURPLE_LIGHT("themes.purple-light"),
    RED("themes.red"), RED_LIGHT("themes.red-light"),
    YELLOW("themes.yellow"), YELLOW_LIGHT("themes.yellow-light");

    private final String description;

    /**
     * Constructor
     *
     * @param description the description for this enum, also is the i18n key
     */
    ThemeType(String description) {
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
