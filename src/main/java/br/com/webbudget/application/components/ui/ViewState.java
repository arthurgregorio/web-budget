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
package br.com.webbudget.application.components.ui;

/**
 * This enum represents the possible view states to control the views/forms
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 26/03/2018
 */
public enum ViewState {
    
    LISTING,
    ADDING,
    EDITING,
    DELETING,
    DETAILING;

    /**
     * To check if in the current state is possible to edit values
     *
     * @return true if is, false otherwise
     */
    public boolean isEditable() {
        return this == ADDING || this == EDITING;
    }

    /**
     * To check if the state is EDITING
     *
     * @return true if is, false otherwise
     */
    public boolean isEditing() {
        return this == EDITING;
    }

    /**
     * To check if the state is ADDING
     *
     * @return true if is, false otherwise
     */
    public boolean isAdding() {
        return this == ADDING;
    }

    /**
     * To check if the state is DETAILING
     *
     * @return true if is, false otherwise
     */
    public boolean isDetailing() {
        return this == DETAILING;
    }

    /**
     * To check if the state is DELETING
     *
     * @return true if is, false otherwise
     */
    public boolean isDeleting() {
        return this == DELETING;
    }
}
