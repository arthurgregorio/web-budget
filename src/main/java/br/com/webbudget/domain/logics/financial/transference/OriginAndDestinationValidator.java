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
package br.com.webbudget.domain.logics.financial.transference;

import br.com.webbudget.domain.entities.financial.Transference;
import br.com.webbudget.domain.entities.registration.Wallet;
import br.com.webbudget.domain.exceptions.BusinessLogicException;

import javax.enterprise.context.Dependent;

/**
 * The validator for the {@link Wallet} origin and destination inside the {@link Transference}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 06/10/2018
 */
@Dependent
public class OriginAndDestinationValidator implements TransferenceSavingLogic {

    /**
     * {@inheritDoc}
     *
     * @param value
     */
    @Override
    public void run(Transference value) {

        final Wallet origin = value.getOrigin();
        final Wallet destination = value.getDestination();

        if (origin.equals(destination)) {
            throw new BusinessLogicException("error.transference.same-wallet");
        }
    }
}
