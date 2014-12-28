package br.com.webbudget.application.controller.entries;

import br.com.webbudget.application.ViewState;
import br.com.webbudget.application.components.MessagesFactory;
import br.com.webbudget.domain.entity.users.Contact;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
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
public class ContactBean implements Serializable {

    @Getter
    private ViewState viewState;
    
    @Getter
    private Contact contact;
    @Getter
    private List<Contact> contacts;
    
    @Setter
    @ManagedProperty("#{messagesFactory}")
    private transient MessagesFactory messages;
    
    private final Logger LOG = LoggerFactory.getLogger(ContactBean.class);
}
