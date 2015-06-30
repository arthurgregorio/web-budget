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
package br.com.webbudget.domain.entity.security;

import java.io.Serializable;
import javax.persistence.Column;
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
 * @since 2.0.0, 26/05/2015
 */
@Entity
@EqualsAndHashCode
@Table(name = "partitions")
@IdentityManaged(Partition.class)
@Inheritance(strategy = InheritanceType.JOINED)
public class PartitionTypeEntity implements Serializable {

    @Id
    @Identifier
    @Column(name = "id", unique = true)
    private String id;
    @Getter
    @Setter
    @AttributeValue
    @Column(name = "name")
    private String name;
    @Getter
    @Setter
    @PartitionClass
    @Column(name = "type_name")
    private String typeName;
    @Getter
    @Setter
    @ConfigurationName
    @Column(name = "configuration")
    private String configuration;
}
