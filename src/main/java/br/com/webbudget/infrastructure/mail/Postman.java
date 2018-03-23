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

import java.util.Date;
import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

/**
 * O nosso carteiro, ele escuta por qualquer evento relacionado ao envio de
 * mensagens, e caso alguem dispare um, ele se encarrega de encaminhar a
 * mensagem atraves da sessao ativa de email disponibilizada como recurso
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 1.0.0, 06/07/2014
 */
@ApplicationScoped
public class Postman {

    @Resource(name = "java:/mail/webBudget")
    private Session mailSession;
    
    /**
     * Escuta por eventos de envio de e-mail
     * 
     * @param mailMessage a mensagem a ser enviada
     * @throws Exception caso haja problemas, dispara exception
     */
    @Asynchronous
    public void send(@Observes MailMessage mailMessage) throws Exception {
       
        final MimeMessage message = new MimeMessage(this.mailSession);

        // header da mensagem
        message.setFrom(mailMessage.getFrom());
        message.setSubject(mailMessage.getTitle());
        message.setRecipients(Message.RecipientType.TO, mailMessage.getAddressees());
        message.setRecipients(Message.RecipientType.CC, mailMessage.getCcs());
        
        // a mensagem
        message.setText(mailMessage.getContent(), "UTF-8", "html");
        message.setSentDate(new Date());

        // envia
        Transport.send(message);
    }
}
