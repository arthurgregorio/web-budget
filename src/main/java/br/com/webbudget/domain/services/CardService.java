/*
 * Copyright (C) 2015 Arthur Gregorio, AG.Software
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
package br.com.webbudget.domain.services;

import br.com.webbudget.domain.entities.registration.Card;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.repositories.registration.CardRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;

/**
 * The service responsible by the business operations with {@link Card}
 *
 * @author Arthur Gregorio
 *
 * @version 1.2.0
 * @since 1.0.0, 06/04/2014
 */
@ApplicationScoped
public class CardService {

    @Inject
    private CardRepository cardRepository;

    /**
     * Use this method to persist a {@link Card}
     *
     * @param card the {@link Card} to be persisted
     */
    @Transactional
    public void save(Card card) {

        card.validate();
        
        final Optional<Card> found = this.cardRepository.findOptionalByNumberAndCardType(
                card.getNumber(), card.getCardType());
        
        if (found.isPresent()) {
            throw new BusinessLogicException("error.card.duplicated");
        }

        this.cardRepository.save(card);
    }

    /**
     * Use this method to update a persisted {@link Card}
     *
     * @param card the {@link Card} to be updated
     * @return the updated {@link Card}
     */
    @Transactional
    public Card update(Card card) {

        card.validate();
        
        final Optional<Card> found = this.cardRepository.findOptionalByNumberAndCardType(
                card.getNumber(), card.getCardType());
        
        if (found.isPresent() && !found.get().equals(card)) {
            throw new BusinessLogicException("error.card.duplicated");
        }

        return this.cardRepository.save(card);
    }

    /**
     * Use this method to delete a persisted {@link Card}
     *
     * @param card the {@link Card} to be deleted
     */
    @Transactional
    public void delete(Card card) {
        this.cardRepository.attachAndRemove(card);
    }
}
