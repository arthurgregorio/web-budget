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
package br.com.webbudget.application.controller.financial;

import br.com.webbudget.application.controller.AbstractBean;
import br.com.webbudget.domain.model.entity.movement.Apportionment;
import br.com.webbudget.domain.model.entity.movement.CostCenter;
import br.com.webbudget.domain.model.entity.movement.FinancialPeriod;
import br.com.webbudget.domain.model.entity.movement.FixedMovement;
import br.com.webbudget.domain.model.entity.movement.FixedMovementStatusType;
import br.com.webbudget.domain.model.entity.movement.Launch;
import br.com.webbudget.domain.model.entity.movement.MovementClass;
import br.com.webbudget.domain.misc.ex.InternalServiceError;
import br.com.webbudget.application.component.table.AbstractLazyModel;
import br.com.webbudget.application.component.table.Page;
import br.com.webbudget.application.component.table.PageRequest;
import br.com.webbudget.domain.model.service.FinancialPeriodService;
import br.com.webbudget.domain.model.service.MovementService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.SortOrder;

/**
 * Controller da view de manutencao de movimentos fixos
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 2.1.0, 18/09/2015
 */
@Named
@ViewScoped
public class FixedMovementBean extends AbstractBean {

    @Getter
    @Setter
    private String filter;

    @Getter
    private FixedMovement fixedMovement;

    @Getter
    @Setter
    private Apportionment apportionment;
    @Getter
    @Setter
    private FinancialPeriod financialPeriod;

    @Getter
    private List<CostCenter> costCenters;
    @Getter
    private List<MovementClass> movementClasses;
    @Getter
    private List<FinancialPeriod> openFinancialPeriods;

    @Getter
    @Setter
    private List<FixedMovement> selectedFixedMovements;

    @Inject
    private MovementService movementService;
    @Inject
    private FinancialPeriodService financialPeriodService;

    @Getter
    private AbstractLazyModel<Launch> launchesModel;
    @Getter
    private final AbstractLazyModel<FixedMovement> fixedMovementsModel;

    /**
     *
     */
    public FixedMovementBean() {

        // model dos movimentos fixos
        this.fixedMovementsModel = new AbstractLazyModel<FixedMovement>() {
            @Override
            public List<FixedMovement> load(int first, int pageSize, String sortField,
                    SortOrder sortOrder, Map<String, Object> filters) {

                final PageRequest pageRequest = new PageRequest();

                pageRequest
                        .setFirstResult(first)
                        .withPageSize(pageSize)
                        .sortingBy(sortField, "inclusion")
                        .withDirection(sortOrder.name());

                final Page<FixedMovement> page = movementService
                        .listFixedMovementsByFilter(filter, pageRequest);

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

        this.selectedFixedMovements = new ArrayList<>();

        this.openFinancialPeriods
                = this.financialPeriodService.listOpenFinancialPeriods();
    }

    /**
     *
     * @param fixedMovementId
     * @param detailing
     */
    public void initializeForm(long fixedMovementId, boolean detailing) {

        this.costCenters = this.movementService.listCostCenters(false);

        if (fixedMovementId == 0 && !detailing) {
            this.viewState = ViewState.ADDING;
            this.fixedMovement = new FixedMovement();
        } else if (fixedMovementId != 0 && !detailing) {
            this.viewState = ViewState.EDITING;
            this.fixedMovement = this.movementService
                    .findFixedMovementById(fixedMovementId);
            this.fixedMovement = this.movementService
                    .fetchLaunchesForFixedMovement(this.fixedMovement);
        } else {
            this.viewState = ViewState.DETAILING;
            this.fixedMovement = this.movementService
                    .findFixedMovementById(fixedMovementId);
        }
    }

    /**
     * @return
     */
    public String changeToAdd() {
        return "formFixedMovement.xhtml?faces-redirect=true";
    }

    /**
     * @return
     */
    public String changeToListing() {
        return "listFixedMovements.xhtml?faces-redirect=true";
    }

    /**
     * @param fixedMovementId
     * @return
     */
    public String changeToEdit(long fixedMovementId) {
        return "formFixedMovement.xhtml?faces-redirect=true&fixedMovementId=" + fixedMovementId;
    }

    /**
     * @param fixedMovementId
     */
    public void changeToDelete(long fixedMovementId) {
        this.fixedMovement = this.movementService
                .findFixedMovementById(fixedMovementId);
        this.updateAndOpenDialog("deleteFixedMovementDialog", 
                "dialogDeleteFixedMovement");
    }

    /**
     * @param fixedMovementId
     * @return
     */
    public String changeToDetail(long fixedMovementId) {
        return "formFixedMovement.xhtml?faces-redirect=true&fixedMovementId="
                + fixedMovementId + "&detailing=true";
    }

    /**
     * @return
     */
    public String doCancel() {
        return "listFixedMovements.xhtml?faces-redirect=true";
    }

    /**
     *
     */
    public void doSave() {
        try {
            this.movementService.saveFixedMovement(this.fixedMovement);
            this.fixedMovement = new FixedMovement();
            this.addInfo(true, "fixed-movement.saved");
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
    public void doUpdate() {
        try {
            this.fixedMovement = this.movementService.saveFixedMovement(this.fixedMovement);
            this.addInfo(true, "fixed-movement.updated");
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
            this.movementService.deleteFixedMovement(this.fixedMovement);
            this.addInfo(true, "fixed-movement.deleted");
        } catch (InternalServiceError ex) {
            this.addError(true, ex.getMessage(), ex.getParameters());
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ex);
            this.addError(true, "error.undefined-error", ex.getMessage());
        } finally {
            this.updateComponent("fixedMovementsList");
            this.closeDialog("dialogDeleteFixedMovement");
        }
    }

    /**
     *
     */
    public void doLaunch() {
        try {
            this.movementService.launchFixedMovements(
                    this.selectedFixedMovements, this.financialPeriod);
            this.addInfo(true, "fixed-movement.launched", 
                    this.financialPeriod.getIdentification());
        } catch (InternalServiceError ex) {
            this.addError(false, ex.getMessage(), ex.getParameters());
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ex);
            this.addError(false, "error.undefined-error", ex.getMessage());
        } finally {
            this.updateComponent("fixedMovementsList");
            this.closeDialog("dialogConfirmLaunch");
        }
    }

    /**
     *
     */
    public void showApportionmentDialog() {

        // se o valor do rateio for igual ao total do movimento nem deixa exibir
        // a tela de rateios para que nao seja feito cagada
        if (this.fixedMovement.hasValueToDivide()) {
            this.addError(true, "error.fixed-movement.no-value-divide");
            return;
        }

        this.apportionment = new Apportionment();
        this.apportionment.setValue(this.fixedMovement.getValueToDivide());

        this.updateAndOpenDialog("apportionmentDialog", "dialogApportionment");
    }

    /**
     *
     */
    public void showLaunchConfirmDialog() {
        if (this.selectedFixedMovements.size() < 1) {
            this.addError(true, "error.fixed-movement.no-selection");
            return;
        }
        this.updateAndOpenDialog("confirmLaunchDialog","dialogConfirmLaunch");
    }
    
    /**
     *
     */
    public void showLaunchesDialog() {

        // valida se a selecao tem mais de um item
        if (this.viewState == ViewState.LISTING 
                && this.selectedFixedMovements.size() != 1) {
            this.addError(true, "error.fixed-movement.more-than-one");
            return;
        } 
        
        // se a origem da visualizacao vem da tela de listem, pegamos da lista 
        // de itens da table
        if (this.viewState == ViewState.LISTING) {
            this.fixedMovement = this.selectedFixedMovements.get(0);
        }

        // model dos lancamentos do movimento fixo
        this.launchesModel = new AbstractLazyModel<Launch>() {
            @Override
            public List<Launch> load(int first, int pageSize, String sortField,
                    SortOrder sortOrder, Map<String, Object> filters) {

                final PageRequest pageRequest = new PageRequest();

                pageRequest
                        .setFirstResult(first)
                        .withPageSize(pageSize)
                        .sortingBy(sortField, "inclusion")
                        .withDirection(sortOrder.name());

                final Page<Launch> page = movementService.listLaunchesByFixedMovement(
                        fixedMovement, pageRequest);

                this.setRowCount(page.getTotalPagesInt());

                return page.getContent();
            }
        };
        
        this.updateAndOpenDialog("launchesDialog", "dialogLaunches");
    }

    /**
     *
     */
    public void addApportionment() {
        try {
            this.fixedMovement.addApportionment(this.apportionment);
            this.updateComponent("inValue");
            this.updateComponent("apportionmentBox:container");
            this.closeDialog("dialogApportionment");
        } catch (InternalServiceError ex) {
            this.addError(false, ex.getMessage(), ex.getParameters());
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ex);
            this.addError(false, "error.undefined-error", ex.getMessage());
        } finally {
            this.updateComponent("apportionmentMessages");
        }
    }

    /**
     *
     * @param apportionmentId
     */
    public void deleteApportionment(String apportionmentId) {
        this.fixedMovement.removeApportionment(apportionmentId);
        this.updateComponent("inValue");
        this.updateComponent("apportionmentBox:container");
    }

    /**
     * Trata a mudanca do tipo de parcelamento
     */
    public void onInstallmentChange() {
        if (this.fixedMovement.isUndetermined()) {
            this.fixedMovement.setQuotes(null);
        }
        this.updateComponent("inQuotes");
    }

    /**
     * Atualiza o combo de classes quando o usuário selecionar o centro de custo
     */
    public void loadMovementClasses() {
        this.movementClasses = this.movementService.listMovementClassesByCostCenterAndType(
                this.apportionment.getCostCenter(), null);
    }

    /**
     * @return os status possiveis para um movimento fixo
     */
    public FixedMovementStatusType[] getAvailableFixedMovementStatusTypes() {
        return FixedMovementStatusType.values();
    }
}
