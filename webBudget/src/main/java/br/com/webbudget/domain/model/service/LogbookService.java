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
import br.com.webbudget.domain.misc.MovementBuilder;
import br.com.webbudget.domain.misc.events.MovementDeleted;
import br.com.webbudget.domain.misc.ex.InternalServiceError;
import br.com.webbudget.domain.model.entity.entries.MovementClass;
import br.com.webbudget.domain.model.entity.entries.MovementClassType;
import br.com.webbudget.domain.model.entity.financial.Apportionment;
import br.com.webbudget.domain.model.entity.financial.Movement;
import br.com.webbudget.domain.model.entity.logbook.Entry;
import br.com.webbudget.domain.model.entity.logbook.Vehicle;
import br.com.webbudget.domain.model.repository.entries.IMovementClassRepository;
import br.com.webbudget.domain.model.repository.logbook.IEntryRepository;
import br.com.webbudget.domain.model.repository.logbook.IVehicleRepository;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
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
    private MovementService movementService;
    
    @Inject
    private IEntryRepository entryRepository;
    @Inject
    private IVehicleRepository vehicleRepository;
    @Inject
    private IMovementClassRepository movementClassRepository;

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
     * @param entry
     */
    @Transactional
    public void saveEntry(Entry entry) {

        // caso seja registro financeiro, checa a integridade
        if (entry.isFinancial() && !entry.isFinancialValid()) {
            throw new InternalServiceError("error.entry.financial-invalid");
        } 
        
        // se temos um registro financeiro, incluimos o movimento
        if (entry.isFinancial()) {
            // rateio
            final Apportionment apportionment = new Apportionment(
                            entry.getCostCenter(), 
                            entry.getMovementClass(), 
                            entry.getCost());
            
            // cria o movimento
            final Movement movement = new MovementBuilder()
                    .withValue(entry.getCost())
                    .withDueDate(entry.getEventDate())
                    .withDescription(entry.getTitle())
                    .inTheFinancialPeriod(entry.getFinancialPeriod())
                    .addingApportiomentOf(apportionment)
                    .build();
            
            entry.setMovementCode(movement.getCode());
            
            this.movementService.saveMovement(movement);
        }

        // verificamos se precisa atualizar a medida do odometro
        final int lastVehicleOdometer = this.vehicleRepository
                .findLastOdometer(entry.getVehicle());

        if (entry.getOdometer() > lastVehicleOdometer) {
            // atualizamos a distancia percorrida
            entry.setDistance(entry.getOdometer() - lastVehicleOdometer);

            // atualizamos o odometro
            entry.updateVehicleOdometer();

            // salvamos o carro
            this.vehicleRepository.save(entry.getVehicle());
        }

        // salva o registro
        this.entryRepository.save(entry);
    }

    /**
     *
     * @param entry
     * @return
     */
    @Transactional
    public Entry updateEntry(Entry entry) {
        return this.entryRepository.save(entry);
    }

    /**
     *
     * @param entry
     */
    @Transactional
    public void deleteEntry(Entry entry) {
        
        // deleta o registro
        this.entryRepository.delete(entry);

        // se tem movimento, deleta ele tambem
        if (entry.isFinancial()) {
            
            final Movement movement = this.movementService
                    .findMovementByCode(entry.getMovementCode());
            
            if (movement != null) {
                this.movementService.deleteMovement(movement);
            }
        }
    }
    
    /**
     * Quando um movimento for deletado este evento escurtara por uma possivel
     * delecao de um evento vinculado com um registro do logbook
     * 
     * @param code o codigo do movimento
     */
    public void whenMovementDeleted(@Observes @MovementDeleted String code) {
        
        // procura pelo registro
        final Entry entry = this.entryRepository.findByMovementCode(code);
        
        // se encontrar, limpa as flags de movimentacao financeira
        if (entry != null) {
            
            entry.setFinancial(false);
            entry.setMovementClass(null);
            entry.setFinancialPeriod(null);
            entry.setMovementCode(null);
            
            // salva!
            this.entryRepository.save(entry);
        }
    }

    /**
     * 
     * @param entryId
     * @return 
     */
    public Entry findEntryById(long entryId) {
        return this.entryRepository.findById(entryId, false);
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

    /**
     *
     * @param vehicle
     * @return
     */
    public List<Entry> listEntriesByVehicle(Vehicle vehicle) {
        return this.entryRepository.listByVehicle(vehicle);
    }

    /**
     *
     * @param vehicle
     * @return
     */
    public List<MovementClass> listClassesForVehicle(Vehicle vehicle) {
        return this.movementClassRepository.listByCostCenterAndType(
                vehicle.getCostCenter(), MovementClassType.OUT);
    }

    /**
     * 
     * @param vehicle
     * @param filter
     * @return 
     */
    public List<Entry> listEntriesByVehicleAndFilter(Vehicle vehicle, String filter) {
       return this.entryRepository.listByVehicleAndFilter(vehicle, filter);
    }
}
