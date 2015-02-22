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

package br.com.webbudget.domain.entity.closing;

import br.com.webbudget.domain.entity.PersistentEntity;
import br.com.webbudget.domain.entity.movement.Movement;
import br.com.webbudget.domain.entity.movement.MovementClass;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 09/04/2014
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
    @Temporal(TemporalType.DATE)
    @Column(name = "closing_date", nullable = false)
    private Date closingDate;
    
    @Getter
    @Setter
    @Transient
    public boolean movementsWithoutInvoice;
    
    @Getter
    @Setter
    @Transient
    private List<Movement> openMovements;
        
    /**
     * 
     */
    public Closing() {
        this.code = this.createClosingCode();
    }
    
    /**
     * 
     * @return 
     */
    private String createClosingCode() {
        
        long decimalNumber = System.nanoTime();
        
        String generated = "";
        final String digits = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        
        synchronized (this.getClass()) {
            
            int mod;
            int authCodeLength = 0;

            while (decimalNumber != 0 && authCodeLength < 5) {
                
                mod = (int) (decimalNumber % 36);
                generated = digits.substring(mod, mod + 1) + generated;
                decimalNumber = decimalNumber / 36;
                authCodeLength++;
            }
        }
        return generated;
    }
}
