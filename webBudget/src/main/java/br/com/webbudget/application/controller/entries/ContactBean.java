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
package br.com.webbudget.application.controller.entries;

import br.com.webbudget.application.controller.AbstractBean;
import br.com.webbudget.domain.entity.contact.Contact;
import br.com.webbudget.domain.entity.contact.ContactType;
import br.com.webbudget.domain.service.ContactService;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller para o CRUD de contatos 
 * 
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.2.0, 12/04/2015
 */
@ViewScoped
@ManagedBean
public class ContactBean extends AbstractBean {

    @Getter
    private Contact contact;
    @Getter
    private List<Contact> contacts;

    @Setter
    @ManagedProperty("#{contactService}")
    private ContactService contactService;

    /**
     *
     * @return
     */
    @Override
    protected Logger initializeLogger() {
        return LoggerFactory.getLogger(ContactBean.class);
    }

    /**
     *
     */
    public void initializeListing() {
        this.viewState = ViewState.LISTING;
        this.contacts = this.contactService.listContacts(null);
    }

    /**
     *
     * @param contactId
     */
    public void initializeForm(long contactId) {

        if (contactId == 0) {
            this.viewState = ViewState.ADDING;
            this.contact = new Contact();
        } else {
            this.viewState = ViewState.EDITING;
            this.contact = this.contactService.findContactById(contactId);
        }
    }

    /**
     *
     * @return
     */
    public String changeToAdd() {
        return "formContact.xhtml?faces-redirect=true";
    }

    /**
     *
     * @return
     */
    public String changeToListing() {
        return "listContacts.xhtml?faces-redirect=true";
    }

    /**
     *
     * @param contactId
     * @return
     */
    public String changeToEdit(long contactId) {
        return "formContact.xhtml?faces-redirect=true&contactId=" + contactId;
    }

    /**
     *
     * @param contactId
     */
    public void changeToDelete(long contactId) {
        this.contact = this.contactService.findContactById(contactId);
        this.openDialog("deleteContactDialog", "dialogDeleteContact");
    }

    /**
     * @return pagina para cancelar e voltar para a listagem
     */
    public String doCancel() {
        return "listContacts.xhtml?faces-redirect=true";
    }

    /**
     *
     */
    public void doSave() {

        try {
            this.contactService.saveContact(this.contact);
            this.contact = new Contact();

            this.info("contact.action.saved", true);
        } catch (Exception ex) {
            this.logger.error("ContactBean#doSave found erros", ex);
            this.fixedError(ex.getMessage(), true);
        }
    }

    /**
     *
     */
    public void doUpdate() {

        try {
            this.contact = this.contactService.updateContact(this.contact);

            this.info("contact.action.updated", true);
        } catch (Exception ex) {
            this.logger.error("ContactBean#doUpdate found erros", ex);
            this.fixedError(ex.getMessage(), true);
        }
    }

    /**
     *
     */
    public void doDelete() {

        try {
            this.contactService.deleteContact(this.contact);
            this.contacts = this.contactService.listContacts(false);

            this.info("contact.action.deleted", true);
        } catch (Exception ex) {
            this.logger.error("ContactBean#doDelete found erros", ex);
            this.fixedError(ex.getMessage(), true);
        } finally {
            this.update("contactsList");
            this.closeDialog("dialogDeleteContact");
        }
    }

    /**
     *
     * @return
     */
    public ContactType[] getAvailableContactTypes() {
        return ContactType.values();
    }
}
