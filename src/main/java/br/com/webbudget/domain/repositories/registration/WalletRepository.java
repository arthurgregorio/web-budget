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

import br.com.webbudget.domain.entities.registration.Wallet;
import br.com.webbudget.domain.entities.registration.WalletType;
import br.com.webbudget.domain.entities.registration.Wallet_;
import br.com.webbudget.domain.repositories.DefaultRepository;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.Criteria;

import javax.persistence.metamodel.SingularAttribute;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * The {@link Wallet} repository
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 1.0.0, 04/03/2013
 */
@Repository
public interface WalletRepository extends DefaultRepository<Wallet> {

    /**
     * Use this method to search for a wallet by the name and type
     *
     * @param name the name of the wallet
     * @param walletType the type of the wallet defined by the {@link WalletType} enum
     * @return an {@link Optional} of the wallet
     */
    Optional<Wallet> findByNameAndWalletType(String name, WalletType walletType);

    /**
     * {@inheritDoc}
     *
     * @param filter
     * @return
     */
    @Override
    default Collection<Criteria<Wallet, Wallet>> getRestrictions(String filter) {
        return List.of(
                this.criteria().likeIgnoreCase(Wallet_.name, this.likeAny(filter)),
                this.criteria().likeIgnoreCase(Wallet_.bank, this.likeAny(filter)));
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    default SingularAttribute<Wallet, Boolean> getEntityStateProperty() {
        return Wallet_.active;
    }
}
