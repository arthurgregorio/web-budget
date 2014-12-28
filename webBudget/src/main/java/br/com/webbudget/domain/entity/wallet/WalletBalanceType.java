package br.com.webbudget.domain.entity.wallet;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 03/04/2014
 */
public enum WalletBalanceType {

    ADJUSTMENT("beans.wallet-balance.adjustment"), 
    TRANSFERENCE("beans.wallet-balance.transference"),
    CLOSING_BALANCE("beans.wallet-balance.closing-balance"),
    TRANSFER_ADJUSTMENT("beans.wallet-balance.transfer-adjustment"); 
    
    private final String i18nKey;

    /**
     * 
     * @param i18nKey 
     */
    private WalletBalanceType(String i18nKey) {
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
