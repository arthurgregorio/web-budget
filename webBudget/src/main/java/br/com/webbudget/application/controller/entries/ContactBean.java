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

import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.webbudget.application.controller.AbstractBean;
import br.com.webbudget.domain.entity.contact.Contact;
import br.com.webbudget.domain.entity.contact.ContactType;
import br.com.webbudget.domain.entity.contact.NumberType;
import br.com.webbudget.domain.entity.contact.Telephone;
import br.com.webbudget.domain.service.AddressFinderService;
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
    @Setter
    private String filter;
    
    @Getter
    private Contact contact;
    @Getter
    private Telephone telephone;
    
    @Getter
    private List<Contact> contacts;

    @Setter
    @ManagedProperty("#{contactService}")
    private ContactService contactService;
    @Setter
    @ManagedProperty("#{addressFinderService}")
    private AddressFinderService addressFinderService;

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
     * Pesquisa com filtro
     */
    public void filterList() {
        this.contacts = this.contactService.listContactsByFilter(this.filter);
        this.update("contactsList");
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

        if (!this.validateDocument()) {
            this.error("contact.validate.document", true);
            return;
        }
        
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

        if (!this.validateDocument()) {
            this.error("contact.validate.document", true);
            return;
        }
        
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
     * Chama o servico de busca dos enderecos e completa o endereco do caboclo
     */
    public void doAddressFind() {

        try {
            final AddressFinderService.Address address = this.addressFinderService
                    .findAddressByZipcode(this.contact.getZipcode());

            this.contact.setStreet(address.getLogradouro());
            this.contact.setComplement(address.getComplemento());
            this.contact.setProvince(address.getFullUfName());
            this.contact.setCity(address.getLocalidade());
            this.contact.setNeighborhood(address.getBairro());

            this.update("addressPanel");
        } catch (Exception ex) {
            this.logger.error("ContactBean#completeAddress found erros", ex);
            this.error("contact.action.find-address-error", true);
        }
    }
    
    /**
     * Abre a popup de adicionar telefones
     */
    public void showTelephoneDialog() {
        this.telephone = new Telephone();
        this.openDialog("telephoneDialog", "dialogTelephone");
    }
    
    /**
     * Adiciona um telefone
     */
    public void addTelephone() {
        this.contact.addTelephone(this.telephone);
        this.update("telephonesList");
        this.closeDialog("dialogTelephone");
    }
    
    /**
     * Deleta um telefone da lista
     * 
     * @param telephone o telefone a ser deletado
     */
    public void deleteTelephone(Telephone telephone) {
        this.contact.removeTelephone(telephone);
        this.update("telephonesList");
    }

    /**
     * Valida o documento do contato, se informado
     * 
     * @return true se valido, false se nao
     */
    private boolean validateDocument() {

        try {
            String document = this.contact.getDocument();

            if (document != null && !document.isEmpty()) {
                
                document = document.replace("\\w", "");

                if (this.contact.getContactType() == ContactType.LEGAL) {
                    new CNPJValidator().assertValid(document);
                } else {
                    new CPFValidator().assertValid(document);
                }
            }
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    /**
     * @return os tipos de contato disponiveis para cadastro
     */
    public ContactType[] getAvailableContactTypes() {
        return ContactType.values();
    }
    
    /**
     * @return os tipos de numero de telefone disponiveis
     */
    public NumberType[] getAvailableNumberTypes() {
        return NumberType.values();
    }
}
