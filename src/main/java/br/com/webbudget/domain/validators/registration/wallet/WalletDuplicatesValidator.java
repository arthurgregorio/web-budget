package br.com.webbudget.domain.validators.registration.wallet;

import br.com.webbudget.domain.entities.registration.Wallet;
import br.com.webbudget.domain.entities.registration.WalletType;
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
public class WalletDuplicatesValidator implements WalletSavingValidator, WalletUpdatingValidator {

    @Inject
    private WalletRepository walletRepository;

    /**
     * {@inheritDoc}
     *
     * @param value
     */
    @Override
    public void validate(Wallet value) {
        if (value.isSaved()) {
            this.validateSaved(value);
        } else {
            this.validateNotSaved(value);
        }
    }

    /**
     * If the {@link Wallet} is not saved, don't compare the found with the one to be validated
     *
     * @param value the value to be evaluated
     */
    private void validateNotSaved(Wallet value) {
        final Optional<Wallet> found = this.find(value.getName(), value.getWalletType());
        found.ifPresent(movementClass -> {
            throw BusinessLogicException.create("error.wallet.duplicated");
        });
    }

    /**
     * If the {@link Wallet} is saved, compare the found with the one to be validated
     *
     * @param value the value to be evaluated
     */
    private void validateSaved(Wallet value) {
        final Optional<Wallet> found = this.find(value.getName(), value.getWalletType());
        if (found.isPresent() && found.get().equals(value)) {
            throw BusinessLogicException.create("error.wallet.duplicated");
        }
    }

    /**
     * Find the value on the database to compare with the one to be validated
     *
     * @param name the name of the {@link Wallet}
     * @param walletType the {@link WalletType} of the {@link Wallet}
     * @return a {@link Optional} of the {@link Wallet}
     */
    private Optional<Wallet> find(String name, WalletType walletType) {
        return this.walletRepository.findOptionalByNameAndWalletType(name, walletType);
    }
}
