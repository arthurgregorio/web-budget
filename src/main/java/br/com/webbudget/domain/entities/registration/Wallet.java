/*
 * Copyright (C) 2014 Arthur Gregorio, AG.Software
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
@Table(name = "wallets", schema = REGISTRATION)
@AuditTable(value = "wallets", schema = REGISTRATION_AUDIT)
@ToString(callSuper = true, of = {"name", "walletType", "bank"})
@EqualsAndHashCode(callSuper = true, of = {"name", "walletType", "bank"})
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
    @Getter
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
     * Create a better name to this wallet using the bank account information if available
     *
     * @return the full name of this wallet
     */
    public String getFullName() {
        return this.walletType == WalletType.BANK_ACCOUNT ? this.name + " - " + this.bank : this.name;
    }

    /**
     * To check if the balance of this wallet is negative
     *
     * @return true for negative balance, false otherwise
     */
    public boolean isBalanceNegative() {
        return this.actualBalance.signum() < 0;
    }

    /**
     * To check if the wallet is empty, it means balance = 0
     *
     * @return true for empty wallet, false otherwise
     */
    public boolean isEmpty() {
        return this.actualBalance.equals(BigDecimal.ZERO);
    }
}
