package br.com.webbudget.domain.entity.security;

import br.com.webbudget.domain.security.GroupMembership;
import org.picketlink.idm.jpa.annotations.entity.IdentityManaged;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 18/09/2014
 */
@Entity
@IdentityManaged(GroupMembership.class)
@Table(name = "group_memberships", schema = "security")
public class GroupMembershipTypeEntity extends RelationshipTypeEntity {

}
