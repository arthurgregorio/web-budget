package br.com.webbudget.domain.repository.card;

import br.com.webbudget.domain.entity.card.CardInvoice;
import br.com.webbudget.domain.entity.movement.Movement;
import br.com.webbudget.domain.repository.IGenericRepository;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 04/03/2013
 */
public interface ICardInvoiceRepository extends IGenericRepository<CardInvoice, Long> {

    /**
     * 
     * @param movement
     * @return 
     */
    public CardInvoice findByMovement(Movement movement);
}
