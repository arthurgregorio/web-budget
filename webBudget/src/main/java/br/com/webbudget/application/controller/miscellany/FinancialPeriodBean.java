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
package br.com.webbudget.application.controller.miscellany;

import br.com.webbudget.application.controller.AbstractBean;
import br.com.webbudget.domain.model.entity.miscellany.Closing;
import br.com.webbudget.domain.model.entity.miscellany.FinancialPeriod;
import br.com.webbudget.domain.misc.exceptions.InternalServiceError;
import br.com.webbudget.application.component.table.AbstractLazyModel;
import br.com.webbudget.application.component.table.Page;
import br.com.webbudget.application.component.table.PageRequest;
import br.com.webbudget.domain.model.service.FinancialPeriodService;
import java.util.List;
import java.util.Map;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import org.primefaces.model.SortOrder;

/**
 * Controller da view de periodos financeiros
 *
 * @author Arthur Gregorio
 *
 * @version 1.2.0
 * @since 1.0.0, 23/03/2014
 */
@Named
@ViewScoped
public class FinancialPeriodBean extends AbstractBean {

    @Getter
    private boolean hasOpenPeriod;

    @Getter
    private Closing closing;
    @Getter
    private FinancialPeriod financialPeriod;

    @Inject
    private FinancialPeriodService financialPeriodService;

    @Getter
    private final AbstractLazyModel<FinancialPeriod> financialPeriodsModel;

    /**
     * 
     */
    public FinancialPeriodBean() {

        this.financialPeriodsModel = new AbstractLazyModel<FinancialPeriod>() {
            @Override
            public List<FinancialPeriod> load(int first, int pageSize, String sortField,
                    SortOrder sortOrder, Map<String, Object> filters) {

                final PageRequest pageRequest = new PageRequest();

                pageRequest
                        .setFirstResult(first)
                        .withPageSize(pageSize)
                        .sortingBy(sortField, "inclusion")
                        .withDirection(sortOrder.name());

                final Page<FinancialPeriod> page = financialPeriodService
                        .listFinancialPeriodsLazily(null, pageRequest);

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

        // diz que pode abrir um periodo
        this.hasOpenPeriod = false;

        // validamos se tem periodo em aberto
        this.validateOpenPeriods();

        this.viewState = ViewState.ADDING;
        this.financialPeriod = new FinancialPeriod();
    }

    /**
     * @return
     */
    public String changeToAdd() {
        return "formFinancialPeriod.xhtml?faces-redirect=true";
    }

    /**
     * @param financialPeriodId
     * @return
     */
    public String changeToDetails(long financialPeriodId) {
        return "detailFinancialPeriod.xhtml?faces-redirect=true&periodId=" + financialPeriodId;
    }

    /**
     * @param financialPeriodId
     * @return
     */
    public String changeToClosing(long financialPeriodId) {
        return "../closing/closeFinancialPeriod.xhtml?faces-redirect=true&financialPeriodId=" + financialPeriodId;
    }

    /**
     *
     * @param periodId
     */
    public void changeToDelete(long periodId) {
        this.financialPeriod = this.financialPeriodService
                .findPeriodById(periodId);
        this.updateAndOpenDialog("deletePeriodDialog", "dialogDeletePeriod");
    }

    /**
     * Salva o periodo
     */
    public void doSave() {

        try {
            this.financialPeriodService.openPeriod(this.financialPeriod);

            this.financialPeriod = new FinancialPeriod();

            // validamos se tem periodo em aberto
            this.validateOpenPeriods();

            this.addInfo(true, "financial-period.saved");
        } catch (InternalServiceError ex) {
            this.logger.error("FinancialPeriodBean#doSave found erros", ex);
            this.addError(true, ex.getMessage(), ex.getParameters());
        } catch (Exception ex) {
            this.logger.error("FinancialPeriodBean#doSave found errors", ex);
            this.addError(true, "error.undefined-error", ex.getMessage());
        }
    }

    /**
     * Deleta um periodo
     */
    public void doDelete() {

        try {
            this.financialPeriodService.deletePeriod(this.financialPeriod);
            this.addInfo(true, "financial-period.deleted");
        } catch (InternalServiceError ex) {
            this.logger.error("FinancialPeriodBean#doDelete found erros", ex);
            this.addError(true, ex.getMessage(), ex.getParameters());
        } catch (Exception ex) {
            this.logger.error("FinancialPeriodBean#doDelete found errors", ex);
            this.addError(true, "error.undefined-error", ex.getMessage());
        } finally {
            this.updateComponent("financialPeriodsList");
            this.closeDialog("dialogDeletePeriod");
        }
    }

    /**
     * @return
     */
    public String doCancel() {
        return "listFinancialPeriods.xhtml?faces-redirect=true";
    }

    /**
     * valida se tem algum periodo em aberto, se houver avisa ao usuario que ja
     * tem e se ele tem certeza que quer abrir um novo
     */
    public void validateOpenPeriods() {

        // validamos se ha algum periodo em aberto
        final List<FinancialPeriod> periods
                = this.financialPeriodService.listOpenFinancialPeriods();

        for (FinancialPeriod open : periods) {
            if (open != null && (!open.isClosed() || !open.isExpired())) {
                // se ja houver aberto, nega o que foi dito antes
                this.hasOpenPeriod = true;
                break;
            }
        }
    }
}