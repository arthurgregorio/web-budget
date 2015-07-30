/*
 * Copyright (C) 2015 Arthur Gregorio, AG.Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.com.webbudget.domain.security;

import lombok.Getter;
import lombok.Setter;
import org.picketlink.idm.model.AbstractAttributedType;
import org.picketlink.idm.model.IdentityType;
import org.picketlink.idm.model.Relationship;
import org.picketlink.idm.model.annotation.InheritsPrivileges;
import org.picketlink.idm.model.annotation.RelationshipStereotype;
import org.picketlink.idm.model.annotation.StereotypeProperty;
import org.picketlink.idm.query.RelationshipQueryParameter;
import static org.picketlink.idm.model.annotation.RelationshipStereotype.Stereotype.GRANT;
import static org.picketlink.idm.model.annotation.StereotypeProperty.Property.RELATIONSHIP_GRANT_ASSIGNEE;
import static org.picketlink.idm.model.annotation.StereotypeProperty.Property.RELATIONSHIP_GRANT_ROLE;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.0.0, 26/05/2015
 */
@RelationshipStereotype(GRANT)
public class Grant extends AbstractAttributedType implements Relationship {

    @Getter
    @Setter
    @InheritsPrivileges("role")
    @StereotypeProperty(RELATIONSHIP_GRANT_ASSIGNEE)
    private IdentityType assignee;
    @Getter
    @Setter
    @StereotypeProperty(RELATIONSHIP_GRANT_ROLE)
    private Role role;
    
    public static final RelationshipQueryParameter ROLE = RELATIONSHIP_QUERY_ATTRIBUTE.byName("role");
    public static final RelationshipQueryParameter ASSIGNEE = RELATIONSHIP_QUERY_ATTRIBUTE.byName("assignee");

    /**
     * 
     */
    public Grant() { }

    /**
     * 
     * @param assignee
     * @param role 
     */
    public Grant(Role role, IdentityType assignee) {
        this.role = role;
        this.assignee = assignee;
    }
    
    /**
     * 
     * @return 
     */
    public String getGrantAuthorization() {
        return role.getAuthorization();
    }
    
    /**
     * 
     * @return 
     */
    public String getAuthorization() {
        return this.role.getAuthorization();
    }
}