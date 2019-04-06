/*
 * Copyright (C) 2019 Arthur Gregorio, AG.Software
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
package br.com.webbudget.application.components.builder;

import br.com.webbudget.domain.entities.financial.Apportionment;
import br.com.webbudget.domain.entities.financial.PeriodMovement;
import br.com.webbudget.domain.entities.financial.PeriodMovementType;
import br.com.webbudget.domain.entities.registration.Contact;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

/**
 * The {@link PeriodMovement} builder
 *
 * Use this class to easily create {@link PeriodMovement}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 03/03/2019
 */
public final class PeriodMovementBuilder extends AbstractBuilder<PeriodMovement> {

    /**
     * Constructor
     */
    public PeriodMovementBuilder() {
        this.instance = new PeriodMovement();
    }

    /**
     * Set the identification of this {@link PeriodMovement}
     *
     * @param identification to be added
     * @return this builder
     */
    public PeriodMovementBuilder identification(String identification) {
        this.instance.setIdentification(identification);
        return this;
    }

    /**
     * Set the description to the {@link PeriodMovement}
     *
     * @param description to be added
     * @return this builder
     */
    public PeriodMovementBuilder description(String description) {
        this.instance.setDescription(description);
        return this;
    }

    /**
     * Set the due date for the {@link PeriodMovement}
     *
     * @param dueDate for this {@link PeriodMovement}
     * @return this builder
     */
    public PeriodMovementBuilder dueDate(LocalDate dueDate) {
        this.instance.setDueDate(dueDate);
        return this;
    }

    /**
     * The type of this {@link PeriodMovement}
     *
     * @param periodMovementType for this
     * @return this builder
     */
    public PeriodMovementBuilder type(PeriodMovementType periodMovementType) {
        this.instance.setPeriodMovementType(periodMovementType);
        return this;
    }

    /**
     * The {@link FinancialPeriod} to be used for this {@link PeriodMovement}
     *
     * @param financialPeriod to be added
     * @return this builder
     */
    public PeriodMovementBuilder financialPeriod(FinancialPeriod financialPeriod) {
        this.instance.setFinancialPeriod(financialPeriod);
        return this;
    }

    /**
     * Value of the {@link PeriodMovement}
     *
     * @param value of the {@link PeriodMovement}
     * @return this builder
     */
    public PeriodMovementBuilder value(BigDecimal value) {
        this.instance.setValue(value);
        return this;
    }

    /**
     * The {@link Contact} to link with the {@link PeriodMovement}
     *
     * @param contact to be added
     * @return this builder
     */
    public PeriodMovementBuilder contact(Contact contact) {
        this.instance.setContact(contact);
        return this;
    }

    /**
     * Add the {@link Apportionment} to the {@link PeriodMovement}
     *
     * @param apportionment to be added
     * @return this builder
     */
    public PeriodMovementBuilder addApportionment(Apportionment apportionment) {
        this.instance.add(apportionment);
        return this;
    }

    /**
     * Add a {@link Set} of {@link Apportionment} to the {@link PeriodMovement}
     *
     * @param apportionments a {@link Set} of {@link Apportionment}
     * @return this builder
     */
    public PeriodMovementBuilder addApportionments(Set<Apportionment> apportionments) {
        this.instance.addAll(apportionments);
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public PeriodMovement build() {
        return this.instance;
    }
}