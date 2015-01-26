package br.com.webbudget.domain.entity.movement;

import br.com.webbudget.domain.entity.PersistentEntity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
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
 * @since 1.0, 04/03/2014
 */
@Entity
@Table(name = "movement_classes")
@ToString(callSuper = true, of = "name")
@EqualsAndHashCode(callSuper = true, of = "name")
public class MovementClass extends PersistentEntity {

    @Getter
    @Setter
    @NotEmpty(message = "{movement-class.name}")
    @Column(name = "name", nullable = false, length = 45)
    private String name;
    @Getter
    @Setter
    @Column(name = "budget")
    private BigDecimal budget;
    @Getter
    @Setter
    @Column(name = "blocked")
    private boolean blocked;
    @Getter
    @Setter
    @Enumerated
    @NotNull(message = "{movement-class.movement-class-type}")
    @Column(name = "movement_class_type", nullable = false)
    private MovementClassType movementClassType;
    
    @Getter
    @Setter
    @ManyToOne
    @NotNull(message = "{movement-class.cost-center}")
    @JoinColumn(name = "id_cost_center", nullable = false)
    private CostCenter costCenter;
    
    @Getter
    @Setter
    @Transient
    private BigDecimal totalMovements;

    /**
     * 
     */
    public MovementClass() {
        this.totalMovements = BigDecimal.ZERO;
    }
}
