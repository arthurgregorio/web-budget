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
package br.com.webbudget.domain.model.entity.message;

import br.com.webbudget.domain.model.entity.PersistentEntity;
import br.com.webbudget.domain.model.security.User;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 07/05/2014
 */
@Entity
@Table(name = "private_messages")
@ToString(callSuper = true, of = "code")
@EqualsAndHashCode(callSuper = true, of = "code")
public class PrivateMessage extends PersistentEntity {

    @Getter
    @Column(name = "code", unique = true, nullable = false)
    private String code;
    @Getter
    @Setter
    @NotEmpty(message = "{private-message.title}")
    @Column(name = "title", nullable = false, length = 45)
    private String title;
    @Getter
    @Setter
    @NotEmpty(message = "{private-message.text}")
    @Column(name = "text", nullable = false, length = 255)
    private String text;
    @Getter
    @Setter
    @Column(name = "deleted")
    private boolean deleted;

    @Getter
    @Setter
    @Column(name = "sender", nullable = false, length = 90)
    private String sender;
    
    /**
     * Atributo criado para que possamos mostrar o nome do cara que enviou
     * a mensagem
     */
    @Getter
    @Setter
    @Transient
    private String senderName;

    @Getter
    @Setter
    @Transient
    private List<User> recipients;

    /**
     *
     */
    public PrivateMessage() {
        this.code = this.createMessagetCode();
    }

    /**
     * @return um codigo unico para a mensagem
     */
    private String createMessagetCode() {

        long decimalNumber = System.nanoTime();

        String generated = "";
        final String digits = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        synchronized (this.getClass()) {

            int mod;
            int authCodeLength = 0;

            while (decimalNumber != 0 && authCodeLength < 5) {

                mod = (int) (decimalNumber % 36);
                generated = digits.substring(mod, mod + 1) + generated;
                decimalNumber = decimalNumber / 36;
                authCodeLength++;
            }
        }
        return generated;
    }
}
