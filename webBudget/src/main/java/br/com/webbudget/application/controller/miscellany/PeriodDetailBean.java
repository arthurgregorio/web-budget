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
import br.com.webbudget.domain.service.GraphModelService;
import br.com.webbudget.domain.service.FinancialPeriodService;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import org.primefaces.model.chart.CartesianChartModel;

/**
 * Bean responsavel pelo controle da view de detalhes do periodo
 *
 * @author Arthur Gregorio
 *
 * @version 1.2.0
 * @since 1.0.0, 11/04/2014
 */
@Named
@ViewScoped
public class PeriodDetailBean extends AbstractBean {

    @Getter
    private PeriodDetailsDTO periodDetailsDTO;

    @Getter
    private CartesianChartModel expensesModel;
    @Getter
    private CartesianChartModel revenuesModel;

    @Inject
    private transient GraphModelService graphModelService;
    @Inject
    private transient FinancialPeriodService financialPeriodService;

    /**
     * @param financialPeriodId
     */
    public void initializeDetails(long financialPeriodId) {

        // busca o preview do periodo
        this.periodDetailsDTO = this.financialPeriodService
                .previewPeriod(financialPeriodId);

        // monta o grafico
        this.revenuesModel = this.graphModelService.buildClassesChartModel(
                this.periodDetailsDTO.getRevenueClasses());
        this.expensesModel = this.graphModelService.buildClassesChartModel(
                this.periodDetailsDTO.getExpenseClasses());
    }

    /**
     * @return
     */
    public String doCancel() {
        return "listFinancialPeriods.xhtml?faces-redirect=true";
    }

    /**
     * @return
     */
    public String doRefresh() {
        return "detailFinancialPeriod.xhtml?faces-redirect=true&financialPeriodId="
                + this.periodDetailsDTO.getFinancialPeriod().getId();
    }
}
