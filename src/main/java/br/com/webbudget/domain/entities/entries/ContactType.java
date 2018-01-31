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
package br.com.webbudget.domain.entities.entries;

import br.com.caelum.stella.format.CNPJFormatter;
import br.com.caelum.stella.format.CPFFormatter;
import br.com.caelum.stella.format.Formatter;
import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.Validator;

/**
 * Definicao dos tipos de contato
 *
 * @author Arthur Gregorio
 *
 * @version 1.1.0
 * @since 1.2.0, 11/04/2015
 */
public enum ContactType {

    LEGAL("contact-type.legal", 
            new CNPJValidator(false), 
            new CNPJFormatter()),
    PERSONAL("contact-type.personal", 
            new CPFValidator(false), 
            new CPFFormatter());

    private final String description;
    private final Formatter formatter;
    private final Validator<String> validator;

    /**
     * 
     * @param description
     * @param validator
     * @param formatter 
     */
    private ContactType(String description, Validator<String> validator, Formatter formatter) {
        this.formatter = formatter;
        this.validator = validator;
        this.description = description;
    }

    /**
     * Valida o documento do contato, se nao for valido lancara uma exception
     *
     * @param document o documento a ser validado, sem a pontuacao
     */
    public void validadeDocument(String document) {
        this.validator.assertValid(document);
    }
    
    /**
     * Formata um documento de acordo com o tipo utilizado
     * 
     * @param document o documento
     * @return o documento formatado
     */
    public String formatDocument(String document) {
        return document != null && !document.isEmpty() 
                ? this.formatter.format(document) : document;
    }

    /**
     * @return a string representando este tipo
     */
    @Override
    public String toString() {
        return this.description;
    }
}
