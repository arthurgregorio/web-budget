package br.com.webbudget.domain.repository.card;

import br.com.webbudget.domain.entity.card.CardInvoice;
import br.com.webbudget.domain.entity.movement.Movement;
import br.com.webbudget.domain.repository.GenericRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 18/10/2013
 */
@Repository
public class CardInvoiceRepository extends GenericRepository<CardInvoice, Long> implements ICardInvoiceRepository {

    /**
     * 
     * @param movement
     * @return 
     */
    @Override
    public CardInvoice findByMovement(Movement movement) {
        
        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());
        
        criteria.createAlias("movement", "mv");
        criteria.add(Restrictions.eq("mv.id", movement.getId()));
        
        return (CardInvoice) criteria.uniqueResult();
    }
}
