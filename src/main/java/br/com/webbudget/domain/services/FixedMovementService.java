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
package br.com.webbudget.domain.services;

import br.com.webbudget.domain.entities.financial.FixedMovement;
import br.com.webbudget.domain.logics.financial.movement.fixed.FixedMovementDeletingLogic;
import br.com.webbudget.domain.logics.financial.movement.fixed.FixedMovementSavingLogic;
import br.com.webbudget.domain.repositories.financial.ApportionmentRepository;
import br.com.webbudget.domain.repositories.financial.FixedMovementRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 * Service to make all operations related to the {@link FixedMovement}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 21/03/2019
 */
@ApplicationScoped
public class FixedMovementService {

    @Inject
    private ApportionmentRepository apportionmentRepository;
    @Inject
    private FixedMovementRepository fixedMovementRepository;

    @Any
    @Inject
    private Instance<FixedMovementSavingLogic> fixedMovementSavingLogics;
    @Any
    @Inject
    private Instance<FixedMovementDeletingLogic> fixedMovementDeletingLogics;

    /**
     * Method used to save an {@link FixedMovement}
     *
     * @param fixedMovement to be saved
     */
    @Transactional
    public void save(FixedMovement fixedMovement) {

        this.fixedMovementSavingLogics.forEach(logic -> logic.run(fixedMovement));

        final FixedMovement saved = this.fixedMovementRepository.save(fixedMovement);

        fixedMovement.getApportionments().forEach(apportionment -> {
            apportionment.setMovement(saved);
            this.apportionmentRepository.save(apportionment);
        });
    }

    /**
     * Method used to update a {@link FixedMovement}
     *
     * @param fixedMovement to be updated
     * @return the updated {@link FixedMovement}
     */
    @Transactional
    public FixedMovement update(FixedMovement fixedMovement) {
        this.fixedMovementRepository.saveAndFlushAndRefresh(fixedMovement);
        return fixedMovement;
    }

    /**
     * Method used to delete a {@link FixedMovement}
     *
     * @param fixedMovement to be deleted
     */
    @Transactional
    public void delete(FixedMovement fixedMovement) {
        this.fixedMovementDeletingLogics.forEach(logic -> logic.run(fixedMovement));
        this.fixedMovementRepository.attachAndRemove(fixedMovement);
    }
}