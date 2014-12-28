package br.com.webbudget.domain.entity.movement;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 03/04/2014
 */
public enum PaymentMethodType {

    IN_CASH("beans.payment-method-type.in-cash"), 
    DEBIT_CARD("beans.payment-method-type.debit-card"),
    CREDIT_CARD("beans.payment-method-type.credit-card");
//    INSTALLMENT("beans.payment-method-type.installment");
    
    private final String i18nKey;

    /**
     * 
     * @param i18nKey 
     */
    private PaymentMethodType(String i18nKey) {
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
