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

import java.util.ArrayList;
import java.util.List;
import lombok.Setter;

/**
 * A representacao das series do nosso grafico
 *
 * @param <T> o tipo dos dados utilizados na serie
 * 
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.2.0, 09/02/2016
 */
public class LineChartDataset<T> {

    @Setter
    private String label;
    @Setter
    private String fillColor;
    @Setter
    private String strokeColor;
    @Setter
    private String pointColor;
    @Setter
    private String pointStrokeColor;
    @Setter
    private String pointHighlightFill;
    @Setter
    private String pointHighlightStroke;

    @Setter
    private List<T> data;

    /**
     * 
     */
    public LineChartDataset() {
        this.data = new ArrayList<>();
    }
    
    /**
     * @param data um novo dado para esta serie
     */
    public void addData(T data) {
        this.data.add(data);
    }
    
    /**
     * @return se os dados estao ou nao vazios
     */
    public boolean isEmpty() {
        return this.data.isEmpty();
    }
}
