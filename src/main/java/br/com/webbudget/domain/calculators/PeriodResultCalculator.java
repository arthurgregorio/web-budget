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
package br.com.webbudget.domain.calculators;

import br.com.webbudget.application.components.dto.Color;
import br.com.webbudget.application.components.ui.chart.LineChartDataset;
import br.com.webbudget.application.components.ui.chart.LineChartModel;
import br.com.webbudget.domain.entities.view.PeriodResult;
import br.com.webbudget.domain.repositories.view.PeriodResultRepository;
import br.com.webbudget.infrastructure.i18n.MessageSource;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A calculator to help the process of calculate the result of each financial period closed
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 17/04/2019
 */
@Dependent
public class PeriodResultCalculator {

    private List<PeriodResult> results;

    @Inject
    private PeriodResultRepository periodResultRepository;

    /**
     * Load this calculator with some data
     */
    public void load() {
        this.results = this.periodResultRepository.findTop6OrderById();
    }

    /**
     * Use this method to check if this calculator contains data
     *
     * @return true if has data, false otherwise
     */
    public boolean isEmpty() {
        return this.results.isEmpty();
    }

    /**
     * Transform the data contained in this calculator in a {@link LineChartModel}
     *
     * @return the {@link LineChartModel} created
     */
    public LineChartModel<BigDecimal> toChartModel() {

        final String revenuesLabel = MessageSource.get("dashboard.financial-period.revenues");
        final String expensesLabel = MessageSource.get("dashboard.financial-period.expenses");
        final String balancesLabel = MessageSource.get("dashboard.financial-period.balance");

        final Color green = new Color(34, 139, 34);
        final Color red = new Color(220, 20, 60);
        final Color blue = new Color(30, 144, 255);

        final LineChartDataset<BigDecimal> revenuesDataset = new LineChartDataset<>();

        revenuesDataset.setLabel(revenuesLabel);
        revenuesDataset.setBorderColor(green.toString());
        revenuesDataset.setBackgroundColor(green.transparent().toString());
        revenuesDataset.addAllData(this.results.stream()
                .map(PeriodResult::getRevenues)
                .collect(Collectors.toList()));

        final LineChartDataset<BigDecimal> expensesDataset = new LineChartDataset<>();

        expensesDataset.setLabel(expensesLabel);
        expensesDataset.setBorderColor(red.toString());
        expensesDataset.setBackgroundColor(red.transparent().toString());
        expensesDataset.addAllData(this.results.stream()
                .map(PeriodResult::getExpenses)
                .collect(Collectors.toList()));

        final LineChartDataset<BigDecimal> balancesDataset = new LineChartDataset<>();

        balancesDataset.setLabel(balancesLabel);
        balancesDataset.setBorderColor(blue.toString());
        balancesDataset.setBackgroundColor(blue.transparent().toString());
        balancesDataset.addAllData(this.results.stream()
                .map(PeriodResult::getBalance)
                .collect(Collectors.toList()));

        final LineChartModel<BigDecimal> model = new LineChartModel<>();

        model.addAllDatasets(List.of(revenuesDataset, expensesDataset, balancesDataset));
        model.addAllLabels(this.results.stream()
                .map(PeriodResult::getFinancialPeriod)
                .collect(Collectors.toList()));

        return model;
    }
}