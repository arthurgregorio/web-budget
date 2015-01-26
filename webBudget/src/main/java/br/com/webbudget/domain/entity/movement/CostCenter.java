package br.com.webbudget.domain.entity.movement;

import br.com.webbudget.domain.entity.PersistentEntity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
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
 * @version 1.0
 * @since 1.0, 28/03/2014
 */
@Entity
@Table(name = "cost_centers")
@ToString(callSuper = true, of = "name")
@EqualsAndHashCode(callSuper = true, of = "name")
public class CostCenter extends PersistentEntity {

    @Getter
    @Setter
    @NotEmpty(message = "{cost-center.name}")
    @Column(name = "name", nullable = false, length = 90)
    private String name;
    @Getter
    @Setter
    @NotNull(message = "{cost-center.budget}")
    @Column(name = "budget", nullable = false)
    private BigDecimal budget;
    @Getter
    @Setter
    @Column(name = "blocked")
    private boolean blocked;
    @Getter
    @Setter
    @Column(name = "description", length = 255)
    private String description;
    
    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "id_parent_cost_center")
    private CostCenter parentCostCenter;    
    
    @Setter
    @Getter
    @Transient
    private BigDecimal percentage;
    @Setter
    @Getter
    @Transient
    private BigDecimal totalMovements;

    /**
     * 
     */
    public CostCenter() {
        this.percentage = BigDecimal.ZERO;
        this.totalMovements = BigDecimal.ZERO;
    }
}
