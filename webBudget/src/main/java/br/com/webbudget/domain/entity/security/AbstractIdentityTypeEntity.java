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
