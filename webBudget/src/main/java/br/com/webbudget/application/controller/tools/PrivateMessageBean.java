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
import br.com.webbudget.application.producer.qualifier.AuthenticatedUser;
import br.com.webbudget.domain.entity.message.PrivateMessage;
import br.com.webbudget.domain.entity.message.UserPrivateMessage;
import br.com.webbudget.domain.misc.ex.InternalServiceError;
import br.com.webbudget.domain.misc.table.AbstractLazyModel;
import br.com.webbudget.domain.misc.table.Page;
import br.com.webbudget.domain.misc.table.PageRequest;
import br.com.webbudget.domain.security.User;
import br.com.webbudget.domain.service.AccountService;
import br.com.webbudget.domain.service.PrivateMessageService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.SortOrder;

/**
 * Controle da view de mensagens privadas
 *
 * @author Arthur Gregorio
 *
 * @version 1.2.0
 * @since 1.0.0, 12/05/2014
 */
@Named
@ViewScoped
public class PrivateMessageBean extends AbstractBean {

    @Getter
    @Inject
    @AuthenticatedUser
    private User authenticatedUser;
            
    @Getter
    @Setter
    private boolean selectAll;

    @Getter
    private PrivateMessage privateMessage;

    @Getter
    private List<User> users;

    @Inject
    private transient AccountService accountService;
    @Inject
    private transient PrivateMessageService privateMessageService;

    @Getter
    private final AbstractLazyModel<PrivateMessage> privateMessagesModel;

    /**
     * 
     */
    public PrivateMessageBean() {
        
        this.privateMessagesModel = new AbstractLazyModel<PrivateMessage>() {
            @Override
            public List<PrivateMessage> load(int first, int pageSize, String sortField, 
                    SortOrder sortOrder, Map<String, Object> filters) {
                
                final PageRequest pageRequest = new PageRequest();
                
                pageRequest
                        .setFirstResult(first)
                        .withPageSize(pageSize)
                        .sortingBy(sortField, "inclusion")
                        .withDirection(sortOrder.name());
                
                final Page<PrivateMessage> page = privateMessageService
                        .listPrivateMessagesSentLazily(authenticatedUser.getId(), pageRequest);
                
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
     * @param privateMessageId
     */
    public void initializeForm(long privateMessageId) {

        // preenchemos a lista de usuarios
        this.users = this.accountService.listUsers(false);

        if (privateMessageId == 0) {
            this.viewState = ViewState.ADDING;

            // iniciamos e dizemos que o cara logado e o dono da mensagem
            this.privateMessage = new PrivateMessage();
            this.privateMessage.setSender(this.authenticatedUser.getId());
        } else {

            this.privateMessage = this.privateMessageService.findPrivateMessageById(privateMessageId);

            // pegamos os destinatarios
            final List<UserPrivateMessage> associatives = this.privateMessageService
                    .listPrivateMessageReceipts(this.privateMessage);

            // marcamos para mostrar na tabela
            this.users.forEach(user -> {
                for (UserPrivateMessage associative : associatives) {
                    
                    final User receipt = this.accountService
                            .findUserById(associative.getRecipient().getId());

                    user.setSelected(receipt.equals(user));
                }
            });
        }
    }

    /**
     * @return 
     */
    public String changeToAdd() {
        return "formPrivateMessage.xhtml?faces-redirect=true";
    }

    /**
     * @return
     */
    public String changeToListing() {
        return "formPrivateMessage.xhtml?faces-redirect=true";
    }

    /**
     * @param privateMessageId
     * @return
     */
    public String changeToDetails(long privateMessageId) {
        return "formPrivateMessage.xhtml?faces-redirect=true&privateMessageId=" + privateMessageId;
    }

    /**
     * @param privateMessageId
     */
    public void changeToDelete(long privateMessageId) {
        this.privateMessage = this.privateMessageService.findPrivateMessageById(privateMessageId);
//        this.openDialog("deletePrivateMessageDialog", "dialogDeletePrivateMessage");
    }

    /**
     *
     */
    public void doSave() {

        // pegamos os destinatarios
        final List<User> receipts = new ArrayList<>();

        this.users.stream().filter((user) -> (user.isSelected())).forEach((user) -> {
            receipts.add(user);
        });

        // setamos eles na mensagem
        this.privateMessage.setRecipients(receipts);

        try {
            this.privateMessageService.savePrivateMessage(this.privateMessage);

            // limpamos o form
            this.privateMessage = new PrivateMessage();
            this.privateMessage.setSender(this.authenticatedUser.getId());

            this.users = this.accountService.listUsers(false);

//            this.info("private-message.action.sent", true);
        } catch (InternalServiceError ex) {
            this.logger.error("PrivateMessageBean#doSave found erros", ex);
//            this.fixedError("generic.operation-error", true, ex.getMessage());
        }
    }

    /**
     *
     */
    public void doDelete() {

        try {
            this.privateMessageService.deletePrivateMessage(this.privateMessage);
//            this.info("private-message.action.deleted", true);
        } catch (InternalServiceError ex) {
            this.logger.error("PrivateMessageBean#doDelete found erros", ex);
//            this.fixedError("generic.operation-error", true, ex.getMessage());
        } finally {
//            this.update("privateMessagesList");
            this.closeDialog("dialogDeletePrivateMessage");
        }
    }

    /**
     * @return
     */
    public String doCancel() {
        return "listPrivateMessages.xhtml?faces-redirect=true";
    }

    /**
     * Atualiza a selecao dos usuarios para envio quando visualiza a mensagem
     */
    public void onUserSelectionChange() {
        this.users.forEach((user) -> {
            user.setSelected(this.selectAll);
        });
//        this.update("usersList");
    }
}
