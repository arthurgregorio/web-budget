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
package br.com.webbudget.application.controller.registration;

import br.com.webbudget.application.components.ui.LazyFormBean;
import br.com.webbudget.application.components.ui.ViewState;
import br.com.webbudget.application.components.ui.table.Page;
import br.com.webbudget.domain.entities.registration.Card;
import br.com.webbudget.domain.entities.registration.CardType;
import br.com.webbudget.domain.entities.registration.Wallet;
import br.com.webbudget.domain.events.CardCreated;
import br.com.webbudget.domain.logics.registration.card.CardDeletingLogic;
import br.com.webbudget.domain.logics.registration.card.CardSavingLogic;
import br.com.webbudget.domain.logics.registration.card.CardUpdatingLogic;
import br.com.webbudget.domain.repositories.registration.CardRepository;
import br.com.webbudget.domain.repositories.registration.WalletRepository;
import lombok.Getter;
import org.primefaces.model.SortOrder;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.util.List;

import static br.com.webbudget.application.components.ui.NavigationManager.PageType.*;
import static br.com.webbudget.application.components.ui.NavigationManager.Parameter.of;

/**
 * The {@link Card} maintenance routine controller
 *
 * @author Arthur Gregorio
 *
 * @version 1.3.0
 * @since 1.0.0, 06/04/2014
 */
@Named
@ViewScoped
public class CardBean extends LazyFormBean<Card> {

    @Getter
    private List<Wallet> wallets;

    @Inject
    private CardRepository cardRepository;
    @Inject
    private WalletRepository walletRepository;

    @Inject
    @CardCreated
    private Event<Card> cardCreatedEvent;

    @Any
    @Inject
    private Instance<CardSavingLogic> savingLogics;
    @Any
    @Inject
    private Instance<CardUpdatingLogic> updatingLogics;
    @Any
    @Inject
    private Instance<CardDeletingLogic> deletingLogics;

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param viewState
     */
    @Override
    public void initialize(long id, ViewState viewState) {
        this.viewState = viewState;
        this.wallets = this.walletRepository.findAllActive();
        this.value = this.cardRepository.findById(id).orElseGet(Card::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initializeNavigationManager() {
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
    public Page<Card> load(int first, int pageSize, String sortField, SortOrder sortOrder) {
        return this.cardRepository.findAllBy(this.filter.getValue(), this.filter.getEntityStatusValue(), first, pageSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void doSave() {
        this.savingLogics.forEach(logic -> logic.run(this.value));
        this.cardCreatedEvent.fire(this.cardRepository.save(this.value));
        this.value = new Card();
        this.addInfo(true, "saved");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void doUpdate() {
        this.updatingLogics.forEach(logic -> logic.run(this.value));
        this.value = this.cardRepository.saveAndFlushAndRefresh(this.value);
        this.addInfo(true, "updated");
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    @Transactional
    public String doDelete() {
        this.deletingLogics.forEach(logic -> logic.run(this.value));
        this.cardRepository.attachAndRemove(this.value);
        this.addInfoAndKeep("deleted");
        return this.changeToListing();
    }

    /**
     * Send the user to the card statistics page
     *
     * @param id the id of the card to show statistics
     * @return the navigation case
     */
    public String changeToStatistics(long id) {
        return this.navigation.to("cardStatistics.xhtml", of("id", id));
    }

    /**
     * Helper method to get the types defined in the {@link CardType} enum
     *
     * @return array of types from {@link CardType}
     */
    public CardType[] getCardTypes() {
        return CardType.values();
    }
}
