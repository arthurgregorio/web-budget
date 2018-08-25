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
