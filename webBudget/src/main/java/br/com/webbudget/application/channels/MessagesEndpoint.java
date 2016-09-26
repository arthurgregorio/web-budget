package br.com.webbudget.application.channels;

import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * Endpoint para fazer o push das notificacoes de mensangens do sistema
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.3.0, 20/09/2016
 */
@ServerEndpoint("/channels/messages")
public class MessagesEndpoint {

    @Inject
    private WebSocketSessions sessions;

    /**
     * Quando uma sessao abrir, adiciona na lista
     *
     * @param session a sessao que se abre
     */
    @OnOpen
    public void onOpenSession(Session session) {
        this.sessions.add(session);
    }

    /**
     * Quando uma sessao se encerrar, remove da lista
     *
     * @param session a sessao que se encerra
     */
    @OnClose
    public void onCloseSession(Session session) {
        this.sessions.remove(session);
    }
}
