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

import br.com.webbudget.domain.entities.journal.Refueling;
import br.com.webbudget.domain.repositories.journal.FuelRepository;
import br.com.webbudget.domain.repositories.journal.RefuelingRepository;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 27/05/2018
 */
@ApplicationScoped
public class RefuelingService {

    @Inject
    private FuelRepository fuelRepository;
    @Inject
    private RefuelingRepository refuelingRepository;
    
    /**
     * 
     * @param refueling 
     */
    @Transactional
    public void save(Refueling refueling) {
        
        // TODO after save, call movement service to create a movement 
    }

    /**
     * 
     * @param refueling 
     */
    @Transactional
    public void delete(Refueling refueling) {
        
        // TODO revert the financial launch for this refueling
    }
}
