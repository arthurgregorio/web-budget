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
package br.com.webbudget.application.components.filter;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 05/03/2018
 */
public enum StatusFilterType {

    ALL("status-filter-type.all", null),
    ACTIVE("status-filter-type.active", Boolean.TRUE), 
    INACTIVE("status-filter-type.inactive", Boolean.FALSE);
    
    private final Boolean value;
    private final String i18nKey;

    /**
     * 
     * @param i18nKey
     * @param value 
     */
    private StatusFilterType(String i18nKey, Boolean value) {
        this.value = value;
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

    /**
     * 
     * @return 
     */
    public Boolean value() {
        return this.value;
    }
}
