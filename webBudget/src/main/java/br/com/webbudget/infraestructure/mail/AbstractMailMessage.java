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

import java.util.ArrayList;
import java.util.List;
import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import lombok.Setter;

/**
 * Iterface que define uma mensagem de email
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.0.0, 06/07/2015
 */
public class AbstractMailMessage implements MailMessage {

    @Setter
    private String title;
    @Setter
    private String content;
    @Setter
    private Address from;
    @Setter
    private Address replyTo;
    @Setter
    private List<InternetAddress> ccs;
    @Setter
    private List<InternetAddress> addressees;

    /**
     * 
     */
    public AbstractMailMessage() {
        this.ccs = new ArrayList<>();
        this.addressees = new ArrayList<>();
    }
    
    /**
     * 
     * @param cc 
     */
    public void addCc(InternetAddress cc) {
        this.ccs.add(cc);
    }
    
    /**
     * 
     * @param addressee 
     */
    public void addAddressees(InternetAddress addressee) {
        this.addressees.add(addressee);
    }
    
    /**
     * 
     * @return 
     */
    @Override
    public String getTitle() {
        return this.title;
    }

    /**
     * 
     * @return 
     */
    @Override
    public String getContent() {
        return this.content;
    }

    /**
     * 
     * @return 
     */
    @Override
    public Address getFrom() {
        return this.from;
    }

    /**
     * 
     * @return 
     */
    @Override
    public Address getReplyTo() {
        return this.replyTo;
    }
    
    /**
     * 
     * @return 
     */
    @Override
    public InternetAddress[] getCcs() {
        return this.ccs.toArray(new InternetAddress[0]);
    }
    
    /**
     * 
     * @return 
     */
    @Override
    public InternetAddress[] getAddressees() {
        return this.addressees.toArray(new InternetAddress[0]);
    }
}