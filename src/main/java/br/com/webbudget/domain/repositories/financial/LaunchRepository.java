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
package br.com.webbudget.domain.repositories.financial;

import br.com.webbudget.domain.entities.financial.FixedMovement;
import br.com.webbudget.domain.entities.financial.Launch;
import br.com.webbudget.domain.entities.financial.PeriodMovement;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.repositories.DefaultRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;
import java.util.Optional;

/**
 * {@link Launch} entity repository
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 27/03/2019
 */
@Repository
public interface LaunchRepository extends DefaultRepository<Launch> {

    /**
     * Find the last {@link Launch} for a give {@link FixedMovement}
     *
     * @param fixedMovementId of the {@link FixedMovement} to find his launch
     * @return an {@link Optional} of the {@link Launch}
     */
    @Query("SELECT lc.quoteNumber " +
            "FROM Launch lc " +
            "WHERE lc.id = (SELECT MAX(id) FROM Launch WHERE fixedMovement.id = ?1)")
    Optional<Integer> findLastLaunchCounterFor(long fixedMovementId);

    /**
     * Find all {@link Launch} for a given {@link FixedMovement}
     *
     * @param fixedMovement to search for the {@link Launch}
     * @return the {@link List} of {@link Launch} found
     */
    List<Launch> findByFixedMovement(FixedMovement fixedMovement);

    /**
     * Find a {@link Launch} by a {@link PeriodMovement}
     *
     * @param periodMovement to use as filter
     * @return an {@link Optional} of the {@link Launch}
     */
    Optional<Launch> findByPeriodMovement(PeriodMovement periodMovement);

    /**
     * Find the last {@link Launch} for the {@link FixedMovement} given
     *
     * @param fixedMovement to be used as filter
     * @return an {@link Optional} of the last {@link Launch} for this {@link FixedMovement}
     */
    @Query("FROM Launch la " +
            "WHERE la.quoteNumber = (SELECT MAX(l.quoteNumber) FROM Launch l WHERE l.fixedMovement = ?1) " +
            "AND la.fixedMovement = ?1")
    Optional<Launch> findLastLaunch(FixedMovement fixedMovement);

    /**
     * Method to count how much {@link Launch} we have for a given {@link FixedMovement} at open {@link FinancialPeriod}
     *
     * @param fixedMovement to search for {@link Launch}
     * @return the total of {@link Launch} found for this {@link FixedMovement}
     */
    @Query("SELECT COUNT(*) " +
            "FROM Launch la " +
            "WHERE la.fixedMovement = ?1 " +
            "AND la.financialPeriod.id IN " +
            "   (SELECT fp.id FROM FinancialPeriod fp WHERE fp.closed = false AND fp.expired = false)")
    long countByFixedMovementAtCurrentFinancialPeriod(FixedMovement fixedMovement);
}
