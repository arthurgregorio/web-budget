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

package br.com.webbudget.domain.entity.card;

import br.com.webbudget.domain.entity.PersistentEntity;
import br.com.webbudget.domain.entity.movement.CostCenter;
import br.com.webbudget.domain.entity.movement.FinancialPeriod;
import br.com.webbudget.domain.entity.movement.Movement;
import br.com.webbudget.domain.entity.movement.MovementClass;
import br.com.webbudget.domain.entity.wallet.Wallet;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
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
 * @since 1.0, 01/05/2014
 */
@Entity
@Table(name = "card_invoices")
@ToString(callSuper = true, of = {"identification", "value"})
@EqualsAndHashCode(callSuper = true, of = {"identification", "value"})
public class CardInvoice extends PersistentEntity {

    @Getter
    @Column(name = "identification", nullable = false, length = 45, unique = true)
    private String identification;
    @Getter
    @Setter
    @Column(name = "value", nullable = false)
    private BigDecimal value;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_card")
    private Card card;
    @Getter
    @Setter
    @OneToOne
    @JoinColumn(name = "id_movement")
    private Movement movement;
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_financial_period")
    public FinancialPeriod financialPeriod;

    @Getter
    @Setter
    @Transient
    private Wallet wallet;
    @Getter
    @Setter
    @Transient
    private CostCenter costCenter;
    @Getter
    @Setter
    @Transient
    private MovementClass movementClass;

    @Getter
    @Setter
    @Transient
    private List<Movement> movements;

    /**
     * 
     */
    public CardInvoice() {
        this.movements = new ArrayList<>();
    }
    
    /**
     * 
     * @param name 
     */
    public CardInvoice(String name){
       
        final StringBuilder builder = new StringBuilder();
        
        builder.append(name);
        builder.append("-");
        builder.append(this.createInvoiceCode());
        
        this.identification = builder.toString();
        
        this.movements = new ArrayList<>();
    }
    
    /**
     * 
     * @return 
     */
    private String createInvoiceCode() {
        
        long decimalNumber = System.nanoTime();
        
        String generated = "";
        final String digits = "0123456789";
        
        synchronized (this.getClass()) {
            
            int mod;
            int authCodeLength = 0;

            while (decimalNumber != 0 && authCodeLength < 5) {
                
                mod = (int) (decimalNumber % 10);
                generated = digits.substring(mod, mod + 1) + generated;
                decimalNumber = decimalNumber / 10;
                authCodeLength++;
            }
        }
        return generated;
    }
    
    /**
     * Gera o total da fatura
     * 
     * @return o valor total da fatura com base nos movimentos pagos nela
     */
    public BigDecimal getTotal() {
        
        BigDecimal total = BigDecimal.ZERO;
        
        for (Movement m : this.movements) {
            total = total.add(m.getValue());
        }
        
        return total;
    }
}
