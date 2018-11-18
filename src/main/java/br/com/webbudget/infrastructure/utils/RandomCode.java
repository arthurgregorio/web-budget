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
package br.com.webbudget.infrastructure.utils;

/**
 * Class that's generate sequences of random codes to use in unique identifications of certain objects of this project
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 22/11/2017
 */
public final class RandomCode {

    private static final String NUMERIC = "1234567890";
    private static final String ALPHANUMERIC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * Generates a random code of numbers to identify something
     *
     * @param length the length of this random code
     * @return the generated random code
     */
    public static String numeric(int length) {
        return RandomCode.generate(length, RandomCode.NUMERIC);
    }

    /**
     * Generates a random code of numbers and chars to identify something
     *
     * @param length the length of this random code
     * @return the generated random code
     */
    public static String alphanumeric(int length) {
        return RandomCode.generate(length, RandomCode.ALPHANUMERIC);
    }

    /**
     * The real method that generate the codes based on the current time in nanos
     *
     * @param length the size of the code
     * @param baseSequence the sequence to be used to create de codes
     * @return the generated code
     */
    private static String generate(int length, String baseSequence) {

        long decimalNumber = System.nanoTime();

        int mod;
        int codeLength = 0;

        final StringBuilder builder = new StringBuilder();

        while (decimalNumber != 0 && codeLength < length) {
            mod = (int) (decimalNumber % baseSequence.length());
            builder.append(baseSequence.substring(mod, mod + 1));
            decimalNumber = decimalNumber / baseSequence.length();
            codeLength++;
        }

        return builder.toString();
    }
}
