/*
 * Copyright (C) 2018 Arthur Gregorio, AG.Software
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
package br.com.webbudget.infrastructure.mail;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;

/**
 * Simple facade to define how a mail message looks like
 * 
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 02/04/2018
 */
public interface MailMessage {

    /**
     * @return the title
     */
    public String getTitle();
    
    /**
     * @return the content
     */
    public String getContent();
    
    /**
     * @return the from {@link InternetAddress}
     */
    public Address getFrom();
    
    /**
     * @return the replay to {@link InternetAddress}
     */
    public Address getReplyTo();
    
    /**
     * @return the list of addressees of this message
     */
    public Address[] getAddressees();
    
    /**
     * @return the list of 'with-copy' for this message
     */
    public Address[] getCcs();
}
