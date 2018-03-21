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
package br.com.webbudget.application.components.table;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 20/03/2018
 */
public class LazyFilter {

    @Getter
    @Setter
    public String value;
    @Getter
    @Setter
    public EntityStatus entityStatus;

    /**
     * 
     */
    private LazyFilter() {
       this.entityStatus = EntityStatus.UNBLOCKED; 
    }
    
    /**
     * 
     * @return 
     */
    public static LazyFilter initialize() {
        return new LazyFilter();
    }

    /**
     * 
     * @return 
     */
    public Boolean getEntityStatusValue() {
        return entityStatus.value();
    }
    
    /**
     * 
     */
    public enum EntityStatus {

        ALL("entity-status.all", null),
        BLOCKED("entity-status.blocked", Boolean.TRUE),
        UNBLOCKED("entity-stats.unblocked", Boolean.FALSE);

        private final Boolean value;
        private final String description;

        /**
         *
         * @param description
         * @param value
         */
        private EntityStatus(String description, Boolean value) {
            this.value = value;
            this.description = description;
        }

        /**
         *
         * @return
         */
        @Override
        public String toString() {
            return this.description;
        }

        /**
         *
         * @return
         */
        public Boolean value() {
            return this.value;
        }
    }
}
