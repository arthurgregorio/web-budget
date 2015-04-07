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
package br.com.webbudget.domain.entity.users;

import br.com.webbudget.domain.entity.PersistentEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Classe que mapeia os atributos necessarios para a existencia de um contato
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.2.0, 07/04/2015
 */
@Entity
@Table(name = "contacts")
@ToString(callSuper = true, of = "name")
@EqualsAndHashCode(callSuper = true, of = "name")
public class Contact extends PersistentEntity {

    @Getter
    @Column(name = "code", nullable = false, length = 8, unique = true)
    private String code;
    @Getter
    @Setter
    @Column(name = "name", nullable = false, length = 90)
    private String name;
    
    @Getter
    @Setter
    @Column(name = "blocked")
    private boolean blocked;

    /**
     * Inicializa o contato
     */
    public Contact() {
        this.code = this.createContactCode();
    }
    
    /**
     * @return um codigo unico para este contato
     */
    private String createContactCode() {

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
