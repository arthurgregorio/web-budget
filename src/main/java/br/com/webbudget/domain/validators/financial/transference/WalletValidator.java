package br.com.webbudget.domain.validators.financial.transference;

import br.com.webbudget.domain.entities.financial.Transference;
import br.com.webbudget.domain.entities.registration.Wallet;
import br.com.webbudget.domain.exceptions.BusinessLogicException;

import javax.enterprise.context.Dependent;

/**
 * The validator for the {@link Wallet} origin and destination inside the {@link Transference}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 06/10/2018
 */
@Dependent
public class WalletValidator implements TransferenceSavingValidator {

    /**
     * {@inheritDoc}
     *
     * @param value
     */
    @Override
    public void validate(Transference value) {

        final Wallet origin = value.getOrigin();
        final Wallet destination = value.getDestination();

        if (origin.equals(destination)) {
            throw new BusinessLogicException("error.transference.same-wallet");
        }
    }
}
