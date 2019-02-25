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
package br.com.webbudget.domain.logics.registration.costcenter;

import br.com.webbudget.domain.entities.registration.CostCenter;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.repositories.registration.CostCenterRepository;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 * The {@link CostCenter} duplication validator
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 27/09/2018
 */
@Dependent
public class CostCenterDuplicatesValidator implements CostCenterSavingLogic {

    @Inject
    private CostCenterRepository costCenterRepository;

    /**
     * {@inheritDoc}
     *
     * @param value
     */
    @Override
    public void run(CostCenter value) {
        this.costCenterRepository.findByName(value.getName())
                .ifPresent(costCenter -> {
                    throw new BusinessLogicException("error.cost-center.duplicated");
                });
    }
}
