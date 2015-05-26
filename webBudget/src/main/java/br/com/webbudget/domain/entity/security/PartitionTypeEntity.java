package br.com.webbudget.domain.entity.security;

import java.io.Serializable;
import org.picketlink.idm.jpa.annotations.AttributeValue;
import org.picketlink.idm.jpa.annotations.Identifier;
import org.picketlink.idm.jpa.annotations.PartitionClass;
import org.picketlink.idm.jpa.annotations.entity.ConfigurationName;
import org.picketlink.idm.jpa.annotations.entity.IdentityManaged;
import org.picketlink.idm.model.Partition;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode
@IdentityManaged(Partition.class)
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "partitions", schema = "security")
public class PartitionTypeEntity implements Serializable {

    @Id
    @Getter
    @Setter
    @Identifier
    private String id;
    @Getter
    @Setter
    @AttributeValue
    private String name;
    @Getter
    @Setter
    @PartitionClass
    private String typeName;
    @Getter
    @Setter
    @ConfigurationName
    private String configurationName;
}
