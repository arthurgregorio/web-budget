/*
 * Copyright (C) 2016 Arthur Gregorio, AG.Software
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
package br.com.webbudget.domain.model.service;

import br.com.webbudget.application.component.table.Page;
import br.com.webbudget.application.component.table.PageRequest;
import br.com.webbudget.domain.model.entity.logbook.Vehicle;
import br.com.webbudget.domain.model.repository.logbook.IVehicleRepository;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 * Servico para executar as operacoes pertinentes ao diario de bordo
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.3.0, 05/06/2016
 */
@ApplicationScoped
public class LogbookService {

    @Inject
    private IVehicleRepository vehicleRepository;

    /**
     * 
     * @param vehicle 
     */
    @Transactional
    public void saveVehicle(Vehicle vehicle) {
        this.vehicleRepository.save(vehicle);
    }

    /**
     * 
     * @param vehicle
     * @return 
     */
    @Transactional
    public Vehicle updateVehicle(Vehicle vehicle) {
        return this.vehicleRepository.save(vehicle);
    }

    /**
     * 
     * @param vehicle 
     */
    @Transactional
    public void deleteVehicle(Vehicle vehicle) {
        this.vehicleRepository.delete(vehicle);
    }

    /**
     * 
     * @param vehicleId
     * @return 
     */
    public Vehicle findVehicleById(long vehicleId) {
        return this.vehicleRepository.findById(vehicleId, false);
    }

    /**
     * 
     * @param isBlocked
     * @return 
     */
    public List<Vehicle> listVehicles(boolean isBlocked) {
        return this.vehicleRepository.listByStatus(isBlocked);
    }

    /**
     * 
     * @param isBlocked
     * @param pageRequest
     * @return 
     */
    public Page<Vehicle> listVehiclesLazily(Boolean isBlocked, PageRequest pageRequest) {
        return this.vehicleRepository.listLazilyByStatus(isBlocked, pageRequest);
    }
}
