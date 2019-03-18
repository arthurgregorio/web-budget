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
package br.com.webbudget.application.controller.journal;

import br.com.webbudget.application.components.ui.AbstractBean;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 * Controller para a view de ocorrencias do diario de bordo
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.3.0, 09/05/2016
 */
@Named
@ViewScoped
public class EntryBean extends AbstractBean {

//    @Getter
//    @Setter
//    private String filter;
//    @Getter
//    @Setter
//    private Vehicle vehicle;
//
//    @Getter
//    private Entry entry;
//
//    @Getter
//    private List<Entry> registration;
//    @Getter
//    private List<Vehicle> vehicles;
//    @Getter
//    private List<FinancialPeriod> openPeriods;
//    @Getter
//    private List<MovementClass> movementClasses;
//
//    @Inject
//    private LogbookService logbookService;
//    @Inject
//    private FinancialPeriodService periodService;
//
//    /**
//     * Inicializa a etapa de selecao dos veiculos
//     */
//    public void initializeSelection() {
//        this.vehicles = this.logbookService.listVehicles(false);
//    }
//
//    /**
//     * Inicializa a etapa onde os registros sao listados
//     *
//     * @param vehicleId
//     */
//    public void initializeListing(long vehicleId) {
//        this.vehicle = this.logbookService.findVehicleById(vehicleId);
//        this.filterList();
//    }
//
//    /**
//     *
//     * @param vehicleId
//     */
//    public void initializeForm(long vehicleId) {
//
//        // busca o veiculo
//        this.vehicle = this.logbookService.findVehicleById(vehicleId);
//
//        // busca as classes do CC do veiculo
//        this.movementClasses
//                = this.logbookService.listClassesForVehicle(this.vehicle);
//
//        // pegamos os periodos financeiros em aberto
//        this.openPeriods = this.periodService.listOpenFinancialPeriods();
//
//        // cria a entrada 
//        this.entry = new Entry(this.vehicle);
//    }
//
//    /**
//     * Filtra a listagem quando utilizamos o campo de pesquisa ou no load da 
//     * pagina, trazendo todos os registro
//     */
//    public void filterList() {
//        try {
//            this.registration = this.logbookService
//                    .listEntriesByVehicleAndFilter(this.vehicle, this.text);
//        } catch (InternalServiceError ex) {
//            this.addError(true, ex.getMessage(), ex.getParameters());
//        } catch (Exception ex) {
//            this.logger.error(ex.getMessage(), ex);
//            this.addError(true, "error.undefined-error", ex.getMessage());
//        }
//    }
//
//    /**
//     * @return a pagina para inclusao de um novo registro
//     */
//    public String changeToAddEntry() {
//        return "formEntry.xhtml?faces-redirect=true&vehicleId="
//                + this.vehicle.getId();
//    }
//    
//    /**
//     * Inicia o processo para deletar um registro
//     *
//     * @param entryId o registro que queremos deletar
//     */
//    public void changeToDelete(long entryId) {
//        this.entry = this.logbookService.findEntryById(entryId);
//        this.updateAndOpenDialog("deleteEntryDialog", "dialogDeleteEntry");
//    }
//
//    /**
//     * @return o metodo para compor a navegacao apos selecionar o veiculo
//     */
//    public String changeToList() {
//        if (this.vehicle == null) {
//            this.addError(true, "error.entry.no-vehicle");
//            return null;
//        } else {
//            return "listEntries.xhtml?faces-redirect=true&vehicleId="
//                    + this.vehicle.getId();
//        }
//    }
//
//    /**
//     *
//     */
//    public void doSave() {
//        try {
//            this.logbookService.saveEntry(this.entry);
//            this.entry = new Entry(this.logbookService.findVehicleById(
//                    this.entry.getVehicle().getId()));
//            this.addInfo(true, "entry.saved");
//        } catch (InternalServiceError ex) {
//            this.addError(true, ex.getMessage(), ex.getParameters());
//        } catch (Exception ex) {
//            this.logger.error(ex.getMessage(), ex);
//            this.addError(true, "error.undefined-error", ex.getMessage());
//        }
//    }
//
//    /**
//     *
//     */
//    public void doDelete() {
//        try {
//            this.logbookService.deleteEntry(this.entry);
//            this.registration = this.logbookService
//                    .listEntriesByVehicle(this.vehicle);
//            this.addInfo(true, "entry.deleted");
//        } catch (InternalServiceError ex) {
//            this.addError(true, ex.getMessage(), ex.getParameters());
//        } catch (Exception ex) {
//            this.logger.error(ex.getMessage(), ex);
//            this.addError(true, "error.undefined-error", ex.getMessage());
//        } finally {
//            this.updateComponent("entriesBox");
//            this.closeDialog("dialogDeleteEntry");
//        }
//    }
//
//    /**
//     * Pega todos os registro do diario de bordo agrupados pela data indicada
//     *
//     * @param eventDate a data do evento registrado
//     * @return a lista com os itens para a data indicada
//     */
//    public List<Entry> entriesByEventDate(LocalDate eventDate) {
//        return this.registration.stream()
//                .text(e -> e.getEventDate().equals(eventDate))
//                .sorted((e1, e2) -> e2.getInclusion().compareTo(e1.getInclusion()))
//                .collect(Collectors.toList());
//    }
//
//    /**
//     * @return agrupa todos os registros pela data do evento
//     */
//    public List<LocalDate> groupEntriesByEventDate() {
//
//        final List<LocalDate> grouped = new ArrayList<>();
//
//        this.registration.stream().forEach(e -> {
//            if (!grouped.contains(e.getEventDate())) {
//                grouped.add(e.getEventDate());
//            }
//        });
//
//        grouped.sort((d1, d2) -> d2.compareTo(d1));
//
//        return grouped;
//    }
//
//    /**
//     * Quando muda o status de movimentacao do registro, limpa a classe se for
//     * necessario
//     */
//    public void onFinancialChange() {
//        if (!this.entry.isFinancial()) {
//            this.entry.setMovementClass(null);
//            this.entry.setFinancialPeriod(null);
//        }
//    }
//
//    /**
//     * @return os tipos de registros possiveis, menos o de abastecimento que e 
//     * feito em uma tela separada
//     */
//    public List<EntryType> getEntryTypes() {
//        return Arrays.asList(EntryType.values())
//                .stream()
//                .text(e -> e != EntryType.REFUELING)
//                .collect(Collectors.toList());
//    }
}
