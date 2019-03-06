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
package br.com.webbudget.domain.entities.registration;

import br.com.webbudget.domain.entities.PersistentEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static br.com.webbudget.infrastructure.utils.DefaultSchemes.REGISTRATION;
import static br.com.webbudget.infrastructure.utils.DefaultSchemes.REGISTRATION_AUDIT;

/**
 * The representation of a telephone in the application
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.2.0, 19/04/2015
 */
@Entity
@Audited
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "telephones", schema = REGISTRATION)
@AuditTable(value = "telephones", schema = REGISTRATION_AUDIT)
public class Telephone extends PersistentEntity {

    @Getter
    @Setter
    @NotBlank(message = "{telephone.number}")
    @Column(name = "number", nullable = false, length = 20)
    private String number;
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @NotNull(message = "{telephone.type}")
    @Column(name = "number_type", nullable = false, length = 45)
    private NumberType numberType;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_contact", nullable = false)
    private Contact contact;
}
