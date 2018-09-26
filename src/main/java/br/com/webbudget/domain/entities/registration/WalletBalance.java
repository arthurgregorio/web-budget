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
package br.com.webbudget.domain.entities.registration;

import br.com.webbudget.domain.entities.PersistentEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

import static br.com.webbudget.infrastructure.utils.DefaultSchemes.REGISTRATION;
import static br.com.webbudget.infrastructure.utils.DefaultSchemes.REGISTRATION_AUDIT;

/**
 * The representation of a wallet balance in the application
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 20/05/2014
 */
@Entity
@Audited
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "wallet_balances", schema = REGISTRATION)
@AuditTable(value = "wallet_balances", schema = REGISTRATION_AUDIT)
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
    @Column(name = "movemented_value", nullable = false)
    @NotNull(message = "{wallet-balance.movemented-value}")
    private BigDecimal movementedValue;
    @Getter
    @Setter
    @Column(name = "movement_code")
    private String movementCode;
    @Getter
    @Setter
    @Column(name = "observations", length = 255)
    private String observations;
    @Getter
    @Setter
    @Column(name = "movement_date", nullable = false)
    private LocalDate movementDate;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "balance_type", nullable = false)
    private BalanceType balanceType;
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "reason_type", nullable = false)
    private ReasonType reasonType;
    
    @Getter
    @Setter
    @ManyToOne
    @NotNull(message = "{wallet-balance.null-wallet}")
    @JoinColumn(name = "id_wallet", nullable = false)
    private Wallet wallet;

    /**
     * Process the balances of this balance when requested
     *
     * Use this after set a new actual balance
     */
    public void processBalances() {
        
        // calculate the actual and the old balance
        this.oldBalance = this.wallet.getActualBalance();
        this.actualBalance = (this.wallet.getActualBalance()
                .add(this.movementedValue));
        
        // update the target wallet balance
        this.wallet.setActualBalance(this.actualBalance);
    }

    /**
     *
     * @return
     */
    public boolean isOldBalanceNegative() {
        return this.oldBalance.signum() < 0;
    }

    /**
     *
     * @return
     */
    public boolean isActualBalanceNegative() {
        return this.actualBalance.signum() < 0;
    }

    /**
     *
     * @return
     */
    public boolean isMovementedValueNegative() {
        return this.movementedValue.signum() < 0;
    }
}
