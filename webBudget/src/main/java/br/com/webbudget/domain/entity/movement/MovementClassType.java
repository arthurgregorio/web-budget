package br.com.webbudget.domain.entity.movement;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 13/03/2014
 */
public enum MovementClassType {

    IN("beans.movement-class-type.in"),
    OUT("beans.movement-class-type.out");
    
    private final String i18nKey;

    /**
     * 
     * @param i18nKey 
     */
    private MovementClassType(String i18nKey) {
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
