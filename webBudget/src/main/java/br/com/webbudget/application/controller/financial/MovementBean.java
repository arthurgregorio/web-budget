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
import br.com.webbudget.domain.model.entity.entries.Card;
import br.com.webbudget.domain.model.entity.entries.Contact;
import br.com.webbudget.domain.model.entity.financial.Apportionment;
import br.com.webbudget.domain.model.entity.entries.CostCenter;
import br.com.webbudget.domain.model.entity.miscellany.FinancialPeriod;
import br.com.webbudget.domain.model.entity.financial.Movement;
import br.com.webbudget.domain.model.entity.entries.MovementClass;
import br.com.webbudget.domain.model.entity.entries.MovementClassType;
import br.com.webbudget.domain.model.entity.financial.MovementStateType;
import br.com.webbudget.domain.model.entity.financial.MovementType;
import br.com.webbudget.domain.model.entity.financial.Payment;
import br.com.webbudget.domain.model.entity.entries.Wallet;
import br.com.webbudget.domain.misc.filter.MovementFilter;
import br.com.webbudget.application.component.table.AbstractLazyModel;
import br.com.webbudget.application.component.table.MovementsListModel;
import br.com.webbudget.domain.misc.ex.InternalServiceError;
import br.com.webbudget.domain.model.entity.financial.PaymentMethodType;
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
    private List<MovementClass> movementClasses;

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

    @Getter
    @Setter
    private DualListModel<FinancialPeriod> periodsModel;

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

        // seta a primeira busca sendo pelos periodos em aberto
        this.filter.setPeriods(this.getOpenPeriods());
    }

    /**
     * Inicializa o form de cadastro, edicao ou visualizacao dos movimentos
     *
     * @param movementId o id do movimento a ser visualizado ou editado
     * @param viewState o estado da tela a ser aplicado
     */
    public void initializeForm(long movementId, String viewState) {

        this.viewState = ViewState.valueOf(viewState);

        // lista as formas de pagamento
        this.wallets = this.walletService.listWallets(Boolean.FALSE);
        this.debitCards = this.cardService.listDebitCards(Boolean.FALSE);
        this.creditCards = this.cardService.listCreditCards(Boolean.FALSE);

        // carrega os centros de custo e os periodos validos
        this.costCenters = this.movementService.listCostCenters(false);
        this.periods = this.financialPeriodService.listFinancialPeriods(false);

        // inicializa o movimento
        if (this.viewState == ViewState.ADDING) {
            this.movement = new Movement();
        } else {
            this.movement = this.movementService.findMovementById(movementId);
        }
    }

    /**
     * Inicializa o formulario de pagamento de movimentos
     *
     * @param movementId o id do movimento a ser pago
     */
    public void initializePayment(long movementId) {
        
        // inicializa o movimento
        this.movement = this.movementService.findMovementById(movementId);
        
        final Payment payment = new Payment();
        
        // se for um lancamento, pega a data de incio como data de pagamento
        if (this.movement.getLaunch() != null) {
            payment.setPaymentDate(this.movement.getLaunch().getStartDate());
        }
        
        this.movement.setPayment(payment);
        
        // inicializa carteiras e afins
        this.wallets = this.walletService.listWallets(Boolean.FALSE);
        this.debitCards = this.cardService.listDebitCards(Boolean.FALSE);
        this.creditCards = this.cardService.listCreditCards(Boolean.FALSE);
    }

    /**
     * Salva o movimento
     */
    public void doSave() {
        try {
            this.movementService.saveMovement(this.movement);
            this.movement = new Movement();
            this.addInfo(true, "movement.saved");
        } catch (InternalServiceError ex) {
            this.addError(false, ex.getMessage(), ex.getParameters());
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ex);
            this.addError(false, "error.undefined-error", ex.getMessage());
        }
    }

    /**
     * Atualiza o movimento
     */
    public void doUpdate() {
        try {
            this.movement = this.movementService.updateMovement(this.movement);
            this.movement = this.movementService
                    .findMovementById(this.movement.getId());
            this.addInfo(true, "movement.updated");
        } catch (InternalServiceError ex) {
            this.addError(false, ex.getMessage(), ex.getParameters());
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ex);
            this.addError(false, "error.undefined-error", ex.getMessage());
        }
    }

    /**
     * Salva e paga o movimento
     */
    public void doSaveAndPayment() {
        try {
            this.movementService.payMovement(this.movement);
            this.movement = new Movement();
            this.closeDialog("dialogPayment");
            this.addInfo(false, "movement.saved-paid");
            this.updateComponent("movementForm");
            this.temporizeHiding("messages");
        } catch (InternalServiceError ex) {
            this.addError(false, ex.getMessage(), ex.getParameters());
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ex);
            this.addError(false, "error.undefined-error", ex.getMessage());
        }
    }
    
    /**
     * 
     */
    public void doUpdateAndPayment() {
        try {
            this.movementService.payMovement(this.movement);
            this.changeToDetail(this.movement.getId());
        } catch (InternalServiceError ex) {
            this.addError(false, ex.getMessage(), ex.getParameters());
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ex);
            this.addError(false, "error.undefined-error", ex.getMessage());
        }
    }
    
    /**
     * Aepnas paga o movimento
     */
    public void doPayment() {
        try {
            this.movementService.payMovement(this.movement);
            this.updateAndOpenDialog("confirmPaymentDialog", "dialogConfirmPayment");
        } catch (InternalServiceError ex) {
            this.addError(true, ex.getMessage(), ex.getParameters());
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ex);
            this.addError(true, "error.undefined-error", ex.getMessage());
        }
    }

    /**
     * Deleta o movimento
     */
    public void doDelete() {
        try {
            final MovementType movementType = this.movement.getMovementType();

            // fazemos a selecao do tipo de delecao a ser executado
            if (movementType == MovementType.MOVEMENT) {
                this.movementService.deleteMovement(this.movement);
            } else if (movementType == MovementType.CARD_INVOICE) {
                this.movementService.deleteCardInvoiceMovement(this.movement);
            }
            this.addInfo(true, "movement.deleted");
        } catch (InternalServiceError ex) {
            this.addError(false, ex.getMessage(), ex.getParameters());
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ex);
            this.addError(false, "error.undefined-error", ex.getMessage());
        } finally {
            this.updateComponent("movementsList");
            this.closeDialog("dialogDeleteMovement");
        }
    }

    /**
     * Exibe a telinha de pagamento
     */
    public void showPaymentDialog() {
        
        final Payment payment = new Payment();
       
        // se for um lancamento, pega a data de incio como data de pagamento
        if (this.movement.getLaunch() != null) {
            payment.setPaymentDate(this.movement.getLaunch().getStartDate());
        }
        this.movement.setPayment(payment);
        this.updateAndOpenDialog("paymentDialog", "dialogPayment");
    }

    /**
     * @return volta para a tela de listagem
     */
    public String changeToList() {
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
     * Da um redirect para os detalhes do movimento
     *
     * @param movementId qual o movimento que vamos ver os detalhes
     */
    public void changeToDetail(long movementId) {
        this.redirectTo("formMovement.xhtml?faces-redirect=true&movementId="
                + movementId + "&viewState=" + ViewState.DETAILING);
    }

    /**
     * @param movementId o id do movimento a ser pago
     * @return a tela de pagamento de movimentos
     */
    public String changeToPay(long movementId) {
        return "formPayment.xhtml?faces-redirect=true&movementId=" + movementId;
    }

    /**
     *
     */
    public void showPaymentDetails() {
        this.updateAndOpenDialog("paymentDetailsDialog", "dialogPaymentDetails");
    }

    /**
     * Abre a dialog de busca de contatos
     */
    public void showContactDialog() {
        this.contactFilter = null;
        this.contacts = new ArrayList<>();
        this.updateAndOpenDialog("contactDialog", "dialogContact");
    }

    /**
     * Busca o contato de acordo com o filtro
     */
    public void filterContactsList() {
        try {
            this.contacts = this.contactService
                    .listContactsByFilter(this.contactFilter, false);
        } catch (InternalServiceError ex) {
            this.addError(false, ex.getMessage(), ex.getParameters());
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ex);
            this.addError(false, "error.undefined-error", ex.getMessage());
        } finally {
            this.updateComponent("contactForm");
        }
    }

    /**
     * Apos selecionar um contato este metodo fechara a dialog e atualizara a
     * view
     */
    public void onContactSelect() {
        this.updateComponent("contactBox");
        this.closeDialog("dialogContact");
    }

    /**
     * Remove o contato que foi vinculado ao movimento
     */
    public void removeContact() {
        this.movement.setContact(null);
        this.updateComponent("contactBox");
    }

    /**
     *
     */
    public void showApportionmentDialog() {

        // se o valor do rateio for igual ao total do movimento nem deixa exibir
        // a tela de rateios para que nao seja feito cagada
        if (this.movement.hasValueToDivide()) {
            this.addError(true, "error.fixed-movement.no-value-divide");
            return;
        }

        this.apportionment = new Apportionment();
        this.apportionment.setValue(this.movement.getValueToDivide());

        this.updateAndOpenDialog("apportionmentDialog", "dialogApportionment");
    }

    /**
     *
     */
    public void addApportionment() {
        try {
            this.movement.addApportionment(this.apportionment);
            this.updateComponent("inValue");
            this.updateComponent("apportionmentBox:container");
            this.closeDialog("dialogApportionment");
        } catch (InternalServiceError ex) {
            this.addError(false, ex.getMessage(), ex.getParameters());
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ex);
            this.addError(false, "error.undefined-error", ex.getMessage());
        } finally {
            this.updateComponent("apportionmentMessages");
        }
    }

    /**
     *
     * @param code
     */
    public void deleteApportionment(String code) {
        this.movement.removeApportionment(code);
        this.updateComponent("inValue");
        this.updateComponent("apportionmentBox:container");
    }

    /**
     * Atualiza o combo de classes quando o usuario selecionar o centro de custo
     */
    public void loadMovementClasses() {
        this.movementClasses = this.movementService.listMovementClassesByCostCenterAndType(
                this.apportionment.getCostCenter(), null);
    }

    /**
     * Exibe a tela de customizacao dos filros avancados
     */
    public void showFilterConfigDialog() {
        this.updateAndOpenDialog("configFilterDialog", "dialogConfigFilter");
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
        this.filter.setCriteria(null);
        this.filter.setPeriods(this.getOpenPeriods());
        this.updateComponent("movementsList");
        this.updateComponent("controlsForm");
    }

    /**
     * @return da lista de periodos, retorna apenas o que estiver ativo
     */
    public FinancialPeriod getActivePeriod() {
        return this.periods.stream()
                .filter(FinancialPeriod::isActive)
                .findFirst()
                .orElse(null);
    }

    /**
     * @return os periodos financeiros em aberto no sistema
     */
    public List<FinancialPeriod> getOpenPeriods() {
        if (this.viewState == ViewState.DETAILING) {
            return this.periods;
        } else {
            return this.periods.stream()
                .filter(period -> !period.isClosed())
                .collect(Collectors.toList());
        }
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

    /**
     * @return os possiveis metodos de pagamento
     */
    public PaymentMethodType[] getPaymentMethodTypes() {
        return PaymentMethodType.values();
    }
}
