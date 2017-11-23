/*
 * Copyright (C) 2016 Arthur Gregorio, AG.Software
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
package br.com.webbudget.domain.model.entity.tools;

import br.com.webbudget.domain.model.entity.PersistentEntity;
import br.com.webbudget.domain.model.security.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.2.0, 05/04/2016
 */
@Entity
@ToString(callSuper = true)
@Table(name = "user_messages")
@EqualsAndHashCode(callSuper = true)
public class UserMessage extends PersistentEntity {

    @Getter
    @Setter
    @Column(name = "was_read")
    private boolean read;
    @Getter
    @Setter
    @Column(name = "deleted")
    private boolean deleted;
    @Getter
    @Setter
    @Column(name = "recipient", nullable = false)
    private User recipient;
    
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "message_id")
    private Message message;

    @Getter
    @Setter
    @Transient
    private String elapsedTime;
    @Getter
    @Setter
    @Transient
    private String timeUnit;
    
    /**
     * 
     */
    protected UserMessage() { }

    /**
     * 
     * @param recipient
     * @param message 
     */
    public UserMessage(User recipient, Message message) {
        this.recipient = recipient;
        this.message = message;
    }
    
    /**
     * @return a prioridade da mensagem
     */
    public MessagePriorityType getPriority() {
        return this.message.getPriorityType();
    }
    
    /**
     * @return o titulo
     */
    public String getTitle() {
        return this.message.getTitle();
    }
    
    /**
     * @return o conteudo
     */
    public String getContent() {
        return this.message.getContent();
    }
    
    /**
     * Calcula o tempo que se passou desde o envio da mensagem
     */
    public void calculateElapsedTime() {
        
        final LocalDateTime sentOn = LocalDateTime.ofInstant(
                this.message.getInclusion().toInstant(), ZoneId.systemDefault());
        
        if (sentOn.toLocalDate().isBefore(LocalDate.now())) {
            this.elapsedTime = DateTimeFormatter
                    .ofPattern("dd/MM/yyyy HH:mm").format(sentOn);
        } else {
            long difference = ChronoUnit.MINUTES
                    .between(sentOn.toLocalTime(), LocalTime.now());
            
            if (difference > 60) {
                difference = ChronoUnit.HOURS
                    .between(sentOn.toLocalTime(), LocalTime.now());
                this.elapsedTime = String.valueOf(difference);
                this.timeUnit = "message-box.hours";
            } else {
                this.elapsedTime = String.valueOf(difference);
                this.timeUnit = "message-box.minutes";
            }
        }
    }
}
