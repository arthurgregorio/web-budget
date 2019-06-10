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

import br.com.webbudget.application.components.builder.PeriodMovementBuilder;
import br.com.webbudget.domain.entities.financial.FixedMovement;
import br.com.webbudget.domain.entities.financial.FixedMovementState;
import br.com.webbudget.domain.entities.financial.Launch;
import br.com.webbudget.domain.entities.financial.PeriodMovement;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.events.FinancialPeriodOpened;
import br.com.webbudget.domain.logics.financial.movement.fixed.FixedMovementDeletingLogic;
import br.com.webbudget.domain.logics.financial.movement.fixed.FixedMovementSavingLogic;
import br.com.webbudget.domain.repositories.financial.ApportionmentRepository;
import br.com.webbudget.domain.repositories.financial.FixedMovementRepository;
import br.com.webbudget.domain.repositories.financial.LaunchRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

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
    private PeriodMovementService periodMovementService;

    @Inject
    private LaunchRepository launchRepository;
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

    /**
     * Method used to launch a {@link FixedMovement} into a {@link FinancialPeriod}
     *
     * @param fixedMovementId to find the {@link FixedMovement} to be launched
     * @param financialPeriod to be used to launch the {@link FixedMovement}
     */
    @Transactional
    public void launch(long fixedMovementId, FinancialPeriod financialPeriod) {

        // query the fixed movement by the id
        final FixedMovement fixedMovement = this.fixedMovementRepository.findById(fixedMovementId)
                .orElseThrow(() -> new IllegalStateException("Can't find fixed movement with id: " + fixedMovementId));

        // create the identification
        final String identification;

        if (fixedMovement.isUndetermined()) {
            identification = fixedMovement.getIdentification();
        } else {

            // find the last launch for this fixed movement
            final int lastQuote = this.launchRepository.findLastLaunchCounterFor(fixedMovement.getId()).orElse(0);
            final int actualQuote = lastQuote == 0 ? fixedMovement.getStartingQuote() : (lastQuote + 1);

            fixedMovement.setActualQuote(actualQuote);

            identification = fixedMovement.getIdentification() + " " + actualQuote + "/" + fixedMovement.getTotalQuotes();
        }

        // create the period movement for the launch
        final PeriodMovement periodMovement = new PeriodMovementBuilder()
                .financialPeriod(financialPeriod)
                .contact(fixedMovement.getContact())
                .value(fixedMovement.getValue())
                .description(fixedMovement.getDescription())
                .identification(identification)
                .dueDate(fixedMovement.getStartDate())
                .addApportionments(fixedMovement.copyApportionments())
                .build();

        // save the period movement
        final PeriodMovement saved = this.periodMovementService.save(periodMovement);

        // create the launch to link the fixed and the period movement
        final Launch launch = new Launch();

        launch.setFinancialPeriod(financialPeriod);
        launch.setFixedMovement(fixedMovement);
        launch.setPeriodMovement(saved);
        launch.setQuoteNumber(fixedMovement.getActualQuote());

        this.launchRepository.save(launch);

        // update the fixed movement
        if (launch.isLastQuote() && !fixedMovement.isUndetermined()) {
            fixedMovement.setFixedMovementState(FixedMovementState.FINISHED);
        }

        this.fixedMovementRepository.saveAndFlushAndRefresh(fixedMovement);
    }

    /**
     * Same as {@link #launch(long, FinancialPeriod)} but this one takes a {@link Set} as parameter and for every
     * {@link FixedMovement} make a launch
     *
     * @param fixedMovements {@link Set} to be launched
     * @param financialPeriod to be used to launch the {@link FixedMovement}
     */
    @Transactional
    public void launch(List<FixedMovement> fixedMovements, FinancialPeriod financialPeriod) {
        fixedMovements.forEach(fixedMovement -> this.launch(fixedMovement.getId(), financialPeriod));
    }

    /**
     * When a new {@link FinancialPeriod} is opened this method listen to the event and launch all the {@link FixedMovement}
     * marked as auto-launch
     *
     * @param financialPeriod opened
     */
    public void onFinancialPeriodOpen(@Observes @FinancialPeriodOpened FinancialPeriod financialPeriod) {
        this.launch(this.fixedMovementRepository.findByAutoLaunchAndFixedMovementState(
                true, FixedMovementState.ACTIVE), financialPeriod);
    }
}