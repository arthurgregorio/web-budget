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
import br.com.webbudget.domain.entity.movement.Apportionment;
import br.com.webbudget.domain.entity.movement.CostCenter;
import br.com.webbudget.domain.entity.movement.FinancialPeriod;
import br.com.webbudget.domain.entity.movement.FixedMovement;
import br.com.webbudget.domain.entity.movement.FixedMovementStatusType;
import br.com.webbudget.domain.entity.movement.Launch;
import br.com.webbudget.domain.entity.movement.MovementClass;
import br.com.webbudget.domain.misc.ex.WbDomainException;
import br.com.webbudget.domain.misc.model.AbstractLazyModel;
import br.com.webbudget.domain.misc.model.Page;
import br.com.webbudget.domain.misc.model.PageRequest;
import br.com.webbudget.domain.service.FinancialPeriodService;
import br.com.webbudget.domain.service.MovementService;
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
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
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
    public FixedMovementBean(){

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
        
        this.openFinancialPeriods = 
                this.financialPeriodService.listOpenFinancialPeriods();
    }

    /**
     * 
     * @param fixedMovementId 
     */
    public void initializeForm(long fixedMovementId) {
        
        this.costCenters = this.movementService.listCostCenters(false);

        if (fixedMovementId == 0) {
            this.viewState = ViewState.ADDING;
            this.fixedMovement = new FixedMovement();
        } else {
            this.viewState = ViewState.EDITING;
            this.fixedMovement = this.movementService
                    .findFixedMovementById(fixedMovementId);
            this.fixedMovement = this.movementService
                    .fetchLaunchesForFixedMovement(this.fixedMovement);
        }
    }
    
    /**
     * 
     */
    public void filterList() {
        this.update("fixedMovementsList");
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
        this.fixedMovement = this.movementService.findFixedMovementById(fixedMovementId);
        this.openDialog("deleteFixedMovementDialog", "dialogDeleteFixedMovement");
    }
    
    /**
     * Redireciona o usuario para ver o movimento referente a fatura
     * 
     * @param launch o lancamento que queremos ver o movimento
     * @return a URL para visualizar o movimento
     */
    public String changeToViewMovement(Launch launch) {
        return "../movement/formMovement.xhtml?faces-redirect=true"
                + "&movementId=" + launch.getMovement().getId() + "&detailing=true";
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
            this.info("fixed-movement.action.saved", true);
        } catch (WbDomainException ex) {
            this.logger.error("FixedMovementBean#doSave found erros", ex);
            this.fixedError(ex.getMessage(), true, ex.getParameters());
        } catch (Exception ex) {
            this.logger.error("FixedMovementBean#doSave found erros", ex);
            this.fixedError("generic.operation-error", true, ex.getMessage());
        }
    }

    /**
     *
     */
    public void doUpdate() {

        try {
            this.fixedMovement = this.movementService.saveFixedMovement(this.fixedMovement);
            this.info("fixed-movement.action.updated", true);
        } catch (WbDomainException ex) {
            this.logger.error("FixedMovementBean#doUpdate found erros", ex);
            this.fixedError(ex.getMessage(), true, ex.getParameters());
        } catch (Exception ex) {
            this.logger.error("FixedMovementBean#doUpdate found erros", ex);
            this.fixedError("generic.operation-error", true, ex.getMessage());
        }
    }

    /**
     *
     */
    public void doDelete() {

        try {
            this.movementService.deleteFixedMovement(this.fixedMovement);
            this.info("fixed-movement.action.deleted", true);
        } catch (WbDomainException ex) {
            this.logger.error("FixedMovementBean#doDelete found erros", ex);
            this.fixedError(ex.getMessage(), true, ex.getParameters());
        } catch (Exception ex) {
            this.logger.error("FixedMovementBean#doDelete found erros", ex);
            this.fixedError("generic.operation-error", true, ex.getMessage());
        } finally {
            this.update("fixedMovementsList");
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
            this.info("fixed-movement.action.launched", true);
        } catch (WbDomainException ex) {
            this.logger.error("FixedMovementBean#doSave found erros", ex);
            this.fixedError(ex.getMessage(), true, ex.getParameters());
        } catch (Exception ex) {
            this.logger.error("FixedMovementBean#doSave found erros", ex);
            this.fixedError("generic.operation-error", true, ex.getMessage());
        } finally {
            this.closeDialog("dialogConfirmLaunch");
        }
    }
    
    /**
     *
     */
    public void showApportionmentDialog() {

        // se o valor do rateio for igual ao total do movimento nem deixa exibir
        // a tela de rateios para que nao seja feito cagada
        if (this.fixedMovement.isApportionmentsValid()) {
            this.error("fixed-movement.validate.no-value-divide", true);
            return;
        }

        this.apportionment = new Apportionment();
        this.apportionment.setValue(this.fixedMovement.getValueToDivide());

        this.openDialog("apportionmentDialog", "dialogApportionment");
    }
    
    /**
     * 
     */
    public void showLaunchConfirmDialog() {
        if (this.selectedFixedMovements.size() < 1) {
            this.error("fixed-movement.validate.no-selection", true);
            return;
        }
        this.openDialog("confirmLaunchDialog","dialogConfirmLaunch");
    }
    
    /**
     * 
     */
    public void showLaunchesDialog() {
        if (this.selectedFixedMovements.size() != 1) {
            this.error("fixed-movement.validate.more-than-one", true);
            return;
        }
        
        // model dos lancamentos
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
                        selectedFixedMovements.get(0), pageRequest);
                
                this.setRowCount(page.getTotalPagesInt());
                
                return page.getContent();
            }
        };
        
        this.openDialog("launchesDialog","dialogLaunches");
    }
    
    /**
     *
     */
    public void addApportionment() {
        try {
            this.fixedMovement.addApportionment(this.apportionment);
            this.update("inValue");
            this.update("apportionmentList");
            this.closeDialog("dialogApportionment");
        } catch (WbDomainException ex) {
            this.logger.error("FixedMovementBean#addApportionment found erros", ex);
            this.fixedError(ex.getMessage(), false, ex.getParameters());
        } catch (Exception ex) {
            this.logger.error("FixedMovementBean#addApportionment found erros", ex);
            this.fixedError("generic.operation-error", false, ex.getMessage());
        }
    }

    /**
     *
     * @param id
     */
    public void deleteApportionment(String id) {
        this.fixedMovement.removeApportionment(id);
        this.update("inValue");
        this.update("apportionmentList");
    }
    
    /**
     * Trata a mudanca do tipo de parcelamento
     */
    public void onInstallmentChange() {
        if (this.fixedMovement.isUndetermined()) {
            this.fixedMovement.setQuotes(null);
        }
        this.update("inQuotes");
    }
    
    /**
     * Atualiza o combo de classes quando o usuário selecionar o centro de custo
     */
    public void loadMovementClasses() {
        this.movementClasses = this.movementService.listMovementClassesByCostCenterAndType(
                this.apportionment.getCostCenter(), null);
        this.update("inMovementClass");
    }
    
    /**
     * @return os status possiveis para um movimento fixo
     */
    public FixedMovementStatusType[] getAvailableFixedMovementStatusTypes() {
        return FixedMovementStatusType.values();
    }
}
