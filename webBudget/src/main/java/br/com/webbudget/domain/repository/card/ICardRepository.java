package br.com.webbudget.domain.repository.card;

import br.com.webbudget.domain.entity.card.Card;
import br.com.webbudget.domain.entity.card.CardType;
import br.com.webbudget.domain.repository.IGenericRepository;
import java.util.List;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 04/03/2013
 */
public interface ICardRepository extends IGenericRepository<Card, Long> {

    /**
     * 
     * @param isBlocked
     * @return 
     */
    public List<Card> listDebit(Boolean isBlocked);
    
    /**
     * 
     * @param isBlocked
     * @return 
     */
    public List<Card> listCredit(Boolean isBlocked);
    
    /**
     * 
     * @param isBlocked
     * @return 
     */
    public List<Card> listByStatus(Boolean isBlocked);
   
    /**
     * 
     * @param number
     * @param cardType
     * @return 
     */   
    public Card findByNumberAndType(String number, CardType cardType);
}
