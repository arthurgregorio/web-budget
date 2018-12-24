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
package br.com.webbudget.domain.validators;

import br.com.webbudget.domain.entities.PersistentEntity;

/**
 * This interface represents an abstraction of the business validators running inside the services. With this kind of
 * approach, we almost remove all the coupling with the services and some business rules
 *
 * @param <T> the type to be manipulated by the validator
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 09/08/2018
 */
public interface BusinessLogic<T extends PersistentEntity> {

    /**
     * Call this method to run the validation process defined in one of the implementations of this interface
     *
     * @param value the value to work with
     */
    void run(T value);
}
