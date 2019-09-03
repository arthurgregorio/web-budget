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
package br.com.webbudget.domain.repositories.financial;

import br.com.webbudget.application.components.ui.filter.TransferenceFilter;
import br.com.webbudget.domain.entities.financial.Transference;
import br.com.webbudget.domain.entities.financial.Transference_;
import br.com.webbudget.domain.entities.registration.Wallet;
import br.com.webbudget.domain.entities.registration.Wallet_;
import br.com.webbudget.domain.repositories.DefaultRepository;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.Criteria;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@link Transference} repository
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 03/10/2018
 */
@Repository
public interface TransferenceRepository extends DefaultRepository<Transference> {

    /**
     * Find all transference using a given filter
     *
     * @param filter used to search for {@link Transference}
     * @return a list of {@link Transference} found
     */
    default List<Transference> findByFilter(TransferenceFilter filter) {

        final Criteria<Transference, Transference> criteria = this.criteria();

        final List<Criteria<Transference, Transference>> restrictions = new ArrayList<>();

        if (filter.getOriginWallet() != null) {
            restrictions.add(this.criteria().join(Transference_.origin,
                    where(Wallet.class).eq(Wallet_.id, filter.getOriginWallet().getId())));
        }

        if (filter.getDestinationWallet() != null) {
            restrictions.add(this.criteria().join(Transference_.destination,
                    where(Wallet.class).eq(Wallet_.id, filter.getDestinationWallet().getId())));
        }

        if (filter.getOperationDate() != null) {
            restrictions.add(this.criteria().eq(Transference_.transferDate, filter.getOperationDate()));
        }

        if (!restrictions.isEmpty()) {
            criteria.or(restrictions);
        }

        return criteria.getResultList();
    }
}
