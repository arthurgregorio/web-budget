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
import br.com.webbudget.domain.entity.movement.Payment;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 12/03/2014
 */
@Entity
@Table(name = "wallets")
@ToString(callSuper = true, of = {"name", "walletType"})
@EqualsAndHashCode(callSuper = true, of = {"name", "walletType"})
public class Wallet extends PersistentEntity {

    @Getter
    @Setter
    @NotEmpty(message = "{wallet.name}")
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
    @Column(name = "balance", nullable = false)
    private BigDecimal balance;
    @Getter
    @Setter
    @Column(name = "blocked")
    private boolean blocked;

    @Getter
    @Setter
    @NotNull(message = "{wallet.wallet-type}")
    @Column(name = "wallet_type", nullable = false)
    private WalletType walletType;

    @Getter
    @OneToMany(mappedBy = "wallet")
    private List<Payment> payments;

    @Getter
    @Setter
    @Transient
    private String adjustmentReason;
    @Getter
    @Setter
    @Transient
    @NotNull(message = "{wallet.adjustment-value}")
    private BigDecimal adjustmentValue;

    /**
     *
     */
    public Wallet() {
        this.balance = BigDecimal.ZERO;
    }

    /**
     *
     * @return
     */
    public String getAccountFormatted() {

        final StringBuilder builder = new StringBuilder();

        builder.append(this.agency);
        builder.append(" ");
        builder.append(this.account);
        builder.append("-");
        builder.append(this.digit);

        return builder.toString();
    }

    /**
     *
     * @return
     */
    public String getFriendlyName() {
        if (this.walletType == WalletType.BANK_ACCOUNT) {
            return this.name + " - " + this.bank;
        }
        return this.name;
    }
}
