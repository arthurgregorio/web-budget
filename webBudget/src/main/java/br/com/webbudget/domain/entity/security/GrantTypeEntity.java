package br.com.webbudget.domain.entity.security;

import br.com.webbudget.domain.security.Grant;
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
@IdentityManaged(Grant.class)
@Table(name = "grants", schema = "security")
public class GrantTypeEntity extends RelationshipTypeEntity {

}
