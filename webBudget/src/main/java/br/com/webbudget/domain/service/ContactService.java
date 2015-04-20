/*
 * Copyright (C) 2015 Arthur
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
package br.com.webbudget.domain.service;

import br.com.webbudget.domain.entity.contact.Contact;
import br.com.webbudget.domain.entity.contact.Telephone;
import br.com.webbudget.domain.repository.contact.IContactRepository;
import br.com.webbudget.domain.repository.contact.ITelephoneRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.2.0, 12/04/2015
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ContactService {

    @Autowired
    private IContactRepository contactRepository;
    @Autowired
    private ITelephoneRepository telephoneRepository;

    /**
     * Salva um contato
     *
     * @param contact o contato a ser salvo
     */
    public void saveContact(Contact contact) {

        final List<Telephone> telephones = contact.getTelephones();

        contact = this.contactRepository.save(contact);

        for (Telephone telephone : telephones) {
            telephone.setContact(contact);
            this.telephoneRepository.save(telephone);
        }
    }

    /**
     * Atualiza um contato
     *
     * @param contact o contato a ser atualizado
     * @return o contato atualizado
     */
    public Contact updateContact(Contact contact) {

        // deleta os telefone deletados na grid
        if (!contact.getDeletedTelephones().isEmpty()) {
            for (Telephone telephone : contact.getDeletedTelephones()) {
                this.telephoneRepository.delete(telephone);
            }
        }

        // captura a lista dos telefones restantes
        final List<Telephone> telephones = contact.getTelephones();

        contact = this.contactRepository.save(contact);

        // limpa a lista de telefones
        contact.getTelephones().clear();
        
        // atualiza
        for (Telephone telephone : telephones) {
            telephone.setContact(contact);
            contact.addTelephone(this.telephoneRepository.save(telephone));
        }

        return contact;
    }

    /**
     * Deleta um contato
     *
     * @param contact o contato a ser deletado
     */
    public void deleteContact(Contact contact) {
        this.contactRepository.delete(contact);
    }

    /**
     *
     * @param blocked
     * @return
     */
    public List<Contact> listContacts(Boolean blocked) {
        return this.contactRepository.listByStatus(blocked);
    }
    
    /**
     * 
     * @param filter
     * @param blocked 
     * @return 
     */
    public List<Contact> listContactsByFilter(String filter, Boolean blocked) {
        return this.contactRepository.listByFilter(filter, blocked);
    }

    /**
     *
     * @param contactId
     * @return
     */
    public Contact findContactById(long contactId) {
        return this.contactRepository.findById(contactId, false);
    }
}
