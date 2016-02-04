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
package br.com.webbudget.application.controller;

import br.com.webbudget.application.producer.qualifier.AuthenticatedUser;
import br.com.webbudget.domain.entity.movement.FinancialPeriod;
import br.com.webbudget.domain.security.User;
import br.com.webbudget.domain.service.FinancialPeriodService;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;

/**
 * Mbean utilizado na dashboard do sistema, por ele carregamos os graficos da
 * dashboard e tambem alguns elementos da template, como o nome no botao de
 * informacoes da conta do usuario
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 1.0.0, 27/02/2014
 */
@Named
@ViewScoped
public class DashboardBean extends AbstractBean {

    @Getter
    private List<FinancialPeriod> financialPeriods;

    @Inject
    @AuthenticatedUser
    private User authenticatedUser;

    @Inject
    private FinancialPeriodService financialPeriodService;

    /**
     * Inicializa os graficos e tambem carrega as mensagens privadas no box de
     * mensagens
     */
    public void initialize() {

        this.financialPeriods = this.financialPeriodService
                .listFinancialPeriods(Boolean.FALSE);
    }

    /**
     * @return um texto identificando os periodos financeiros em aberto
     */
    public String getOpenPeriods() {

        if (this.financialPeriods == null || this.financialPeriods.isEmpty()) {
            return "";
        }

        final StringBuilder builder = new StringBuilder();

        for (int i = 0; i < this.financialPeriods.size(); i++) {

            final FinancialPeriod period = this.financialPeriods.get(i);

            builder.append(period.getIdentification());

            if ((i + 1) == (this.financialPeriods.size() - 1)) {
                builder.append(" e ");
            } else if (i != (this.financialPeriods.size() - 1)) {
                builder.append(", ");
            }
        }

        return builder.toString();
    }

    /**
     * Pega do bundle da aplicacao o numero da versao setado no maven
     *
     * @return a versao da aplicacao
     */
    public String getVersion() {
        return ResourceBundle.getBundle("webbudget").getString("application.version");
    }
}
