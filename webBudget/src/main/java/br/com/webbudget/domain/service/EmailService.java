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

import br.com.webbudget.domain.entity.movement.FinancialPeriod;
import br.com.webbudget.domain.entity.users.PrivateMessage;
import br.com.webbudget.infraestructure.MessagesFactory;
import br.com.webbudget.infraestructure.Postman;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.NumberTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

/**
 * O servico de envio de email, por este cara invocamos o {@link Postman} que
 * fara envio da mensagem para o envio
 *
 * E neste servico que os metodos de envio especificos para cada mensagem devem
 * ser criados, a montagem da mesma deve seguir a prerrogativa que todas as
 * mensagens devem implementar {@link SimpleMailMessage} para que o postman a
 * reconheca e envie
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.2.0, 22/04/2015
 */
@Service
public class EmailService {

    @Autowired
    private Postman postman;
    @Autowired
    private VelocityEngine engine;
    @Autowired
    private MessagesFactory messagesFactory;

    /**
     * 
     * @param period 
     */
    public void notifyPeriodEnd(FinancialPeriod  period) {

        final SimpleMailMessage message = new SimpleMailMessage();       
        
        //mensagens internacionalizadas
        final Map<String, String> i18n = new HashMap<>();

        i18n.put("email-header", this.messagesFactory.getMessage("email.header"));

        final VelocityContext context = this.createContext();
        
        context.put("period", period);
        context.put("i18n", i18n);

        this.prepareToSend("/mail/periodEnd.vm", message, context);
    }
    
    /**
     * 
     * @param privateMessage 
     */
    public void notifyNewMessage(PrivateMessage privateMessage) {

        final SimpleMailMessage message = new SimpleMailMessage();       
        
        //mensagens internacionalizadas
        final Map<String, String> i18n = new HashMap<>();

        i18n.put("email-header", this.messagesFactory.getMessage("email.header"));

        final VelocityContext context = this.createContext();
        
        context.put("privateMessage", privateMessage);
        context.put("i18n", i18n);

        this.prepareToSend("/mail/newPrivateMessage.vm", message, context);
    }
    
    /**
     * Prepara a mensagem de acordo com a template e invoca o {@link Postman}
     * para que ela seja enviada
     * 
     * @param template a templa a ser mesclada
     * @param message a mensagem
     * @param context o contexto do velocity
     */
    private void prepareToSend(String template, SimpleMailMessage message, 
            VelocityContext context) {
        
        final StringWriter writer = new StringWriter();

        this.engine.getTemplate(template, "UTF-8").merge(context, writer);

        message.setText(writer.toString());
        
        this.postman.sendMail(message);
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
