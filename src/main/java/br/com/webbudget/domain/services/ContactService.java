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
package br.com.webbudget.domain.services;

import br.com.webbudget.domain.entities.registration.Contact;
import br.com.webbudget.domain.entities.registration.Telephone;
import br.com.webbudget.domain.repositories.registration.ContactRepository;
import br.com.webbudget.domain.repositories.registration.TelephoneRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Service responsible by all the business logic operations involving the {@link Contact}
 *
 * @author Arthur Gregorio
 *
 * @version 1.3.0
 * @since 1.2.0, 12/04/2015
 */
@ApplicationScoped
public class ContactService {

    @Inject
    private ContactRepository contactRepository;
    @Inject
    private TelephoneRepository telephoneRepository;

    /**
     * Use this method to persist a {@link Contact}
     *
     * @param contact the {@link Contact} to be persisted
     */
    @Transactional
    public void save(Contact contact) {

        contact.validateDocument();

        final List<Telephone> telephones = contact.getTelephones();

        final Contact saved = this.contactRepository.save(contact);

        telephones.forEach(telephone -> {
            telephone.setContact(saved);
            this.telephoneRepository.save(telephone);
        });
    }

    /**
     * Use this method to update a persisted {@link Contact}
     *
     * @param contact the {@link Contact} to be updated
     * @return the updated {@link Contact}
     */
    @Transactional
    public Contact update(Contact contact) {

        contact.validateDocument();

        // delete the old numbers
        contact.getDeletedTelephones().forEach(telephone -> {
            this.telephoneRepository.attachAndRemove(telephone);
        });

        // get the new ones and save the contact to save the numbers
        final List<Telephone> telephones = contact.getTelephones();

        final Contact saved = this.contactRepository.saveAndFlushAndRefresh(contact);

        telephones.forEach(telephone -> {
            telephone.setContact(saved);
            this.telephoneRepository.saveAndFlush(telephone);
        });

        this.contactRepository.refresh(saved);

        return saved;
    }

    /**
     * Use this method to delete a persisted {@link Contact}
     *
     * @param contact the {@link Contact} to be deleted
     */
    @Transactional
    public void delete(Contact contact) {
        this.contactRepository.attachAndRemove(contact);
    }
}
