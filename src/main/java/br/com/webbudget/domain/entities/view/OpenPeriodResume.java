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
import lombok.*;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;

import static br.com.webbudget.infrastructure.utils.DefaultSchemes.FINANCIAL;

/**
 * Immutable entity used to summarize the totals for all open (or expired) {@link FinancialPeriod}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 16/04/2019
 */
@Entity
@Immutable
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "wb_view_003", schema = FINANCIAL)
public class OpenPeriodResume extends ImmutableEntity {

    @Getter
    @Column(name = "revenues")
    private BigDecimal revenues;
    @Getter
    @Column(name = "expenses")
    private BigDecimal expenses;
    @Getter
    @Column(name = "cash_expenses")
    private BigDecimal cashExpenses;
    @Getter
    @Column(name = "credit_card_expenses")
    private BigDecimal creditCardExpenses;
    @Getter
    @Column(name = "debit_card_expenses")
    private BigDecimal debitCardExpenses;
    @Getter
    @Column(name = "movements_open")
    private BigDecimal movementsOpen;
    @Getter
    @Column(name = "balance")
    private BigDecimal balance;

    @Getter
    @Setter
    @Transient
    private BigDecimal accumulated;

    /**
     * Constructor...
     */
    public OpenPeriodResume() {
        this.revenues = BigDecimal.ZERO;
        this.expenses = BigDecimal.ZERO;
        this.cashExpenses = BigDecimal.ZERO;
        this.creditCardExpenses = BigDecimal.ZERO;
        this.debitCardExpenses = BigDecimal.ZERO;
        this.movementsOpen = BigDecimal.ZERO;
        this.balance = BigDecimal.ZERO;
        this.accumulated = BigDecimal.ZERO;
    }
}
