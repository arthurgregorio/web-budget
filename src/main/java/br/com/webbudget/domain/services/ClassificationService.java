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
package br.com.webbudget.domain.services;

import br.com.webbudget.domain.entities.entries.CostCenter;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.repositories.entries.CostCenterRepository;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 09/04/2018
 */
@ApplicationScoped
public class ClassificationService {

    @Inject
    private CostCenterRepository costCenterRepository;
    
    /**
     *
     * @param costCenter
     */
    @Transactional
    public void saveCostCenter(CostCenter costCenter) {

        final Optional<CostCenter> found = this.costCenterRepository
                .findOptionalByNameAndParent(costCenter.getName(), costCenter.getParent());

        if (found.isPresent()) {
            throw new BusinessLogicException("error.cost-center.duplicated");
        }
        this.costCenterRepository.save(costCenter);
    }

    /**
     *
     * @param costCenter
     * @return
     */
    @Transactional
    public CostCenter updateCostCenter(CostCenter costCenter) {

        final Optional<CostCenter> found = this.costCenterRepository
                .findOptionalByNameAndParent(costCenter.getName(), costCenter.getParent());

        if (found.isPresent() && !found.get().equals(costCenter)) {
            throw new BusinessLogicException("error.cost-center.duplicated");
        }
        return this.costCenterRepository.save(costCenter);
    }

    /**
     *
     * @param costCenter
     */
    @Transactional
    public void deleteCostCenter(CostCenter costCenter) {
        this.costCenterRepository.attachAndRemove(costCenter);
    }
}