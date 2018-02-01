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
package br.com.webbudget.application.components;

import java.util.concurrent.ThreadLocalRandom;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.3.1, 22/01/2017
 */
public class Color {

    @Getter
    private int red;
    @Getter
    private int green;
    @Getter
    private int blue;
    @Getter
    private double alpha;

    /**
     *
     */
    public Color() { }

    /**
     *
     * @param red
     * @param green
     * @param blue
     */
    public Color(int red, int green, int blue) {
        this(red, green, blue, 1.0f);
    }

    /**
     *
     * @param red
     * @param green
     * @param blue
     * @param alpha
     */
    public Color(int red, int green, int blue, double alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    /**
     * @return a versao desta mesma cor em um tom mais escuro
     */
    public Color darker() {
        return new Color(this.getRed(), this.getGreen(), this.getBlue());
    }
    
    /**
     * @return a versao desta mesma cor em um tom mais claro
     */
    public Color lighter() {
        return new Color(this.getRed(), this.getGreen(), this.getBlue(), 0.6);
    }

    /**
     * @return a repesentacao em string desta cor: rgba(255,255,255,1)
     */
    @Override
    public String toString() {
        
        final StringBuilder color = new StringBuilder("rgba(");

        color.append(this.getRed());
        color.append(",");
        color.append(this.getGreen());
        color.append(",");
        color.append(this.getBlue());
        color.append(",");
        color.append(this.getAlpha());
        color.append(")");

        return color.toString();
    }

    /**
     * @return uma cor randomica
     */
    public static Color randomize() {

        int rgb[] = new int[3];

        for (int i = 0; i < 3; i++) {
            rgb[i] = ThreadLocalRandom.current().nextInt(1, 255 + 1);
        }
        return new Color(rgb[0], rgb[1], rgb[2]);
    }
    
    /**
     *
     * @param data
     * @return
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
        
        final String color[] = numbers.split(",");
        
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
