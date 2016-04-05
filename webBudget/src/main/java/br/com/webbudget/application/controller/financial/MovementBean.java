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
import br.com.webbudget.domain.model.entity.card.Card;
import br.com.webbudget.domain.model.entity.contact.Contact;
import br.com.webbudget.domain.model.entity.movement.Apportionment;
import br.com.webbudget.domain.model.entity.movement.CostCenter;
import br.com.webbudget.domain.model.entity.movement.FinancialPeriod;
import br.com.webbudget.domain.model.entity.movement.Movement;
import br.com.webbudget.domain.model.entity.movement.MovementClass;
import br.com.webbudget.domain.model.entity.movement.MovementClassType;
import br.com.webbudget.domain.model.entity.movement.MovementStateType;
import br.com.webbudget.domain.model.entity.movement.MovementType;
import br.com.webbudget.domain.model.entity.movement.Payment;
import br.com.webbudget.domain.model.entity.wallet.Wallet;
import br.com.webbudget.domain.misc.filter.MovementFilter;
import br.com.webbudget.application.component.table.AbstractLazyModel;
import br.com.webbudget.application.component.table.MovementsListModel;
import br.com.webbudget.domain.model.service.CardService;
import br.com.webbudget.domain.model.service.ContactService;
import br.com.webbudget.domain.model.service.FinancialPeriodService;
import br.com.webbudget.domain.model.service.MovementService;
import br.com.webbudget.domain.model.service.WalletService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.DualListModel;

/**
 * Controller da tela de movimentos do periodo
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 1.0.0, 18/03/2014
 */
@Named
@ViewScoped
public class MovementBean extends AbstractBean {

    @Getter
    @Setter
    private String contactFilter;
    
    @Getter
    @Setter
    private MovementFilter filter;

    @Getter
    @Setter
    private Movement movement;
    @Getter
    @Setter
    private Apportionment apportionment;
    
    @Getter
    private Payment payment;
    @Getter
    private FinancialPeriod financialPeriod;

    @Getter
    private List<Wallet> wallets;
    @Getter
    private List<Card> debitCards;
    @Getter
    private List<Card> creditCards;
    @Getter
    private List<Contact> contacts;
    @Getter
    private List<CostCenter> costCenters;
    @Getter
    private List<FinancialPeriod> periods;
    @Getter
    private List<FinancialPeriod> openPeriods;
    @Getter
    private List<MovementClass> movementClasses;
    
    @Getter
    @Setter
    private DualListModel<FinancialPeriod> periodsModel;

    @Inject
    private CardService cardService;
    @Inject
    private WalletService walletService;
    @Inject
    private ContactService contactService;
    @Inject
    private MovementService movementService;
    @Inject
    private FinancialPeriodService financialPeriodService;

    @Getter
    private AbstractLazyModel<Movement> movementsModel;

    /**
     * Inicializamos os objetos necessarios
     */
    @PostConstruct
    protected void initialize() {
        
        // inicializa o model customizado
        this.movementsModel = new MovementsListModel(
                this.movementService, () -> this.getFilter());
        
        // inicializa o filtro
        this.filter = new MovementFilter();
    }

    /**
     * Inicializa a tela de listagem de movimentos
     */
    public void initializeListing() {
        this.viewState = ViewState.LISTING;

        // inicializa o filtro
        this.filter = new MovementFilter();

        // cria o filtro por periodo
        this.periods = this.financialPeriodService.listFinancialPeriods(null);
        this.periodsModel = new DualListModel<>(this.periods, new ArrayList<>());
        
        // filtra somente os que estao em aberto
        this.openPeriods = periods.stream()
                .filter(period -> !period.isClosed())
                .collect(Collectors.toList());
        
        // seta a primeira busca sendo pelos periodos em aberto
        this.filter.setPeriods(this.openPeriods);
    }

    /**
     * Inicializa o form de cadastro, edicao ou visualizacao dos movimentos
     * 
     * @param movementId o id do movimento a ser visualizado ou editado
     * @param viewState o estado da tela a ser aplicado
     */
    public void initializeForm(long movementId, String viewState) {

        this.viewState = ViewState.valueOf(viewState);
        
    }

    /**
     * Inicializa o formulario de pagamento de movimentos
     * 
     * @param movementId o id do movimento a ser pago
     */
    public void initializePayment(long movementId) {

    }

    /**
     * @return volta para a tela de listagem
     */
    public String changeToListing() {
        return "listMovements.xhtml?faces-redirect=true";
    }
    
    /**
     * @return envia o usuario para a tela de cadastro
     */
    public String changeToAdd() {
        return "formMovement.xhtml?faces-redirect=true&viewState=" 
                + ViewState.ADDING;
    }

    /**
     * @param movementId o id do movimento a ser editado
     * @return a tela de edicao
     */
    public String changeToEdit(long movementId) {
        return "formMovement.xhtml?faces-redirect=true&movementId=" 
                + movementId + "&viewState=" + ViewState.EDITING;
    }
    
    /**
     * @param movementId o id do movimento a ser excluido
     */
    public void changeToDelete(long movementId) {
        this.movement = this.movementService.findMovementById(movementId);
        this.updateAndOpenDialog("deleteMovementDialog", "dialogDeleteMovement");
    }
    
    /**
     * Da um redirect para os detalhes do movimento
     */
    public void changeToDetail() {
        this.redirectTo("formMovement.xhtml?faces-redirect=true&movementId=" 
                + this.movement.getId() + "&viewState=" + ViewState.DETAILING);
    }

    /**
     * @param movementId o id do movimento a ser pago
     * @return a tela de pagamento de movimentos
     */
    public String changeToPay(long movementId) {
        return "formPayment.xhtml?faces-redirect=true&movementId=" + movementId;
    }
    
    /**
     * Aplica os filtros customizados selecionados na listagem de movimentos
     */
    public void applyCustomFilters() {
        
        this.filter.setPeriods(this.periodsModel.getTarget());
        
        this.updateComponent("movementsList");
        this.closeDialog("dialogConfigFilter");
    }
    
    /**
     * Limpa todos os filtro ja realizados
     */
    public void clearFilters() {
        
    }
    
    /**
     * Exibe a tela de customizacao dos filros avancados
     */
    public void showFilterConfigDialog() {
        this.updateAndOpenDialog("configFilterDialog", "dialogConfigFilter");
    }
    
    /**
     * @return os estados possiveis dos movimentos
     */
    public MovementStateType[] getMovementStateTypes() {
        return MovementStateType.values();
    }
    
    /**
     * @return as possiveis direcoes de um movimento
     */
    public MovementClassType[] getMovementClassTypes() {
        return MovementClassType.values();
    }

    /**
     * @return os possiveis tipos de um movimento
     */
    public MovementType[] getMovementTypes() {
        return MovementType.values();
    }
}
