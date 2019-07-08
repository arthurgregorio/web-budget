/*
 * Copyright (C) 2014 Arthur Gregorio, AG.Software
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

import br.com.webbudget.application.components.ui.AbstractBean;
import br.com.webbudget.domain.entities.financial.Closing;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.repositories.registration.FinancialPeriodRepository;
import br.com.webbudget.domain.services.ClosingService;
import lombok.Getter;
import lombok.Setter;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Controller for the {@link Closing} process
 *
 * @author Arthur Gregorio
 *
 * @version 3.0.0
 * @since 1.0.0, 14/04/2014
 */
@Named
@ViewScoped
public class ClosingBean extends AbstractBean {

    @Getter
    @Setter
    private FinancialPeriod financialPeriod;

    @Getter
    private Closing closing;

    @Getter
    private List<FinancialPeriod> openFinancialPeriods;

    @Inject
    private FinancialPeriodRepository financialPeriodRepository;

    @Inject
    private ClosingService closingService;

    /**
     * Initialize the view
     *
     * @param financialPeriodId if this parameter is used, search and set this {@link FinancialPeriod} to be closed
     */
    public void initialize(long financialPeriodId) {

        this.openFinancialPeriods = this.financialPeriodRepository.findByClosedOrderByIdentificationAsc(false);

        if (financialPeriodId != 0) {
            this.financialPeriod = this.financialPeriodRepository.findById(financialPeriodId)
                    .orElseThrow(() -> new BusinessLogicException("error.closing.cant-find-period"));
        }
    }

    /**
     * Execute the closing process
     */
    public void doClosing() {
        this.closingService.close(this.financialPeriod);
        this.closing = null;
        this.openFinancialPeriods = this.financialPeriodRepository.findByClosedOrderByIdentificationAsc(false);
        this.closeDialog("dialogClosingConfirmation");
        this.addInfo(true, "info.closing.period-closed", this.financialPeriod.getIdentification());
    }

    /**
     * Cancel the action of {@link Closing} a {@link FinancialPeriod} and send back the user to the {@link Closing} page
     * refreshing the UI to the starting state
     *
     * @return the outcome to the {@link Closing} form
     */
    public String doCancel() {
        return "formClosing.xhtml?faces-redirect=true";
    }

    /**
     * Update and open the {@link Closing} confirmation dialog
     */
    public void showClosingConfirmationDialog() {
        this.updateAndOpenDialog("closingConfirmationDialog", "dialogClosingConfirmation");
    }

    /**
     * Simulate the closing process to display a resume of the totals
     */
    public void doSimulation() {
        this.closing = this.closingService.simulate(this.financialPeriod);
    }
}
