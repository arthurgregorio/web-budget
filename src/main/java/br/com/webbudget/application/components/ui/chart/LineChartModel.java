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
package br.com.webbudget.application.components.ui.chart;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Chart model used by the line chart
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 19/04/2019
 */
public class LineChartModel<T> extends AbstractChartModel {

    @Getter
    private List<String> labels;
    @Getter
    private List<LineChartDataset<T>> datasets;

    /**
     * Constructor...
     */
    public LineChartModel() {
        this.labels = new ArrayList<>();
        this.datasets = new ArrayList<>();
    }

    /**
     * Add a new dataset to the model
     *
     * @param dataset to be added
     */
    public void addDataset(LineChartDataset<T> dataset) {
        this.datasets.add(dataset);
    }

    /**
     * Add a new {@link List} of dataset
     *
     * @param datasets {@link List} to be added
     */
    public void addAllDatasets(List<LineChartDataset<T>> datasets) {
        this.datasets.addAll(datasets);
    }

    /**
     * Add a new label
     *
     * @param label to be added
     */
    public void addDataset(String label) {
        this.labels.add(label);
    }

    /**
     * Add a {@link List} of labels to the model
     *
     * @param labels {@link List} to be added
     */
    public void addAllLabels(List<String> labels) {
        this.labels.addAll(labels);
    }
}
