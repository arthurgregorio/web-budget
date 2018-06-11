/*
 * Copyright (C) 2015 Arthur Gregorio, AG.Software
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
import java.util.Optional;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.Criteria;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 1.0.0, 04/03/2013
 */
@Repository
public interface WalletRepository extends DefaultRepository<Wallet> {

    /**
     * 
     * @param name
     * @param walletType
     * @return 
     */
    Optional<Wallet> findOptionalByNameAndWalletType(String name, WalletType walletType);
    
    /**
     * 
     * @param filter
     * @return 
     */
    @Override
    public default Criteria<Wallet, Wallet> getRestrictions(String filter) {
        return criteria()
                .eqIgnoreCase(Wallet_.bank, filter)
                .eqIgnoreCase(Wallet_.name, filter);
    }
    
    /**
     * 
     * @return 
     */
    @Override
    public default SingularAttribute<Wallet, Boolean> getBlockedProperty() {
        return Wallet_.blocked;
    }
    
//    /**
//     *
//     * @param name
//     * @param bank
//     * @param walletType
//     * @return
//     */
//    public Wallet findByNameAndBankAndType(String name, String bank, WalletType walletType);
}
