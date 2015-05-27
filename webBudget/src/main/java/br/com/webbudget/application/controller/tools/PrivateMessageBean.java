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
package br.com.webbudget.application.controller.tools;

import br.com.webbudget.application.controller.AbstractBean;
import br.com.webbudget.application.exceptions.ApplicationException;
import br.com.webbudget.domain.entity.message.PrivateMessage;
import br.com.webbudget.domain.security.User;
import br.com.webbudget.domain.entity.message.UserPrivateMessage;
import br.com.webbudget.domain.service.AccountService;
import br.com.webbudget.domain.service.EmailService;
import br.com.webbudget.domain.service.PrivateMessageService;
import java.util.ArrayList;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.1.0
 * @since 1.0.0, 12/05/2014
 */
@Named
@ViewScoped
public class PrivateMessageBean extends AbstractBean {

    @Getter
    @Setter
    private boolean selectAll;

    @Getter
    private PrivateMessage privateMessage;

    @Getter
    private List<User> users;
    @Getter
    private List<PrivateMessage> privateMessages;

    @Inject
    private transient EmailService emailService;
    @Inject
    private transient PrivateMessageService privateMessageService;

    /**
     *
     */
    public void initializeListing() {
        this.viewState = ViewState.LISTING;
//        this.privateMessages = this.privateMessageService.listPrivateMessagesSent();
    }

    /**
     *
     * @param privateMessageId
     */
    public void initializeForm(long privateMessageId) {

//        // preenchemos a lista de usuarios
//        this.users = this.privateMessageService.listUsersByStatus(false, true);
//
//        if (privateMessageId == 0) {
//            this.viewState = ViewState.ADDING;
//
//            // iniciamos e dizemos que o cara logado e o dono da mensagem
//            this.privateMessage = new PrivateMessage();
//            this.privateMessage.setSender(AccountService.getCurrentAuthenticatedUser());
//        } else {
//
//            this.privateMessage = this.privateMessageService.findPrivateMessageById(privateMessageId);
//
//            // pegamos os destinatarios
//            final List<UserPrivateMessage> receipts = this.privateMessageService
//                    .listPrivateMessageReceipts(this.privateMessage);
//
//            // marcamos para mostrar na tabela
//            this.users.stream().forEach((User user) -> {
//                receipts.stream().filter((userPrivateMessage) -> 
//                        (userPrivateMessage.getRecipient().equals(user))).forEach((item) -> {
//                    user.setSelected(true);
//                });
//            });
//        }
    }

    /**
     *
     * @return o form de inclusao
     */
    public String changeToAdd() {
        return "formPrivateMessage.xhtml?faces-redirect=true";
    }

    /**
     *
     * @return
     */
    public String changeToListing() {
        return "formPrivateMessage.xhtml?faces-redirect=true";
    }

    /**
     *
     * @param privateMessageId
     * @return
     */
    public String changeToDetails(long privateMessageId) {
        return "formPrivateMessage.xhtml?faces-redirect=true&privateMessageId=" + privateMessageId;
    }

    /**
     *
     * @param privateMessageId
     */
    public void changeToDelete(long privateMessageId) {
        this.privateMessage = this.privateMessageService.findPrivateMessageById(privateMessageId);
        this.openDialog("deletePrivateMessageDialog", "dialogDeletePrivateMessage");
    }

    /**
     *
     */
    public void doSave() {

//        // pegamos os destinatarios
//        final List<User> receipts = new ArrayList<>();
//
//        this.users.stream().filter((user) -> (user.isSelected())).forEach((user) -> {
//            receipts.add(user);
//        });
//
//        // setamos eles na mensagem
//        this.privateMessage.setRecipients(receipts);
//
//        try {
//            this.privateMessageService.savePrivateMessage(this.privateMessage);
//
//            // limpamos o form
//            this.privateMessage = new PrivateMessage();
//            this.privateMessage.setSender(AccountService.getCurrentAuthenticatedUser());
//
//            this.users = this.privateMessageService.listUsersByStatus(false, true);
//
//            // notifica a galera por email
//            this.emailService.notifyNewMessage(this.privateMessage, receipts);
//            
//            this.info("private-message.action.sent", true);
//        } catch (ApplicationException ex) {
//            this.logger.error("PrivateMessageBean#doSave found erros", ex);
//            this.fixedError(ex.getMessage(), true);
//        } catch (MessagingException ex) {
//            this.logger.warn("PrivateMessageBean#doSave found erros", ex);
//            this.warn("private-message.action.mail-error", true);
//        }
    }

    /**
     *
     */
    public void doDelete() {

//        try {
//            this.privateMessageService.deletePrivateMessage(this.privateMessage);
//            this.privateMessages = this.privateMessageService.listPrivateMessagesSent();
//
//            this.info("private-message.action.deleted", true);
//        } catch (ApplicationException ex) {
//            this.logger.error("PrivateMessageBean#doDelete found erros", ex);
//            this.fixedError(ex.getMessage(), true);
//        } finally {
//            this.update("privateMessagesList");
//            this.closeDialog("dialogDeletePrivateMessage");
//        }
    }

    /**
     * Cancela e volta para a listagem
     *
     * @return
     */
    public String doCancel() {
        return "listPrivateMessages.xhtml?faces-redirect=true";
    }

    /**
     * Atualiza a selecao dos usuarios para envio quando visualiza a mensagem
     */
    public void onUserSelectionChange() {
        for (User user : this.users) {
            user.setSelected(this.selectAll);
        }
        this.update("usersList");
    }
}
