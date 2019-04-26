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

import br.com.webbudget.domain.entities.view.CardConsumeDetailed;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

/**
 * The {@link CardConsumeDetailed} repository
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 25/04/2019
 */
@Repository
public interface CardConsumeDetailedRepository extends EntityRepository<CardConsumeDetailed, Long> {

    /**
     * Find the {@link CardConsumeDetailed} by the card id
     *
     * @param cardId to use as filter
     * @return a {@link List} of {@link CardConsumeDetailed}
     */
    List<CardConsumeDetailed> findByCardId(long cardId);
}