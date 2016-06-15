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
package br.com.webbudget.application.controller.logbook;

import br.com.webbudget.application.controller.AbstractBean;
import br.com.webbudget.domain.misc.ex.InternalServiceError;
import br.com.webbudget.domain.model.entity.logbook.Entry;
import br.com.webbudget.domain.model.entity.logbook.EntryType;
import br.com.webbudget.domain.model.entity.logbook.Vehicle;
import br.com.webbudget.domain.model.service.LogbookService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;

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

    @Getter
    @Setter
    private EntryType selectedType;
    @Getter
    @Setter
    private Vehicle selectedVehicle;

    @Getter
    private List<Entry> entries;
    @Getter
    private List<Vehicle> vehicles;

    @Inject
    private LogbookService logbookService;

    /**
     * Inicializa a view de listagem das ocorrencias o diario de bordo
     */
    public void initialize() {

        this.entries = new ArrayList<>();

        this.vehicles = this.logbookService.listVehicles(false);
    }

    /**
     * @return a pagina para inclusao de um novo registro
     */
    public String changeToAdd() {
        return "formEntry.xhtml?faces-redirect=true&vehicle=" 
                + this.selectedVehicle.getId() + "&type=" + this.selectedType;
    }

    /**
     * Abre a dialog de selecao do tipo de registro que iremos realizar
     */
    public void showEntryTypeDialog() {
        if (this.selectedVehicle == null) {
            this.addError(true, "error.entry.no-vehicle");
        } else {
            this.updateAndOpenDialog("entryTypeDialog", "dialogEntryType");
        }
    }

    /**
     * Carrega todas os registros para o diario de bordo do carro selecionado
     */
    public void viewEntries() {

        // sem veiculo, sem pesquisa
        if (this.selectedVehicle == null) {
            return;
        }

        // com veiculo, com pesquisa :D
        try {
            this.entries = this.logbookService
                    .listEntriesByVehicle(this.selectedVehicle);

            // se os resultados forem vazios, avisa o user
            if (this.entries.isEmpty()) {
                this.addError(true, "error.entry.no-entries",
                        this.selectedVehicle.getIdentification());
            }
        } catch (InternalServiceError ex) {
            this.addError(true, ex.getMessage(), ex.getParameters());
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ex);
            this.addError(true, "error.undefined-error", ex.getMessage());
        }
    }

    /**
     * Pega os registro apenas de uma data de inclusao especifica
     *
     * @param inclusion a data de inclusao
     * @return a lista com os itens incluidos nesta data
     */
    public List<Entry> entriesByInclusion(LocalDate inclusion) {
        return this.entries.stream()
                .filter(balance -> balance.getInclusionAsLocalDate().equals(inclusion))
                .collect(Collectors.toList());
    }

    /**
     * @return agrupa todos os registros pela data de inclusao
     */
    public List<LocalDate> groupEntriesByInclusion() {

        final List<LocalDate> grouped = new ArrayList<>();

        this.entries.stream().forEach(entry -> {
            if (!grouped.contains(entry.getInclusionAsLocalDate())) {
                grouped.add(entry.getInclusionAsLocalDate());
            }
        });
        return grouped;
    }
    
    /**
     * @return os tipos de registros que poderemos ter
     */
    public EntryType[] getEntryTypes() {
        return EntryType.values();
    }
}
