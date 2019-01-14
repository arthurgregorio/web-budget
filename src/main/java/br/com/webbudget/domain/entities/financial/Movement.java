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
import br.com.webbudget.domain.entities.registration.Contact;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.infrastructure.utils.RandomCode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.deltaspike.data.api.EntityGraph;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.*;

import static br.com.webbudget.infrastructure.utils.DefaultSchemes.FINANCIAL;
import static br.com.webbudget.infrastructure.utils.DefaultSchemes.FINANCIAL_AUDIT;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REMOVE;

/**
 * This is a superclass used to map the common attributes between the {@link FixedMovement} and the
 * {@link PeriodMovement}. This class also represents the basic type of financial transaction in the application
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 1.0.0, 04/03/2014
 */
@Entity
@Audited
@ToString
@EqualsAndHashCode(callSuper = true)
@Table(name = "movements", schema = FINANCIAL)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@AuditTable(value = "movements", schema = FINANCIAL_AUDIT)
@NamedEntityGraph(name = "Movement.full", attributeNodes = @NamedAttributeNode(value = "apportionments"))
@DiscriminatorColumn(name = "discriminator_value", length = 15, discriminatorType = DiscriminatorType.STRING)
public class Movement extends PersistentEntity {

    @Getter
    @Column(name = "code", nullable = false, length = 6, unique = true)
    private String code;
    @Getter
    @Setter
    @NotBlank(message = "{movement.identification}")
    @Column(name = "identification", nullable = false, length = 45)
    private String identification;
    @Getter
    @Setter
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @Getter
    @Setter
    @NotNull(message = "{movement.value}")
    @Column(name = "value", nullable = false)
    private BigDecimal value;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_contact")
    private Contact contact;

    @OneToMany(mappedBy = "movement")
    private List<Apportionment> apportionments;

    @Transient
    private Set<Apportionment> deletedApportionments;

    /**
     * Constructor...
     */
    public Movement() {
        this.code = RandomCode.alphanumeric(6);
        this.apportionments = new ArrayList<>();
        this.deletedApportionments = new HashSet<>();
    }

    /**
     * Getter for the apportionments
     *
     * @return an unmodifiable {@link List} of the {@link #apportionments}
     */
    public List<Apportionment> getApportionments() {
        return Collections.unmodifiableList(this.apportionments);
    }

    /**
     * Getter for the deleted apportionments
     *
     * @return an unmodifiable {@link Set} of the {@link #deletedApportionments}
     */
    public Set<Apportionment> getDeletedApportionments() {
        return Collections.unmodifiableSet(this.deletedApportionments);
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
     * Method used to add new an new {@link Apportionment} to this movement
     *
     * @param apportionment the {@link Apportionment} to be added
     */
    public void add(Apportionment apportionment) {
        apportionment.setMovement(this);
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
     * Remove the contact linked to this movement
     */
    public void clearContact() {
        this.contact = null;
    }

    /**
     * Calculate the amount not divided into the apportionments
     *
     * @return the total possible to be divided by an {@link Apportionment}
     */
    public BigDecimal calculateRemainingTotal() {

        final BigDecimal remaining = this.value.subtract(this.calculateApportionmentsTotal());

        if (remaining.signum() <= 0) {
            throw new BusinessLogicException("error.period-movement.no-value-to-divide");
        }

        return remaining;
    }

    /**
     * Sum all the apportionments and give the total
     *
     * @return the total of all {@link Apportionment}
     */
    private BigDecimal calculateApportionmentsTotal() {
        return this.apportionments
                .stream()
                .map(Apportionment::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
