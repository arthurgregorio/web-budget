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
package br.com.webbudget.infraestructure.components.chart.donut;

import br.com.webbudget.infraestructure.components.chart.AbstractChartModel;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

/**
 * Datamodel para os graficos donut do ChatJS
 *  
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.2.0, 27/02/2016
 */
public class DonutChartModel extends AbstractChartModel {

    private final List<DonutChartDataset> chartDatasets;

    /**
     * 
     */
    public DonutChartModel() {
        this.chartDatasets = new ArrayList<>();
    }
    
    /**
     * @param dataset os dados para o grafico
     */
    public void addData(DonutChartDataset dataset) {
        this.chartDatasets.add(dataset);
    }
    
    /**
     * @return se este grafico contem ou nao dados
     */
    public boolean containsData() {
        return !this.chartDatasets.isEmpty();
    }
    
    /**
     * @return o JSON representando os dados para o grafico
     */
    @Override
    public String toJson() {
        return new Gson().toJson(this.chartDatasets);
    }
}
