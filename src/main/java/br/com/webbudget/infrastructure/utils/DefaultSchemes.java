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

/**
 * Default database schemes list
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 23/09/2018
 */
public interface DefaultSchemes {

    // the configuration
    String CONFIGURATION = "configuration";
    String CONFIGURATION_AUDIT = "configuration_audit";

    // the registration schema
    String REGISTRATION = "registration";
    String REGISTRATION_AUDIT = "registration_audit";

    // the journal schema
    String JOURNAL = "journal";
    String JOURNAL_AUDIT = "journal_audit";

    // the financial schema
    String FINANCIAL = "financial";
    String FINANCIAL_AUDIT = "financial_audit";
}
