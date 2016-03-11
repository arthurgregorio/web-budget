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
package br.com.webbudget.domain.entity.movement;

import br.com.webbudget.application.converter.JPALocalDateConverter;
import br.com.webbudget.domain.entity.PersistentEntity;
import br.com.webbudget.domain.entity.closing.Closing;
import br.com.webbudget.domain.misc.ex.InternalServiceError;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 20/03/2014
 */
@Entity
@Table(name = "financial_periods")
@ToString(callSuper = true, of = "identification")
@EqualsAndHashCode(callSuper = true, of = "identification")
public class FinancialPeriod extends PersistentEntity {

    @Getter
    @Setter
    @NotEmpty(message = "{financial-period.identification}")
    @Column(name = "identification", nullable = false)
    private String identification;
    @Getter
    @Setter
    @NotNull(message = "{financial-period.credit-goal-empty}")
    @Column(name = "credit_card_goal")
    private BigDecimal creditCardGoal;
    @Getter
    @Setter
    @NotNull(message = "{financial-period.expenses-goal-empty}")
    @Column(name = "expenses_goal")
    private BigDecimal expensesGoal;
    @Getter
    @Setter
    @NotNull(message = "{financial-period.revenues-goal-empty}")
    @Column(name = "revenues_goal")
    private BigDecimal revenuesGoal;
    @Getter
    @Setter
    @NotNull(message = "{financial-period.start}")
    @Convert(converter = JPALocalDateConverter.class)
    @Column(name = "start", nullable = false)
    private LocalDate start;
    @Getter
    @Setter
    @NotNull(message = "{financial-period.end}")
    @Convert(converter = JPALocalDateConverter.class)
    @Column(name = "end", nullable = false)
    private LocalDate end;
    @Getter
    @Setter
    @Column(name = "closed")
    private boolean closed;

    @Setter
    @Getter
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
        if (this.closed) {
            return this.closing.getAccumulated();
        }
        throw new InternalServiceError(
                "error.financial-period.not-closed", this.identification);
    }
    
    /**
     * 
     * @return 
     */
    public BigDecimal getBalance() {
        if (this.closed) {
            return this.closing.getBalance();
        }
        throw new InternalServiceError(
                "error.financial-period.not-closed", this.identification);
    }
    
    /**
     * 
     * @return 
     */
    public BigDecimal getExpensesTotal() {
        if (this.closed) {
            return this.closing.getExpenses();
        }
        throw new InternalServiceError(
                "error.financial-period.not-closed", this.identification);
    }
    
    /**
     * 
     * @return 
     */
    public BigDecimal getRevenuesTotal() {
        if (this.closed) {
            return this.closing.getRevenues();
        }
        throw new InternalServiceError(
                "error.financial-period.not-closed", this.identification);
    }
    
    /**
     *
     * @return
     */
    public String getFriendlyName() {

        final StringBuilder builder = new StringBuilder();

        final DateTimeFormatter formatter = 
                DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        builder.append(this.identification);
        builder.append(" | ");
        builder.append(this.start.format(formatter));
        builder.append(" - ");
        builder.append(this.end.format(formatter));

        return builder.toString();
    }
}
