package br.com.webbudget.domain.entity.wallet;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 03/04/2014
 */
public enum WalletType {

    PERSONAL("beans.wallet.personal"), 
    BANK_ACCOUNT("beans.wallet.bank-account");
    
    private final String i18nKey;

    /**
     * 
     * @param i18nKey 
     */
    private WalletType(String i18nKey) {
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
