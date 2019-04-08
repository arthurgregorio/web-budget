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

import br.com.webbudget.domain.entities.financial.Closing;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.repositories.financial.ClosingRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 * Service used by the {@link Closing} process
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 1.0.0, 09/04/2014
 */
@ApplicationScoped
public class ClosingService {

    @Inject
    private ClosingRepository closingRepository;

    /**
     * Effectively close the {@link FinancialPeriod}
     *
     * @param financialPeriod to be closed
     */
    @Transactional
    public void close(FinancialPeriod financialPeriod) {

    }

    /**
     * Simulate the closing process for the given {@link FinancialPeriod}
     *
     * @param financialPeriod to be simulated
     */
    public void simulateClosing(FinancialPeriod financialPeriod) {

    }
}
