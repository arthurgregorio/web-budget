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
    @Column(name = "in_value", nullable = false)
    private BigDecimal inValue;
    @Getter
    @Setter
    @Column(name = "out_value", nullable = false)
    private BigDecimal outValue;
    @Getter
    @Setter
    @Temporal(TemporalType.DATE)
    @Column(name = "closing_date", nullable = false)
    private Date closingDate;
    
    @Getter
    @Setter
    @Transient
    private BigDecimal totalInsPaid;
    @Getter
    @Setter
    @Transient
    private BigDecimal totalInsOpen;
    @Getter
    @Setter
    @Transient
    private BigDecimal totalOutsPaid;
    @Getter
    @Setter
    @Transient
    private BigDecimal totalOutsOpen;

    @Getter
    @Setter
    @Transient
    private List<Movement> openMovements;
    @Getter
    @Setter
    @Transient
    private List<MovementClass> topFiveClassesIn;
    @Getter
    @Setter
    @Transient
    private List<MovementClass> topFiveClassesOut;
    
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
