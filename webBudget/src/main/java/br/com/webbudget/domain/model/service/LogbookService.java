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
import br.com.webbudget.domain.misc.ApportionmentBuilder;
import br.com.webbudget.domain.misc.Builder;
import br.com.webbudget.domain.misc.MovementBuilder;
import br.com.webbudget.domain.misc.events.CreateMovement;
import br.com.webbudget.domain.misc.events.DeleteMovement;
import br.com.webbudget.domain.misc.events.MovementDeleted;
import br.com.webbudget.domain.misc.ex.InternalServiceError;
import br.com.webbudget.domain.model.entity.entries.MovementClass;
import br.com.webbudget.domain.model.entity.entries.MovementClassType;
import br.com.webbudget.domain.model.entity.financial.Apportionment;
import br.com.webbudget.domain.model.entity.financial.Movement;
import br.com.webbudget.domain.model.entity.logbook.Entry;
import br.com.webbudget.domain.model.entity.logbook.Refueling;
import br.com.webbudget.domain.model.entity.logbook.Vehicle;
import br.com.webbudget.domain.model.repository.entries.IMovementClassRepository;
import br.com.webbudget.domain.model.repository.logbook.IEntryRepository;
import br.com.webbudget.domain.model.repository.entries.IVehicleRepository;
import br.com.webbudget.domain.model.repository.logbook.IFuelRepository;
import br.com.webbudget.domain.model.repository.logbook.IRefuelingRepository;
import java.math.BigDecimal;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
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
    private IFuelRepository fuelRepository;
    @Inject
    private IEntryRepository entryRepository;
    @Inject
    private IVehicleRepository vehicleRepository;
    @Inject
    private IRefuelingRepository refuelingRepository;
    @Inject
    private IMovementClassRepository movementClassRepository;

    @Inject
    @DeleteMovement
    private Event<String> deleteMovementEvent;
    @Inject
    @CreateMovement
    private Event<MovementBuilder> createMovementEvent;

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
            // cria o movimento
            final MovementBuilder builder = new MovementBuilder()
                    .withValue(entry.getCost())
                    .onDueDate(entry.getEventDate())
                    .describedBy(entry.getTitle())
                    .inThePeriodOf(entry.getFinancialPeriod())
                    .dividedAmong(new ApportionmentBuilder()
                            .onCostCenter(entry.getCostCenter())
                            .withMovementClass(entry.getMovementClass())
                            .withValue(entry.getCost()));

            entry.setMovementCode(builder.getMovementCode());

            this.createMovementEvent.fire(builder);
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
            this.deleteMovementEvent.fire(entry.getMovementCode());
        }
    }

    /**
     *
     * @param refueling
     */
    @Transactional
    public void saveRefueling(Refueling refueling) {

        if (!refueling.isFuelsValid()) {
            throw new InternalServiceError("error.refueling.invalid-fuels");
        }

        // pegamos a lista dos abastecimentos nao contabilizados e reservamos
        final List<Refueling> unaccounteds = this.refuelingRepository
                .findUnaccountedsForVehicle(refueling.getVehicle());

        // se nao e um tanque cheio, marcamos que sera contabilizado no proximo
        // tanque cheio, se for, contabiliza a media do tanque
        if (refueling.isFullTank()) {

            final int lastOdometer;

            // montamos o valor do ultimo odometro com base nas parciais ou 
            // com base no ultimo odometro registrado com tanque cheio
            if (!unaccounteds.isEmpty()) {
                lastOdometer = unaccounteds
                        .stream()
                        .sorted((r1, r2) -> Integer.compare(
                                r1.getOdometer(), r2.getOdometer()))
                        .findFirst()
                        .get()
                        .getOdometer();

                refueling.setFirstRefueling(lastOdometer == 0);

                // pega o total de litros utilizados 
                BigDecimal liters = unaccounteds.stream()
                        .map(Refueling::getLiters)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                // adiciona os litros atuais e manda calcular a media
                refueling.calculateAverageComsumption(lastOdometer,
                        liters.add(refueling.getLiters()));
            } else {
                lastOdometer = this.refuelingRepository
                        .findLastOdometerForVehicle(refueling.getVehicle());
                refueling.setFirstRefueling(lastOdometer == 0);
                refueling.calculateAverageComsumption(lastOdometer);
            }

            // seta os que nao estavam contabilizados como contabilizados
            unaccounteds.stream().forEach(unaccounted -> {
                unaccounted.setAccounted(true);
                unaccounted.setAccountedBy(refueling.getCode());
                this.refuelingRepository.save(unaccounted);
            });

            // marca o abastecimento atual como contabilizado
            refueling.setAccounted(true);
        } else {
            refueling.setAccounted(false);
        }

        // salvamos o abastecimento
        final Refueling saved = this.refuelingRepository.save(refueling);

        // salvamos os combustiveis
        refueling.getFuels().stream().forEach(fuel -> {
            fuel.setRefueling(saved);
            this.fuelRepository.save(fuel);
        });

        // gerar o movimento financeiro
        final MovementBuilder builder = new MovementBuilder()
                .withValue(saved.getCost())
                .onDueDate(saved.getEventDate())
                .describedBy(saved.createMovementDescription())
                .inThePeriodOf(saved.getFinancialPeriod())
                .dividedAmong(new ApportionmentBuilder()
                        .onCostCenter(saved.getCostCenter())
                        .withMovementClass(saved.getMovementClass())
                        .withValue(saved.getCost()));

        refueling.setMovementCode(builder.getMovementCode());

        // invoca inclusao do movimento
        this.createMovementEvent.fire(builder);
        
        // atualiza o codigo do movimento no abastecimento
        this.refuelingRepository.save(saved);

        // TODO atualizar o odometro do carro
    }

    /**
     *
     * @param refueling
     */
    @Transactional
    public void deleteRefueling(Refueling refueling) {

        // verifica se estamos deletando o ultimo abastecimento
        if (!this.refuelingRepository.isLast(refueling)) {
            throw new InternalServiceError("error.refueling.not-last");
        }

        // lista os contabilizados por esta abastecimento que queremos deletar        
        final List<Refueling> accounteds = this.refuelingRepository
                .listAccountedsBy(refueling.getCode());

        // volta os contabilizados para nao contabilizados
        accounteds.stream().forEach(accounted -> {
            accounted.setAccounted(false);
            accounted.setAccountedBy(null);
            this.refuelingRepository.save(refueling);
        });

        // deleta o abastecimento
        this.refuelingRepository.delete(refueling);

        // TODO ecluir o movimento financeiro
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
     * @param refuelingId
     * @return
     */
    public Refueling findRefuelingById(long refuelingId) {
        return this.refuelingRepository.findById(refuelingId, false);
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

    /**
     *
     * @param filter
     * @param pageRequest
     * @return
     */
    public Page<Refueling> listRefuelingsLazily(String filter, PageRequest pageRequest) {
        return this.refuelingRepository.listLazily(filter, pageRequest);
    }
}
