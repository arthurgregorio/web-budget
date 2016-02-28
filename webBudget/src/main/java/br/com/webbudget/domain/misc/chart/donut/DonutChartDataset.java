/*
 * Copyright (C) 2016 Arthur Gregorio, AG.Software
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
package br.com.webbudget.domain.misc.chart.donut;

import lombok.Getter;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.2.0, 27/02/2016
 */
public class DonutChartDataset<T> {

    @Getter
    private T value;
    @Getter
    private String color;
    @Getter
    private String highlight;
    @Getter
    private String label;

    /**
     * 
     * @param value
     * @param color
     * @param highlight
     * @param label 
     */
    public DonutChartDataset(T value, String color, String highlight, String label) {
        this.value = value;
        this.color = color;
        this.highlight = highlight;
        this.label = label;
    }
}
