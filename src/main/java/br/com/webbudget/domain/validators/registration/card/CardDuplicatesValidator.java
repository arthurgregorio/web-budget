package br.com.webbudget.domain.validators.registration.card;

import br.com.webbudget.domain.entities.registration.Card;
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
public class CardDuplicatesValidator implements CardSavingValidator, CardUpdatingValidator {

    @Inject
    private CardRepository cardRepository;

    /**
     * {@inheritDoc}
     *
     * @param value
     */
    @Override
    public void validate(Card value) {

        final Optional<Card> found = this.cardRepository.findOptionalByNumberAndCardType(
                value.getNumber(), value.getCardType());

        if (found.isPresent() && !found.get().equals(value)) {
            throw BusinessLogicException.create("error.card.duplicated");
        }
    }
}
