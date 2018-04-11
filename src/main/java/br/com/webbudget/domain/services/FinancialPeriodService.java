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

import br.com.webbudget.domain.entities.miscellany.FinancialPeriod;
import br.com.webbudget.domain.events.PeriodOpened;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.repositories.miscellany.FinancialPeriodRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.transaction.Transactional;

/**
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
    @PeriodOpened
    private Event<FinancialPeriod> periodOpenEvent;
    
    /**
     *
     * @param financialPeriod
     */
    @Transactional
    public void save(FinancialPeriod financialPeriod) {

        // check for duplicated financial periods
        final Optional<FinancialPeriod> found = this.financialPeriodRepository
                .findOptionalByIdentification(financialPeriod.getIdentification());

        if (found.isPresent()) {
            throw new BusinessLogicException("error.financial-period.duplicated");
        }

        // check for periods with the same date period of this new one
        final long periodCount = this.financialPeriodRepository
                .countOnTheSamePeriod(financialPeriod.getStart(), financialPeriod.getEnd());

        if (periodCount > 0) {
            throw new BusinessLogicException("error.financial-period.truncated-dates", periodCount);
        }
        
        // se o fim for o mesmo dia ou anterior a data atual, erro!
        if (financialPeriod.getEnd().compareTo(LocalDate.now()) < 1) {
            throw new BusinessLogicException("error.financial-period.invalid-end");
        }

        final FinancialPeriod opened = 
                this.financialPeriodRepository.save(financialPeriod);
        
        // fire a event to notify the listeners
        this.periodOpenEvent.fire(opened);
    }
    
    /**
     * 
     * @param financialPeriod 
     */
    @Transactional
    public void delete(FinancialPeriod financialPeriod) {
        
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
