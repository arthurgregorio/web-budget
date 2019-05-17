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

import br.com.webbudget.domain.exceptions.BusinessLogicException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Simple implementation of the {@link MailMessage} with a build and a fluent interface to construct e-mail messages
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
     * Private constructor to prevent misuse
     */
    private SimpleMailMessage() {
        this.ccs = new ArrayList<>();
        this.addressees = new ArrayList<>();
    }

    /**
     * @return the builder of {@link SimpleMailMessage}
     */
    public static SimpleMailMessageBuilder getBuilder() {
        return new SimpleMailMessageBuilder();
    }

    /**
     * Add a cc
     *
     * @param cc the cc to add
     */
    private void addCc(InternetAddress cc) {
        this.ccs.add(cc);
    }

    /**
     * Add a addressee
     *
     * @param addressee the addressee to add
     */
    private void addAddressees(InternetAddress addressee) {
        this.addressees.add(addressee);
    }

    /**
     * @return all the addressees for the message
     */
    @Override
    public InternetAddress[] getAddressees() {
        return this.addressees.toArray(new InternetAddress[]{});
    }

    /**
     * @return all the cc for the message
     */
    @Override
    public InternetAddress[] getCcs() {
        return this.ccs.toArray(new InternetAddress[]{});
    }

    /**
     * A builder pattern implementation to create e-mail messages
     */
    public static class SimpleMailMessageBuilder {

        private final SimpleMailMessage message;

        /**
         * Private constructor to prevent misuse
         */
        private SimpleMailMessageBuilder() {
            this.message = new SimpleMailMessage();
        }

        /**
         * Add the addressee of the message
         *
         * @param to the addressee of the message
         * @return this builder
         */
        public SimpleMailMessageBuilder to(String to) {
            this.message.addAddressees(this.toAddress(to));
            return this;
        }

        /**
         * From field of the message
         *
         * @param from address
         * @return this builder
         */
        public SimpleMailMessageBuilder from(String from) {
            this.message.setFrom(this.toAddress(from));
            return this;
        }

        /**
         * From field of the message
         *
         * @param from the address
         * @param name the name
         * @return this builder
         */
        public SimpleMailMessageBuilder from(String from, String name) {
            this.message.setFrom(this.toAddress(from, name));
            return this;
        }

        /**
         * The reply to address
         *
         * @param replyTo reply to address
         * @return this builder
         */
        public SimpleMailMessageBuilder replyTo(String replyTo) {
            this.message.setReplyTo(this.toAddress(replyTo));
            return this;
        }

        /**
         * The with-copy address to the message
         *
         * @param copy the address to copy the message
         * @return this builder
         */
        public SimpleMailMessageBuilder withCopy(String copy) {
            this.message.addCc(this.toAddress(copy));
            return this;
        }

        /**
         * The string content of the message
         *
         * @param content content in string format
         * @return this builder
         */
        public SimpleMailMessageBuilder withContent(String content) {
            this.message.setContent(content);
            return this;
        }

        /**
         * Set a provider to provide the content to the message
         *
         * @param provider the provider to provide the content
         * @return this builder
         */
        public SimpleMailMessageBuilder withContent(MailContentProvider provider) {
            this.message.setContent(checkNotNull(provider).getContent());
            return this;
        }

        /**
         * The title of the message
         *
         * @param title the title
         * @return this builder
         */
        public SimpleMailMessageBuilder withTitle(String title) {
            this.message.setTitle(title);
            return this;
        }

        /**
         * @return the instance of the {@link SimpleMailMessage} to be created
         */
        public SimpleMailMessage build() {
            return this.message;
        }

        /**
         * Convert a string to a {@link Address}
         *
         * @param address the string address
         * @return the {@link InternetAddress} implementation of {@link Address}
         */
        private InternetAddress toAddress(String address) {
            return this.toAddress(address, null);
        }

        /**
         * Convert a string to a {@link Address}
         *
         * @param address the string address
         * @param personal the name
         * @return the {@link InternetAddress} implementation of {@link Address}
         */
        private InternetAddress toAddress(String address, String personal) {
            try {
                return new InternetAddress(address, personal);
            } catch (UnsupportedEncodingException ex) {
                throw new BusinessLogicException("error.core.email-address-invalid", ex, address);
            }
        }
    }
}