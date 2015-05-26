package br.com.webbudget.domain.entity.security;

import br.com.webbudget.domain.security.Partition;
import org.picketlink.idm.jpa.annotations.AttributeValue;
import org.picketlink.idm.jpa.annotations.entity.IdentityManaged;
import javax.persistence.Entity;
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
@IdentityManaged(Partition.class)
@Table(name = "realms", schema = "security")
public class RealmTypeEntity extends PartitionTypeEntity {

    @Getter
    @Setter
    @AttributeValue
    private int numberFailedLoginAttempts;
}
