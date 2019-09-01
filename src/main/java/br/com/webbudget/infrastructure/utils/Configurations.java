/*
 * Copyright (C) 2015 Arthur Gregorio, AG.Software
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
package br.com.webbudget.infrastructure.utils;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * This is a utility class to help the process of get some configuration from the system properties file
 *
 * @author Arthur Gregorio
 *
 * @version 1.1.0
 * @since 2.0.0, 07/07/2015
 */
public final class Configurations {

    private static final ResourceBundle CONFIG_PROPERTIES;

    static {
        CONFIG_PROPERTIES = ResourceBundle.getBundle("application");
    }

    /**
     * Search in the configuration source for a key and retrieve his value, as {@link String}
     *
     * @param configuration the configuration to search his value
     * @return the value for this configuration
     */
    public static String get(String configuration) {
        try {
            return CONFIG_PROPERTIES.getString(Objects.requireNonNull(configuration));
        } catch (MissingResourceException ex) {
            return null;
        }
    }

    /**
     * Same as {@link #get(String)} but return the value as {@link Boolean}
     *
     * @param configuration the configuration to search his value
     * @return the value for this configuration
     */
    public static boolean getAsBoolean(String configuration) {
        return Boolean.parseBoolean(Objects.requireNonNull(get(configuration)));
    }

    /**
     * Same as {@link #get(String)} but return the value as {@link Integer}
     *
     * @param configuration the configuration to search his value
     * @return the value for this configuration
     */
    public static int getAsInteger(String configuration) {
        return Integer.parseInt(Objects.requireNonNull(get(configuration)));
    }

    /**
     * Retrieve the base URL of the application
     *
     * @return the base URL
     */
    public static String getBaseURL() {

        final FacesContext facesContext = FacesContext.getCurrentInstance();

        final HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();

        final StringBuilder builder = new StringBuilder();

        String actualPath = request.getRequestURL().toString();

        builder.append(actualPath.replace(request.getRequestURI(), ""));
        builder.append(request.getContextPath());

        return builder.toString();
    }
}
