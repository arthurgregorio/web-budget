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

import br.com.webbudget.domain.entity.message.PrivateMessage;
import br.com.webbudget.domain.security.User;
import br.com.webbudget.domain.entity.message.UserPrivateMessage;
import br.com.webbudget.domain.misc.ex.WbDomainException;
import br.com.webbudget.domain.repository.user.IPrivateMessageRepository;
import br.com.webbudget.domain.repository.user.IUserPrivateMessageRepository;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 * Servico de mensagens privadas, ele faz todo o trabalho relacionado as mensa-
 * gens privadas do sistema
 *
 * @author Arthur Gregorio
 *
 * @version 1.2.0
 * @since 1.0.0, 07/05/2014
 */
@ApplicationScoped
public class PrivateMessageService {

    @Inject
    private IPrivateMessageRepository privateMessageRepository;
    @Inject
    private IUserPrivateMessageRepository userPrivateMessageRepository;

    /**
     *
     * @param privateMessage
     */
    public void savePrivateMessage(PrivateMessage privateMessage) {

        if (privateMessage.getRecipients() == null || privateMessage.getRecipients().isEmpty()) {
            throw new WbDomainException("private-message.validate.no-receipts");
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
    @Transactional
    public PrivateMessage findPrivateMessageById(long privateMessageId) {
        return this.privateMessageRepository.findById(privateMessageId, false);
    }

    /**
     *
     * @param userPrivateMessageId
     * @return
     */
    @Transactional
    public UserPrivateMessage findUserPrivateMessageById(long userPrivateMessageId) {
        return this.userPrivateMessageRepository.findById(userPrivateMessageId, false);
    }

    /**
     *
     * @param user
     * @param showUnread
     * @return
     */
    @Transactional
    public List<UserPrivateMessage> listMessagesByUser(User user, Boolean showUnread) {
        return this.userPrivateMessageRepository.listByUser(user, showUnread);
    }

    /**
     * 
     * @param userId
     * @return 
     */
    @Transactional
    public List<PrivateMessage> listPrivateMessagesSent(String userId) {
        return this.privateMessageRepository.listSent(userId);
    }

    /**
     *
     * @param privateMessage
     * @return
     */
    @Transactional
    public List<UserPrivateMessage> listPrivateMessageReceipts(PrivateMessage privateMessage) {
        return this.userPrivateMessageRepository.listReceipts(privateMessage);
    }
}
