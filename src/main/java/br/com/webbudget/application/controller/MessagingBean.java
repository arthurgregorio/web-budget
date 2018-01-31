package br.com.webbudget.application.controller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.0.0, 13/04/2016
 */
@Named
@SessionScoped
public class MessagingBean extends AbstractBean {

//    @Getter
//    private int newMessages;
//    @Getter
//    private List<UserMessage> unreadMessages;
//
//    @Inject
//    private MessagingService messagingService;
//
//    /**
//     * Atualiza o status das mensagens
//     */
//    public void updateMessageStatus() {
//
//        // lista as mensagens
//        this.unreadMessages = this.listUnreadMessages();
//
//        // conta as mensagens
//        this.newMessages = this.unreadMessages.size();
//    }
//
//    /**
//     * @return as mensagens nao lidas
//     */
//    private List<UserMessage> listUnreadMessages() {
//
//        final List<UserMessage> messages
//                = this.messagingService.listUnreadMessages();
//
//        messages.stream().forEach(message -> {
//            message.calculateElapsedTime();
//        });
//
//        return messages;
//    }
//
//    /**
//     * Redireciona para pagina de detalhes do vendedor
//     *
//     * @param userMessageId o id da mensagem a ser visualizada
//     */
//    public void changeToDetail(long userMessageId) {
//        this.redirectTo("detailReceivedMessage.xhtml?faces-redirect=true&id="
//                + userMessageId + "&viewState=" + ViewState.DETAILING);
//    }
}
