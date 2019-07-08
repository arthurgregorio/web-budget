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
package br.com.webbudget.domain.schedules;

import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.repositories.registration.FinancialPeriodRepository;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;

/**
 * Scheduled task to mark all expired {@link FinancialPeriod} as expired
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 06/04/2019
 */
@Startup
@Singleton
public class FinancialPeriodExpiredTask {

    @Inject
    private FinancialPeriodRepository financialPeriodRepository;

    /**
     * This method is scheduled to run everyday at midnight and get all expired {@link FinancialPeriod} to mark them as
     * expired
     */
    @Schedule(persistent = false, info = "Everyday at midnight")
    public void markAsExpired() {

        final List<FinancialPeriod> periods = this.financialPeriodRepository.findByClosedOrderByIdentificationAsc(false);

        periods.stream()
                .filter(period -> LocalDate.now().compareTo(period.getEnd()) > 0)
                .forEach(period -> {
                    period.setExpired(true);
                    this.financialPeriodRepository.saveAndFlush(period);
                });
    }
}