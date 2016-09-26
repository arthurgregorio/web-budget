/*
 * Copyright (C) 2016 Arthur Gregorio, AG.Software
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
package br.com.webbudget.domain.model.service;

import br.com.webbudget.application.channels.WebSocketSessions;
import br.com.webbudget.application.component.table.Page;
import br.com.webbudget.application.component.table.PageRequest;
import br.com.webbudget.application.producer.qualifier.AuthenticatedUser;
import br.com.webbudget.domain.misc.ex.InternalServiceError;
import br.com.webbudget.domain.model.entity.tools.Message;
import br.com.webbudget.domain.model.entity.tools.UserMessage;
import br.com.webbudget.domain.model.repository.tools.IMessageRepository;
import br.com.webbudget.domain.model.repository.tools.IUserMessageRepository;
import br.com.webbudget.domain.model.security.User;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.2.0, 11/04/2016
 */
@ApplicationScoped
public class MessagingService {

    @Inject
    @AuthenticatedUser
    private User authenticatedUser;

    @Inject
    private AccountService accountService;

    @Inject
    private WebSocketSessions sessions;
    
    @Inject
    private IMessageRepository messageRepository;
    @Inject
    private IUserMessageRepository userMessageRepository;

    /**
     * Realiza as validacoes necesarias e envia a mensagem
     *
     * @param message a mensagem a ser enviada
     */
    @Transactional
    public void sendMessage(Message message) {

        // valida se tem ao menos um destinatario
        if (!message.hasRecipients()) {
            throw new InternalServiceError("error.message.no-recipients");
        }

        // pega a lista de destinatarios
        final List<User> recipients = message.getRecipients();
        
        // salva a mensagem
        message = this.messageRepository.save(message);
        
        // envia para os destinatarios
        for (User recipient : recipients) {
            this.userMessageRepository.save(new UserMessage(recipient, message));
        }
        
        // notifica todas as sessoes abertas 
        this.sessions.notifyOpenSessions();
    }

    /**
     *
     * @param message
     */
    @Transactional
    public void deleteMessage(Message message) {
        message.setDeleted(true);
        this.messageRepository.save(message);
    }
    
    /**
     * 
     * @param message 
     */
    @Transactional
    public void deleteUserMessage(UserMessage message) {
        message.setDeleted(true);
        this.userMessageRepository.save(message);
    }

    /**
     *
     * @param userMessageId
     * @return
     */
    @Transactional
    public UserMessage detailReceivedMessage(long userMessageId) {

        final UserMessage userMessage = 
                this.userMessageRepository.findById(userMessageId, false);

        final Message message = userMessage.getMessage();

        message.setSender(this.fillUserData(message.getSender().getId()));

        final List<UserMessage> userMessages = 
                this.userMessageRepository.listByMessage(message);

        final List<User> users = new ArrayList<>();

        userMessages.stream().forEach(_userMessage -> {
            users.add(this.fillUserData(_userMessage.getRecipient().getId()));
        });

        message.setRecipients(users);
        
        // marca mensagem como lida
        userMessage.setRead(true);
        this.userMessageRepository.save(userMessage);
        
        return userMessage;
    }

    /**
     *
     * @param messageId
     * @param withRecipients
     * @return
     */
    public Message findMessageById(long messageId, boolean withRecipients) {

        final Message message
                = this.messageRepository.findById(messageId, false);

        if (withRecipients) {
            final List<UserMessage> userMessages = 
                    this.userMessageRepository.listByMessage(message);

            final List<User> users = new ArrayList<>();

            userMessages.stream().forEach(userMessage -> {
                users.add(this.fillUserData(userMessage.getRecipient().getId()));
            });

            message.setRecipients(users);
        }

        return message;
    }
    
    /**
     * 
     * @return 
     */
    public long countNewMessages() {
        return this.userMessageRepository.countUnread(this.authenticatedUser);
    }
    
    /**
     * 
     * @return 
     */
    public List<UserMessage> listUnreadMessages() {
        return this.userMessageRepository.listUnread(this.authenticatedUser);
    }

    /**
     *
     * @param filter
     * @param pageRequest
     * @return
     */
    public Page<Message> listSentMessages(String filter, PageRequest pageRequest) {
        return this.messageRepository.listSent(this.authenticatedUser, filter, pageRequest);
    }

    /**
     *
     * @param filter
     * @param pageRequest
     * @return
     */
    public Page<UserMessage> listReceivedMessages(String filter, PageRequest pageRequest) {
        return this.userMessageRepository.listReceived(this.authenticatedUser, filter, pageRequest);
    }

    /**
     *
     * @param userId
     * @return
     */
    private User fillUserData(String userId) {
        return this.accountService.findUserById(userId, false);
    }
}
