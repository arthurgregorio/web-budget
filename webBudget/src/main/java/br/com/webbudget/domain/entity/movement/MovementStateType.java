package br.com.webbudget.domain.entity.movement;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 21/03/2014
 */
public enum MovementStateType {

    PAID("beans.movement-state-type.paid"), 
    OPEN("beans.movement-state-type.open"),
    CANCELED("beans.movement-state-type.canceled"),
    CALCULATED("beans.movement-state-type.calculated");
    
    private final String i18nKey;

    /**
     * 
     * @param i18nKey 
     */
    private MovementStateType(String i18nKey) {
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
