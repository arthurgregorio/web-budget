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
import br.com.webbudget.domain.entity.closing.Closing;
import br.com.webbudget.domain.entity.movement.FinancialPeriod;
import br.com.webbudget.domain.service.FinancialPeriodService;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 23/03/2014
 */
@ViewScoped
@ManagedBean
public class FinancialPeriodBean extends AbstractBean {

    @Getter
    private boolean hasOpenPeriod;

    @Getter
    private Closing closing;
    @Getter
    private FinancialPeriod financialPeriod;

    @Getter
    private List<FinancialPeriod> financialPeriods;

    @Setter
    @ManagedProperty("#{financialPeriodService}")
    private transient FinancialPeriodService financialPeriodService;

    /**
     *
     * @return
     */
    @Override
    protected Logger initializeLogger() {
        return LoggerFactory.getLogger(FinancialPeriodBean.class);
    }

    /**
     *
     */
    public void initializeListing() {
        this.viewState = ViewState.LISTING;
        this.financialPeriods = this.financialPeriodService.listFinancialPeriods(null);
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
     *
     * @return
     */
    public String changeToAdd() {
        return "formFinancialPeriod.xhtml?faces-redirect=true";
    }

    /**
     *
     * @param financialPeriodId
     * @return
     */
    public String changeToDetails(long financialPeriodId) {
        return "detailFinancialPeriod.xhtml?faces-redirect=true&financialPeriodId=" + financialPeriodId;
    }

    /**
     *
     * @param financialPeriodId
     * @return
     */
    public String changeToClosing(long financialPeriodId) {
        return "../closing/closeFinancialPeriod.xhtml?faces-redirect=true&financialPeriodId=" + financialPeriodId;
    }

    /**
     *
     */
    public void doSave() {

        try {
            this.financialPeriodService.openPeriod(this.financialPeriod);

            this.financialPeriod = new FinancialPeriod();

            // validamos se tem periodo em aberto
            this.validateOpenPeriods();

            this.info("financial-period.action.saved", true);
        } catch (Exception ex) {
            this.logger.error("FinancialPeriodBean#doSave found errors", ex);
            this.fixedError(ex.getMessage(), true);
        }
    }

    /**
     * Cancela e volta para a listagem
     *
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
