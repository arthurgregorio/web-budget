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
package br.com.webbudget.domain.services;

import br.com.webbudget.domain.entities.financial.ReasonType;
import br.com.webbudget.domain.entities.financial.Transference;
import br.com.webbudget.domain.entities.financial.WalletBalance;
import br.com.webbudget.domain.events.UpdateWalletBalance;
import br.com.webbudget.domain.logics.financial.transference.TransferenceSavingLogic;
import br.com.webbudget.domain.repositories.financial.TransferenceRepository;
import br.com.webbudget.application.components.builder.WalletBalanceBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 * The {@link WalletBalance} transference service
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 06/10/2018
 */
@ApplicationScoped
public class TransferenceService {

    @Inject
    private TransferenceRepository transferenceRepository;

    @Inject
    @UpdateWalletBalance
    private Event<WalletBalance> updateWalletBalanceEvent;

    @Any
    @Inject
    private Instance<TransferenceSavingLogic> savingBusinessLogics;

    /**
     * Method to make the {@link WalletBalance} transference
     *
     * @param transference the transference
     */
    @Transactional
    public void transfer(Transference transference) {

        this.savingBusinessLogics.forEach(logic -> logic.run(transference));

        this.transferenceRepository.save(transference);

        // transfer
        this.updateWalletBalanceEvent.fire(WalletBalanceBuilder.getInstance()
                .to(transference.getDestination())
                .value(transference.getValue())
                .withReason(ReasonType.TRANSFERENCE)
                .build()
        );

        // adjust the origin balance
        this.updateWalletBalanceEvent.fire(WalletBalanceBuilder.getInstance()
                .to(transference.getOrigin())
                .value(transference.getValue().negate())
                .withReason(ReasonType.TRANSFERENCE)
                .build()
        );
    }
}