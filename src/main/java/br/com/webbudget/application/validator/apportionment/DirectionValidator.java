package br.com.webbudget.application.validator.apportionment;

import br.com.webbudget.domain.entities.financial.Apportionment;
import br.com.webbudget.domain.entities.financial.Movement;
import br.com.webbudget.domain.exceptions.BusinessLogicException;

import javax.enterprise.context.Dependent;

/**
 * Validator to check if the direction of the {@link Apportionment} is the same of the {@link Apportionment} already
 * in the list
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 06/01/2019
 */
@Dependent
public class DirectionValidator implements ApportionmentValidator {

    /**
     * {@inheritDoc}
     *
     * @param apportionment
     * @param movement
     */
    @Override
    public void validate(Apportionment apportionment, Movement movement) {
        if (movement.isExpense() && apportionment.isRevenue()) {
            throw new BusinessLogicException("error.movement.expense-to-revenue");
        } else if (movement.isRevenue() && apportionment.isExpense()) {
            throw new BusinessLogicException("error.movement.revenue-to-expense");
        }
    }
}
