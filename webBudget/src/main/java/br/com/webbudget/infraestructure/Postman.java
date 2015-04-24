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

import br.com.webbudget.domain.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailMessage;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * O nosso carteiro, ele envia todas as mensagens de email pela abstracao de
 * {@link SimpleMailMessage}
 *
 * @author Arthur Gregorio
 *
 * @version 1.1.0
 * @since 1.0.0, 06/07/2014
 */
@Component
public class Postman {

    @Autowired
    private MailSender mailSender;

    private final Logger LOG = LoggerFactory.getLogger(Postman.class);

    /**
     * Metodo utilizado para pegar a mensagem montada e enviar para o destino
     *
     * Este metodo apenas realiza o envio, nenhum processamento extra deve ser
     * realizado aqui pois a mensagem deve vir montada partindo da implementacao
     * de uma {@link MailMessage} e passando pelo servico de montagem definido
     * em {@link EmailService}
     *
     * @param message a mensagem a ser enviada
     */
    @Async
    public void sendMail(SimpleMailMessage message) {
        try {
            this.mailSender.send(message);
        } catch (MailException ex) {
            LOG.error("Error trying to send email to {0}", message.getTo(), ex);
        }
    }
}
