package br.com.webbudget.domain.entities.tools;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 07/03/2018
 */
public enum StoreType {

    LDAP("store-type.ldap"),
    LOCAL("store-type.local");
    
    private final String description;

    /**
     *
     * @param description
     */
    private StoreType(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return this.description;
    }
}

