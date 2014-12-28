package br.com.webbudget.application.components.validators;

import br.com.webbudget.application.components.MessagesFactory;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Validador customizado para senhas do usuario, garante a seguranca do valor<br/>
 * digitado no fomulario de cadastro ou alteracao de senha
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 03/03/2014
 */
@Component
@Scope("request")
public class PasswordValidator implements Validator {

    @Autowired
    private MessagesFactory messages;
    
    /**
     * 
     * @param context
     * @param component
     * @param value
     * @throws ValidatorException 
     */
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        if (value != null) {

            final String password = String.valueOf(value);

            if (password.length() < 5) {
                throw new ValidatorException(new FacesMessage(this.messages.getMessage(
                        "user-account.validate.password")));
            } else if (password.replaceAll("[0-9]", "").isEmpty()) {
                throw new ValidatorException(new FacesMessage(this.messages.getMessage(
                        "user-account.validate.password")));
            } else if (password.replaceAll("[a-zA-Z]", "").isEmpty()) {
                throw new ValidatorException(new FacesMessage(this.messages.getMessage(
                        "user-account.validate.password")));
            }
        } else {
            
            final boolean isSaved = (boolean) component.getAttributes().get("saved");
            
            if (!isSaved) {
                throw new ValidatorException(new FacesMessage(this.messages.getMessage(
                            "user-account.validate.password")));
            }
        }
    }
}
