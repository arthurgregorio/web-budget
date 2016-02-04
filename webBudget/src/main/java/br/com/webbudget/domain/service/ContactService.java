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
package br.com.webbudget.domain.service;

import br.com.webbudget.domain.entity.contact.Contact;
import br.com.webbudget.domain.entity.contact.Telephone;
import br.com.webbudget.domain.entity.movement.Movement;
import br.com.webbudget.domain.misc.ex.InternalServiceError;
import br.com.webbudget.domain.misc.table.Page;
import br.com.webbudget.domain.misc.table.PageRequest;
import br.com.webbudget.domain.repository.contact.IContactRepository;
import br.com.webbudget.domain.repository.contact.ITelephoneRepository;
import br.com.webbudget.domain.repository.movement.IMovementRepository;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 * Service responsavel pro todos os processos relacionados aos contatos
 *
 * @author Arthur Gregorio
 *
 * @version 1.3.0
 * @since 1.2.0, 12/04/2015
 */
@ApplicationScoped
public class ContactService {

    @Inject
    private IContactRepository contactRepository;
    @Inject
    private IMovementRepository movementRepository;
    @Inject
    private ITelephoneRepository telephoneRepository;

    /**
     * Salva um contato
     *
     * @param contact o contato a ser salvo
     */
    @Transactional
    public void saveContact(Contact contact) {

        // valida o documento do contato
        try {
            contact.validateDocument();
        } catch (Exception ex) {
            throw new InternalServiceError("error.contact.invalid-document");
        }
        
        // salva o contato
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
    @Transactional
    public Contact updateContact(Contact contact) {

        // valida o documento do contato
        try {
            contact.validateDocument();
        } catch (Exception ex) {
            throw new InternalServiceError("error.contact.invalid-document");
        }
        
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
    @Transactional
    public void deleteContact(Contact contact) {
        
        final List<Movement> movements = 
                this.movementRepository.listByContact(contact);
        
        // se tem vinculos, nao deleta
        if (!movements.isEmpty()) {
            throw new InternalServiceError("error.contact.has-movements");
        }
        
        this.contactRepository.delete(contact);
    }

    /**
     * 
     * @param filter
     * @param blocked
     * @param pageRequest
     * @return 
     */
    public Page<Contact> listContactsLazilyByFilter(String filter, Boolean blocked, PageRequest pageRequest) {
        return this.contactRepository.listLazilyByFilter(filter, blocked, pageRequest);
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
