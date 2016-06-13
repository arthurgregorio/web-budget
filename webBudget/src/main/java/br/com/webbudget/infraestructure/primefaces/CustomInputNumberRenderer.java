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
package br.com.webbudget.infraestructure.primefaces;

import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.inputnumber.InputNumber;
import org.primefaces.component.inputnumber.InputNumberRenderer;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.util.HTML;

/**
 * Customizacao da renderer do inputNumber para acertar sua classe CSS no 
 * layout da aplicacao
 * 
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.2.0, 28/01/2016
 */
public class CustomInputNumberRenderer extends InputNumberRenderer {

    private static final String CUSTOM_CLASSES = "form-control";
    
    /**
     * GAMBIARRA NECESSARIA! Zeus tenha piedade de mim e do programador que fez
     * a merda do inputnumber, o class deve ser escrito no input de saida e nao
     * na span que envolve o componente. Alias, pq tem um span la? :P
     * 
     * @param context
     * @param inputNumber
     * @param clientId
     * @throws IOException 
     */
    @Override
    protected void encodeOutput(FacesContext context, InputNumber inputNumber, String clientId) throws IOException {
        
        final ResponseWriter writer = context.getResponseWriter();
        
        final String inputId = clientId + "_input";

        // como o PF nao sabe colocar a class na input, coloca apenas 
        // na span que envolve o elemento, fiz esse bypass para que quando
        // renderizar o elemento a classe do form-control seja adicionada
        String defaultClass = InputText.STYLE_CLASS + " pe-inputNumber " + CUSTOM_CLASSES;
        
        defaultClass = inputNumber.isValid() ? defaultClass : defaultClass + " ui-state-error";
        defaultClass = !inputNumber.isDisabled() ? defaultClass : defaultClass + " ui-state-disabled";

        writer.startElement("input", null);
        writer.writeAttribute("id", inputId, null);
        writer.writeAttribute("name", inputId, null);
        writer.writeAttribute("type", inputNumber.getType(), null);

        renderPassThruAttributes(context, inputNumber, HTML.INPUT_TEXT_ATTRS_WITHOUT_EVENTS);
        renderDomEvents(context, inputNumber, HTML.INPUT_TEXT_EVENTS);

        if (inputNumber.isReadonly()) {
            writer.writeAttribute("readonly", "readonly", "readonly");
        }
        if (inputNumber.isDisabled()) {
            writer.writeAttribute("disabled", "disabled", "disabled");
        }

        writer.writeAttribute("class", defaultClass, "");

        writer.endElement("input");
    }
}
