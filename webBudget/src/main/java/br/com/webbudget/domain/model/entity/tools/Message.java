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
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.2.0, 05/04/2016
 */
@Entity
@Table(name = "messages")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Message extends PersistentEntity {

    @Getter
    @Setter
    @NotBlank(message = "{message.title}")
    @Column(name = "title", nullable = false, length = 90)
    private String title;
    @Getter
    @Setter
    @NotBlank(message = "{message.content}")
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;
    @Getter
    @Setter
    @Column(name = "deleted")
    private boolean deleted;
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "priority_type", nullable = false)
    private MessagePriorityType priorityType;
    
    @Getter
    @Setter
    @Column(name = "sender", nullable = false)
    private User sender;
    
    @Getter
    @Setter
    @Transient
    private List<User> recipients;

    /**
     * 
     */
    public Message() {
        this.priorityType = MessagePriorityType.LOW;
        this.recipients = new ArrayList<>();
    }
    
    /**
     * 
     * @param sender 
     */
    public Message(User sender) {
        this();
        this.sender = sender;
    }

    /**
     * @return o nome da pessoa que envio a mensagem
     */
    public String getSenderName() {
        return this.sender.getName();
    }
    
    /**
     * @return se nossa mensagem tem ou nao destinatarios
     */
    public boolean hasRecipients() {
        return !this.recipients.isEmpty();
    }
}
