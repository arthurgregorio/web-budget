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
package br.com.webbudget.domain.logics.registration.movementclass;

import br.com.webbudget.domain.entities.registration.CostCenter;
import br.com.webbudget.domain.entities.registration.MovementClass;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.repositories.registration.MovementClassRepository;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Validate if the {@link MovementClass} has a duplicated name
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 29/09/2018
 */
@Dependent
public class MovementClassDuplicatesValidator implements MovementClassSavingLogic, MovementClassUpdatingLogic {

    @Inject
    private MovementClassRepository movementClassRepository;

    /**
     * {@inheritDoc}
     *
     * @param value
     */
    @Override
    public void run(MovementClass value) {
        if (value.isSaved()) {
            this.validateSaved(value);
        } else {
            this.validateNotSaved(value);
        }
    }

    /**
     * If the {@link MovementClass} is not saved, don't compare the found with the one to be validated
     *
     * @param value the value to be evaluated
     */
    private void validateNotSaved(MovementClass value) {
        final Optional<MovementClass> found = this.find(value.getName(), value.getCostCenter().getName());
        found.ifPresent(movementClass -> {
            throw new BusinessLogicException("error.movement-class.duplicated");
        });
    }

    /**
     * If the {@link MovementClass} is saved, compare the found with the one to be validated
     *
     * @param value the value to be evaluated
     */
    private void validateSaved(MovementClass value) {
        final Optional<MovementClass> found = this.find(value.getName(), value.getCostCenter().getName());
        if (found.isPresent() && found.get().equals(value)) {
            throw new BusinessLogicException("error.movement-class.duplicated");
        }
    }

    /**
     * Find the value on the database to compare with the one to be validated
     *
     * @param name the name of the {@link MovementClass}
     * @param costCenterName the {@link CostCenter} of the {@link MovementClass}
     * @return a {@link Optional} of the {@link MovementClass}
     */
    private Optional<MovementClass> find(String name, String costCenterName) {
        return this.movementClassRepository.findByNameAndCostCenter_name(name, costCenterName);
    }
}
