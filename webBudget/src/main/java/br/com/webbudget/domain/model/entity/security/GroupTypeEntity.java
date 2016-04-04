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
package br.com.webbudget.domain.model.entity.security;

import br.com.webbudget.domain.model.security.Group;
import javax.persistence.Column;
import org.picketlink.idm.jpa.annotations.AttributeValue;
import org.picketlink.idm.jpa.annotations.OwnerReference;
import org.picketlink.idm.jpa.annotations.entity.IdentityManaged;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.0.0, 26/05/2015
 */
@Entity
@Table(name = "groups")
@IdentityManaged(Group.class)
public class GroupTypeEntity extends AbstractIdentityTypeEntity {

    @Getter
    @Setter
    @AttributeValue
    @Column(name = "name")
    private String name;

    @Getter
    @Setter
    @ManyToOne
    @AttributeValue
    @JoinColumn(name = "id_parent")
    private GroupTypeEntity parent;
    @Getter
    @Setter
    @ManyToOne
    @OwnerReference
    @JoinColumn(name = "id_partition")
    private PartitionTypeEntity partition;
}
