package br.com.webbudget.domain.entity.security;

import java.io.Serializable;
import org.picketlink.idm.jpa.annotations.Identifier;
import org.picketlink.idm.jpa.annotations.RelationshipClass;
import org.picketlink.idm.jpa.annotations.entity.IdentityManaged;
import org.picketlink.idm.model.Relationship;
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
@IdentityManaged(Relationship.class)
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "relationships", schema = "security")
public class RelationshipTypeEntity implements Serializable {

    @Id
    @Getter
    @Setter
    @Identifier
    private String id;
    @Getter
    @Setter
    @RelationshipClass
    private String typeName;
}
