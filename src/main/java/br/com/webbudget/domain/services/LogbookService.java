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
package br.com.webbudget.domain.services;

import javax.enterprise.context.ApplicationScoped;

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

//    @Inject
//    private IFuelRepository fuelRepository;
//    @Inject
//    private IEntryRepository entryRepository;
//    @Inject
//    private IVehicleRepository vehicleRepository;
//    @Inject
//    private IRefuelingRepository refuelingRepository;
//    @Inject
//    private IMovementClassRepository movementClassRepository;
//
//    @Inject
//    @DeleteMovement
//    private Event<String> deleteMovementEvent;
//    @Inject
//    @CreateMovement
//    private Event<MovementBuilder> createMovementEvent;
//
//    /**
//     *
//     * @param entry
//     */
//    @Transactional
//    public void saveEntry(Entry entry) {
//
//        // caso seja registro financeiro, checa a integridade
//        if (entry.isFinancial() && !entry.isFinancialValid()) {
//            throw new InternalServiceError("error.entry.financial-invalid");
//        }
//
//        // se temos um registro financeiro, incluimos o movimento
//        if (entry.isFinancial()) {
//            // cria o movimento
//            final MovementBuilder builder = new MovementBuilder()
//                    .value(entry.getCost())
//                    .onDueDate(entry.getEventDate())
//                    .describedBy(entry.getTitle())
//                    .inThePeriodOf(entry.getFinancialPeriod())
//                    .dividedAmong(new ApportionmentBuilder()
//                            .onCostCenter(entry.getCostCenter())
//                            .withMovementClass(entry.getMovementClass())
//                            .value(entry.getCost()));
//
//            entry.setMovementCode(builder.getMovementCode());
//
//            this.createMovementEvent.fire(builder);
//        }
//
//        final int lastVehicleOdometer = this.vehicleRepository
//                .findLastOdometer(entry.getVehicle());
//
//        if (entry.getOdometer() > lastVehicleOdometer) {
//            // atualizamos a distancia percorrida
//            entry.setDistance(entry.getOdometer() - lastVehicleOdometer);
//
//            // atualizamos o odometro
//            entry.updateVehicleOdometer();
//
//            // salvamos o carro
//            this.vehicleRepository.save(entry.getVehicle());
//        }
//
//        // salva o registro
//        this.entryRepository.save(entry);
//    }
//
//    /**
//     *
//     * @param entry
//     * @return
//     */
//    @Transactional
//    public Entry updateEntry(Entry entry) {
//        return this.entryRepository.save(entry);
//    }
//
//    /**
//     *
//     * @param entry
//     */
//    @Transactional
//    public void deleteEntry(Entry entry) {
//
//        // deleta o registro
//        this.entryRepository.delete(entry);
//
//        // se tem movimento, deleta ele tambem
//        if (entry.isFinancial()) {
//            this.deleteMovementEvent.fire(entry.getMovementCode());
//        }
//    }
//
//    /**
//     * Quando um movimento for deletado este evento escurtara por uma possivel
//     * delecao de um evento vinculado com um registro do journal
//     *
//     * @param code o codigo do movimento
//     */
//    public void whenEntryMovementDeleted(@Observes @MovementDeleted String code) {
//
//        // procura pelo registro
//        final Entry entry = this.entryRepository.findByMovementCode(code);
//
//        // se encontrar, limpa as flags de movimentacao financeira
//        if (entry != null) {
//
//            entry.setFinancial(false);
//            entry.setMovementClass(null);
//            entry.setFinancialPeriod(null);
//            entry.setMovementCode(null);
//
//            this.entryRepository.save(entry);
//        }
//    }
//    
//    /**
//     * Escuta o evento de delecao de um movimento para verificar se o mesmo 
//     * pertence a um abastecimento, se sim, limpa a flag do codigo do movimento
//     * vinculado a ele
//     * 
//     * @param code o codigo do movimento deletado
//     */
//    public void whenRefuelingMovementDeleted(@Observes @MovementDeleted String code) {
//        
//        final Refueling refueling = this.refuelingRepository.findByMovementCode(code);
//        
//        // se achar, limpa a flag e salva
//        if (refueling != null) {
//            refueling.setMovementCode(null);
//            this.refuelingRepository.save(refueling);
//        }        
//    }
//
//    /**
//     *
//     * @param entryId
//     * @return
//     */
//    public Entry findEntryById(long entryId) {
//        return this.entryRepository.findById(entryId, false);
//    }
//
//    /**
//     *
//     * @param vehicleId
//     * @return
//     */
//    public Vehicle findVehicleById(long vehicleId) {
//        return this.vehicleRepository.findById(vehicleId, false);
//    }
//
//    /**
//     *
//     * @param refuelingId
//     * @return
//     */
//    public Refueling findRefuelingById(long refuelingId) {
//        return this.refuelingRepository.findById(refuelingId, false);
//    }
//
//    /**
//     *
//     * @param isBlocked
//     * @return
//     */
//    public List<Vehicle> listVehicles(boolean isBlocked) {
//        return this.vehicleRepository.listByStatus(isBlocked);
//    }
//
//    /**
//     *
//     * @param isBlocked
//     * @param pageRequest
//     * @return
//     */
//    public Page<Vehicle> listVehiclesLazily(Boolean isBlocked, PageRequest pageRequest) {
//        return this.vehicleRepository.listLazilyByStatus(isBlocked, pageRequest);
//    }
//
//    /**
//     *
//     * @param vehicle
//     * @return
//     */
//    public List<Entry> listEntriesByVehicle(Vehicle vehicle) {
//        return this.entryRepository.listByVehicle(vehicle);
//    }
//
//    /**
//     *
//     * @param vehicle
//     * @return
//     */
//    public List<MovementClass> listClassesForVehicle(Vehicle vehicle) {
//        return this.movementClassRepository.listByCostCenterAndType(
//                vehicle.getCostCenter(), MovementClassType.OUT);
//    }
//
//    /**
//     *
//     * @param vehicle
//     * @param filter
//     * @return
//     */
//    public List<Entry> listEntriesByVehicleAndFilter(Vehicle vehicle, String filter) {
//        return this.entryRepository.listByVehicleAndFilter(vehicle, filter);
//    }
//
//    /**
//     *
//     * @param filter
//     * @param pageRequest
//     * @return
//     */
//    public Page<Refueling> listRefuelingsLazily(String filter, PageRequest pageRequest) {
//        return this.refuelingRepository.listLazily(filter, pageRequest);
//    }
}
