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
package br.com.webbudget.application.components.ui.filter;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * An generic implementation of the {@link BasicFilter} to be used with a basic CRUD in the application
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 20/03/2018
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class LazyFilter extends BasicFilter {

    @Getter
    @Setter
    public EntityStatus entityStatus;

    /**
     * Private constructor to prevent misuse 
     */
    private LazyFilter() {
        this.entityStatus = EntityStatus.ACTIVE;
    }

    /**
     * This replace the default constructor to build instances of this filter
     *
     * @return a instance of this filter
     */
    public static LazyFilter getInstance() {
        return new LazyFilter();
    }

    /**
     * Clear the filter internal parameters
     */
    public void clear() {
        this.value = null;
        this.entityStatus = EntityStatus.ACTIVE;
    }

    /**
     * The status value, if the entity to be queried is blocked, unblocked or
     * if all entities will returned
     *
     * @return the status value
     */
    public Boolean getEntityStatusValue() {
        return this.entityStatus.value();
    }

    /**
     * @return the values to be used on the selection box of the status
     */
    public EntityStatus[] getEntityStatusValues() {
        return EntityStatus.values();
    }

    /**
     * The enum representation of the possible entity status
     */
    public enum EntityStatus {

        ALL("entity-status.all", null),
        ACTIVE("entity-status.active", Boolean.TRUE),
        INACTIVE("entity-status.inactive", Boolean.FALSE);

        private final Boolean value;
        private final String description;

        /**
         * Constructor...
         *
         * @param description the i18n description
         * @param value the value
         */
        EntityStatus(String description, Boolean value) {
            this.value = value;
            this.description = description;
        }

        /**
         * {@inheritDoc }
         *
         * @return
         */
        @Override
        public String toString() {
            return this.description;
        }

        /**
         * @return the value of the current enum instance
         */
        public Boolean value() {
            return this.value;
        }
    }
}