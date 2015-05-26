package br.com.webbudget.domain.entity.security;

import java.io.Serializable;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 18/09/2014
 */
@Entity
@ManagedCredential(EncodedPasswordStorage.class)
@Table(name = "passwords", schema = "security")
public class PasswordTypeEntity implements Serializable {

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter
    @Setter
    @CredentialClass
    private String typeName;
    @Getter
    @Setter
    @EffectiveDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectiveDate;
    @Getter
    @Setter
    @ExpiryDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;
    @Getter
    @Setter
    @CredentialProperty(name = "encodedHash")
    private String passwordEncodedHash;
    @Getter
    @Setter
    @CredentialProperty(name = "salt")
    private String passwordSalt;
    
    @Getter
    @Setter
    @ManyToOne
    @OwnerReference
    private UserTypeEntity owner;
}
