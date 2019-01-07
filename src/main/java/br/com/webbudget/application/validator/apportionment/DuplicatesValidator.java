package br.com.webbudget.application.validator.apportionment;

import br.com.webbudget.domain.entities.financial.Apportionment;
import br.com.webbudget.domain.entities.financial.Movement;
import br.com.webbudget.domain.exceptions.BusinessLogicException;

import javax.enterprise.context.Dependent;

/**
 * The validator to check if a duplicate {@link Apportionment} will be added
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 06/01/2019
 */
@Dependent
public class DuplicatesValidator implements ApportionmentValidator {

    /**
     * {@inheritDoc}
     *
     * @param apportionment
     * @param movement
     */
    @Override
    public void validate(Apportionment apportionment, Movement movement) {
        movement.getApportionments().forEach(added -> {
            if (added.isCostCenterAndMovementClassEquals(apportionment)) {
                throw new BusinessLogicException("error.movement.duplicate-apportionment");
            }
        });
    }
}
