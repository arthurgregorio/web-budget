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
package br.com.webbudget.application.controller.entries;

import br.com.webbudget.application.components.filter.Filter;
import br.com.webbudget.application.components.table.AbstractLazyModel;
import br.com.webbudget.application.controller.AbstractBean;
import br.com.webbudget.application.controller.NavigationManager;
import br.com.webbudget.application.controller.NavigationManager.Parameter;
import br.com.webbudget.domain.entities.entries.Card;
import br.com.webbudget.domain.entities.entries.Wallet;
import br.com.webbudget.domain.repositories.entries.CardRepository;
import br.com.webbudget.domain.repositories.entries.WalletRepository;
import br.com.webbudget.domain.services.CardService;
import br.com.webbudget.domain.services.WalletService;
import java.util.List;
import java.util.Map;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import org.primefaces.model.SortOrder;

/**
 * Controller para a view do manutencao dos cartoes de credito
 *
 * @author Arthur Gregorio
 *
 * @version 1.3.0
 * @since 1.0.0, 06/04/2014
 */
@Named
@ViewScoped
public class CardBean extends AbstractBean {

    @Getter
    private Filter<Card> filter;
    
    @Getter
    private Card card;
    
    @Getter
    private List<Wallet> wallets;
    
    @Inject
    private CardService cardService;
    @Inject
    private WalletService walletService;
    
    @Inject
    private CardRepository cardRepository;
    @Inject
    private WalletRepository walletRepository;
    
    @Getter
    private final AbstractLazyModel<Card> cardsModel;
    
    private final NavigationManager formManager;
    private final NavigationManager listManager;
    private final NavigationManager detailManager;

    /**
     * 
     */
    public CardBean() {
        
        this.filter = new Filter();
        
        this.formManager = new NavigationManager("formCard.xhtml");
        this.listManager = new NavigationManager("listCards.xhtml");
        this.detailManager = new NavigationManager("detailCard.xhtml");
        
        this.cardsModel = new AbstractLazyModel<Card>() {
            @Override
            public List<Card> load(int first, int pageSize, String sortField, 
                    SortOrder sortOrder, Map<String, Object> filters) {
                return cardRepository.findByLike(filter.toExample(), 
                        first, pageSize, filter.getFilterAttributes());
            }
        };
    }
    
    /**
     * 
     */
    public void initialize() {
        this.viewState = ViewState.LISTING;
    }

    /**
     * 
     * @param id
     * @param viewState 
     */
    public void initialize(long id, ViewState viewState) {
        this.viewState = viewState;
        this.wallets = this.walletRepository.findAllActive();
        this.card = this.cardRepository.findOptionalById(id)
                .orElseGet(Card::new);
    }
    
    /**
     * @return 
     */
    public String changeToListing() {
        return this.listManager.toRoot();
    }
    
    /**
     * @return 
     */
    public String changeToAdd() {
        return this.formManager.toRootWithParameter(
                Parameter.of("viewState", ViewState.ADDING));
    }

    /**
     * @param id 
     * @return 
     */
    public String changeToEdit(long id) {
        return this.formManager.toRootWithParameters(
                Parameter.of("cardId", id),
                Parameter.of("viewState", ViewState.EDITING));
    }
    
    /**
     * @param id 
     * @return 
     */
    public String changeToDetails(long id) {
        return this.formManager.toRootWithParameters(
                Parameter.of("cardId", id),
                Parameter.of("viewState", ViewState.DETAILING));
    }

    /**
     * 
     * @param id
     * @return 
     */
    public String changeToDelete(long id) {
        return this.detailManager.toRootWithParameters(
                Parameter.of("cardId", id),
                Parameter.of("viewState", ViewState.DELETING));
    }
    
    /**
     * 
     */
    public void clearFilters() {
        this.filter = null;
        this.updateComponent("controlsForm");
    }
    
    
    /**
     * 
     * @param id
     * @return 
     */
    public String changeToStatistics(long id) {
        return "";
    }

//
//    /**
//     * 
//     */
//    public void doSave() {
//
//        try {
//            this.cardService.saveCard(this.card);
//            this.card = new Card();
//
//            this.addInfo(true, "card.saved");
//        } catch (InternalServiceError ex) {
//            this.addError(true, ex.getMessage(), ex.getParameters());
//        } catch (Exception ex) {
//            this.logger.error(ex.getMessage(), ex);
//            this.addError(true, "error.undefined-error", ex.getMessage());
//        }
//    }
//
//    /**
//     *
//     */
//    public void doUpdate() {
//
//        try {
//            this.card = this.cardService.updateCard(this.card);
//            this.addInfo(true, "card.updated");
//        } catch (InternalServiceError ex) {
//            this.addError(true, ex.getMessage(), ex.getParameters());
//        } catch (Exception ex) {
//            this.logger.error(ex.getMessage(), ex);
//            this.addError(true, "error.undefined-error", ex.getMessage());
//        }
//    }
//
//    /**
//     *
//     */
//    public void doDelete() {
//
//        try {
//            this.cardService.deleteCard(this.card);
//            this.addInfo(true, "card.deleted");
//        } catch (InternalServiceError ex) {
//            this.addError(true, ex.getMessage(), ex.getParameters());
//        } catch (Exception ex) {
//            if (this.containsException(ConstraintViolationException.class, ex)) {
//                this.addError(true, "error.card.integrity-violation",
//                        this.card.getName());
//            } else {
//                this.logger.error(ex.getMessage(), ex);
//                this.addError(true, "error.undefined-error", ex.getMessage());
//            }
//        } finally {
//            this.updateComponent("cardsList");
//            this.closeDialog("dialogDeleteCard");
//        }
//    }
//
//    /**
//     * @return a lista de tipos validos para cartoes
//     */
//    public CardType[] getAvailableCardTypes() {
//        return CardType.values();
//    }
}
