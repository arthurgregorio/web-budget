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
import br.com.webbudget.domain.entities.financial.Closing;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

import static br.com.webbudget.infrastructure.utils.DefaultSchemes.REGISTRATION;
import static br.com.webbudget.infrastructure.utils.DefaultSchemes.REGISTRATION_AUDIT;

/**
 * The representation of a financial period in the application
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 20/03/2014
 */
@Entity
@Audited
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "financial_periods", schema = REGISTRATION)
@AuditTable(value = "financial_periods", schema = REGISTRATION_AUDIT)
public class FinancialPeriod extends PersistentEntity {

    @Getter
    @Setter
    @Column(name = "identification", nullable = false)
    @NotBlank(message = "{financial-period.identification}")
    private String identification;
    @Getter
    @Setter
    @Column(name = "credit_card_goal")
    @NotNull(message = "{financial-period.credit-goal-empty}")
    private BigDecimal creditCardGoal;
    @Getter
    @Setter
    @Column(name = "expenses_goal")
    @NotNull(message = "{financial-period.expenses-goal-empty}")
    private BigDecimal expensesGoal;
    @Getter
    @Setter
    @Column(name = "revenues_goal")
    @NotNull(message = "{financial-period.revenues-goal-empty}")
    private BigDecimal revenuesGoal;
    @Getter
    @Setter
    @Column(name = "start_date", nullable = false)
    @NotNull(message = "{financial-period.start}")
    private LocalDate start;
    @Getter
    @Setter
    @Column(name = "end_date", nullable = false)
    @NotNull(message = "{financial-period.end}")
    private LocalDate end;
    @Getter
    @Setter
    @Column(name = "closed")
    private boolean closed;

    @Setter
    @OneToOne
    @JoinColumn(name = "id_closing")
    private Closing closing;

    /**
     * Default constructor
     */
    public FinancialPeriod() {
        this.expensesGoal = BigDecimal.ZERO;
        this.revenuesGoal = BigDecimal.ZERO;
        this.creditCardGoal = BigDecimal.ZERO;
    }

    /**
     * If the period is closed, this method should return the {@link Closing} data
     *
     * @return the {@link Closing} data of this period
     */
    public Closing getClosing() {
        if (this.closing == null) {
            throw new BusinessLogicException("error.financial-period.not-closed", this.identification);
        }
        return this.closing;
    }

    /**
     * Method to check if the period is expired or not
     *
     * @return <code>true</code> for expired, <code>false</code> for non expired
     */
    public boolean isExpired() {
        return LocalDate.now().compareTo(this.end) > 0;
    }

    /**
     * The total accumulated by the past periods until the end of this period
     *
     * @return the total
     */
    public BigDecimal getAccumulated() {
        return this.getClosing().getAccumulated();
    }

    /**
     * The final balance of this period
     *
     * @return the final balance value
     */
    public BigDecimal getBalance() {
        return this.getClosing().getBalance();
    }

    /**
     * The total of expenses in this period
     *
     * @return the total
     */
    public BigDecimal getExpensesTotal() {
        return this.getClosing().getExpenses();
    }

    /**
     * The total of revenues in this period
     *
     * @return the total
     */
    public BigDecimal getRevenuesTotal() {
        return this.getClosing().getRevenues();
    }

    /**
     * Method to check if this period is active or not
     *
     * @return if this period is active or not with with <code>true</code> or <code>false</code> respectively
     */
    public boolean isActive() {
        return !this.isExpired() && !this.isClosed();
    }
}
