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
package br.com.webbudget.infraestructure.mail;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;

/**
 * Iterface que define uma mensagem de email
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.0.0, 21/05/2015
 */
public interface MailMessage {

    /**
     * @return o titulo da mensagem
     */
    public String getTitle();
    
    /**
     * @return o conteudo da mensagem
     */
    public String getContent();
    
    /**
     * @return os emissores
     */
    public Address getFrom();
    
    /**
     * @return os emails usados para caso o cliente quiser responder o email
     */
    public Address getReplyTo();
    
    /**
     * @return os destinatarios
     */
    public InternetAddress[] getAddressees();
    
    /**
     * @return os destinatarios em copia
     */
    public InternetAddress[] getCcs();
}