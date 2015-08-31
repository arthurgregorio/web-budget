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

import br.com.webbudget.application.controller.AbstractBean;
import br.com.webbudget.domain.entity.card.Card;
import br.com.webbudget.domain.entity.card.CardType;
import br.com.webbudget.domain.entity.wallet.Wallet;
import br.com.webbudget.domain.misc.ex.WbDomainException;
import br.com.webbudget.domain.service.CardService;
import br.com.webbudget.domain.service.WalletService;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;

/**
 * Controller para a view do manutencao dos cartoes de credito
 *
 * @author Arthur Gregorio
 *
 * @version 1.2.0
 * @since 1.0.0, 06/04/2014
 */
@Named
@ViewScoped
public class CardBean extends AbstractBean {

    @Getter
    private Card card;
    @Getter
    private List<Card> cards;
    @Getter
    private List<Wallet> wallets;

    @Inject
    private CardService cardService;
    @Inject
    private WalletService walletService;

    /**
     * 
     */
    public void initializeListing() {
        this.viewState = ViewState.LISTING;
        this.cards = this.cardService.listCards(null);
    }

    /**
     * 
     * @param cardId 
     */
    public void initializeForm(long cardId) {

        this.wallets = this.walletService.listWallets(false);

        if (cardId == 0) {
            this.viewState = ViewState.ADDING;
            this.card = new Card();
        } else {
            this.viewState = ViewState.EDITING;
            this.card = this.cardService.findCardById(cardId);
        }
    }

    /**
     * @return 
     */
    public String changeToAdd() {
        return "formCard.xhtml?faces-redirect=true";
    }

    /**
     * @return 
     */
    public String changeToListing() {
        return "listCards.xhtml?faces-redirect=true";
    }

    /**
     * @param cardId 
     * @return 
     */
    public String changeToEdit(long cardId) {
        return "formCard.xhtml?faces-redirect=true&cardId=" + cardId;
    }

    /**
     * @param cardId 
     */
    public void changeToDelete(long cardId) {
        this.card = this.cardService.findCardById(cardId);
        this.openDialog("deleteCardDialog", "dialogDeleteCard");
    }

    /**
     * @return 
     */
    public String doCancel() {
        return "listCards.xhtml?faces-redirect=true";
    }

    /**
     * 
     */
    public void doSave() {

        try {
            this.cardService.saveCard(this.card);
            this.card = new Card();

            this.info("card.action.saved", true);
        } catch (WbDomainException ex) {
            this.logger.error("CardBean#doSave found erros", ex);
            this.fixedError(ex.getMessage(), true, ex.getParameters());
        } catch (Exception ex) {
            this.logger.error("CardBean#doSave found erros", ex);
            this.fixedError("generic.operation-error", true, ex.getMessage());
        }
    }

    /**
     *
     */
    public void doUpdate() {

        try {
            this.card = this.cardService.updateCard(this.card);

            this.info("card.action.updated", true);
        } catch (WbDomainException ex) {
            this.logger.error("CardBean#doUpdate found erros", ex);
            this.fixedError(ex.getMessage(), true, ex.getParameters());
        } catch (Exception ex) {
            this.logger.error("CardBean#doUpdate found erros", ex);
            this.fixedError("generic.operation-error", true, ex.getMessage());
        }
    }

    /**
     *
     */
    public void doDelete() {

        try {
            this.cardService.deleteCard(this.card);
            this.cards = this.cardService.listCards(false);

            this.info("card.action.deleted", true);
        } catch (WbDomainException ex) {
            this.logger.error("CardBean#doDelete found erros", ex);
            this.fixedError(ex.getMessage(), true, ex.getParameters());
        } catch (Exception ex) {
            this.logger.error("CardBean#doDelete found erros", ex);
            this.fixedError("generic.operation-error", true, ex.getMessage());
        } finally {
            this.update("cardsList");
            this.closeDialog("dialogDeleteCard");
        }
    }

    /**
     * @return a lista de tipos validos para cartoes
     */
    public CardType[] getAvailableCardTypes() {
        return CardType.values();
    }
}
