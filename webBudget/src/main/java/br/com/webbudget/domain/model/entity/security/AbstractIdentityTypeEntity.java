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
package br.com.webbudget.domain.model.entity.security;

import java.io.Serializable;
import java.util.Date;
import org.picketlink.idm.jpa.annotations.AttributeValue;
import org.picketlink.idm.jpa.annotations.Identifier;
import org.picketlink.idm.jpa.annotations.IdentityClass;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.0.0, 26/05/2015
 */
@MappedSuperclass
public abstract class AbstractIdentityTypeEntity implements Serializable {

    @Id
    @Getter
    @Identifier
    @Column(name = "id", unique = true)
    private String id;
    @Getter
    @Setter
    @IdentityClass
    @Column(name = "type_name")
    private String typeName;
    @Getter
    @Setter
    @AttributeValue
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate;
    @Getter
    @Setter
    @AttributeValue
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiration_date")
    private Date expirationDate;
    @Getter
    @Setter
    @AttributeValue
    @Column(name = "enable")
    private boolean enabled;
}
