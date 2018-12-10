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

import br.com.webbudget.domain.entities.financial.PeriodMovement;
import br.com.webbudget.domain.repositories.financial.PeriodMovementRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 * The {@link PeriodMovement} service
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 04/12/2018
 */
@ApplicationScoped
public class PeriodMovementService {

    @Inject
    private PeriodMovementRepository periodMovementRepository;

    /**
     * Save a new {@link PeriodMovement}
     *
     * @param periodMovement the {@link PeriodMovement} to be saved
     */
    @Transactional
    public void save(PeriodMovement periodMovement) {
        this.periodMovementRepository.save(periodMovement);
    }

    /**
     * Update some {@link PeriodMovement}
     *
     * @param periodMovement the {@link PeriodMovement} to be updated
     * @return the updated {@link PeriodMovement}
     */
    @Transactional
    public PeriodMovement update(PeriodMovement periodMovement) {
        return this.periodMovementRepository.saveAndFlushAndRefresh(periodMovement);
    }

    /**
     * Attach and delete the give {@link PeriodMovement}
     *
     * @param periodMovement the {@link PeriodMovement} to be deleted
     */
    @Transactional
    public void delete(PeriodMovement periodMovement) {
        this.periodMovementRepository.attachAndRemove(periodMovement);
    }
}