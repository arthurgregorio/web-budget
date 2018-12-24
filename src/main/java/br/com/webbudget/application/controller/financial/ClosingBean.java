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
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 * MBean que contem os metodos para encerramento dos periodos financeiros e
 * calculo do fechamento
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 1.0.0, 14/04/2014
 */
@Named
@ViewScoped
public class ClosingBean extends AbstractBean {

//    @Getter
//    @Setter
//    private FinancialPeriod financialPeriod;
//
//    @Getter
//    private MovementCalculator calculator;
//    
//    @Getter
//    private List<FinancialPeriod> financialPeriods;
//
//    @Inject
//    private ClosingService closingService;
//    @Inject
//    private FinancialPeriodService financialPeriodService;
//
//    /**
//     * Inicializa o form do fechamento com os periodos disponiveis para
//     * encerramento
//     *
//     * @param financialPeriodId se informado, apos a pesquisa por periodos
//     * disponiveis selecione o periodo passado por parametro para fechamento
//     */
//    public void initialize(long financialPeriodId) {
//
//        this.financialPeriods = 
//                this.financialPeriodService.listOpenFinancialPeriods();
//
//        if (financialPeriodId > 0) {
//            this.financialPeriod = this.financialPeriodService
//                    .findPeriodById(financialPeriodId);
//        }
//    }
//
//    /**
//     * @return
//     */
//    public String doCancel() {
//        return "closeFinancialPeriod.xhtml?faces-redirect=true";
//    }
//
//    /**
//     * Processa o periodo financeiro selecionado e habilita ou nao a funcao de
//     * fechamento
//     */
//    public void processPeriod() {
//
//        try {
//            final List<Movement> movements =
//                    this.closingService.process(this.financialPeriod);
//            this.calculator = new MovementCalculator(movements);
//            this.updateComponent("periodBox");
//        } catch (InternalServiceError ex) {
//            this.addError(true, ex.getMessage(), ex.getParameters());
//        } catch (Exception ex) {
//            this.logger.error(ex.getMessage(), ex);
//            this.addError(true, "error.undefined-error", ex.getMessage());
//        }
//    }
//
//    /**
//     * Dependendo da selecao do usuario este metodo calcula e encerra o periodo
//     */
//    public void closePeriod() {
//
//        try {
//            this.closingService.close(this.financialPeriod, this.calculator);
//            this.updateAndOpenDialog(
//                    "closingConfirmationDialog", "dialogClosingConfirmation");
//        } catch (InternalServiceError ex) {
//            this.addError(true, ex.getMessage(), ex.getParameters());
//        } catch (Exception ex) {
//            this.logger.error(ex.getMessage(), ex);
//            this.addError(true, "error.undefined-error", ex.getMessage());
//        } 
//    }
//
//    /**
//     * Devolve o usuario para a dashboard do sistema
//     * 
//     * @return o link para navegacao
//     */
//    public String changeToDashboard() {
//        return "/main/dashboard.xhtml?faces-redirect=true";
//    }
}
