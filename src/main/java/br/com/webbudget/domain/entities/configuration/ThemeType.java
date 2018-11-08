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
package br.com.webbudget.domain.entities.configuration;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * Enum to represent the themes of the application
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 18/07/2018
 */
public enum ThemeType {

    BLACK("theme-type.black", "skin-black"),
    BLUE("theme-type.blue", "skin-blue"),
    GREEN("theme-type.green", "skin-green"),
    PURPLE("theme-type.purple", "skin-purple"),
    RED("theme-type.red", "skin-red"),
    YELLOW("theme-type.yellow", "skin-yellow");

    @Getter
    private final String value;
    private final String description;

    /**
     * Constructor
     *
     * @param description the description for this enum, also is the i18n key
     * @param value the value of this enum type
     */
    ThemeType(String description, String value) {
        this.value = value;
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

    /**
     * Get only the color name of the skin
     *
     * @return the color name
     */
    public String getColorName() {
        return this.value.replace("skin-", "");
    }

    /**
     * This method is used to parse a theme from his value to his type
     *
     * @param value the value of the theme
     * @return the type of the theme
     */
    public static ThemeType parseFromValue(String value) {

        final List<ThemeType> themes = Arrays.asList(ThemeType.values());

        return themes.stream()
                .filter(theme -> theme.matchValue(value))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Can't parse value ["+ value +"] for ThemeType enum"));
    }

    /**
     * Helper method to check if the value matches the instance value
     *
     * @param value the value to be tested
     * @return if the value matches or not
     */
    private boolean matchValue(String value) {
        return this.value.equals(value);
    }
}
