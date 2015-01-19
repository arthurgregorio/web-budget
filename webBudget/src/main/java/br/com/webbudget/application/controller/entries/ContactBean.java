package br.com.webbudget.application.controller.entries;

import br.com.webbudget.application.controller.AbstractBean;
import br.com.webbudget.domain.entity.users.Contact;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 06/04/2014
 */
@ViewScoped
@ManagedBean
public class ContactBean extends AbstractBean {

    @Getter
    private Contact contact;
    @Getter
    private List<Contact> contacts;

    /**
     * 
     * @return 
     */
    @Override
    protected Logger initializeLogger() {
        return LoggerFactory.getLogger(ContactBean.class);
    }
}
