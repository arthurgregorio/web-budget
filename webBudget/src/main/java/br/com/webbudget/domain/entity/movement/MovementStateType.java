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

package br.com.webbudget.domain.entity.movement;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 21/03/2014
 */
public enum MovementStateType {

    PAID("beans.movement-state-type.paid"), 
    OPEN("beans.movement-state-type.open"),
    CANCELED("beans.movement-state-type.canceled"),
    CALCULATED("beans.movement-state-type.calculated");
    
    private final String i18nKey;

    /**
     * 
     * @param i18nKey 
     */
    private MovementStateType(String i18nKey) {
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
