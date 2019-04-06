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
package br.com.webbudget.domain.logics.financial.movement.period;

import br.com.webbudget.domain.entities.financial.FixedMovement;
import br.com.webbudget.domain.entities.financial.FixedMovementState;
import br.com.webbudget.domain.entities.financial.Launch;
import br.com.webbudget.domain.entities.financial.PeriodMovement;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.repositories.financial.FixedMovementRepository;
import br.com.webbudget.domain.repositories.financial.LaunchRepository;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 * Logic to check if this {@link PeriodMovement} is part of a {@link FixedMovement} {@link Launch}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 05/04/2019
 */
@Dependent
public class QuoteDeleteLogic implements PeriodMovementDeletingLogic {

    @Inject
    private LaunchRepository launchRepository;
    @Inject
    private FixedMovementRepository fixedMovementRepository;

    /**
     * {@inheritDoc}
     *
     * @param value
     */
    @Override
    public void run(PeriodMovement value) {
        this.launchRepository.findByPeriodMovement(value).ifPresent(this::rollbackLaunch);
    }

    /**
     * Method to check and rollback the state of the {@link FixedMovement} linked to this {@link PeriodMovement}
     *
     * @param launch the {@link Launch} to be deleted
     */
    private void rollbackLaunch(Launch launch) {

        final FixedMovement fixedMovement = launch.getFixedMovement();

        if (!fixedMovement.isUndetermined()) {

            final Launch lastLaunch = this.launchRepository.findLastLaunch(launch.getFixedMovement()).orElse(null);

            if (!launch.equals(lastLaunch)) {
                throw new BusinessLogicException("error.period-movement.not-last-launch");
            }

            // if is the last quote, just reactivate the fixed movement
            if (launch.isLastQuote()) {
                fixedMovement.setFixedMovementState(FixedMovementState.ACTIVE);
            }

            // back to begin, then set the actual counter to null or else, subtract one
            if (fixedMovement.getActualQuote() - 1 < fixedMovement.getStartingQuote()) {
                fixedMovement.setActualQuote(null);
            } else {
                fixedMovement.setActualQuote(fixedMovement.getActualQuote() - 1);
            }

            // update the fixed movement and delete the launch
            this.fixedMovementRepository.saveAndFlushAndRefresh(fixedMovement);
        }

        this.launchRepository.attachAndRemove(launch);
    }
}
