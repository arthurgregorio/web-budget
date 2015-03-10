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

package br.com.webbudget.infraestructure;

import br.com.webbudget.domain.entity.movement.CostCenter;
import br.com.webbudget.domain.entity.movement.FinancialPeriod;
import br.com.webbudget.domain.entity.movement.Movement;
import br.com.webbudget.domain.entity.users.User;
import br.com.webbudget.domain.service.AccountService;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 06/07/2014
 */
@Component
public class Postman {

//    @Autowired
//    private MessagesFactory messages;
//    @Autowired
//    private JavaMailSender mailSender;
//    @Autowired
//    private TemplateEngine templateEngine;
    
    private final Logger LOG = LoggerFactory.getLogger(Postman.class);
    
    /**
     * Avisa que um periodo expirou
     * 
     * @param financialPeriod 
     */
    @Async
    public void expiredPeriodWarning(FinancialPeriod financialPeriod) {
        
    }
    
    /**
     * 
     * @param movement 
     */
    @Async
    public void movementOverdueWarning(Movement movement) {
        
    }
    
    /**
     * 
     * @param costCenter 
     */
    @Async
    public void costCenterBudgetOverflowWarning(CostCenter costCenter) {
        
    }
    
    /**
     * 
     * @param addressee 
     */
    @Async
    public void newMessageWarning(User addressee) {
       
//        LOG.info("Sending new message warning to {}", addressee.getUsername());
//        
//        try {
//            final MimeMessage message = this.mailSender.createMimeMessage();
//            final MimeMessageHelper helper = this.createHelper(message);
//
//            helper.setTo(addressee.getEmail());
//            helper.setSubject(this.messages.getMessage(
//                    "mail.private-message.new-message-subject"));
//        
//            final Context context = new Context(FacesContext.getCurrentInstance()
//                    .getViewRoot().getLocale());
//            
//            context.setVariable("greeting", this.messages.getMessage(
//                    "mail.private-message.saudation", addressee.getName()));
//            context.setVariable("message", this.messages.getMessage("mail.private-message.body", 
//                    AccountService.getCurrentAuthenticatedUser().getName()));
//            context.setVariable("urlDescription", this.messages.getMessage("mail.private-message.link"));
//            context.setVariable("appUrl", this.getApplicationURL().toString());
//            context.setVariable("logo", this.getLogoPath().toString());
//            
//            final String body = this.templateEngine.process("NewMesageWarning.html", context);
//            
//            helper.setText(body, true);
//            
//            this.mailSender.send(message);
//                        
//            LOG.info("New message warning sent to {}", addressee.getUsername());
//        } catch (MessagingException | MailException ex) {
//            LOG.error("Error when try to send the message", ex);
//        }
    }
    
    /**
     * Encapsula a criacao do helper para poder adicionar o logo da aplicacao 
     * em cada mensagem
     * 
     * @return o helper configurado para a mensagem
     */
    private MimeMessageHelper createHelper(MimeMessage message) throws MessagingException {
       
        final MimeMessageHelper helper = new MimeMessageHelper(message, true);
        
        helper.setFrom(ResourceBundle.getBundle("config/webbudget").getString("mail.user"));
        
        return helper;
    }
    
    /**
     * Metodo que monta o caminho para a logo do sistema
     * 
     * @return a url para a logo do sistema
     */
    private URL getLogoPath() {
        
        final ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        
        final String requestDestination = externalContext.getRequestServletPath();
        final HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        
        final String baseURL = request.getRequestURL().toString().replace(requestDestination, "");
        
        try {
            return new URL(baseURL + "/resources/images/ui-webbudget.png");
        } catch (MalformedURLException ex) {
            LOG.error("Erro when try to create logo url", ex);
            return null;
        }
    }
    
    /**
     * Metodo que monta a URL base do sistema
     * 
     * @return a url base do sistema
     */
    private URL getApplicationURL() {
        
        final ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        
        final String requestDestination = externalContext.getRequestServletPath();
        final HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        
        final String baseURL = request.getRequestURL().toString().replace(requestDestination, "");
        
        try {
            return new URL(baseURL);
        } catch (MalformedURLException ex) {
            LOG.error("Erro when try to create logo url", ex);
            return null;
        }
    }
}
