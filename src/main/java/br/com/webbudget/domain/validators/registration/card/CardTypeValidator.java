package br.com.webbudget.domain.validators.registration.card;

import br.com.webbudget.domain.entities.registration.Card;
import br.com.webbudget.domain.entities.registration.CardType;
import br.com.webbudget.domain.exceptions.BusinessLogicException;

import javax.enterprise.context.Dependent;

/**
 * The {@link CardType} validator
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 27/09/2018
 */
@Dependent
public class CardTypeValidator implements CardSavingValidator, CardUpdatingValidator {

    /**
     * {@inheritDoc }
     *
     * @param value the value to work with
     */
    @Override
    public void validate(Card value) {

        final CardType cardType = value.getCardType();

        if (cardType == CardType.DEBIT && value.getWallet() == null) {
            throw BusinessLogicException.create("error.card.no-wallet");
        }
    }
}
