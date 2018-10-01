/*
 * Copyright (C) 2015 Arthur Gregorio, AG.Software
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
import br.com.webbudget.domain.events.NewPeriodOpened;
import br.com.webbudget.domain.repositories.registration.FinancialPeriodRepository;
import br.com.webbudget.domain.validators.registration.financialperiod.FinancialPeriodSavingValidator;
import br.com.webbudget.domain.validators.registration.financialperiod.FinancialPeriodUpdatingValidator;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.transaction.Transactional;

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
    @NewPeriodOpened
    private Event<FinancialPeriod> newPeriodOpenedEvent;

    @Any
    @Inject
    private Instance<FinancialPeriodSavingValidator> savingValidators;
    @Any
    @Inject
    private Instance<FinancialPeriodUpdatingValidator> updatingValidators;

    /**
     * Use this method to persist a {@link FinancialPeriod}
     *
     * @param financialPeriod the {@link FinancialPeriod} to be persisted
     */
    @Transactional
    public void save(FinancialPeriod financialPeriod) {

        this.savingValidators.forEach(validator -> validator.validate(financialPeriod));

        // fire a event to notify the listeners
        this.newPeriodOpenedEvent.fire(this.financialPeriodRepository.save(financialPeriod));
    }

    /**
     * Use this method to delete a persisted {@link FinancialPeriod}
     *
     * @param financialPeriod the {@link FinancialPeriod} to be deleted
     */
    @Transactional
    public void delete(FinancialPeriod financialPeriod) {

        // FIXME when we can persist movements, put this to work again

//        final List<Movement> movements = this.movementRepository
//                .listByPeriodAndStateAndType(financialPeriod, null, null);
//        
//        // se houver movimentos, lanca o erro
//        if (movements != null && !movements.isEmpty()) {
//            throw new BusinessLogicException("error.financial-period.has-movements",
//                    financialPeriod.getIdentification());
//        } 

        this.financialPeriodRepository.attachAndRemove(financialPeriod);
    }
}
