package br.com.webbudget.domain.entity.movement;

import br.com.webbudget.domain.entity.PersistentEntity;
import br.com.webbudget.domain.entity.wallet.Wallet;
import br.com.webbudget.domain.entity.card.Card;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 03/04/2014
 */
@Entity
@Table(name = "payments")
@ToString(callSuper = true, of = "code")
@EqualsAndHashCode(callSuper = true, of = "code")
public class Payment extends PersistentEntity {
    
    @Getter
    @Setter
    @Column(name = "code", nullable = false, length = 8, unique = true)
    private String code;
    @Getter
    @Setter
    @NotNull(message = "payment.validate.payment-date")
    @Temporal(TemporalType.DATE)
    @Column(name = "payment_date", nullable = false)
    private Date paymentDate;
    @Getter
    @Setter
    @NotNull(message = "payment.validate.payment-method")
    @Enumerated
    @Column(name = "payment_method_type", nullable = false)
    private PaymentMethodType paymentMethodType;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_card")
    private Card card;
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_wallet")
    private Wallet wallet;

    /**
     * 
     */
    public Payment() {
        this.paymentDate = new Date();
        this.code = this.createPaymentCode();
    }
    
    /**
     * 
     * @return 
     */
    private String createPaymentCode() {
        
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
