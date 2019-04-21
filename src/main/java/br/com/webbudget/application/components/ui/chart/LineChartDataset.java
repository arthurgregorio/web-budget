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
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Dataset to be used as data carrier to the {@link LineChartModel}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 19/04/2019
 */
public class LineChartDataset<T> {

    @Getter
    @Setter
    private String label;
    @Getter
    @Setter
    private String backgroundColor;
    @Getter
    @Setter
    private String borderColor;
    @Getter
    @Setter
    private boolean fill;

    @Getter
    private List<T> data;

    /**
     * Constructor...
     */
    public LineChartDataset() {
        this.fill = false;
        this.data = new ArrayList<>();
    }

    /**
     * Add a value to this dataset
     *
     * @param value to be added
     */
    public void addData(T value) {
        this.data.add(value);
    }

    /**
     * Add a {@link List} of values to this dataset
     *
     * @param values {@link List} to be added
     */
    public void addAllData(List<T> values) {
        this.data.addAll(values);
    }
}
