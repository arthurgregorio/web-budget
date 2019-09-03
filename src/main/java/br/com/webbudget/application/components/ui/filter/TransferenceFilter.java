package br.com.webbudget.application.components.ui.filter;

import br.com.webbudget.domain.entities.registration.Wallet;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * Filter to be used on the transference historic listing
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.1.0, 01/09/2019
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TransferenceFilter extends BasicFilter {

    @Getter
    @Setter
    private Wallet originWallet;
    @Getter
    @Setter
    private Wallet destinationWallet;

    @Getter
    @Setter
    private LocalDate operationDate;

    /**
     * Clear this filter
     */
    public void clear() {
        this.originWallet = null;
        this.destinationWallet = null;
        this.operationDate = null;
    }
}
