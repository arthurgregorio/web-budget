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

import br.com.webbudget.domain.service.AccountService;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.picketlink.idm.model.AbstractIdentityType;
import org.picketlink.idm.model.annotation.AttributeProperty;
import org.picketlink.idm.model.annotation.IdentityStereotype;
import org.picketlink.idm.model.annotation.InheritsPrivileges;
import org.picketlink.idm.model.annotation.StereotypeProperty;
import org.picketlink.idm.model.annotation.Unique;
import org.picketlink.idm.query.QueryParameter;
import static org.picketlink.idm.model.annotation.IdentityStereotype.Stereotype.GROUP;
import static org.picketlink.idm.model.annotation.StereotypeProperty.Property.IDENTITY_GROUP_NAME;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.0.0, 26/05/2015
 */
@IdentityStereotype(GROUP)
public class Group extends AbstractIdentityType {

    @Getter
    @Setter
    @Unique
    @AttributeProperty
    @NotEmpty(message = "{group.name}")
    @StereotypeProperty(IDENTITY_GROUP_NAME)
    private String name;
    @Getter
    @Setter
    @AttributeProperty
    @InheritsPrivileges
    private Group parent;
    
    /**
     * Cache dos grants deste grupo preenchido pelo metodo 
     * {@link AccountService#listUserGroupsAndGrants(User user)}
     */
    @Getter
    @Setter
    private List<Grant> grants;
    
    public static final QueryParameter NAME = QUERY_ATTRIBUTE.byName("name");
    public static final QueryParameter PARENT = QUERY_ATTRIBUTE.byName("parent");

    /**
     * 
     */
    public Group() { }

    /**
     * 
     * @param name 
     */
    public Group(String name) {
        this.name = name;
    }

    /**
     * 
     * @param name
     * @param parent 
     */
    public Group(String name, Group parent) {
        this.name = name;
        this.parent = parent;
    }
    
    /**
     * 
     * @return 
     */
    public boolean isBlocked() {
        return !this.isEnabled();
    }
    
    /**
     * 
     * @param blocked 
     */
    public void setBlocked(boolean blocked) {
        this.setEnabled(!blocked);
    }
}
