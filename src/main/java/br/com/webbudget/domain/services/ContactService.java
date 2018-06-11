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

import br.com.webbudget.domain.entities.registration.Address;
import br.com.webbudget.domain.entities.registration.Contact;
import br.com.webbudget.domain.entities.registration.Telephone;
import br.com.webbudget.domain.repositories.registration.AddressRepository;
import br.com.webbudget.domain.repositories.registration.ContactRepository;
import br.com.webbudget.domain.repositories.financial.IMovementRepository;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import br.com.webbudget.domain.repositories.registration.TelephoneRepository;

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
    private AddressRepository addressRepository;
    
    @Inject
    private ContactRepository contactRepository;
    @Inject
    private IMovementRepository movementRepository;
    @Inject
    private TelephoneRepository telephoneRepository;
    
    /**
     * 
     * @param contact 
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
     * 
     * @param contact
     * @return 
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
     * 
     * @param contact 
     */
    @Transactional
    public void delete(Contact contact) {
        
//        final List<Movement> movements = 
//                this.movementRepository.listByContact(contact);
//        
//        // se tem vinculos, nao deleta
//        if (!movements.isEmpty()) {
//            throw new BusinessLogicException("error.contact.has-movements");
//        }
        
        this.contactRepository.attachAndRemove(contact);
    }
    
    /**
     * 
     * @param zipcode
     * @return 
     */
    public Address findAddressBy(String zipcode) {
        return this.addressRepository.findByZipcode(zipcode);
    }
}
