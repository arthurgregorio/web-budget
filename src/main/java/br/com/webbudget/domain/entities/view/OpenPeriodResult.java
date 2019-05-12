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

import br.com.webbudget.domain.entities.ImmutableEntity;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

import static br.com.webbudget.infrastructure.utils.DefaultSchemes.FINANCIAL;

/**
 * Immutable entity used to represent a result of the current (not expired) {@link FinancialPeriod}
 *
 * @author Arthur Gregorio
 *
 * @version 1.1.0
 * @since 3.0.0, 29/04/2019
 */
@Entity
@Immutable
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "wb_view_011", schema = FINANCIAL)
public class OpenPeriodResult extends ImmutableEntity {

    @Getter
    @Column(name = "financial_period_id")
    private Long financialPeriodId;
    @Getter
    @Column(name = "financial_period")
    private String financialPeriod;
    @Getter
    @Column(name = "expired")
    private boolean expired;
    @Getter
    @Column(name = "revenues")
    private BigDecimal revenues;
    @Getter
    @Column(name = "expenses")
    private BigDecimal expenses;
    @Getter
    @Column(name = "balance")
    private BigDecimal balance;

    /**
     * Constructor...
     */
    public OpenPeriodResult() {
        this.revenues = BigDecimal.ZERO;
        this.expenses = BigDecimal.ZERO;
        this.balance = BigDecimal.ZERO;
    }
}
