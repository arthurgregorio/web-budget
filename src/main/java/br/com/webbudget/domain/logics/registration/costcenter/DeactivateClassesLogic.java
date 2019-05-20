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
package br.com.webbudget.domain.logics.registration.costcenter;

import br.com.webbudget.domain.entities.registration.CostCenter;
import br.com.webbudget.domain.entities.registration.MovementClass;
import br.com.webbudget.domain.repositories.registration.MovementClassRepository;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.List;

/**
 * Logic to deactivate all {@link MovementClass} from a given {@link CostCenter} when it is deactivated
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 17/03/2019
 */
@Dependent
public class DeactivateClassesLogic implements CostCenterUpdatingLogic {

    @Inject
    private MovementClassRepository movementClassRepository;

    /**
     * {@inheritDoc}
     *
     * @param value
     */
    @Override
    public void run(CostCenter value) {

        if (!value.isActive()) {
            final List<MovementClass> movementClasses =
                    this.movementClassRepository.findByActiveAndCostCenterOrderByNameAsc(true, value);

            movementClasses.forEach(movementClass -> {
                movementClass.setActive(false);
                this.movementClassRepository.saveAndFlushAndRefresh(movementClass);
            });
        }
    }
}
