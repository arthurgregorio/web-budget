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
import static feign.Logger.Level.FULL;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.apache.commons.lang3.StringUtils;

/**
 * FIXME create JavaDoc
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 08/04/2018
 */
public final class FeignClientFactory {

    /**
     * 
     */
    private FeignClientFactory() { }

    /**
     *
     * @return
     */
    public static FeignClientFactory getInstance() {
        return new FeignClientFactory();
    }

    /**
     *
     * @param <T>
     * @param clazz
     * @param targetUri
     * @return
     */
    public <T> T build(Class<T> clazz, String targetUri) {
        return getDefaults()
                .target(clazz, targetUri);
    }

    /**
     *
     * @param <T>
     * @param clazz
     * @param targetUri
     * @return
     */
    public <T> T build(Class<T> clazz, DefaultTarget targetUri) {
        return getDefaults()
                .target(clazz, targetUri.build());
    }

    /**
     *
     * @return
     */
    protected Feign.Builder getDefaults() {
        return Feign.builder()
                .logLevel(FULL)
                .decode404()
                .encoder(new JacksonEncoder(this.configureMapper()))
                .decoder(new JacksonDecoder(this.configureMapper()));
    }

    /**
     * 
     * @return 
     */
    private ObjectMapper configureMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
    }

    /**
     *
     */
    public enum DefaultTarget {

        ZIPCODE_SERVICE_URI("zipcode-search.service", null);

        private final String uriConfig;
        private final String uriPath;

        /**
         *
         * @param uriConfig
         * @param uriPath
         */
        private DefaultTarget(String uriConfig, String uriPath) {
            this.uriConfig = uriConfig;
            this.uriPath = uriPath;
        }

        /**
         *
         * @return
         */
        public String build() {
            final String uri = Configurations.get(this.uriConfig);
            return uri + (StringUtils.isNotBlank(this.uriPath) ? this.uriPath : "");
        }
    }
}
