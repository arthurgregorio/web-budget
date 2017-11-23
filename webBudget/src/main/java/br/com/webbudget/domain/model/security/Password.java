/*
 * Copyright (C) 2017 Arthur Gregorio, AG.Software 
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
package br.com.webbudget.domain.model.security;

import br.com.webbudget.domain.model.entity.PersistentEntity;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Arthur Gregorio
 * 
 * @version 1.0.0
 * @since 3.0.0, 22/11/2017
 */
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "passwords", schema = "security")
public class Password extends PersistentEntity {
    
    @Getter
    @Setter
    @Column(name = "passphrase", nullable = false, length = 256)
    private String passphrase;
    @Getter
    @Setter
    @Column(name = "expired", nullable = false)
    private boolean expired;
    @Getter
    @Setter
    @Column(name = "mus_change", nullable = false)
    private boolean mustChange;
    @Getter
    @Setter
    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @Getter
    @Setter
    private User user;
    
    /**
     * 
     */
    public Password() {
        this.expired = false;
        this.mustChange = true;
        this.expirationDate = LocalDateTime.now().plusMonths(6);
    }
}
