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
package br.com.webbudget.domain.repositories.view;

import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.entities.view.PeriodResult;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The {@link PeriodResult} repository
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 16/04/2019
 */
@Repository
public interface PeriodResultRepository extends EntityRepository<PeriodResult, Long> {

    /**
     * List the first six results
     *
     * @return a {@link List} with the first six results
     */
    List<PeriodResult> findTop6OrderByIdDesc();

    /**
     * Find the result for a specific {@link FinancialPeriod}
     *
     * @param financialPeriodId to use as filter
     * @return an {@link Optional} of the result found
     */
    Optional<PeriodResult> findByFinancialPeriodId(Long financialPeriodId);
}