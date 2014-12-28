package br.com.webbudget.domain.entity.movement;

import br.com.webbudget.domain.entity.users.Contact;
import br.com.webbudget.domain.entity.PersistentEntity;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 04/03/2014
 */
@Entity
@Table(name = "movement")
@ToString(callSuper = true, of = "code")
@EqualsAndHashCode(callSuper = true, of = "code")
public class Movement extends PersistentEntity {

    @Getter
    @Column(name = "code", nullable = false, length = 8, unique = true)
    private String code;
    @Getter
    @Setter
    @NotNull(message = "movement.validate.value")
    @Column(name = "value", nullable = false, length = 8)
    private BigDecimal value;
    @Getter
    @Setter
    @NotNull(message = "movement.validate.description")
    @NotBlank(message = "movement.validate.description")
    @Column(name = "description", nullable = false, length = 255)
    private String description;
    @Getter
    @Setter
    @NotNull(message = "movement.validate.due-date")
    @Temporal(TemporalType.DATE)
    @Column(name = "due_date", nullable = false)
    private Date dueDate;
    @Getter
    @Setter
    @NotNull(message = "movement.validate.movement-state-type")
    @Enumerated
    @Column(name = "movement_state_type", nullable = false)
    private MovementStateType movementStateType;
    @Getter
    @Setter
    @Enumerated
    @Column(name = "movement_type", nullable = false)
    private MovementType movementType;
    @Getter
    @Setter
    @Column(name = "card_invoice_paid")
    private Boolean cardInvoicePaid;
    @Getter
    @Setter
    @Column(name = "card_invoice")
    private String cardInvoice;
    
    @Getter
    @Setter
    @OneToOne
    @JoinColumn(name = "id_payment")
    private Payment payment;
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_contact")
    private Contact contact;
    @Getter
    @Setter
    @NotNull(message = "movement.validate.movement-class")
    @ManyToOne
    @JoinColumn(name = "id_movement_class", nullable = false)
    private MovementClass movementClass;
    @Getter
    @Setter
    @NotNull(message = "movement.validate.financial-period")
    @ManyToOne
    @JoinColumn(name = "id_financial_period", nullable = false)
    private FinancialPeriod financialPeriod;

    @Getter
    @Setter
    @Transient
    private boolean delete;
    @Getter
    @Setter
    @Transient
    private boolean transfer;
    
    /**
     * Usado apenas para conseguir mostrar a classe na tela
     */
    @Getter
    @Setter
    @Transient
    private CostCenter costCenter;
    
    /**
     * 
     */
    public Movement() {
        this.cardInvoicePaid = false;
        this.code = this.createMovementCode();
        this.movementType = MovementType.MOVEMENT;
        this.movementStateType = MovementStateType.OPEN;
    }
    
    /**
     * 
     * @return 
     */
    private String createMovementCode() {
        
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
    
    /**
     * 
     * @return 
     */
    public boolean isEditable() {
        return (this.movementStateType == MovementStateType.OPEN 
                && !this.financialPeriod.isClosed()); 
    }
    
    /**
     * 
     * @return 
     */
    public boolean isPayable() {
        return (this.movementStateType == MovementStateType.OPEN 
                && !this.financialPeriod.isClosed()); 
    }
    
    /**
     * 
     * @return 
     */
    public boolean isDeletable() {
        return ((this.movementStateType == MovementStateType.OPEN 
                || this.movementStateType == MovementStateType.PAID)
                && !this.financialPeriod.isClosed()); 
    }
    
    /**
     * 
     * @return 
     */
    public boolean isOverdue() {
        
        final Calendar calendar = Calendar.getInstance();
        
        calendar.setTime(new Date());
        
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        
        return this.dueDate.compareTo(calendar.getTime()) < 0;
    }
    
    /**
     * 
     * @return 
     */
    public MovementClassType getMovementDirection() {
        return this.movementClass.getMovementClassType();
    }
}
