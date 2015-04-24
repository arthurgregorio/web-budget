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

import br.com.webbudget.infraestructure.Postman;
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
    
    
}
