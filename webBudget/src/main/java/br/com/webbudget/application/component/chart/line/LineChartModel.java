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
package br.com.webbudget.application.component.chart.line;

import br.com.webbudget.application.component.chart.AbstractChartModel;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import lombok.Setter;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.2.0, 09/02/2016
 */
public class LineChartModel extends AbstractChartModel {
    
    private final List<String> labels;
    
    @Setter
    private final List<LineChartDataset> datasets;

    /**
     * 
     */
    public LineChartModel() {
        this.labels = new ArrayList<>();
        this.datasets = new ArrayList<>();
    }

    /**
     * @param label um label para as series do grafico
     */
    public void addLabel(String label) {
        this.labels.add(label);
    }
    
    /**
     * @param chartDataset os datasets do nosso model
     */
    public void addDataset(LineChartDataset chartDataset) {
        this.datasets.add(chartDataset);
    }
    
    /**
     * @return o JSON representando os dados para o grafico
     */
    @Override
    public String toJson() {
        return new Gson().toJson(this);
    }
}
