package br.com.webbudget.domain.entities.financial;

import br.com.webbudget.domain.entities.PersistentEntity;
import br.com.webbudget.domain.entities.registration.Wallet;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

import static br.com.webbudget.infrastructure.utils.DefaultSchemes.FINANCIAL;
import static br.com.webbudget.infrastructure.utils.DefaultSchemes.FINANCIAL_AUDIT;

/**
 * This class represents the base entity to all the wallet balance transfers made in the system
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 03/10/2018
 */
@Entity
@Audited
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "transfers", schema = FINANCIAL)
@AuditTable(value = "transfers", schema = FINANCIAL_AUDIT)
public class Transference extends PersistentEntity {

    @Getter
    @Setter
    @NotNull(message = "{transference.value}")
    @Column(name = "value", nullable = false)
    private BigDecimal value;
    @Getter
    @Setter
    @Column(name = "transfer_date", nullable = false)
    @NotNull(message = "{transference.transfer-date}")
    private LocalDate transferDate;
    @Getter
    @Setter
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Getter
    @Setter
    @ManyToOne
    @NotNull(message = "{transference.origin}")
    @JoinColumn(name = "id_origin", nullable = false)
    private Wallet origin;
    @Getter
    @Setter
    @ManyToOne
    @NotNull(message = "{transference.destination}")
    @JoinColumn(name = "id_destination", nullable = false)
    private Wallet destination;

    /**
     * Constructor...
     */
    public Transference() {
        this.transferDate = LocalDate.now();
    }
}
