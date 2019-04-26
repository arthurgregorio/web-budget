/*
 * Copyright (C) 2019 Arthur Gregorio, AG.Software
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
package br.com.webbudget.domain.entities.view;

import br.com.webbudget.application.components.dto.Color;
import br.com.webbudget.domain.entities.ImmutableEntity;
import br.com.webbudget.domain.entities.registration.CostCenter;
import br.com.webbudget.infrastructure.jpa.ColorConverter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

import static br.com.webbudget.infrastructure.utils.DefaultSchemes.FINANCIAL;

/**
 * Immutable entity representing a database view with with a resume of the consume for every card grouped by
 * {@link CostCenter}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 25/04/2019
 */
@Entity
@Immutable
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "wb_view_006", schema = FINANCIAL)
public class CardConsume extends ImmutableEntity {

    @Getter
    @Column(name = "card_id")
    private Long cardId;
    @Getter
    @Column(name = "cost_center_color")
    @Convert(converter = ColorConverter.class)
    private Color costCenterColor;
    @Getter
    @Column(name = "cost_center")
    private String costCenter;
    @Getter
    @Column(name = "total_value")
    private BigDecimal value;
}
