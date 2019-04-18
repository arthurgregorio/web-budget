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

import br.com.webbudget.application.components.dto.Color;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstraction of a dataset to be used in any pie chart
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 15/04/2019
 */
public class PieChartDataset {

    @Getter
    @Setter
    private String label;
    @Getter
    @Setter
    private int borderWidth;

    @Getter
    private List<Integer> data;
    @Getter
    private List<String> backgroundColor;
    @Getter
    private List<String> borderColor;

    /**
     * Constructor...
     */
    public PieChartDataset(String label) {

        this.label = label;

        this.borderWidth = 1;

        this.data = new ArrayList<>();
        this.borderColor = new ArrayList<>();
        this.backgroundColor = new ArrayList<>();
    }

    /**
     * Constructor...
     *
     * @param label of the series
     * @param data list with the data
     * @param backgroundColor the color of this series
     */
    public PieChartDataset(String label, List<Integer> data, List<String> backgroundColor) {
        this.label = label;
        this.data = data;
        this.backgroundColor = backgroundColor;
    }

    /**
     * Helper method to add the data
     *
     * @param data to be added
     */
    public void addData(Integer data) {
        this.data.add(data);
    }

    /**
     * Helper method to add the color
     *
     * @param color to be added
     */
    public void addColor(Color color) {
        this.backgroundColor.add(color.transparent().toString());
        this.borderColor.add(color.darker().toString());
    }
}