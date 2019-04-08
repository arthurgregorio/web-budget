/*
 * Copyright (C) 2014 Arthur Gregorio, AG.Software
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

import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.events.FinancialPeriodOpened;
import br.com.webbudget.domain.logics.registration.financialperiod.PeriodDeletingLogic;
import br.com.webbudget.domain.logics.registration.financialperiod.PeriodSavingLogic;
import br.com.webbudget.domain.repositories.registration.FinancialPeriodRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDate;

/**
 * The service responsible for the business operations with the {@link FinancialPeriod}
 *
 * @author Arthur Gregorio
 *
 * @version 1.3.0
 * @since 1.0.0, 20/03/2014
 */
@ApplicationScoped
public class FinancialPeriodService {

    @Inject
    private FinancialPeriodRepository financialPeriodRepository;

    @Inject
    @FinancialPeriodOpened
    private Event<FinancialPeriod> financialPeriodOpenedEvent;

    @Any
    @Inject
    private Instance<PeriodSavingLogic> savingBusinessLogics;
    @Any
    @Inject
    private Instance<PeriodDeletingLogic> periodDeletingLogics;

    /**
     * Use this method to persist a {@link FinancialPeriod}
     *
     * @param financialPeriod the {@link FinancialPeriod} to be persisted
     */
    @Transactional
    public void save(FinancialPeriod financialPeriod) {

        this.savingBusinessLogics.forEach(logic -> logic.run(financialPeriod));

        // check if the period is already expired and mark it as expired
        if (LocalDate.now().compareTo(financialPeriod.getEnd()) > 0) {
            financialPeriod.setExpired(true);
        }

        // fire a event to notify the listeners
        this.financialPeriodOpenedEvent.fire(this.financialPeriodRepository.save(financialPeriod));
    }

    /**
     * Use this method to delete a persisted {@link FinancialPeriod}
     *
     * @param financialPeriod the {@link FinancialPeriod} to be deleted
     */
    @Transactional
    public void delete(FinancialPeriod financialPeriod) {
        this.periodDeletingLogics.forEach(logic -> logic.run(financialPeriod));
        this.financialPeriodRepository.attachAndRemove(financialPeriod);
    }
}
