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
package br.com.webbudget.infrastructure.mail;

import br.com.webbudget.infrastructure.i18n.MessageSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

/**
 * Simple utility class to be used when you are creating e-mail templates
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.1.0, 01/09/2019
 */
public class MailUtils {

    /**
     * Define a simple function to translate any text inside the template
     *
     * @return the {@link Function} to translate a text
     */
    public static Function<String, String> translateFunction() {
        return MessageSource::get;
    }

    /**
     * Convert a {@link LocalDate} as string using dd/MM/yyyy as a pattern
     *
     * @param localDate to be converted to {@link String}
     * @return the {@link String} of the {@link LocalDate}
     */
    public static String toString(LocalDate localDate) {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(localDate);
    }

    /**
     * Convert a {@link LocalTime} as string using HH:mm as a pattern
     *
     * @param localTime to be converted to {@link String}
     * @return the {@link String} of the {@link LocalTime}
     */
    public static String toString(LocalTime localTime) {
        return DateTimeFormatter.ofPattern("HH:mm").format(localTime);
    }

    /**
     * Convert a {@link LocalDateTime} as string using dd/MM/yyyy HH:mm as a pattern
     *
     * @param localDateTime to be converted to {@link String}
     * @return the {@link String} of the {@link LocalDateTime}
     */
    public static String toString(LocalDateTime localDateTime) {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").format(localDateTime);
    }
}
