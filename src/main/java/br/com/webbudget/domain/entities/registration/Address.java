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
package br.com.webbudget.domain.entities.registration;

import br.com.webbudget.infrastructure.feign.ProvinceDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * The representation of the address of a {@link Contact}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 08/04/2018
 */
@ToString
@EqualsAndHashCode
public class Address implements Serializable {

    @Getter
    @Setter
    @JsonProperty("cep")
    private String zipcode;
    @Getter
    @Setter
    @JsonProperty("logradouro")
    private String street;
    @Getter
    @Setter
    @JsonProperty("complemento")
    private String complement;
    @Getter
    @Setter
    @JsonProperty("bairro")
    private String neighborhood;
    @Getter
    @Setter
    @JsonProperty("localidade")
    private String city;
    @Getter
    @Setter
    @JsonProperty("uf")
    @JsonDeserialize(using = ProvinceDeserializer.class)
    private String province;
}
