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
package br.com.webbudget.application.controller.registration;

import br.com.webbudget.application.components.ui.AbstractBean;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.repositories.registration.FinancialPeriodRepository;
import lombok.Getter;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.time.format.DateTimeFormatter;

/**
 * Controller for the {@link FinancialPeriod} statistics view
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 27/04/2019
 */
@Named
@ViewScoped
public class PeriodStatisticsBean extends AbstractBean {

    @Getter
    private FinancialPeriod financialPeriod;

    @Inject
    private FinancialPeriodRepository financialPeriodRepository;

    /**
     * Initialize this view
     *
     * @param financialPeriodId to search for the {@link FinancialPeriod}
     */
    public void initialize(Long financialPeriodId) {
        this.financialPeriod = this.financialPeriodRepository.findById(financialPeriodId)
                .orElseThrow(() -> new BusinessLogicException("error.period-statistics.period-not-found"));
    }

    /**
     * Load the charts model to display data on the UI
     */
    public void loadCharts() {

    }

    /**
     * Helper method to get the start date of this period as {@link String}
     *
     * @return the start date as {@link String}
     */
    public String getStartAsString() {
        return this.financialPeriod.getStart().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    /**
     * Helper method to get the end date of this period as {@link String}
     *
     * @return the end date as {@link String}
     */
    public String getEndAsString() {
        return this.financialPeriod.getEnd().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
