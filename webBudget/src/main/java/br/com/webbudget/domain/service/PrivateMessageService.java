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

import br.com.webbudget.application.exceptions.ApplicationException;
import br.com.webbudget.domain.entity.users.PrivateMessage;
import br.com.webbudget.domain.entity.users.User;
import br.com.webbudget.domain.entity.users.UserPrivateMessage;
import br.com.webbudget.domain.repository.user.IPrivateMessageRepository;
import br.com.webbudget.domain.repository.user.IUserPrivateMessageRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 07/05/2014
 */
@Service
@Transactional
public class PrivateMessageService {

    @Autowired
    private IPrivateMessageRepository privateMessageRepository;
    @Autowired
    private IUserPrivateMessageRepository userPrivateMessageRepository;
    
    /**
     * 
     * @param privateMessage 
     */
    public void savePrivateMessage(PrivateMessage privateMessage) {
        
        if (privateMessage.getRecipients() == null || privateMessage.getRecipients().isEmpty()) {
            throw new ApplicationException("private-message.validate.no-receipts");
        }
        
        // pegamos os destinatarios
        final List<User> receipts = new ArrayList<>(privateMessage.getRecipients());
        
        // salvamos a mensagem
        privateMessage = this.privateMessageRepository.save(privateMessage);
        
        // enviamos para os usuarios pela associacao das tabelas
        for (User user : receipts) {
            
            final UserPrivateMessage associative = new UserPrivateMessage();
            
            associative.setRecipient(user);
            associative.setPrivateMessage(privateMessage);
            
            // salva a mensagem
            this.userPrivateMessageRepository.save(associative);
        }
    }
    
    /**
     * 
     * @param privateMessage 
     */
    public void deletePrivateMessage(PrivateMessage privateMessage) {
        privateMessage.setDeleted(true);
        this.privateMessageRepository.save(privateMessage);
    }
    
    /**
     * 
     * @param userPrivateMessage 
     */
    public void markAsRead(UserPrivateMessage userPrivateMessage) {
        userPrivateMessage.setWasRead(true);
        this.userPrivateMessageRepository.save(userPrivateMessage);
    }
    
    /**
     * 
     * @param userPrivateMessage 
     */
    public void markAsDeleted(UserPrivateMessage userPrivateMessage) {
        userPrivateMessage.setDeleted(true);
        this.userPrivateMessageRepository.save(userPrivateMessage);
    }
    
    /**
     * 
     * @param privateMessageId
     * @return 
     */
    @Transactional(readOnly = true)
    public PrivateMessage findPrivateMessageById(long privateMessageId) {
        return this.privateMessageRepository.findById(privateMessageId, false);
    }
    
    /**
     * 
     * @param userPrivateMessageId
     * @return 
     */
    @Transactional(readOnly = true)
    public UserPrivateMessage findUserPrivateMessageById(long userPrivateMessageId) {
        return this.userPrivateMessageRepository.findById(userPrivateMessageId, false);
    }
    
    /**
     * 
     * @param user
     * @param showUnread
     * @return 
     */
    @Transactional(readOnly = true)
    public List<UserPrivateMessage> listMessagesByUser(User user, Boolean showUnread) {
        return this.userPrivateMessageRepository.listByUser(user, showUnread);
    }
    
    /**
     * 
     * @param showUnread
     * @return 
     */
    @Transactional(readOnly = true)
    public List<UserPrivateMessage> listMessagesByCurrentUser(Boolean showUnread) {
        final User user = AccountService.getCurrentAuthenticatedUser();
        return this.userPrivateMessageRepository.listByUser(user, showUnread);
    }
    
    /**
     * 
     * @return 
     */
    @Transactional(readOnly = true)
    public List<PrivateMessage> listPrivateMessagesSent() {
        return this.privateMessageRepository.listSent(AccountService.getCurrentAuthenticatedUser());
    }
    
    /**
     * 
     * @param privateMessage
     * @return 
     */
    @Transactional(readOnly = true)
    public List<UserPrivateMessage> listPrivateMessageReceipts(PrivateMessage privateMessage) {
        return this.userPrivateMessageRepository.listReceipts(privateMessage);
    }
}
