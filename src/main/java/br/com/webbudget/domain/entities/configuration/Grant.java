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

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import static br.com.webbudget.infrastructure.utils.DefaultSchemes.*;

/**
 * This is the class responsible to create the link between a {@link Group} and a {@link Authorization}
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
@Table(name = "grants", schema = CONFIGURATION)
@AuditTable(value = "grants", schema = CONFIGURATION_AUDIT)
public class Grant extends PersistentEntity {

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_group", nullable = false)
    private Group group;
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_authorization", nullable = false)
    private Authorization authorization;

    /**
     * Constructor
     * 
     * @param group the {@link Group} to receive this grant
     * @param authorization the {@link Authorization} to link with the {@link Group}
     */
    public Grant(Group group, Authorization authorization) {
        this.group = group;
        this.authorization = authorization;
    }
}
