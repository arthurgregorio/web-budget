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
package br.com.webbudget.domain.service;

import br.com.webbudget.domain.entity.users.PrivateMessage;
import br.com.webbudget.domain.entity.users.User;
import br.com.webbudget.infraestructure.MessagesFactory;
import br.com.webbudget.infraestructure.mail.Postman;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.NumberTool;

/**
 * O servico de envio de email, por este cara invocamos o {@link Postman} que
 * fara envio da mensagem para o envio
 *
 * E neste servico que os metodos de envio especificos para cada mensagem devem
 * ser criados, a montagem da mesma deve seguir a prerrogativa que todas as
 * mensagens devem implementar {@link MimeMessage} para que o postman a
 * reconheca e envie
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.2.0, 22/04/2015
 */
public class EmailService {

    @Inject
    private Postman postman;
    @Inject
    private VelocityEngine engine;
    @Inject
    private MessagesFactory messagesFactory;

    private final ResourceBundle mailConfig = ResourceBundle.getBundle("config.webbudget");

    /**
     *
     * @param privateMessage
     * @param receipts
     *
     * @throws MessagingException
     */
    public void notifyNewMessage(PrivateMessage privateMessage, List<User> receipts)
            throws MessagingException {

        // iteramos na lista de destinatarios e enviamos as mensagens
        for (User receipt : receipts) {

            final MimeMessageHelper helper = new MimeMessageHelper(
                    this.postman.createMimeMessage());

            helper.setTo(receipt.getEmail());
            helper.setSubject(this.messagesFactory.getMessage("new-message.title"));
            helper.setFrom(this.mailConfig.getString("mail.user"));

            //mensagens internacionalizadas
            final Map<String, String> i18n = new HashMap<>();

            i18n.put("header", this.messagesFactory.getMessage(
                    "new-message.header"));
            i18n.put("hi-user", this.messagesFactory.getMessage(
                    "new-message.hi-user"));
            i18n.put("new-message-warning", this.messagesFactory.getMessage(
                    "new-message.new-message-warning"));
            i18n.put("tip", this.messagesFactory.getMessage("new-message.tip"));

            final VelocityContext context = this.createContext();

            context.put("i18n", i18n);
            context.put("receipt", receipt.getName());
            context.put("sender", privateMessage.getSender().getName());

            // manda a mensagem para montagem e envio
            this.prepareToSend("/mail/newPrivateMessage.html", helper, context);
        }
    }

    /**
     * Prepara a mensagem de acordo com a template e invoca o {@link Postman}
     * para que ela seja enviada
     *
     * @param template a template a ser mesclada
     * @param helper o helper de montagem da mensagem
     * @param context o contexto do velocity
     */
    private void prepareToSend(String template, MimeMessageHelper helper,
            VelocityContext context) throws MessagingException {

        final StringWriter writer = new StringWriter();

        this.engine.getTemplate(template, "UTF-8").merge(context, writer);

        helper.setText(writer.toString(), true);

        this.postman.sendMail(helper.getMimeMessage());
    }

    /**
     * Cria um contexto do velocity para manipulacao de datas e numeros na
     * template de e-mail
     *
     * @return o contexto
     */
    private VelocityContext createContext() {

        final VelocityContext context = new VelocityContext();

        context.put("date", new DateTool());
        context.put("number", new NumberTool());

        return context;
    }
}
