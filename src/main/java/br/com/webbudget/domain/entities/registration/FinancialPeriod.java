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
import br.com.webbudget.domain.entities.miscellany.Closing;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 20/03/2014
 */
@Entity
@Audited
@Table(name = "financial_periods")
@ToString(callSuper = true, of = "identification")
@EqualsAndHashCode(callSuper = true, of = "identification")
public class FinancialPeriod extends PersistentEntity {

    @Getter
    @Setter
    @Column(name = "identification", nullable = false)
    @NotEmpty(message = "{financial-period.identification}")
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
    @Column(name = "start", nullable = false)
    @NotNull(message = "{financial-period.start}")
    private LocalDate start;
    @Getter
    @Setter
    @Column(name = "end", nullable = false)
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
     *
     */
    public FinancialPeriod() {
        this.expensesGoal = BigDecimal.ZERO;
        this.revenuesGoal = BigDecimal.ZERO;
        this.creditCardGoal = BigDecimal.ZERO;
    }

    /**
     * 
     * @return 
     */
    public Closing getClosing() {
        if (this.closing == null) {
            throw new BusinessLogicException(
                    "error.financial-period.not-closed", this.identification);
        }
        return closing;
    }

    /**
     * Se o periodo ja expirou ou nao
     *
     * @return
     */
    public boolean isExpired() {
        return LocalDate.now().compareTo(this.end) > 0;
    }

    /**
     *
     * @return
     */
    public BigDecimal getAccumulated() {
        return this.getClosing().getAccumulated();
    }

    /**
     *
     * @return
     */
    public BigDecimal getBalance() {
        return this.getClosing().getBalance();
    }

    /**
     *
     * @return
     */
    public BigDecimal getExpensesTotal() {
        return this.getClosing().getExpenses();
    }

    /**
     *
     * @return
     */
    public BigDecimal getRevenuesTotal() {
        return this.getClosing().getRevenues();
    }

    /**
     * @return se este periodo esta ou nao ativo
     */
    public boolean isActive() {
        return !this.isExpired() && !this.isClosed();
    }

    /**
     * @return o resultado para o periodo caso ele esteja fechado
     */
    public BigDecimal getResult() {
        return this.getClosing().getBalance();
    }
}
