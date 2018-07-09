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
package br.com.webbudget.domain.repositories.registration;

import br.com.webbudget.domain.entities.registration.Address;
import br.com.webbudget.domain.entities.registration.Contact;
import feign.Param;
import feign.RequestLine;

/**
 * The rest repository for the {@link Address} of the {@link Contact}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 08/04/2018
 */
public interface AddressRepository {

    /**
     * Find the {@link Address} of a contact by invoking a REST service on the Viacep.com
     *
     * @param zipcode the zip code to find the CEP
     * @return the {@link Address} of this zip code
     */
    @RequestLine("GET /ws/{zipcode}/json/")
    Address findByZipcode(@Param("zipcode") String zipcode);
}
