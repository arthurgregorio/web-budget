package br.com.webbudget.domain.entity.security;

import br.com.webbudget.domain.security.User;
import org.picketlink.idm.jpa.annotations.AttributeValue;
import org.picketlink.idm.jpa.annotations.OwnerReference;
import org.picketlink.idm.jpa.annotations.entity.IdentityManaged;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
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
@IdentityManaged(User.class)
@Table(name = "users", schema = "security")
public class UserTypeEntity extends AbstractIdentityTypeEntity {

    @Getter
    @Setter
    @AttributeValue
    private String userName;
    @Getter
    @Setter
    @AttributeValue
    private String name;
    @Getter
    @Setter
    @AttributeValue
    private String email;
    @Getter
    @Setter
    @AttributeValue
    private String organization;
  
    @Getter
    @Setter
    @OwnerReference
    @ManyToOne(fetch = FetchType.EAGER)
    private RealmTypeEntity realm;
}
