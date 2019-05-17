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
package br.com.webbudget.infrastructure.mail;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.ObservesAsync;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import java.util.Date;

/**
 * This class has one job: send e-mails
 *
 * @author Arthur Gregorio
 *
 * @version 2.1.0
 * @since 1.0.0, 06/07/2014
 */
@ApplicationScoped
public class Postman {

    @Resource(name = "java:/mail/mailService")
    private Session mailSession;
    
    /**
     * Listen for e-mail requests through CDI events and send the message
     * 
     * @param mailMessage the message to send
     * @throws Exception if any problem occur in the process
     */
    public void send(@Observes MailMessage mailMessage) throws Exception {
       
        final MimeMessage message = new MimeMessage(this.mailSession);

        // message header
        message.setFrom(mailMessage.getFrom());
        message.setSubject(mailMessage.getTitle());
        message.setRecipients(Message.RecipientType.TO, mailMessage.getAddressees());
        message.setRecipients(Message.RecipientType.CC, mailMessage.getCcs());
        
        // message body
        message.setText(mailMessage.getContent(), "UTF-8", "html");
        message.setSentDate(new Date());

        // send
        Transport.send(message);
    }
}
