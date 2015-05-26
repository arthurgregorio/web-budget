package br.com.webbudget.domain.entity.security;

import br.com.webbudget.domain.security.Role;
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
@IdentityManaged(Role.class)
@Table(name = "roles", schema = "security")
public class RoleTypeEntity extends AbstractIdentityTypeEntity {

    @Getter
    @Setter
    @AttributeValue
    private String name;
    @Getter
    @Setter
    @OwnerReference
    @ManyToOne(fetch = FetchType.LAZY)
    private PartitionTypeEntity partition;
}
