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
package br.com.webbudget.infrastructure.jpa;

import br.com.webbudget.application.components.dto.Color;
import javax.persistence.AttributeConverter;

/**
 * A simple JPA converter to use in conjunction of {@link Color} for persistent
 * purpose
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.3.1, 22/01/2017
 */
public class ColorConverter implements AttributeConverter<Color, String>{

    /**
     * {@inheritDoc }
     *
     * @param attribute
     * @return
     */
    @Override
    public String convertToDatabaseColumn(Color attribute) {
        return attribute != null ? attribute.toString() : null;
    }

    /**
     * {@inheritDoc }
     *
     * @param dbData
     * @return
     */
    @Override
    public Color convertToEntityAttribute(String dbData) {
        return dbData != null ? Color.parse(dbData) : null;
    }
}
