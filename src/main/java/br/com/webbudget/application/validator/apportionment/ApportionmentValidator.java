/*
 * Copyright (C) 2019 Arthur Gregorio, AG.Software
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
package br.com.webbudget.application.validator.apportionment;

import br.com.webbudget.domain.entities.financial.Apportionment;
import br.com.webbudget.domain.entities.financial.Movement;
import br.com.webbudget.domain.exceptions.BusinessLogicException;

/**
 * Validation interface to provide a contract to all {@link Apportionment} validators
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 06/01/2019
 */
public interface ApportionmentValidator {

    /**
     * Validate a single apportionment
     *
     * This validator should be used before insert the {@link Apportionment} in the {@link Movement}
     *
     * @param apportionment the {@link Apportionment} to be validated
     * @param movement the {@link Movement} to extract some values used to validate the {@link Apportionment}
     * @throws BusinessLogicException if any problem is found
     */
    void validate(Apportionment apportionment, Movement movement);
}
