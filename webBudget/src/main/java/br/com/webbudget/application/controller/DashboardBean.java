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
import br.com.webbudget.domain.entity.message.PrivateMessage;
import br.com.webbudget.domain.entity.movement.FinancialPeriod;
import br.com.webbudget.domain.entity.movement.Movement;
import br.com.webbudget.domain.entity.message.UserPrivateMessage;
import br.com.webbudget.domain.security.User;
import br.com.webbudget.domain.service.AccountService;
import br.com.webbudget.domain.service.GraphModelService;
import br.com.webbudget.domain.service.MovementService;
import br.com.webbudget.domain.service.PrivateMessageService;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.chart.PieChartModel;

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
    @Setter
    private UserPrivateMessage selectedPrivateMessage;

    @Getter
    private List<Movement> movements;
    @Getter
    private List<FinancialPeriod> financialPeriods;
    @Getter
    private List<UserPrivateMessage> userPrivateMessages;

    @Getter
    private PieChartModel revenueModel;
    @Getter
    private PieChartModel expensesModel;

    @Inject
    @AuthenticatedUser
    private User authenticatedUser;

    @Inject
    private AccountService accountService;
    @Inject
    private MovementService movementService;
    @Inject
    private GraphModelService graphModelService;
    @Inject
    private PrivateMessageService privateMessageService;

    /**
     * Inicializa os graficos e tambem carrega as mensagens privadas no box de
     * mensagens
     */
    public void initialize() {

        // carregamos os graficos
        this.revenueModel = this.graphModelService.buildRevenueModelByCostCenter();
        this.expensesModel = this.graphModelService.buildExpensesModelByCostCenter();

        // carrega as mensagens do usuario
        this.userPrivateMessages = this.privateMessageService
                .listMessagesByUser(this.authenticatedUser, null);

        // carregamos os movimentos para pagamento
        this.movements = this.movementService
                .listMovementsByDueDate(LocalDate.now(), true);
    }

    /**
     * Pega mensagem selecionada e mostra a dialog
     */
    public void displayMessage() {
        
        // marca a mensagem como lida
        this.privateMessageService.markAsRead(this.selectedPrivateMessage);
        
        // carrega o nome do cara que enviou
        final PrivateMessage message = this.selectedPrivateMessage.getPrivateMessage();
        
        final User sender = this.accountService.findUserById(message.getSender());
        
        message.setSenderName(sender.getName());
        
        this.openDialog("displayPrivateMessageDialog", "dialogDisplayPrivateMessage");
    }

    /**
     * Atualiza as mensagens e fecha a popup de mensagem
     */
    public void closeMessge() {
        this.selectedPrivateMessage = null;
        this.userPrivateMessages = this.privateMessageService
                .listMessagesByUser(this.authenticatedUser, null);

        this.update("messagesList");
        this.closeDialog("dialogDisplayPrivateMessage");
    }

    /**
     * Deleta a mensagem e atualiza a view
     */
    public void deleteAndCloseMessage() {

        this.privateMessageService.markAsDeleted(this.selectedPrivateMessage);

        this.userPrivateMessages = this.privateMessageService
                .listMessagesByUser(this.authenticatedUser, null);

        this.update("messagesList");
        this.closeDialog("dialogDisplayPrivateMessage");
    }

    /**
     * Quando este metodo e invocado o sistema realiza um redirecionamento para
     * a view de pagamento de movimentos para que o movimento selecionado seja
     * pago pelo usuario
     *
     * @param movementId o id do movimento a ser pago
     * @return a URL para redirecionar o usuario
     */
    public String changeToPay(long movementId) {
        return "financial/movement/formPayment.xhtml?faces-redirect=true&movementId=" + movementId;
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
