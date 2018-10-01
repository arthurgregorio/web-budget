/*
 * Copyright (C) 2013 Arthur Gregorio, AG.Software
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
package br.com.webbudget.domain.repositories.registration;

import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.entities.registration.FinancialPeriod_;
import br.com.webbudget.domain.repositories.DefaultRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.Criteria;

import javax.persistence.metamodel.SingularAttribute;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * The {@link FinancialPeriod} repository
 *
 * @author Arthur Gregorio
 *
 * @version 3.0.0
 * @since 1.0.0, 04/03/2013
 */
@Repository
public interface FinancialPeriodRepository extends DefaultRepository<FinancialPeriod> {

    /**
     * Method used to validate if the dates give are within other periods
     *
     * @param start the date to start
     * @param end the date to end
     * @return total of periods within the dates
     */
    @Query("SELECT count(*) FROM FinancialPeriod fp " +
            "WHERE (?1 BETWEEN fp.start AND fp.end) " +
            "OR (?2 BETWEEN fp.start AND fp.end)")
    Long validatePeriodDates(LocalDate start, LocalDate end);

    /**
     * List all {@link FinancialPeriod} by the closing status
     * 
     * @param isClosed <code>true</code> for closed periods or <code>false</code> for open periods
     * @return a {@link List} of {@link FinancialPeriod}
     */
    List<FinancialPeriod> findByClosed(boolean isClosed);

    /**
     * Find a {@link FinancialPeriod} by the identification
     * 
     * @param identification the identification of the {@link FinancialPeriod} to search
     * @return an {@link Optional} of the {@link FinancialPeriod}
     */
    Optional<FinancialPeriod> findOptionalByIdentification(String identification);

    /**
     * {@inheritDoc}
     *
     * @param filter
     * @return
     */
    @Override
    default Criteria<FinancialPeriod, FinancialPeriod> getRestrictions(String filter) {
        return this.criteria().likeIgnoreCase(FinancialPeriod_.identification, filter);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    default SingularAttribute<FinancialPeriod, Boolean> getEntityStateProperty() {
        return FinancialPeriod_.closed;
    }

    /**
     * {@inheritDoc}
     *
     * @param criteria
     */
    @Override
    default void setOrder(Criteria<FinancialPeriod, FinancialPeriod> criteria) {
        criteria.orderDesc(FinancialPeriod_.start);
    }
}
