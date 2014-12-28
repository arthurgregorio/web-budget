package br.com.webbudget.domain.entity.movement;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 15/05/2014
 */
public enum MovementType {

    MOVEMENT("beans.movement-type.movement"),
    CARD_INVOICE("beans.movement-type.card-invoice");

    private final String i18nKey;

    /**
     * 
     * @param i18nKey 
     */
    private MovementType(String i18nKey) {
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
