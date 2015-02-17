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
import br.com.webbudget.application.exceptions.ApplicationException;
import br.com.webbudget.domain.entity.closing.Closing;
import br.com.webbudget.domain.entity.movement.FinancialPeriod;
import br.com.webbudget.domain.entity.movement.Movement;
import br.com.webbudget.domain.entity.movement.MovementType;
import br.com.webbudget.domain.service.ClosingService;
import br.com.webbudget.domain.service.FinancialPeriodService;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MBean que contem os metodos para encerramento dos periodos financeiros e <br/>
 * calculo do fechamento
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 14/04/2014
 */
@ViewScoped
@ManagedBean
public class ClosingBean extends AbstractBean {

    @Getter
    @Setter
    private FinancialPeriod financialPeriod;
    
    @Getter
    private Closing closing;
    
    @Getter
    private List<FinancialPeriod> financialPeriods;
    
    @Setter
    @ManagedProperty("#{closingService}")
    private transient ClosingService closingService;
    @Setter
    @ManagedProperty("#{financialPeriodService}")
    private transient FinancialPeriodService financialPeriodService;

    /**
     * 
     * @return 
     */
    @Override
    protected Logger initializeLogger() {
        return LoggerFactory.getLogger(ClosingBean.class);
    }
    
    /**
     * Inicializa o form do fechamento com os periodos disponiveis para encerramento
     * 
     * @param financialPeriodId se informado, apos a pesquisa por periodos
     * disponiveis selecione o periodo passado por parametro para fechamento
     */
    public void initializeClosing(long financialPeriodId){
        if (!FacesContext.getCurrentInstance().isPostback()) {

            this.financialPeriods = this.financialPeriodService.listOpenFinancialPeriods();
            
            if (financialPeriodId > 0) {
                this.financialPeriod = this.financialPeriodService
                        .findFinancialPeriodById(financialPeriodId); 
            }      
        }
    }

    /**
     * 
     * @return 
     */
    public String doCancel() {
        return "closeFinancialPeriod.xhtml?faces-redirect=true";
    }
    
    /**
     * Processa o periodo financeiro selecionado e habilita ou nao a funcao de
     * fechamento
     */
    public void process() {

        if (this.financialPeriod == null) {
            this.error("closing.validate.null-period", true);
            return;
        }

        try {
            this.closing = this.closingService.process(this.financialPeriod);
        } catch (ApplicationException ex) {
            this.logger.error("ClosingBean#process found errors", ex);
            this.fixedError(ex.getMessage(), true);
        } finally {
            this.update("closingPanel");
        }
    }
    
    /**
     * Dependendo da selecao do usuario este metodo calcula e encerra o periodo
     */
    public void close() {

        try {
            this.closeDialog("dialogConfirmClosing");
            this.execute("PF('closingPanelBlock').block()");
            
            //this.closingService.close(this.financialPeriod);
        } catch (ApplicationException ex) {
            this.logger.error("ClosingBean#close found errors", ex);
            this.fixedError(ex.getMessage(), true);
        } finally {
            this.execute("PF('closingPanelBlock').unblock()");
            this.openDialog("closingConfirmationDialog", "dialogClosingConfirmation");
        }
    }
    
    /**
     * Faz popup de confirmacao do fechamento aparecer na tela apos ter processado
     * o periodo
     */
    public void changeToClose() {
        this.openDialog("confirmClosingDialog","dialogConfirmClosing");
    }

    /**
     * @return caso haja irregularidades que afetem o fechamento, avisa o usuario
     */
    public boolean hasIrregularities() {
        if (this.closing != null) {
            return !this.closing.getOpenMovements().isEmpty() 
                    || this.closing.isMovementsWithoutInvoice();
        }
        return false;
    }
    
    /**
     * @return se true, renderiza o botao para fechamento
     */
    public boolean canClosePeriod() {
        if (closing != null) {
            return this.closing.getOpenMovements().isEmpty();
        } 
        return false;
    }
}