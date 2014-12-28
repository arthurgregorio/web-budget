package br.com.webbudget.domain.repository.card;

import br.com.webbudget.domain.entity.card.Card;
import br.com.webbudget.domain.entity.card.CardType;
import br.com.webbudget.domain.repository.GenericRepository;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
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
public class CardRepository extends GenericRepository<Card, Long> implements ICardRepository {

    /**
     * 
     * @param isBlocked
     * @return 
     */
    @Override
    public List<Card> listDebit(Boolean isBlocked) {

        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());
        
        if (isBlocked != null) {
            criteria.add(Restrictions.eq("blocked", isBlocked));
        }
        
        criteria.add(Restrictions.eq("cardType", CardType.DEBIT));
        
        criteria.addOrder(Order.asc("number"));
        
        return criteria.list();
    }

    /**
     * 
     * @param isBlocked
     * @return 
     */
    @Override
    public List<Card> listCredit(Boolean isBlocked) {
        
        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());
        
        if (isBlocked != null) {
            criteria.add(Restrictions.eq("blocked", isBlocked));
        }
        
        criteria.add(Restrictions.eq("cardType", CardType.CREDIT));
        
        criteria.addOrder(Order.asc("number"));
        
        return criteria.list();
    }
    
    /**
     * 
     * @param isBlocked
     * @return 
     */
    @Override
    public List<Card> listByStatus(Boolean isBlocked) {
       
        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());
        
        if (isBlocked != null) {
            criteria.add(Restrictions.eq("blocked", isBlocked));
        }
        
        criteria.addOrder(Order.asc("number"));
        
        return criteria.list();
    }

    /**
     * 
     * @param number
     * @param cardType
     * @return 
     */
    @Override
    public Card findByNumberAndType(String number, CardType cardType) {
        
        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());

        criteria.add(Restrictions.eq("number", number));
        criteria.add(Restrictions.eq("cardType", cardType));
        
        return (Card) criteria.uniqueResult();
    }
}
