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
package br.com.webbudget.domain.mail;

import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.infrastructure.mail.MailContentProvider;
import br.com.webbudget.infrastructure.mail.MailMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 03/04/2018
 */
@ToString
public class SimpleMailMessage implements MailMessage {

    @Getter
    @Setter
    private String title;
    @Getter
    @Setter
    private String content;
    @Getter
    @Setter
    private Address from;
    @Getter
    @Setter
    private Address replyTo;
    @Setter
    private List<InternetAddress> ccs;
    @Setter
    private List<InternetAddress> addressees;

    /**
     * 
     */
    private SimpleMailMessage() {
        this.ccs = new ArrayList<>();
        this.addressees = new ArrayList<>();
    }
    
    /**
     * 
     * @return 
     */
    public static SimpleMailMessageBuilder getBuilder() {
        return new SimpleMailMessageBuilder();
    }
    
    /**
     * 
     * @param cc 
     */
    private void addCc(InternetAddress cc) {
        this.ccs.add(cc);
    }
    
    /**
     * 
     * @param addressee 
     */
    private void addAddressees(InternetAddress addressee) {
        this.addressees.add(addressee);
    }

    /**
     * 
     * @return 
     */
    @Override
    public InternetAddress[] getAddressees() {
        return this.addressees.toArray(new InternetAddress[]{});
    }

    /**
     * 
     * @return 
     */
    @Override
    public InternetAddress[] getCcs() {
        return this.ccs.toArray(new InternetAddress[]{});
    }
    
    /**
     * 
     */
    public static class SimpleMailMessageBuilder {
        
        private final SimpleMailMessage message;

        /**
         * 
         */
        private SimpleMailMessageBuilder() {
            this.message = new SimpleMailMessage();
        }
        
        /**
         * 
         * @param to
         * @return 
         */
        public SimpleMailMessageBuilder to(String to) {
            this.message.addAddressees(this.toAddress(to));
            return this;
        }
        
        /**
         * 
         * @param from
         * @return 
         */
        public SimpleMailMessageBuilder from(String from) {
            this.message.setFrom(this.toAddress(from));
            return this;
        }
        
        /**
         * 
         * @param replyTo
         * @return 
         */
        public SimpleMailMessageBuilder replyTo(String replyTo) {
            this.message.setReplyTo(this.toAddress(replyTo));
            return this;
        }
        
        /**
         * 
         * @param copy
         * @return 
         */
        public SimpleMailMessageBuilder withCopy(String copy) {
            this.message.addCc(this.toAddress(copy));
            return this;
        }
        
        /**
         * 
         * @param content
         * @return 
         */
        public SimpleMailMessageBuilder withContent(String content) {
            this.message.setContent(content);
            return this;
        }
        
        /**
         * 
         * @param provider
         * @return 
         */
        public SimpleMailMessageBuilder withContent(MailContentProvider provider) {
            this.message.setContent(provider.getContent());
            return this;
        }
        
        /**
         * 
         * @param title
         * @return 
         */
        public SimpleMailMessageBuilder withTitle(String title) {
            this.message.setTitle(title);
            return this;
        }
        
        /**
         * 
         * @return 
         */
        public SimpleMailMessage build() {
            return this.message;
        }
        
        /**
         * 
         * @param address
         * @return 
         */
        private InternetAddress toAddress(String address) {
            try {
                return new InternetAddress(address);
            } catch (AddressException ex) {
                throw new BusinessLogicException(
                        "error.core.email-address-invalid", ex, address);
            }
        }
    }
}

