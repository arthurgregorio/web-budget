/*
 * Copyright (C) 2016 Arthur Gregorio, AG.Software
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
package br.com.webbudget.domain.model.entity.logbook;

import br.com.webbudget.domain.model.entity.PersistentEntity;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Representacao de um registro no logbook, cada registro e composto por uma 
 * ocorrencia que pode determinar um categoria para o registro e tambem se
 * este registro gerou ou nao uma movimentacao financeira
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.3.0, 09/05/2016
 */
@Entity
@Table(name = "records")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Record extends PersistentEntity {

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_occurrence")
    private Occurrence occurrence;
}
