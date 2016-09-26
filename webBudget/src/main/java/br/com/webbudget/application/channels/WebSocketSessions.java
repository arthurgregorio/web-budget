package br.com.webbudget.application.channels;

import java.util.HashSet;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.Session;
import org.slf4j.Logger;

/**
 * Classe para representar o usuarios conectados nos channels via websocket
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.3.0, 20/09/2016
 */
@ApplicationScoped
public class WebSocketSessions {
    
    @Inject
    private Logger logger;
    
    private final Set<Session> sessions;

    /**
     * 
     */
    public WebSocketSessions() {
        this.sessions = new HashSet<>();
    }

    /**
     * Adiciona na lista de sessoes uma nova sessao de um usuario conectado
     * 
     * @param session a sessao do usuario
     */
    public void add(Session session) {
        this.sessions.add(session);
    }
    
    /**
     * Remove uma sessao da lista de sessoes
     * 
     * @param session a session que precisa ser removida
     */
    public void remove(Session session) {
        this.sessions.remove(session);
    }
        
    /**
     * Metodo para enviar as mensagens a todos os clientes conectados
     *
     */
    public void notifyOpenSessions() {
        this.sessions
                .stream()
                .filter(Session::isOpen)
                .forEach(session -> {
                    try {
                        session.getBasicRemote().sendText("newMessage");
                    } catch (Exception ex) {
                        this.logger.error("Can't notify session {0}",
                                session.getId(), ex);
                    }
                });
    }
}
