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
package br.com.webbudget.domain.entity.security;

import java.io.Serializable;
import java.time.LocalDate;
import org.picketlink.idm.jpa.annotations.AttributeValue;
import org.picketlink.idm.jpa.annotations.Identifier;
import org.picketlink.idm.jpa.annotations.IdentityClass;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Column;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.0.0, 26/05/2015
 */
@ToString
@MappedSuperclass
@EqualsAndHashCode
public abstract class AbstractIdentityTypeEntity implements Serializable {

    @Id
    @Identifier
    @Column(name = "id", nullable = false, unique = true)
    private String id;
    @Getter
    @Setter
    @IdentityClass
    @Column(name = "type_name", nullable = false)
    private String typeName;
    @Getter
    @Setter
    @AttributeValue
    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;
    @Getter
    @Setter
    @AttributeValue
    @Column(name = "expiration_date")
    private LocalDate expirationDate;
    @Getter
    @Setter
    @AttributeValue
    @Column(name = "enable", nullable = false)
    private boolean enabled;
}
