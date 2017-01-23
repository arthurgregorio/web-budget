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
package br.com.webbudget.domain.model.entity.converter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Converter customizado para dar suporte para LocalTime no JPA
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.2.0, 09/02/2015
 */
@Converter(autoApply = true)
public class JPALocalTimeConverter implements AttributeConverter<LocalTime, String> {

    /**
     * Converte o valor da entity para a base de dados
     * 
     * @param attribute
     * @return 
     */
    @Override
    public String convertToDatabaseColumn(LocalTime attribute) {
        return attribute == null ? null : attribute.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    /**
     * Converte o valor da base para entidade
     * 
     * @param dbData
     * @return 
     */
    @Override
    public LocalTime convertToEntityAttribute(String dbData) {
        return dbData == null ? null : LocalTime.parse(dbData, DateTimeFormatter.ofPattern("HH:mm"));
    }
}
