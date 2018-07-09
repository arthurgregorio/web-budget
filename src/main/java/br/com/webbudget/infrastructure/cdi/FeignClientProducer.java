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
package br.com.webbudget.infrastructure.cdi;

import br.com.webbudget.domain.repositories.registration.AddressRepository;
import br.com.webbudget.infrastructure.feign.FeignClientFactory;
import static br.com.webbudget.infrastructure.feign.FeignClientFactory.DefaultTarget.ZIPCODE_SERVICE_URI;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * Feign client producer to enable injection of the feign REST client
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 08/04/2018
 */
@ApplicationScoped
public class FeignClientProducer {

    /**
     * Produce the {@link AddressRepository} REST client to access ViaCEP services
     * 
     * @return the {@link AddressRepository} client
     */
    @Produces
    @ApplicationScoped
    AddressRepository addressRepositoryProducer() {
        return FeignClientFactory.getInstance()
                .build(AddressRepository.class, ZIPCODE_SERVICE_URI);
    }
}