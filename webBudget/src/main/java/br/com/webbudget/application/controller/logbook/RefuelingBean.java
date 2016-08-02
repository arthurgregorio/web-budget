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

import br.com.webbudget.application.component.table.AbstractLazyModel;
import br.com.webbudget.application.component.table.Page;
import br.com.webbudget.application.component.table.PageRequest;
import br.com.webbudget.application.controller.AbstractBean;
import br.com.webbudget.domain.misc.ex.InternalServiceError;
import br.com.webbudget.domain.model.entity.entries.MovementClass;
import br.com.webbudget.domain.model.entity.logbook.FuelType;
import br.com.webbudget.domain.model.entity.logbook.Refueling;
import br.com.webbudget.domain.model.entity.logbook.Vehicle;
import br.com.webbudget.domain.model.entity.miscellany.FinancialPeriod;
import br.com.webbudget.domain.model.service.FinancialPeriodService;
import br.com.webbudget.domain.model.service.LogbookService;
import br.com.webbudget.domain.model.service.MovementService;
import java.util.List;
import java.util.Map;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import org.primefaces.model.SortOrder;

/**
 * Controller responsavel pela view de abastecimento no diario de bordo
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.3.0, 27/06/2016
 */
@Named
@ViewScoped
public class RefuelingBean extends AbstractBean {

    @Getter
    private Refueling refueling;

    @Getter
    private List<Vehicle> vehicles;
    @Getter
    private List<Refueling> refuelings;
    @Getter
    private List<FinancialPeriod> openPeriods;
    @Getter
    private List<MovementClass> movementClasses;

    @Inject
    private LogbookService logbookService;
    @Inject
    private MovementService movementService;
    @Inject
    private FinancialPeriodService periodService;

    @Getter
    private final AbstractLazyModel<Refueling> refuelingsModel;

    /**
     * Inicializa o tablemodel 
     */
    public RefuelingBean() {

        this.refuelingsModel = new AbstractLazyModel<Refueling>() {
            @Override
            public List<Refueling> load(int first, int pageSize, String sortField,
                    SortOrder sortOrder, Map<String, Object> filters) {

                final PageRequest pageRequest = new PageRequest();

                pageRequest
                        .setFirstResult(first)
                        .withPageSize(pageSize)
                        .sortingBy(sortField, "inclusion")
                        .withDirection(sortOrder.name());

                final Page<Refueling> page
                        = logbookService.listRefuelingsLazily(null, pageRequest);

                this.setRowCount(page.getTotalPagesInt());

                return page.getContent();
            }
        };
    }

    /**
     *
     */
    public void initializeListing() {
        this.viewState = ViewState.LISTING;
    }

    /**
     * 
     */
    public void initializeForm() {

        // pegamos os periodos financeiros em aberto
        this.openPeriods = this.periodService.listOpenFinancialPeriods();

        this.viewState = ViewState.ADDING;
        this.refueling = new Refueling();
    }

    /**
     * @return
     */
    public String changeToAdd() {
        return "formRefueling.xhtml?faces-redirect=true";
    }

    /**
     * @return
     */
    public String changeToListing() {
        return "listRefuelings.xhtml?faces-redirect=true";
    }

    /**
     * @param refuelingId
     * @return
     */
    public String changeToEdit(long refuelingId) {
        return "formRefueling.xhtml?faces-redirect=true&refuelingId=" + refuelingId;
    }

    /**
     * @param refuelingId
     */
    public void changeToDelete(long refuelingId) {
        this.refueling = this.logbookService.findRefuelingById(refuelingId);
        this.updateAndOpenDialog("deleteRefuelingDialog", "dialogDeleteRefueling");
    }

    /**
     * @return
     */
    public String doCancel() {
        return "listRefuelings.xhtml?faces-redirect=true";
    }

    /**
     *
     */
    public void doSave() {

        try {
            this.logbookService.saveRefueling(this.refueling);
            this.refueling = new Refueling();
            this.addInfo(true, "refueling.saved");
        } catch (InternalServiceError ex) {
            this.addError(true, ex.getMessage(), ex.getParameters());
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ex);
            this.addError(true, "error.undefined-error", ex.getMessage());
        }
    }

    /**
     *
     */
    public void doDelete() {

        try {
            this.logbookService.deleteRefueling(this.refueling);
            this.addInfo(true, "refueling.deleted");
        } catch (InternalServiceError ex) {
            this.addError(true, ex.getMessage(), true, ex.getParameters());
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ex);
            this.addError(true, "error.undefined-error", ex.getMessage());
        } finally {
            this.closeDialog("dialogDeleteRefueling");
            this.updateComponent("refuelingsList");
        }
    }

    /**
     * @return os tipos de combustivel disponiveis para selecao
     */
    public FuelType[] getFuelTypes() {
        return FuelType.values();
    }
}
