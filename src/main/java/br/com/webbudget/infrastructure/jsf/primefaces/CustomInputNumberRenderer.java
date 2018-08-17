/*
 * Copyright (C) 2016 Arthur Gregorio, AG.Software
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
package br.com.webbudget.infrastructure.jsf.primefaces;

import org.primefaces.component.inputnumber.InputNumber;
import org.primefaces.component.inputnumber.InputNumberRenderer;

import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * Customization for bootstrap 3 compatibility in the {@link InputNumber} component
 * 
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.2.0, 28/01/2016
 */
public class CustomInputNumberRenderer extends InputNumberRenderer {

    /**
     * {@inheritDoc}
     *
     * @param context
     * @param inputNumber
     * @param clientId
     * @param valueToRender
     * @throws IOException
     */
    @Override
    protected void encodeInput(FacesContext context, InputNumber inputNumber, String clientId, String valueToRender)
            throws IOException {

        final String styleClass = inputNumber.getInputStyleClass() + " form-control";

        inputNumber.setInputStyleClass(styleClass);
        
        super.encodeInput(context, inputNumber, clientId, valueToRender);
    }
}
