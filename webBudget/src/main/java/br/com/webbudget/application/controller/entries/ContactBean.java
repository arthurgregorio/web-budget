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
import br.com.webbudget.domain.model.entity.entries.Contact;
import br.com.webbudget.domain.model.entity.entries.ContactType;
import br.com.webbudget.domain.model.entity.entries.NumberType;
import br.com.webbudget.domain.model.entity.entries.Telephone;
import br.com.webbudget.domain.misc.AddressFinder;
import br.com.webbudget.domain.misc.AddressFinder.Address;
import br.com.webbudget.domain.misc.exceptions.InternalServiceError;
import br.com.webbudget.application.component.table.AbstractLazyModel;
import br.com.webbudget.application.component.table.Page;
import br.com.webbudget.application.component.table.PageRequest;
import br.com.webbudget.domain.model.service.ContactService;
import java.util.List;
import java.util.Map;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.SortOrder;

/**
 * Controller para a view de contatos
 *
 * @author Arthur Gregorio
 *
 * @version 1.2.0
 * @since 1.2.0, 12/04/2015
 */
@Named
@ViewScoped
public class ContactBean extends AbstractBean {

    @Getter
    @Setter
    private String filter;
    
    @Getter
    private Contact contact;
    @Getter
    private Telephone telephone;
    
    @Inject
    private ContactService contactService;
    @Inject
    private AddressFinder addressFinderService;

    @Getter
    private final AbstractLazyModel<Contact> contactsModel;
    
    /**
     * 
     */
    public ContactBean() {

        this.contactsModel = new AbstractLazyModel<Contact>() {
            @Override
            public List<Contact> load(int first, int pageSize, String sortField, 
                    SortOrder sortOrder, Map<String, Object> filters) {
                
                final PageRequest pageRequest = new PageRequest();
                
                pageRequest
                        .setFirstResult(first)
                        .withPageSize(pageSize)
                        .sortingBy(sortField, "inclusion")
                        .withDirection(sortOrder.name());
                
                final Page<Contact> page = contactService
                        .listContactsLazilyByFilter(filter, null, pageRequest);
                
                this.setRowCount(page.getTotalPagesInt());
                
                return page.getContent();
            }
        };
    }
    
    /**
     *
     */
    public void initializeListing() {
        this.viewState = ViewState.LISTING;
    }

    /**
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
        this.updateComponent("contactsList");
    }
    
    /**
     * @return
     */
    public String changeToAdd() {
        return "formContact.xhtml?faces-redirect=true";
    }

    /**
     * @return
     */
    public String changeToListing() {
        return "listContacts.xhtml?faces-redirect=true";
    }

    /**
     * @param contactId
     * @return
     */
    public String changeToEdit(long contactId) {
        return "formContact.xhtml?faces-redirect=true&contactId=" + contactId;
    }

    /**
     * @param contactId
     */
    public void changeToDelete(long contactId) {
        this.contact = this.contactService.findContactById(contactId);
        this.updateAndOpenDialog("deleteContactDialog", "dialogDeleteContact");
    }

    /**
     * @return
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
            this.addInfo(true, "contact.saved");
        } catch (InternalServiceError ex) {
            this.addError(true, ex.getMessage(), ex.getParameters());
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ex);
            this.addError(true, "error.undefined-error", ex.getMessage());
        }
    }

    /**
     *
     */
    public void doUpdate() {

        try {
            this.contact = this.contactService.updateContact(this.contact);
            this.addInfo(true, "contact.updated");
        } catch (InternalServiceError ex) {
            this.addError(true, ex.getMessage(), ex.getParameters());
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ex);
            this.addError(true, "error.undefined-error", ex.getMessage());
        }
    }

    /**
     *
     */
    public void doDelete() {

        try {
            this.contactService.deleteContact(this.contact);
            this.addInfo(true, "contact.deleted");
        } catch (InternalServiceError ex) {
            this.addError(true, ex.getMessage(), ex.getParameters());
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ex);
            this.addError(true, "error.undefined-error", ex.getMessage());
        } finally {
            this.updateComponent("contactsList");
            this.closeDialog("dialogDeleteContact");
        }
    }

    /**
     * Chama o servico de busca dos enderecos e completa o endereco do caboclo
     */
    public void doAddressFind() {

        try {
            final Address address = this.addressFinderService
                    .findAddressByZipcode(this.contact.getZipcode());

            this.contact.setStreet(address.getLogradouro());
            this.contact.setComplement(address.getComplemento());
            this.contact.setProvince(address.getFullUfName());
            this.contact.setCity(address.getLocalidade());
            this.contact.setNeighborhood(address.getBairro());

            this.updateComponent("addressBox");
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ex);
            this.addError(true, "error.contact.find-address-error");
        }
    }
    
    /**
     * Abre a popup de adicionar telefones
     */
    public void showTelephoneDialog() {
        this.telephone = new Telephone();
        this.updateAndOpenDialog("telephoneDialog", "dialogTelephone");
    }
    
    /**
     * Adiciona um telefone
     */
    public void addTelephone() {
        this.contact.addTelephone(this.telephone);
        this.updateComponent("telephonesList");
        this.closeDialog("dialogTelephone");
    }
    
    /**
     * Deleta um telefone da lista
     * 
     * @param telephone o telefone a ser deletado
     */
    public void deleteTelephone(Telephone telephone) {
        this.contact.removeTelephone(telephone);
        this.updateComponent("telephonesList");
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
