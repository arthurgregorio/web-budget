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
package br.com.webbudget.application.controller.configuration;

import br.com.webbudget.application.controller.AbstractBean;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.2.0, 06/04/2016
 */
@Named
@ViewScoped
public class MessageSentBean extends AbstractBean {

//    @Getter
//    @Setter
//    private String filter;
//
//    @Getter
//    @Setter
//    private Message message;
//
//    @Getter
//    private List<User> users;
//
//    @Getter
//    @Inject
//    @AuthenticatedUser
//    private User authenticatedUser;
//
//    @Inject
//    private AccountService accountService;
//    @Inject
//    private MessagingService messagingService;
//
//    @Getter
//    private final AbstractLazyModel<Message> messagesModel;
//
//    /**
//     *
//     */
//    public MessageSentBean() {
//       
//        this.messagesModel = new AbstractLazyModel<Message>() {
//            @Override
//            public List<Message> load(int first, int pageSize, String sortField,
//                    SortOrder sortOrder, Map<String, Object> filters) {
//
//                // constroi o filtro
//                final PageRequest pageRequest = new PageRequest();
//
//                pageRequest
//                        .setFirstResult(first)
//                        .withPageSize(pageSize)
//                        .sortingBy(sortField, "inclusion")
//                        .withDirection(sortOrder.name());
//
//                final Page<Message> page = messagingService
//                        .listSentMessages(filter, pageRequest);
//
//                this.setRowCount(page.getTotalPagesInt());
//
//                return page.getContent();
//            }
//        };
//    }
//
//    /**
//     *
//     */
//    public void initializeList() {
//        this.viewState = ViewState.LISTING;
//    }
//
//    /**
//     *
//     * @param messageId
//     * @param viewState
//     */
//    public void initializeForm(long messageId, String viewState) {
//
////        // capturamos o estado da tela 
////        this.viewState = ViewState.valueOf(viewState);
////        
////        final List<User> allUsers = this.accountService.listUsers(Boolean.FALSE);
////
////        // remove o usuario logado da lista de destinatarios
////        this.users = allUsers.stream()
////                .filter(user -> !user.getId().equals(this.authenticatedUser.getId()))
////                .collect(Collectors.toList());
////        
////        // inicia a mensagem
////        this.message = new Message(this.authenticatedUser);
//    }
//    
//    /**
//     * 
//     * @param messageId
//     * @param viewState 
//     */
//    public void initializeDetailing(long messageId, String viewState) {
//        
//        // capturamos o estado da tela 
//        this.viewState = ViewState.valueOf(viewState);
//        
//        // inicia a mensagem
//        this.message = this.messagingService.findMessageById(messageId, true);
//    }
//
//    /**
//     * 
//     */
//    public void doSave() {
//        try {
//            this.messagingService.sendMessage(this.message);
//            this.message = new Message(this.authenticatedUser);
//            this.addInfo(true, "message.sent");
//        } catch (InternalServiceError ex) {
//            this.addError(true, ex.getMessage(), ex.getParameters());
//        } catch (Exception ex) {
//            this.logger.error(ex.getMessage(), ex);
//            this.addError(true, "error.undefined-error", ex.getMessage());
//        }
//    }
//    
//    /**
//     * 
//     * @return 
//     */
//    public String doDelete() {
//        try {
//            this.messagingService.deleteMessage(this.message);
//            return this.changeTolist();
//        } catch (InternalServiceError ex) {
//            this.addError(true, ex.getMessage(), ex.getParameters());
//            return null;
//        } catch (Exception ex) {
//            this.logger.error(ex.getMessage(), ex);
//            this.addError(true, "error.undefined-error", ex.getMessage());
//            return null;
//        }
//    }
//    
//    /**
//     * @return redireciona para a pagina de cadastro
//     */
//    public String changeToAdd() {
//        return "formSentMessage.xhtml?faces-redirect=true&viewState="
//                + ViewState.ADDING;
//    }
//
//    /**
//     * Redireciona para pagina de detalhes do vendedor
//     */
//    public void changeToDetail() {
//        this.redirectTo("detailSentMessage.xhtml?faces-redirect=true&id="
//                + this.message.getId() + "&viewState=" + ViewState.DETAILING);
//    }
//
//    /**
//     * @param sellerId
//     * @return
//     */
//    public String changeToDelete(String sellerId) {
//        return "detailSentMessage.xhtml?faces-redirect=true&id=" + sellerId
//                + "&viewState=" + ViewState.DELETING;
//    }
//
//    /**
//     * @return volta para a listagem
//     */
//    public String changeTolist() {
//        return "listSentMessages.xhtml?faces-redirect=true";
//    }
//
//    /**
//     * @return as prioridades
//     */
//    public MessagePriorityType[] getPriorities() {
//        return MessagePriorityType.values();
//    }
}
