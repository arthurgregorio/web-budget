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
package br.com.webbudget.domain.logics.registration.card;

import br.com.webbudget.domain.entities.registration.Card;
import br.com.webbudget.domain.entities.registration.CardType;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.repositories.registration.CardRepository;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.Optional;

/**
 * The {@link Card} duplication validator
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 27/09/2018
 */
@Dependent
public class CardDuplicatesValidator implements CardSavingLogic, CardUpdatingLogic {

    @Inject
    private CardRepository cardRepository;

    /**
     * {@inheritDoc}
     *
     * @param value
     */
    @Override
    public void run(Card value) {
        if (value.isSaved()) {
            this.validateSaved(value);
        } else {
            this.validateNotSaved(value);
        }
    }

    /**
     * If the {@link Card} is not saved, don't compare the found with the one to be validated
     *
     * @param value the value to be evaluated
     */
    private void validateNotSaved(Card value) {
        final Optional<Card> found = this.find(value.getNumber(), value.getCardType());
        found.ifPresent(movementClass -> {
            throw new BusinessLogicException("error.card.duplicated");
        });
    }

    /**
     * If the {@link Card} is saved, compare the found with the one to be validated
     *
     * @param value the value to be evaluated
     */
    private void validateSaved(Card value) {
        final Optional<Card> found = this.find(value.getNumber(), value.getCardType());
        if (found.isPresent() && !found.get().equals(value)) {
            throw new BusinessLogicException("error.card.duplicated");
        }
    }

    /**
     * Find the value on the database to compare with the one to be validated
     *
     * @param number the number of the {@link Card}
     * @param cardType the {@link CardType} of the {@link Card}
     * @return a {@link Optional} of the {@link Card}
     */
    private Optional<Card> find(String number, CardType cardType) {
        return this.cardRepository.findByNumberAndCardType(number, cardType);
    }
}
