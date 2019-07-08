/*
 * Copyright (C) 2018 Arthur Gregorio, AG.Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.com.webbudget.domain.logics.registration.wallet;

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
public class WalletDuplicatesValidator implements WalletSavingLogic, WalletUpdatingLogic {

    @Inject
    private WalletRepository walletRepository;

    /**
     * {@inheritDoc}
     *
     * @param value
     */
    @Override
    public void run(Wallet value) {
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
        final Optional<Wallet> found = this.find(value.getName(), value.getBank(), value.getWalletType());
        found.ifPresent(movementClass -> {
            throw new BusinessLogicException("error.wallet.duplicated");
        });
    }

    /**
     * If the {@link Wallet} is saved, compare the found with the one to be validated
     *
     * @param value the value to be evaluated
     */
    private void validateSaved(Wallet value) {
        final Optional<Wallet> found = this.find(value.getName(), value.getBank(), value.getWalletType());
        if (found.isPresent() && !found.get().equals(value)) {
            throw new BusinessLogicException("error.wallet.duplicated");
        }
    }

    /**
     * Find the value on the database to compare with the one to be validated
     *
     * @param name the name of the {@link Wallet}
     * @param bank the bank used by this {@link Wallet}
     * @param walletType the {@link WalletType} of the {@link Wallet}
     * @return a {@link Optional} of the {@link Wallet}
     */
    private Optional<Wallet> find(String name, String bank, WalletType walletType) {
        return this.walletRepository.findByNameAndBankAndWalletType(name, bank, walletType);
    }
}
