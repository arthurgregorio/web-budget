/*
 * Copyright (C) 2018 Arthur Gregorio, AG.Software
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
package br.com.webbudget.domain.entities.financial;

import br.com.webbudget.domain.entities.PersistentEntity;
import br.com.webbudget.domain.entities.registration.Wallet;
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

import static br.com.webbudget.infrastructure.utils.DefaultSchemes.FINANCIAL;
import static br.com.webbudget.infrastructure.utils.DefaultSchemes.FINANCIAL_AUDIT;

/**
 * This class represents all the value transfers between two {@link Wallet}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 03/10/2018
 */
@Entity
@Audited
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "transfers", schema = FINANCIAL)
@AuditTable(value = "transfers", schema = FINANCIAL_AUDIT)
public class Transference extends PersistentEntity {

    @Getter
    @Setter
    @NotNull(message = "{transference.value}")
    @Column(name = "value", nullable = false)
    private BigDecimal value;
    @Getter
    @Setter
    @Column(name = "transfer_date", nullable = false)
    @NotNull(message = "{transference.transfer-date}")
    private LocalDate transferDate;
    @Getter
    @Setter
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Getter
    @Setter
    @ManyToOne
    @NotNull(message = "{transference.origin}")
    @JoinColumn(name = "id_origin", nullable = false)
    private Wallet origin;
    @Getter
    @Setter
    @ManyToOne
    @NotNull(message = "{transference.destination}")
    @JoinColumn(name = "id_destination", nullable = false)
    private Wallet destination;

    /**
     * Constructor...
     */
    public Transference() {
        this.transferDate = LocalDate.now();
    }

    /**
     * Get the origin wallet balance
     *
     * @return the actual balance
     */
    public BigDecimal getOriginBalance() {
        return this.origin != null ? this.origin.getActualBalance() : BigDecimal.ZERO;
    }

    /**
     * Get the destination wallet balance
     *
     * @return the actual balance
     */
    public BigDecimal getDestinationBalance() {
        return this.destination != null ? this.destination.getActualBalance() : BigDecimal.ZERO;
    }

    /**
     * Helper method to check the current status of the current balance on the origin
     *
     * @return true if the balance is negative, false otherwise
     */
    public boolean isOriginNegative() {
        return this.getOriginBalance().signum() < 0;
    }

    /**
     * Helper method to check the current status of the current balance on the destination
     *
     * @return true if the balance is negative, false otherwise
     */
    public boolean isDestinationNegative() {
        return this.getDestinationBalance().signum() < 0;
    }
}
