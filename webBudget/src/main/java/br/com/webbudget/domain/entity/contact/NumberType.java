/*
 * Copyright (C) 2015 Arthur
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
package br.com.webbudget.domain.entity.contact;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 19/04/2015
 */
public enum NumberType {

    OTHER("beans.number-type.other"),
    FIXED("beans.number-type.fixed"),
    MOBILE("beans.number-type.mobile"),
    COMMERCIAL("beans.number-type.commercial");

    private final String i18nKey;

    /**
     *
     * @param i18nKey
     */
    private NumberType(String i18nKey) {
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
