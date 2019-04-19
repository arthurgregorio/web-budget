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
package br.com.webbudget.domain.repositories.financial;

import br.com.webbudget.domain.entities.financial.Closing;
import br.com.webbudget.domain.repositories.DefaultRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * The {@link Closing} repository
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 1.0.0, 09/04/2014
 */
@Repository
public interface ClosingRepository extends DefaultRepository<Closing> {

    /**
     * Find the value accumulated of the last {@link Closing}
     *
     * @return an {@link Optional} of the value accumulated from the last {@link Closing}
     */
    @Query("SELECT cl.accumulated " +
            "FROM Closing cl " +
            "WHERE cl.id = (SELECT MAX(id) FROM Closing)")
    Optional<BigDecimal> findLastClosingAccumulatedValue();

    /**
     * Select most recent {@link Closing}
     *
     * @return an {@link Optional} of the last {@link Closing}
     */
    @Query("FROM Closing cl " +
            "WHERE cl.id = (SELECT MAX(id) FROM Closing)")
    Optional<Closing> findLastClosing();
}
