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
public final class LazyFilter {

    @Getter
    @Setter
    public String value;
    @Getter
    @Setter
    public EntityStatus entityStatus;

    /**
     * Construtor...
     */
    private LazyFilter() {
       this.entityStatus = EntityStatus.UNBLOCKED; 
    }
    
    /**
     * Inicializa um novo filtro
     * 
     * @return a instancia do filtro
     */
    public static LazyFilter initialize() {
        return new LazyFilter();
    }
    
    /**
     * Reseta o filtro para seu estado inicial
     */
    public void clear() {
        this.value = null;
        this.entityStatus = EntityStatus.UNBLOCKED;
    }

    /**
     * Pega o valor do status de entidade a ser buscado
     * 
     * @return o valor que queremos buscar para o status da entidade
     */
    public Boolean getEntityStatusValue() {
        return entityStatus.value();
    }
    
    /**
     * Enum de tipos de entidades
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
