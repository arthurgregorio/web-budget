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

package br.com.webbudget.domain.entities.miscellany;

import br.com.webbudget.infraestructure.utils.RandomCode;
import br.com.webbudget.domain.entities.PersistentEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 1.0.0, 09/04/2014
 */
@Entity
@Table(name = "closings")
@ToString(callSuper = true, of = "code")
@EqualsAndHashCode(callSuper = true, of = "code")
public class Closing extends PersistentEntity {

    @Getter
    @Setter
    @Column(name = "code", nullable = false, unique = true)
    private String code;
    @Getter
    @Setter
    @Column(name = "revenues", nullable = false)
    private BigDecimal revenues;
    @Getter
    @Setter
    @Column(name = "expenses", nullable = false)
    private BigDecimal expenses;
    @Getter
    @Setter
    @Column(name = "credit_card_expenses", nullable = false)
    private BigDecimal creditCardExpenses;
    @Getter
    @Setter
    @Column(name = "debit_card_expenses", nullable = false)
    private BigDecimal debitCardExpenses;
    @Getter
    @Setter
    @Column(name = "balance", nullable = false)
    private BigDecimal balance;
    @Getter
    @Setter
    @Column(name = "accumulated", nullable = false)
    private BigDecimal accumulated;
    @Getter
    @Setter
    @Column(name = "closing_date", nullable = false)
    private LocalDate closingDate;
        
    /**
     * 
     */
    public Closing() {
        
        this.expenses = BigDecimal.ZERO;
        this.revenues = BigDecimal.ZERO;
        
        this.creditCardExpenses = BigDecimal.ZERO;
        this.debitCardExpenses = BigDecimal.ZERO;
        
        this.balance = BigDecimal.ZERO;
        this.accumulated = BigDecimal.ZERO;
        
        this.closingDate = LocalDate.now();
        
        this.code = RandomCode.alphanumeric(5);
    }
}