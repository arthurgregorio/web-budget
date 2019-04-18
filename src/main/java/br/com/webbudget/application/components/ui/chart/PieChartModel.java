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
 * Data model to hold all {@link PieChartDataset} to be used at a pie chart
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 15/04/2019
 */
public class PieChartModel extends AbstractChartModel {

    @Getter
    private List<String> labels;
    @Getter
    private List<PieChartDataset> datasets;

    /**
     * Constructor...
     */
    public PieChartModel() {
        this.datasets = new ArrayList<>();
        this.labels = new ArrayList<>();
    }

    /**
     * Constructor...
     *
     * @param labels the labels to be used on each series
     * @param datasets holding the data
     */
    public PieChartModel(List<String> labels, List<PieChartDataset> datasets) {
        this.labels = labels;
        this.datasets = datasets;
    }

    /**
     * Add a new label
     *
     * @param label to be added
     */
    public void addLabel(String label) {
        this.labels.add(label);
    }

    /**
     * Add a {@link List} of labels
     *
     * @param labels {@link List} to be added
     */
    public void addAllLabel(List<String> labels) {
        this.labels.addAll(labels);
    }

    /**
     * Add a new dataset
     *
     * @param dataset to be added
     */
    public void addData(PieChartDataset dataset) {
        this.datasets.add(dataset);
    }

    /**
     * Add a {@link List} of datasets
     *
     * @param datasets {@link List} to be added
     */
    public void addAllData(List<PieChartDataset> datasets) {
        this.datasets.addAll(datasets);
    }
}
