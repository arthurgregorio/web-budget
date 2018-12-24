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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

import static br.com.webbudget.infrastructure.utils.DefaultSchemes.REGISTRATION;
import static br.com.webbudget.infrastructure.utils.DefaultSchemes.REGISTRATION_AUDIT;

/**
 * The representation of a wallet in the application
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 12/03/2014
 */
@Entity
@Audited
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "wallets", schema = REGISTRATION)
@AuditTable(value = "wallets", schema = REGISTRATION_AUDIT)
public class Wallet extends PersistentEntity {

    @Getter
    @Setter
    @NotBlank(message = "{wallet.name}")
    @Column(name = "name", nullable = false, length = 45)
    private String name;
    @Getter
    @Setter
    @Column(name = "bank", length = 45)
    private String bank;
    @Getter
    @Setter
    @Column(name = "agency", length = 10)
    private String agency;
    @Getter
    @Setter
    @Column(name = "account", length = 45)
    private String account;
    @Getter
    @Setter
    @Column(name = "digit", length = 4)
    private String digit;
    @Setter
    @Column(name = "description")
    private String description;
    @Getter
    @Setter
    @NotNull(message = "{wallet.balance}")
    @Column(name = "actual_balance", nullable = false)
    private BigDecimal actualBalance;
    @Getter
    @Setter
    @Column(name = "active", nullable = false)
    private boolean active;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @NotNull(message = "{wallet.wallet-type}")
    @Column(name = "wallet_type", nullable = false, length = 45)
    private WalletType walletType;

    /**
     * Default constructor
     */
    public Wallet() {
        this.active = true;
        this.actualBalance = BigDecimal.ZERO;
    }

    /**
     * Create a better description to this wallet using the bank account information if is available
     *
     * @return the description of this wallet
     */
    public String getDescription() {
        return this.walletType == WalletType.BANK_ACCOUNT ? this.name + " - " + this.bank : this.name;
    }

    /**
     * To check if the balance of this wallet is nagative
     *
     * @return <code>true</code> for negative balance, <code>false</code> otherwise
     */
    public boolean isBalanceNegative() {
        return this.actualBalance.signum() < 0;
    }

    /**
     * To check if the wallet is empty, it means balance = 0
     *
     * @return <code>true</code> for empty wallet, <code>false</code> otherwise
     */
    public boolean isEmpty() { // FIXME refactor
        return this.actualBalance == BigDecimal.ZERO;
    }
}
