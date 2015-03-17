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

import br.com.webbudget.domain.entity.movement.FinancialPeriod;
import br.com.webbudget.domain.entity.movement.Movement;
import br.com.webbudget.domain.entity.users.User;
import br.com.webbudget.domain.entity.users.UserPrivateMessage;
import br.com.webbudget.domain.service.AccountService;
import br.com.webbudget.domain.service.GraphModelService;
import br.com.webbudget.domain.service.MovementService;
import br.com.webbudget.domain.service.PrivateMessageService;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.chart.PieChartModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 27/02/2014
 */
@ViewScoped
@ManagedBean
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
    private PieChartModel expensesModel;
    @Getter
    private PieChartModel revenueModel;
    
    @Setter
    @ManagedProperty("#{accountService}")
    private transient AccountService accountService;
    @Setter
    @ManagedProperty("#{movementService}")
    private transient MovementService movementService;
    @Setter
    @ManagedProperty("#{graphModelService}")
    private transient GraphModelService graphModelService;
    @Setter
    @ManagedProperty("#{privateMessageService}")
    private transient PrivateMessageService privateMessageService;

    /**
     * 
     * @return 
     */
    @Override
    protected Logger initializeLogger() {
        return LoggerFactory.getLogger(DashboardBean.class);
    }
    
    /**
     * 
     */
    public void initialize() {
        
        if (!FacesContext.getCurrentInstance().isPostback()) {
        
            // carregamos os graficos
            this.revenueModel = this.graphModelService.buildRevenueModelByCostCenter();
            this.expensesModel = this.graphModelService.buildExpensesModelByCostCenter();

            // carrega as mensagens do usuario
            this.userPrivateMessages = this.privateMessageService.listMessagesByUser(
                    AccountService.getCurrentAuthenticatedUser(), null);

            // carregamos os movimentos para pagamento
            this.movements = this.movementService.listMovementsByDueDate(new Date(), true);
        }
    }
    
    /**
     * Pega mensagem selecionada e mostra a popup
     */
    public void displayMessage() {
        this.privateMessageService.markAsRead(this.selectedPrivateMessage);
        this.openDialog("displayPrivateMessageDialog","dialogDisplayPrivateMessage");
    }
    
    /**
     * Atualiza as mensagens e fecha a popup de mensagem
     */
    public void closeMessge() {
        this.selectedPrivateMessage = null;
        this.userPrivateMessages = this.privateMessageService.listMessagesByUser(
                    AccountService.getCurrentAuthenticatedUser(), null);
        
        this.update("messagesList");
        this.closeDialog("dialogDisplayPrivateMessage");
    }
    
    /**
     * Deleta a mensagem e atualiza a view
     */
    public void deleteAndCloseMessage() {
        
        this.privateMessageService.markAsDeleted(this.selectedPrivateMessage);
        
        this.userPrivateMessages = this.privateMessageService.listMessagesByUser(
                    AccountService.getCurrentAuthenticatedUser(), null);
        
        this.update("messagesList");
        this.closeDialog("dialogDisplayPrivateMessage");
    }
    
    /**
     * 
     * @param movementId
     * @return 
     */
    public String changeToPay(long movementId) {
        return "financial/movement/formPayment.xhtml?faces-redirect=true&movementId=" + movementId;
    }
    
    /**
     * Efetua logout do sistema
     * 
     * @return 
     */
    public String doLogout() {
        this.accountService.logout();
        return "/home.xhtml?faces-redirect=true";
    }

    /**
     * 
     * @return 
     */
    public String getCurrentUserName() {
        return AccountService.getCurrentAuthenticatedUser().getName();
    }
    
    /**
     * Pega do bundle da aplicacao o numero da versao setado no maven
     * 
     * @return a versao da aplicacao
     */
    public String getVersion() {
        return ResourceBundle.getBundle("config/webbudget").getString("application.version");
    }
}
