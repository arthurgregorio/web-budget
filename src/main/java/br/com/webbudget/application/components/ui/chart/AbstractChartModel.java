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

import br.com.webbudget.infrastructure.utils.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Abstract implementation of an chart model
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 15/04/2019
 */
public abstract class AbstractChartModel {

    /**
     * Method used to transform this model into JSON
     *
     * @return this model as JSON
     */
    public String toJson() {
        try {
            return JsonUtils.serialize(this);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Can't parse this chart model to JSON due to unknown errors", ex);
        }
    }
}
