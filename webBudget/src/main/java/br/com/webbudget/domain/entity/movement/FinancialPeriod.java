package br.com.webbudget.domain.entity.movement;

import br.com.webbudget.domain.entity.PersistentEntity;
import br.com.webbudget.domain.entity.closing.Closing;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Future;
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
 * @since 1.0, 20/03/2014
 */
@Entity
@Table(name = "financial_periods")
@ToString(callSuper = true, of = "identification")
@EqualsAndHashCode(callSuper = true, of = "identification")
public class FinancialPeriod extends PersistentEntity {

    @Getter
    @Setter
    @NotNull(message = "financial-period.validate.identification")
    @NotBlank(message = "financial-period.validate.identification")
    @Column(name = "identification", nullable = false)
    private String identification;
    @Getter
    @Setter
    @NotNull(message = "financial-period.validate.start")
    @Temporal(TemporalType.DATE)
    @Column(name = "start", nullable = false)
    private Date start;
    @Getter
    @Setter
    @Future(message = "financial-period.validate.end")
    @NotNull(message = "financial-period.validate.end")
    @Temporal(TemporalType.DATE)
    @Column(name = "end", nullable = false)
    private Date end;
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
     * Se o periodo ja expirou ou nao
     * 
     * @return 
     */
    public boolean isExpired() {
       
        final Calendar today = Calendar.getInstance();
        today.setTime(new Date());

        final Calendar finish = Calendar.getInstance();
        finish.setTime(this.end);

        return today.compareTo(finish) > 0;
    }
    
    /**
     * 
     * @return 
     */
    public String getStartFormatted() {
        return new SimpleDateFormat("dd/MM/yyyy").format(this.start);
    }
    
    /**
     * 
     * @return 
     */
    public String getEndFormatted() {
        return new SimpleDateFormat("dd/MM/yyyy").format(this.end);
    }
}
