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
    @Column(name = "actual_balance", nullable = false)
    private BigDecimal actualBalance;
    @Getter
    @Setter
    @Column(name = "old_balance", nullable = false)
    private BigDecimal oldBalance;
    @Getter
    @Setter
    @NotNull(message = "{wallet-balance.movimented-value}")
    @Column(name = "movimented_value", nullable = false)
    private BigDecimal movimentedValue;
    @Getter
    @Setter
    @Column(name = "movement_code")
    private String movementCode;
    @Getter
    @Setter
    @Column(name = "wallet_balance_type", nullable = false)
    private WalletBalanceType walletBalanceType;
    
    @Getter
    @Setter
    @ManyToOne
    @NotNull(message = "{wallet-balance.null-target}")
    @JoinColumn(name = "id_target_wallet")
    private Wallet targetWallet;
    @Getter
    @Setter
    @ManyToOne
    @NotNull(message = "{wallet-balance.null-source}")
    @JoinColumn(name = "id_source_wallet")
    private Wallet sourceWallet; 
}
