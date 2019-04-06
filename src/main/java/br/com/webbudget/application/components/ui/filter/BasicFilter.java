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
package br.com.webbudget.application.components.ui.filter;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Simple and reusable abstraction to define a filter used by the controllers to help at the filtering process of any
 * data table
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 04/04/2019
 */
@ToString
@EqualsAndHashCode
abstract class BasicFilter implements Serializable {

    @Getter
    @Setter
    protected String value;
}
