/*
 * Copyright (C) 2017 Arthur Gregorio, AG.Software
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
package br.com.webbudget.application.components.dto;

import java.util.concurrent.ThreadLocalRandom;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * This component is used much like a DTO to translate the colors of the UI color picker to the application domain
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.3.1, 22/01/2017
 */
@NoArgsConstructor
public final class Color {

    @Getter
    private int red;
    @Getter
    private int green;
    @Getter
    private int blue;
    @Getter
    private double alpha;

    /**
     * Constructor receiving RGB values for a new color
     *
     * @param red red value
     * @param green green value
     * @param blue blue value
     */
    public Color(int red, int green, int blue) {
        this(red, green, blue, 1.0f);
    }

    /**
     * Same as {@link #Color(int, int, int)} but this one receive the alpha value for the color
     *
     * @param red red value
     * @param green green value
     * @param blue blue value
     * @param alpha alpha value
     */
    public Color(int red, int green, int blue, double alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public String toString() {
        return "rgba(" + this.getRed() + "," + this.getGreen() + "," + this.getBlue() + "," + this.getAlpha()+ ")";
    }

    /**
     * Get a darker version of this color
     *
     * @return more darker version of this color
     */
    public Color darker() {
        return new Color(this.getRed(), this.getGreen(), this.getBlue());
    }

    /**
     * Get a lighter version of this color
     *
     * @return more lighter version of this color
     */
    public Color lighter() {
        return new Color(this.getRed(), this.getGreen(), this.getBlue(), 0.6);
    }

    /**
     * Get this color almost transparent
     *
     * @return almost transparent version of this color
     */
    public Color transparent() {
        return new Color(this.getRed(), this.getGreen(), this.getBlue(), 0.3);
    }

    /**
     * Get a random RGB color
     *
     * @return some random color
     */
    public static Color randomize() {

        int[] rgb = new int[3];

        for (int i = 0; i < 3; i++) {
            rgb[i] = ThreadLocalRandom.current().nextInt(1, 255 + 1);
        }
        return new Color(rgb[0], rgb[1], rgb[2]);
    }

    /**
     * Parse some RGB format color to this object
     *
     * @param data the color data in RGB format
     * @return the color object
     */
    public static Color parse(String data) {
        
        if (StringUtils.isBlank(data)) {
            return null;
        }
        
        final String numbers = data
                .replace(" ", "")
                .replace("rgba", "")
                .replace("rgb", "")
                .replace("(", "")
                .replace(")", "");
        
        final String[] color = numbers.split(",");
        
        if (color.length < 4) {
            return new Color(Integer.valueOf(color[0]), 
                    Integer.valueOf(color[1]), 
                    Integer.valueOf(color[2]));
        } else {
            return new Color(Integer.valueOf(color[0]), 
                    Integer.valueOf(color[1]), 
                    Integer.valueOf(color[2]), 
                    Double.valueOf(color[3]));
        }
    }
}
