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
package br.com.webbudget.domain.logics.tools.user;

import br.com.webbudget.domain.entities.configuration.StoreType;
import br.com.webbudget.domain.entities.configuration.User;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.repositories.configuration.UserRepository;
import br.com.webbudget.domain.logics.BusinessLogic;
import br.eti.arthurgregorio.shiroee.auth.PasswordEncoder;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.Optional;

/**
 * {@link BusinessLogic} for the user password validation logic
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 09/08/2018
 */
@Dependent
public class PasswordLogic implements UserSavingLogic, UserUpdatingLogic {

    @Inject
    private UserRepository userRepository;

    @Inject
    private PasswordEncoder passwordEncoder;
    
    /**
     * {@inheritDoc }
     *
     * @param value
     */
    @Override
    public void run(User value) {
        if (value.getStoreType() == StoreType.LOCAL) {
            if (value.isSaved()) {
                this.validateSavedUserPassword(value);
            } else {
                this.validateUnsavedUserPassword(value);
            }
        }
    }

    /**
     * Validate the user password for already saved users
     */
    private void validateSavedUserPassword(User value) {

        final Optional<User> optionalUser = this.userRepository.findByUsername(value.getUsername());

        if (value.hasChangedPasswords()) {
            if (!value.isPasswordValid()) {
                throw new BusinessLogicException("error.user.password-not-match");
            }
            value.setPassword(this.cryptPassword(value.getPassword()));
        } else {
            value.setPassword(optionalUser.get().getPassword());
        }
    }

    /**
     * Validate the user password for unsaved users
     */
    private void validateUnsavedUserPassword(User value) {
        if (!value.isPasswordValid()) {
            throw new BusinessLogicException("error.user.password-not-match");
        }
        value.setPassword(this.passwordEncoder.encryptPassword(value.getPassword()));
    }

    /**
     * Crypt the user password to protect it
     * 
     * @param plainPassword the plain password to be crypted
     * @return the crypted password
     */
    private String cryptPassword(String plainPassword) {
        return this.passwordEncoder.encryptPassword(plainPassword);
    }
}
