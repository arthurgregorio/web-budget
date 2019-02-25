package br.com.webbudget.domain.logics.financial.periodmovement;

import br.com.webbudget.domain.entities.financial.PeriodMovement;
import br.com.webbudget.domain.exceptions.BusinessLogicException;

import javax.enterprise.context.Dependent;

/**
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 23/02/2019
 */
@Dependent
public class ContainsApportionmentsValidator implements PeriodMovementSavingLogic, PeriodMovementUpdatingLogic {

    /**
     * {@inheritDoc}
     *
     * @param value
     */
    @Override
    public void run(PeriodMovement value) {
        if (value.getApportionments().isEmpty()) {
            throw new BusinessLogicException("error.period-movement.no-apportionments");
        }
    }
}
