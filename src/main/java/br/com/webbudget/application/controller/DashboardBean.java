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
package br.com.webbudget.application.controller;

import br.com.webbudget.application.components.ui.AbstractBean;
import br.com.webbudget.domain.calculators.CostCenterTotalCalculator;
import br.com.webbudget.domain.entities.registration.MovementClassType;
import br.com.webbudget.domain.entities.view.OpenPeriodResume;
import br.com.webbudget.domain.repositories.view.OpenPeriodResumeRepository;
import lombok.Getter;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Application dashboard controller
 *
 * @author Arthur Gregorio
 *
 * @version 3.0.0
 * @since 1.0.0, 27/02/2014
 */
@Named
@ViewScoped
public class DashboardBean extends AbstractBean {

    @Getter
    private OpenPeriodResume openPeriodResume;

    @Inject
    private CostCenterTotalCalculator costCenterTotalCalculator;

    @Inject
    private OpenPeriodResumeRepository openPeriodResumeRepository;

    /**
     * Initialize dashboard with data
     */
    public void initialize() {
        this.openPeriodResume = this.openPeriodResumeRepository.load().orElseGet(OpenPeriodResume::new);

        this.costCenterTotalCalculator.load(MovementClassType.REVENUE);
        this.executeScript("drawPieChart(" + this.costCenterTotalCalculator.toPieChartModel().toJson() + ", 'costCenterRevenues')");

        this.costCenterTotalCalculator.load(MovementClassType.EXPENSE);
        this.executeScript("drawPieChart(" + this.costCenterTotalCalculator.toPieChartModel().toJson() + ", 'costCenterExpenses')");
    }
}
