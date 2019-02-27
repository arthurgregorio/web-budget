/*
 * Copyright (C) 2013 Arthur Gregorio, AG.Software
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
package br.com.webbudget.domain.entities;

import java.io.Serializable;

/**
 * The basic entity definition for all the classes in the application
 *
 * @param <T> the type of the ID for this persistent entity
 *
 * @author Arthur Gregorio
 *
 * @version 4.1.0
 * @since 1.0.0, 06/10/2013
 */
public interface IPersistentEntity<T extends Serializable> {

    /**
     * Get the ID for this entity
     *
     * @return value of the ID field
     */
    T getId();

    /**
     * If this entity is saved or not checking by the presence of the ID value
     *
     * @return true if is saved, false otherwise
     */
    boolean isSaved();

    /**
     * Sames as {@link #isSaved()} but in this case inverted for convenience use in lambda expressions
     *
     * @return true if not saved, false otherwise
     */
    default boolean isNotSaved() {
        return !this.isSaved();
    }
}
