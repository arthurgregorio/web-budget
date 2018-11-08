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
package br.com.webbudget.domain.entities.registration;

import br.com.caelum.stella.format.CNPJFormatter;
import br.com.caelum.stella.format.CPFFormatter;
import br.com.caelum.stella.format.Formatter;
import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.Validator;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import org.apache.commons.lang3.StringUtils;

/**
 * The enum with the possible types of a {@link Contact}
 *
 * @author Arthur Gregorio
 *
 * @version 1.2.0
 * @since 1.2.0, 11/04/2015
 */
public enum ContactType {

    LEGAL("contact-type.legal", new CNPJValidator(false), new CNPJFormatter()),
    PERSONAL("contact-type.personal", new CPFValidator(false), new CPFFormatter());

    private final String description;
    
    private final Formatter formatter;
    private final Validator<String> validator;

    /**
     * Default constructor
     * 
     * @param description the description for this enum, also is the i18n key
     * @param validator the validator
     * @param formatter the formatter
     */
    ContactType(String description, Validator<String> validator, Formatter formatter) {
        this.formatter = formatter;
        this.validator = validator;
        this.description = description;
    }

    /**
     * Validate the document of the {@link Contact} by the type
     * 
     * @param document the document to be validated
     */
    public void validateDocument(String document) {
        try {
            this.validator.assertValid(document.replace("\\w", ""));
        } catch (Exception ex) {
            throw new BusinessLogicException("error.contact.invalid-document");
        }
    }
    
    /**
     * Format the document by the type of the {@link Contact}
     *
     * @param document the document to be formatted
     * @return the formatted document {@link String}
     */
    public String formatDocument(String document) {
        return StringUtils.isNotBlank(document) ? this.formatter.format(document) : "";
    }

    /**
     * {@inheritDoc}
     * 
     * @return 
     */
    @Override
    public String toString() {
        return this.description;
    }
}
