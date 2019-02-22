package br.com.webbudget.application.components;

import br.com.webbudget.domain.entities.financial.PeriodMovement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 18/02/2019
 */
@ToString
@EqualsAndHashCode
public final class PeriodMovementResume implements Serializable {

    @Getter
    private BigDecimal totalOpen;
    @Getter
    private BigDecimal totalPaid;
    @Getter
    private BigDecimal totalRevenue;
    @Getter
    private BigDecimal totalExpense;

    /**
     * Constructor...
     */
    public PeriodMovementResume() {
        this.totalOpen = BigDecimal.ZERO;
        this.totalPaid = BigDecimal.ZERO;
        this.totalRevenue = BigDecimal.ZERO;
        this.totalExpense = BigDecimal.ZERO;
    }

    /**
     * Constructor...
     *
     * Internally this constructor call the {@link #update(List)} method
     *
     * @param periodMovements the list of {@link PeriodMovement} to use for calculate the values
     */
    public PeriodMovementResume(List<PeriodMovement> periodMovements) {
        this.update(periodMovements);
    }

    /**
     * Sum the values and update this value holder
     *
     * @param periodMovements the list of {@link PeriodMovement} to update the model
     */
    public void update(List<PeriodMovement> periodMovements) {

        this.totalOpen = periodMovements.stream()
                .filter(PeriodMovement::isOpen)
                .map(PeriodMovement::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalPaid = periodMovements.stream()
                .filter(PeriodMovement::isPaid)
                .map(PeriodMovement::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalRevenue = periodMovements.stream()
                .filter(PeriodMovement::isRevenue)
                .map(PeriodMovement::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalExpense = periodMovements.stream()
                .filter(PeriodMovement::isExpense)
                .map(PeriodMovement::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
