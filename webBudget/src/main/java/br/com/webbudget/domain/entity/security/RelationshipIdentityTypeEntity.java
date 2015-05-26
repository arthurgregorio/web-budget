package br.com.webbudget.domain.entity.security;

import org.picketlink.idm.jpa.annotations.OwnerReference;
import org.picketlink.idm.jpa.annotations.RelationshipDescriptor;
import org.picketlink.idm.jpa.annotations.RelationshipMember;
import org.picketlink.idm.jpa.annotations.entity.IdentityManaged;
import org.picketlink.idm.model.Relationship;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import javax.persistence.GenerationType;
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
@IdentityManaged({Relationship.class})
@Table(name = "identity_relationships", schema = "security")
public class RelationshipIdentityTypeEntity implements Serializable {

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long identifier;
    @Getter
    @Setter
    @RelationshipDescriptor
    private String descriptor;
    @Getter
    @Setter
    @RelationshipMember
    private String identityType;
    @Getter
    @Setter
    @ManyToOne
    @OwnerReference
    private RelationshipTypeEntity owner;
}
