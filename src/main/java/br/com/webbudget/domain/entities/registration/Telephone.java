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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.2.0, 19/04/2015
 */
@Entity
@Table(name = "telephones")
@ToString(callSuper = true, exclude = "contact")
@EqualsAndHashCode(callSuper = true, exclude = "contact")
public class Telephone extends PersistentEntity {

    @Getter
    @Setter
    @NotBlank(message = "{telephone.validate.number}")
    @Column(name = "number", nullable = false, length = 20)
    private String number;
    @Getter
    @Setter
    @NotNull(message = "{telephone.validate.type}")
    @Enumerated
    @Column(name = "number_type", nullable = false)
    private NumberType numberType;
    
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_contact", nullable = false)
    private Contact contact;
}
