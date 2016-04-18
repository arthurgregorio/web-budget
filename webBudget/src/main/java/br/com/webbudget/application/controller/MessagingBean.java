package br.com.webbudget.application.controller;

import br.com.webbudget.domain.model.entity.tools.UserMessage;
import br.com.webbudget.domain.model.service.MessagingService;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.0.0, 13/04/2016
 */
@Named
@RequestScoped
public class MessagingBean extends AbstractBean {

    @Inject
    private MessagingService messagingService;

    /**
     *
     * @return
     */
    public long countNewMessages() {
        return this.messagingService.countNewMessages();
    }

    /**
     *
     * @return
     */
    public List<UserMessage> getUnreadMessages() {

        final List<UserMessage> unreadMessages
                = this.messagingService.listUnreadMessages();

        unreadMessages.stream().forEach(message -> {
            message.calculateElapsedTime();
        });

        return unreadMessages;
    }

    /**
     * Redireciona para pagina de detalhes do vendedor
     * 
     * @param userMessageId o id da mensagem a ser visualizada
     */
    public void changeToDetail(long userMessageId) {
        this.redirectTo("detailReceivedMessage.xhtml?faces-redirect=true&id="
                + userMessageId + "&viewState=" + ViewState.DETAILING);
    }
}
