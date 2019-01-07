package br.com.webbudget.application.validator.apportionment;

import br.com.webbudget.domain.entities.financial.Apportionment;
import br.com.webbudget.domain.entities.financial.Movement;
import br.com.webbudget.domain.exceptions.BusinessLogicException;

import javax.enterprise.context.Dependent;
import java.math.BigDecimal;

import static br.com.webbudget.infrastructure.utils.Utilities.decimalToString;

/**
 * Validate the {@link Apportionment} value
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 06/01/2019
 */
@Dependent
public class ValueValidator implements ApportionmentValidator {

    /**
     * {@inheritDoc}
     *
     * @param apportionment
     * @param movement
     */
    @Override
    public void validate(Apportionment apportionment, Movement movement) {

        if (apportionment.getValue().equals(BigDecimal.ZERO)) {
            throw new BusinessLogicException("error.movement.value-equal-zero");
        }

        final BigDecimal remainingTotal = movement.calculateRemainingTotal();

        if (apportionment.getValue().compareTo(remainingTotal) > 0) {
            throw new BusinessLogicException("error.movement.value-gt-total", decimalToString(remainingTotal));
        }
    }
}
