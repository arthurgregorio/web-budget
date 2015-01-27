/*
 * Copyright (C) 2015 Arthur Gregorio, AG.Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package br.com.webbudget.domain.entity.wallet;

import br.com.webbudget.domain.entity.PersistentEntity;
import br.com.webbudget.domain.entity.closing.Closing;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 20/05/2014
 */
@Entity
@ToString(callSuper = true)
@Table(name = "wallet_balances")
@EqualsAndHashCode(callSuper = true)
public class WalletBalance extends PersistentEntity {

    @Getter
    @Setter
    @Column(name = "balance", nullable = false)
    private BigDecimal balance;
    @Getter
    @Setter
    @Column(name = "old_balance")
    private BigDecimal oldBalance;
    @Getter
    @Setter
    @Column(name = "adjustment_value")
    private BigDecimal adjustmentValue;
    @Getter
    @Setter
    @NotNull(message = "{transfer.transfer-value}")
    @Column(name = "transfer_value")
    private BigDecimal transferValue;
    @Getter
    @Setter
    @Column(name = "ins")
    private BigDecimal ins;
    @Getter
    @Setter
    @Column(name = "outs")
    private BigDecimal outs;
    @Getter
    @Setter
    @Column(name = "wallet_balance_type", nullable = false)
    private WalletBalanceType walletBalanceType;
    
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_closing")
    private Closing closing;
    @Getter
    @Setter
    @ManyToOne
    @NotNull(message = "{transfer.null-target}")
    @JoinColumn(name = "id_wallet")
    private Wallet wallet;
    @Getter
    @Setter
    @ManyToOne
    @NotNull(message = "{transfer.null-source}")
    @JoinColumn(name = "id_source_wallet")
    private Wallet sourceWallet;    
    
    /**
     * 
     */
    public WalletBalance() {
        this.ins = BigDecimal.ZERO;
        this.outs = BigDecimal.ZERO;
        this.balance = BigDecimal.ZERO;
        this.oldBalance = BigDecimal.ZERO;
        this.adjustmentValue = BigDecimal.ZERO;
    }
}
