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
import org.picketlink.idm.credential.storage.EncodedPasswordStorage;
import org.picketlink.idm.jpa.annotations.CredentialClass;
import org.picketlink.idm.jpa.annotations.CredentialProperty;
import org.picketlink.idm.jpa.annotations.EffectiveDate;
import org.picketlink.idm.jpa.annotations.ExpiryDate;
import org.picketlink.idm.jpa.annotations.OwnerReference;
import org.picketlink.idm.jpa.annotations.entity.ManagedCredential;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Column;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
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
 * @since 2.0.0, 26/05/2015
 */
@Entity
@ToString
@EqualsAndHashCode
@Table(name = "passwords")
@ManagedCredential(EncodedPasswordStorage.class)
public class PasswordTypeEntity implements Serializable {

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;
    @Getter
    @Setter
    @CredentialClass
    @Column(name = "type_name", nullable = false)
    private String typeName;
    @Getter
    @Setter
    @EffectiveDate
    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;
    @Getter
    @Setter
    @ExpiryDate
    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;
    @Getter
    @Setter
    @CredentialProperty(name = "encodedHash")
    @Column(name = "encoded_hash", nullable = false)
    private String encodedHash;
    @Getter
    @Setter
    @CredentialProperty(name = "salt")
    @Column(name = "salt", nullable = false)
    private String salt;
    
    @Getter
    @Setter
    @ManyToOne
    @OwnerReference
    @JoinColumn(name = "id_owner", nullable = false)
    private UserTypeEntity owner;
}