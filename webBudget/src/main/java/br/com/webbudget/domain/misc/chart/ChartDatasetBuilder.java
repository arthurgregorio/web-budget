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
package br.com.webbudget.domain.misc.chart;

import java.util.ArrayList;
import java.util.List;

/**
 * Um builder para as series do nosso grafico
 *
 * @param <T> o tipo de dados da serie
 * 
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 09/02/2016
 */
public class ChartDatasetBuilder<T> {

    private final ChartDataset<T> chartDataset;

    /**
     * 
     */
    public ChartDatasetBuilder() {
        this.chartDataset = new ChartDataset<>();
    }
    
    /**
     * 
     * @param label
     * @return 
     */
    public ChartDatasetBuilder withLabel(String label) {
        this.chartDataset.setLabel(label);
        return this;
    }
    
    /**
     * 
     * @param color
     * @return 
     */
    public ChartDatasetBuilder filledByColor(String color) {
        this.chartDataset.setFillColor(color);
        return this;
    }
    
    /**
     * 
     * @param color
     * @return 
     */
    public ChartDatasetBuilder withPointColor(String color) {
        this.chartDataset.setPointColor(color);
        return this;
    }
    
    /**
     * 
     * @param color
     * @return 
     */
    public ChartDatasetBuilder withPointHighlightFillColor(String color) {
        this.chartDataset.setPointHighlightFill(color);
        return this;
    }
    
    /**
     * 
     * @param color
     * @return 
     */
    public ChartDatasetBuilder withPointHighlightStroke(String color) {
        this.chartDataset.setPointHighlightStroke(color);
        return this;
    }
    
    /**
     * 
     * @param color
     * @return 
     */
    public ChartDatasetBuilder withPointStrokeColor(String color) {
        this.chartDataset.setPointStrokeColor(color);
        return this;
    }
    
    /**
     * 
     * @param color
     * @return 
     */
    public ChartDatasetBuilder withStrokeColor(String color) {
        this.chartDataset.setStrokeColor(color);
        return this;
    }
    
    /**
     * 
     * @param data
     * @return 
     */
    public ChartDatasetBuilder andData(T data) {
        this.chartDataset.addData(data);
        return this;
    }
    
    public ChartDatasetBuilder andData(List<T> data) {
        this.chartDataset.setData(new ArrayList<>(data));
        return this;
    }
    
    /**
     * @return o dataset construido por este builder
     */
    public ChartDataset<T> build() {
        return this.chartDataset;
    }
}
