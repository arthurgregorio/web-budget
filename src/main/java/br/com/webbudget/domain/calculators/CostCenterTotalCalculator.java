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

import br.com.webbudget.application.components.ui.chart.PieChartDataset;
import br.com.webbudget.application.components.ui.chart.PieChartModel;
import br.com.webbudget.domain.entities.registration.CostCenter;
import br.com.webbudget.domain.entities.registration.MovementClassType;
import br.com.webbudget.domain.entities.view.CostCenterTotal;
import br.com.webbudget.domain.repositories.view.CostCenterTotalRepository;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.webbudget.application.components.ui.chart.ChartUtils.percentageOf;

/**
 * Calculator used to calculate and transform the data of the {@link CostCenterTotal}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 17/04/2019
 */
@Dependent
public class CostCenterTotalCalculator {

    private List<CostCenterTotal> costCenterTotals;

    @Inject
    private CostCenterTotalRepository costCenterTotalRepository;

    /**
     * Constructor...
     */
    public CostCenterTotalCalculator() {
        this.costCenterTotals = new ArrayList<>();
    }

    /**
     * Load this calculator with some data
     */
    public void load() {
        this.load(null);
    }

    /**
     * Same as {@link #load()} but this one receive as parameter which direction we want to account our {@link CostCenter}
     *
     * @param direction represented by the {@link MovementClassType}
     */
    public void load(MovementClassType direction) {
        if (direction == null) {
            this.costCenterTotals = this.costCenterTotalRepository.findAll();
        } else {
            this.costCenterTotals = this.costCenterTotalRepository.findByDirection(direction);
        }
    }

    /**
     * To check if this calculator is empty or not
     *
     * @return true if is, false otherwise
     */
    public boolean isEmpty() {
        return this.costCenterTotals.isEmpty();
    }

    /**
     * Get the data of this calculator as a {@link PieChartModel}
     *
     * @return the {@link PieChartModel} with the data of this calculator
     */
    public PieChartModel toChartModel() {

        final BigDecimal total = this.costCenterTotals.stream()
                .map(CostCenterTotal::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        final PieChartDataset dataset = new PieChartDataset("default");

        this.costCenterTotals.forEach(costCenterTotal -> {
            dataset.addColor(costCenterTotal.getCostCenterColor());
            dataset.addData(percentageOf(costCenterTotal.getValue(), total, true));
        });

        final PieChartModel model = new PieChartModel();

        model.addData(dataset);
        model.addAllLabel(this.costCenterTotals.stream()
                .map(CostCenterTotal::getCostCenter)
                .collect(Collectors.toList()));

        return model;
    }
}