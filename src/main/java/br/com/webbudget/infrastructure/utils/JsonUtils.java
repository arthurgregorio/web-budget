/*
 * Copyright (C) 2018 Arthur Gregorio, AG.Software
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Simple helper class to manipulate JSON values with Jackson the {@link ObjectMapper}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 27/12/2017
 */
public final class JsonUtils {

    /**
     * Serialize a given object to a JSON value
     *
     * @param <T> the type of the object
     * @param object the object to be serialized
     * @return the JSON string for the given object
     * @throws JsonProcessingException if any problem occurs
     */
    public static <T> String serialize(T object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

    /**
     * Deserialize a JSON string into a object
     *
     * @param <T> the type of the returned object
     * @param json the JSON string
     * @param outputType the expected output type
     * @return the object of the type T
     * @throws IOException if any problem occurs
     */
    public static <T> T deserialize(String json, Class<T> outputType) throws IOException {
        return new ObjectMapper().readValue(json, outputType);
    }

    /**
     * Deserialize a JSON string into a object
     *
     * @param <T> the type of the returned object
     * @param json the JSON string
     * @param outputType the expected output type
     * @return the object of the type T
     * @throws IOException if any problem occurs
     */
    public static <T> T deserialize(String json, TypeReference<T> outputType) throws IOException {
        return new ObjectMapper().readValue(json, outputType);
    }
}