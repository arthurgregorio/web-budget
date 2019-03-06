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
package br.com.webbudget.domain.entities.configuration;

import br.com.webbudget.domain.entities.PersistentEntity;
import lombok.*;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static br.com.webbudget.infrastructure.utils.DefaultSchemes.*;

/**
 * This class represents a authorization for a single functionality, this is also commonly part of a {@link Grant} for
 * a {@link Group} that have {@link User} linked to
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.0.0, 26/05/2015
 */
@Entity
@Audited
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "authorizations", schema = CONFIGURATION)
@AuditTable(value = "authorizations", schema = CONFIGURATION_AUDIT)
public class Authorization extends PersistentEntity {

    @Getter
    @Setter
    @Column(name = "functionality", nullable = false, length = 90)
    private String functionality;
    @Getter
    @Setter
    @Column(name = "permission", nullable = false, length = 90)
    private String permission;

    /**
     * Constructor
     *
     * @param functionality the functionality of the authorization
     * @param permission the permission of the functionality
     */
    public Authorization(String functionality, String permission) {
        this.functionality = functionality;
        this.permission = permission;
    }

    /**
     * Returns the full authorization represented by this object
     *
     * @return the functionality + the permission
     */
    public String getFullPermission() {
        return this.functionality + ":" + this.permission;
    }

    /**
     * Check if this authorization is for this functionality
     *
     * @param functionality the functionality to test
     * @return true or false
     */
    public boolean isFunctionality(String functionality) {
        return functionality != null && this.functionality.equals(functionality);
    }

    /**
     * Check if this authorization is for this permission
     *
     * @param permission the permission to test
     * @return true or false
     */
    public boolean isPermission(String permission) {
        return permission != null && (this.permission.equals(permission)
                || this.getFullPermission().equals(permission));
    }
}
