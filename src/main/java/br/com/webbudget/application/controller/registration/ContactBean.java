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
package br.com.webbudget.application.controller.registration;

import br.com.webbudget.application.components.ui.ViewState;
import br.com.webbudget.application.components.ui.table.Page;
import br.com.webbudget.application.components.ui.LazyFormBean;
import br.com.webbudget.domain.entities.registration.*;
import br.com.webbudget.domain.repositories.registration.AddressRepository;
import br.com.webbudget.domain.repositories.registration.ContactRepository;
import br.com.webbudget.domain.services.ContactService;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.SortOrder;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import static br.com.webbudget.application.components.ui.NavigationManager.PageType.*;

/**
 * The {@link Contact} maintenance routine controller
 *
 * @author Arthur Gregorio
 *
 * @version 1.3.0
 * @since 1.2.0, 12/04/2015
 */
@Named
@ViewScoped
public class ContactBean extends LazyFormBean<Contact> {

    @Getter
    @Setter
    private Telephone telephone;
    
    @Inject
    private ContactRepository contactRepository;
    @Inject
    private AddressRepository addressRepository;

    @Inject
    private ContactService contactService;

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param viewState
     */
    @Override
    public void initialize(long id, ViewState viewState) {
        this.viewState = viewState;
        this.value = this.contactRepository.findById(id).orElseGet(Contact::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initializeNavigationManager() {
        this.navigation.addPage(LIST_PAGE, "listContacts.xhtml");
        this.navigation.addPage(ADD_PAGE, "formContact.xhtml");
        this.navigation.addPage(UPDATE_PAGE, "formContact.xhtml");
        this.navigation.addPage(DETAIL_PAGE, "detailContact.xhtml");
        this.navigation.addPage(DELETE_PAGE, "detailContact.xhtml");
    }

    /**
     * {@inheritDoc}
     *
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @return
     */
    @Override
    public Page<Contact> load(int first, int pageSize, String sortField, SortOrder sortOrder) {
        return this.contactRepository.findAllBy(this.filter.getValue(),
                this.filter.getEntityStatusValue(), first, pageSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doSave() {
        this.contactService.save(this.value);
        this.value = new Contact();
        this.addInfo(true, "saved");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doUpdate() {
        this.value = this.contactService.update(this.value);
        this.addInfo(true, "updated");
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public String doDelete() {
        this.contactService.delete(this.value);
        this.addInfoAndKeep("deleted");
        return this.changeToListing();
    }

    /**
     * Find the contact address by his brazilian zip code
     */
    public void searchAddress() {

        final Address address = this.addressRepository.findByZipcode(
                this.value.getZipcode());

        this.value.setAddress(address);

        this.updateComponent("addressBox");
    }

    /**
     * Show the telephone dialog
     */
    public void showTelephoneDialog() {
        this.telephone = new Telephone();
        this.updateAndOpenDialog("telephoneDialog", "dialogTelephone");
    }
    
    /**
     * Add a number to the contact
     */
    public void addTelephone() {
        this.value.addTelephone(this.telephone);
        this.updateComponent("telephonesList");
        this.closeDialog("dialogTelephone");
    }
    
    /**
     * Add the number deleted to the list of numbers to delete from DB
     * 
     * @param telephone the number
     */
    public void deleteTelephone(Telephone telephone) {
        this.value.removeTelephone(telephone);
        this.updateComponent("telephonesList");
    }
    
    /**
     * Method to list the possible types of an {@link Contact}
     *
     * @return an array with the values of {@link ContactType} enum
     */
    public ContactType[] getContactTypes() {
        return ContactType.values();
    }

    /**
     * Method to list the possible types of an {@link Telephone}
     *
     * @return an array with the values of {@link NumberType} enum
     */
    public NumberType[] getNumberTypes() {
        return NumberType.values();
    }
}
