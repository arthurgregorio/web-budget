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

import br.com.webbudget.application.components.table.LazyDataProvider;
import br.com.webbudget.application.components.table.LazyFilter;
import br.com.webbudget.application.components.table.LazyModel;
import br.com.webbudget.application.controller.AbstractBean;
import br.com.webbudget.application.controller.NavigationManager;
import static br.com.webbudget.application.controller.NavigationManager.PageType.*;
import static br.com.webbudget.application.controller.NavigationManager.Parameter.of;
import br.com.webbudget.application.controller.ViewState;
import br.com.webbudget.domain.entities.entries.Card;
import br.com.webbudget.domain.entities.entries.CardType;
import br.com.webbudget.domain.entities.entries.Wallet;
import br.com.webbudget.domain.repositories.entries.CardRepository;
import br.com.webbudget.domain.repositories.entries.WalletRepository;
import br.com.webbudget.domain.services.CardService;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
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
public class CardBean extends AbstractBean implements LazyDataProvider<Card> {

    @Getter
    @Setter
    private Card card;

    @Getter
    private List<Wallet> wallets;

    @Inject
    private CardService cardService;

    @Inject
    private CardRepository cardRepository;
    @Inject
    private WalletRepository walletRepository;

    @Getter
    private final LazyFilter filter;
    @Getter
    private final LazyModel<Card> cardsModel;

    private final NavigationManager navigation;

    /**
     *
     */
    public CardBean() {

        // initialize the search and lazy mechanism of datatable
        this.filter = LazyFilter.initialize();
        this.cardsModel = new LazyModel<>(this);

        // configure navigation manager
        this.navigation = NavigationManager.getInstance();
        
        this.navigation.addPage(LIST_PAGE, "listCards.xhtml");
        this.navigation.addPage(ADD_PAGE, "formCard.xhtml");
        this.navigation.addPage(UPDATE_PAGE, "formCard.xhtml");
        this.navigation.addPage(DETAIL_PAGE, "detailCard.xhtml");
        this.navigation.addPage(DELETE_PAGE, "detailCard.xhtml");
    }

    /**
     * {@inheritDoc }
     *
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @return
     */
    @Override
    public List<Card> load(int first, int pageSize, String sortField, SortOrder sortOrder) {
        return this.cardRepository.findAllBy(this.filter.getValue(),
                this.filter.getEntityStatusValue(), first, pageSize);
    }

    /**
     * 
     */
    public void initialize() {
        this.viewState = ViewState.LISTING;
        this.temporizeHiding(this.getDefaultMessagesComponentId());
    }

    /**
     *
     * @param id
     * @param viewState
     */
    public void initialize(long id, ViewState viewState) {
        this.viewState = viewState;
        this.wallets = this.walletRepository.findAllUnblocked();
        this.card = this.cardRepository.findOptionalById(id)
                .orElseGet(Card::new);
    }

    /**
     * @return
     */
    public String changeToListing() {
        return this.navigation.to(LIST_PAGE);
    }

    /**
     * @return
     */
    public String changeToAdd() {
        return this.navigation.to(ADD_PAGE);
    }

    /**
     * @param id
     * @return
     */
    public String changeToEdit(long id) {
        return this.navigation.to(UPDATE_PAGE, of("cardId", id));
    }

    /**
     * 
     */
    public void changeToDetail() {
        this.navigation.redirect(DETAIL_PAGE, of("cardId", this.card.getId()));
    }

    /**
     *
     * @param id
     * @return
     */
    public String changeToDelete(long id) {
        return this.navigation.to(DELETE_PAGE, of("cardId", id));
    }

    /**
     *
     */
    public void clearFilters() {
        this.filter.clear();
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

    /**
     *
     */
    public void doSave() {
        this.cardService.save(this.card);
        this.card = new Card();
        this.addInfo(true, "card.saved");
    }

    /**
     *
     */
    public void doUpdate() {
        this.card = this.cardService.update(this.card);
        this.addInfo(true, "card.updated");
    }

    /**
     * 
     * @return 
     */
    public String doDelete() {
        this.cardService.delete(this.card);
        this.addInfoToFlash("card.deleted");
        return this.changeToListing();
    }

    /**
     * @return a lista de tipos validos para cartoes
     */
    public CardType[] getCardTypes() {
        return CardType.values();
    }
}
