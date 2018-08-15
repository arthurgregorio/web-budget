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
package br.com.webbudget.infrastructure.feign;

import br.com.webbudget.infrastructure.utils.Configurations;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.apache.commons.lang3.StringUtils;

import static feign.Logger.Level.FULL;

/**
 * This class represents a generic REST client to be used with FEIGN
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 08/04/2018
 */
public final class FeignClientFactory {

    private static FeignClientFactory INSTANCE;

    /**
     * Private to protect the object creation process
     */
    private FeignClientFactory() { }

    /**
     * Get the actual instance of the client factory
     *
     * @return a instance of this class configured to be used with feign
     */
    public static FeignClientFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FeignClientFactory();
        }
        return INSTANCE;
    }

    /**
     * Build a new client for a given URL
     *
     * @param <T> the type of this result object
     * @param clazz a class of the client implementation
     * @param targetUri the URL to be used
     * @return the feign client
     */
    public <T> T build(Class<T> clazz, String targetUri) {
        return getDefaults()
                .target(clazz, targetUri);
    }

    /**
     * Build a new client for a given URL
     *
     * @param <T> the type of this result object
     * @param clazz a class of the client implementation
     * @param defaultTarget the URL to be used, but this time using the enum {@link DefaultTarget}
     * @return the feign client
     */
    public <T> T build(Class<T> clazz, DefaultTarget defaultTarget) {
        return getDefaults()
                .target(clazz, defaultTarget.build());
    }

    /**
     * Get the default values of this factory
     *
     * @return the feign builder to create the clients
     */
    private Feign.Builder getDefaults() {
        return Feign.builder()
                .logLevel(FULL)
                .decode404()
                .encoder(new JacksonEncoder(this.configureMapper()))
                .decoder(new JacksonDecoder(this.configureMapper()));
    }

    /**
     * Method to configure the Jackson JSON mappers
     * 
     * @return jakcson {@link ObjectMapper} for JSON objects
     */
    private ObjectMapper configureMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
    }

    /**
     * This is a default enum to allocate the base URL of some services
     */
    public enum DefaultTarget {

        ZIPCODE_SERVICE_URI("zipcode-search.service", null);

        private final String uriConfig;
        private final String uriPath;

        /**
         * Constructor...
         *
         * @param uriConfig the URI configuration key to search in the parametrized bundles
         * @param uriPath the path to be used with this URL
         */
        DefaultTarget(String uriConfig, String uriPath) {
            this.uriConfig = uriConfig;
            this.uriPath = uriPath;
        }

        /**
         * Build the new URI
         *
         * @return the new URI
         */
        public String build() {
            final String uri = Configurations.get(this.uriConfig);
            return uri + (StringUtils.isNotBlank(this.uriPath) ? this.uriPath : "");
        }
    }
}
