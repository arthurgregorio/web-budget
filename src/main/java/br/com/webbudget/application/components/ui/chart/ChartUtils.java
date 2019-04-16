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

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Utility class used when we need to draw a chart
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 15/04/2019
 */
public final class ChartUtils {

    /**
     * Calculate the percentage of a given value in relation to other using a simple operation called 'Rule of 3'
     *
     * @param x the amount representing the value that we want to find (% of)
     * @param total representing 100%
     * @return the corresponding percentage of the total value
     */
    public static int percentageOf(BigDecimal x, BigDecimal total) {

        x = x.setScale(2, RoundingMode.HALF_UP);

        if (x == null || total == null) {
            return 0;
        }

        BigDecimal percentage;

        if (x.compareTo(total) >= 0) {
            return 100;
        } else {
            percentage = x.multiply(new BigDecimal(100)).divide(total, 2, RoundingMode.HALF_UP);
        }

        return percentage.intValue() > 100 ? 100 : percentage.intValue();
    }
}
