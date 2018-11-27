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
package br.com.webbudget.domain.entities.financial;

import br.com.webbudget.domain.entities.PersistentEntity;
import br.com.webbudget.domain.entities.registration.Card;
import br.com.webbudget.domain.entities.registration.CardInvoice;
import br.com.webbudget.domain.entities.registration.Contact;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.infrastructure.utils.RandomCode;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static br.com.webbudget.infrastructure.utils.DefaultSchemes.FINANCIAL;
import static br.com.webbudget.infrastructure.utils.DefaultSchemes.FINANCIAL_AUDIT;
import static javax.persistence.CascadeType.REMOVE;

/**
 * The representation of all financial movements in the application
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 1.0.0, 04/03/2014
 */
@Entity
@Audited
@Table(name = "movements", schema = FINANCIAL)
@ToString(callSuper = true, exclude = "apportionments")
@AuditTable(value = "movements", schema = FINANCIAL_AUDIT)
@EqualsAndHashCode(callSuper = true, exclude = "apportionments")
public class Movement extends PersistentEntity {

    @Getter
    @Column(name = "code", nullable = false, length = 6, unique = true)
    private String code;
    @Getter
    @Setter
    @NotNull(message = "{movement.value}")
    @Column(name = "value", nullable = false)
    private BigDecimal value;
    @Getter
    @Setter
    @NotBlank(message = "{movement.description}")
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;
    @Getter
    @Setter
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "movement_state", nullable = false, length = 45)
    private MovementState movementState;
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false, length = 45)
    private MovementType movementType;

    @Getter
    @Setter
    @OneToOne(cascade = REMOVE)
    @JoinColumn(name = "id_payment")
    private Payment payment;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_contact")
    private Contact contact;
    @Getter
    @Setter
    @ManyToOne
    @NotNull(message = "{movement.financial-period}")
    @JoinColumn(name = "id_financial_period", nullable = false)
    private FinancialPeriod financialPeriod;

    @Getter
    @OneToMany(mappedBy = "movement", cascade = REMOVE)
    private List<Apportionment> apportionments;

    @Getter
    @Transient
    private final Set<Apportionment> deletedApportionments;

    /**
     * Constructor
     */
    public Movement() {

        this.code = RandomCode.alphanumeric(6);

        this.apportionments = new ArrayList<>();
        this.deletedApportionments = new HashSet<>();

        this.movementState = MovementState.OPEN;
        this.movementType = MovementType.MOVEMENT;
    }

    /**
     * Method used to add new an new {@link Apportionment} to this movement
     *
     * @param apportionment the {@link Apportionment} to be added
     */
    public void add(Apportionment apportionment) {

        // TODO validate if you are not inserting duplicates, same CC and MC
        // TODO validate if you are not inserting debit and credit apportionment, only one is permitted
        // TODO validate apportionment with value = 0

        this.apportionments.add(apportionment);
    }

    /**
     * Remove an {@link Apportionment} from the list of apportionments
     *
     * @param apportionment the {@link Apportionment} to be removed
     */
    public void remove(Apportionment apportionment) {
        if (apportionment.isSaved()) {
            this.deletedApportionments.add(apportionment);
        }
        this.apportionments.remove(apportionment);
    }

    /**
     * Get only the name of the {@link Contact} of this movement
     *
     * @return the name of the linked {@link Contact}
     */
    public String getContactName() {
        return this.contact != null ? this.contact.getName() : "";
    }

    /**
     * Get the payment date of this movement
     *
     * @return the payment date
     */
    public LocalDate getPaymentDate() {
        return this.payment != null ? this.payment.getPaymentDate() : null;
    }

    /**
     * To check if this movement is paid with a credit {@link Card}
     *
     * @return true if is, false if not
     */
    public boolean isPaidWithCreditCard() {
        return this.payment.isPaidWithCreditCard();
    }

    /**
     * To check if this movement is paid with a debit {@link Card}
     *
     * @return true if is, false if not
     */
    public boolean isPaidWithDebitCard() {
        return this.payment.isPaidWithDebitCard();
    }

    /**
     * To check if this movement is a {@link CardInvoice}
     *
     * @return true if is, false otherwise
     */
    public boolean isCardInvoice() {
        return this.movementType == MovementType.CARD_INVOICE;
    }

    /**
     * To check if this movement is paid or not
     *
     * @return true if paid, false otherwise
     */
    public boolean isPaid() {
        return this.getMovementState() == MovementState.PAID;
    }

    /**
     * To check if this movement can be edited
     *
     * It only can be edited if the {@link FinancialPeriod} is open and the movement is open to
     *
     * @return true if is, false otherwise
     */
    public boolean isEditable() {
        return !this.isPaid() && !this.financialPeriod.isClosed();
    }

    /**
     * To check if this movement can be deleted
     *
     * @return true if can be, false if not
     */
    public boolean isDeletable() {
        return (this.movementState == MovementState.OPEN || this.movementState == MovementState.PAID)
                && !this.financialPeriod.isClosed();
    }

    /**
     * To check if this movement is overdue
     *
     * @return true if is, false otherwise
     */
    public boolean isOverdue() {
        return this.dueDate.isBefore(LocalDate.now());
    }

    /**
     * To check if this movement is a expense
     *
     * @return true if is, false otherwise
     */
    public boolean isExpense() {
        return this.apportionments
                .stream()
                .findFirst()
                .map(Apportionment::isExpense)
                .orElse(false);
    }

    /**
     * To check if this movement is a revenue
     *
     * @return true if is, false otherwise
     */
    public boolean isRevenue() {
        return this.apportionments
                .stream()
                .findFirst()
                .map(Apportionment::isRevenue)
                .orElse(false);
    }

    /**
     * Sum all the apportionments and give the total
     *
     * @return the total of all {@link Apportionment}
     */
    public BigDecimal calculateApportionmentsTotal() {
        return this.apportionments
                .stream()
                .map(Apportionment::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calculate the amount not divided into the apportionments
     *
     * @return the total possible to be divided by an {@link Apportionment}
     */
    public BigDecimal calculateRemainingTotal() {
        return this.value.subtract(this.calculateApportionmentsTotal());
    }
}
