package br.com.webbudget.domain.entity.card;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 03/04/2014
 */
public enum CardType {

    DEBIT("beans.card-type.debit"),
    CREDIT("beans.card-type.credit");
    
    private final String i18nKey;

    /**
     * 
     * @param i18nKey 
     */
    private CardType(String i18nKey) {
        this.i18nKey = i18nKey;
    }

    /**
     * 
     * @return 
     */
    @Override
    public String toString() {
        return this.i18nKey;
    }
}
