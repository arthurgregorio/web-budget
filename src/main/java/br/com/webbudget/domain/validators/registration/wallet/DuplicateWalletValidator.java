package br.com.webbudget.domain.validators.registration.wallet;

import br.com.webbudget.domain.entities.registration.Wallet;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.repositories.registration.WalletRepository;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.Optional;

/**
 * The validator to prevent duplicated {@link Wallet}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 28/09/2018
 */
@Dependent
public class DuplicateWalletValidator implements WalletSavingValidator, WalletUpdatingValidator {

    @Inject
    private WalletRepository walletRepository;

    /**
     * {@inheritDoc}
     *
     * @param value
     */
    @Override
    public void validate(Wallet value) {

        final Optional<Wallet> found = this.walletRepository
                .findOptionalByNameAndWalletType(value.getName(), value.getWalletType());

        if (found.isPresent() && !found.get().equals(value)) {
            throw new BusinessLogicException("error.wallet.duplicated");
        }
    }
}
