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

import static java.util.Objects.requireNonNull;

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

        requireNonNull(x);
        requireNonNull(total);

        x = x.setScale(2, RoundingMode.CEILING);

        final BigDecimal percentage = x.multiply(new BigDecimal(100)).divide(total, 2, RoundingMode.CEILING);

        return percentage.intValue();
    }

    /**
     * Same as {@link #percentageOf(BigDecimal, BigDecimal, boolean)} but this one takes a third parameter to define if
     * the percentage value is bigger than 100, limit it to 100 only.
     *
     * Example: if the result is 200, limit this to 100. Or if the X is greater than the total, return 100 directly
     *
     * @param x the amount representing the value that we want to find (% of)
     * @param total representing 100%
     * @param compareToLimit if we want to check and limit the value to 100 if the percentage is above 100
     * @return the corresponding percentage of the total value
     */
    public static int percentageOf(BigDecimal x, BigDecimal total, boolean compareToLimit) {
        if (compareToLimit) {
            if (x.compareTo(total) >= 0) {
                return 100;
            } else {
                int percentage = percentageOf(x, total);
                return percentage > 100 ? 100 : percentage;
            }
        } else {
            return percentageOf(x, total);
        }
    }
}
