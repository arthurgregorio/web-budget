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
import br.com.webbudget.domain.misc.ex.InternalServiceError;
import br.com.webbudget.domain.misc.table.AbstractLazyModel;
import br.com.webbudget.domain.misc.table.Page;
import br.com.webbudget.domain.misc.table.PageRequest;
import br.com.webbudget.domain.service.CardService;
import br.com.webbudget.domain.service.WalletService;
import java.util.List;
import java.util.Map;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import org.hibernate.exception.ConstraintViolationException;
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
    private Card card;
    @Getter
    private List<Wallet> wallets;

    @Inject
    private CardService cardService;
    @Inject
    private WalletService walletService;
    
    @Getter
    private final AbstractLazyModel<Card> cardsModel;

    /**
     * 
     */
    public CardBean() {
        
        this.cardsModel = new AbstractLazyModel<Card>() {
            @Override
            public List<Card> load(int first, int pageSize, String sortField, 
                    SortOrder sortOrder, Map<String, Object> filters) {
                
                final PageRequest pageRequest = new PageRequest();
                
                pageRequest
                        .setFirstResult(first)
                        .withPageSize(pageSize)
                        .sortingBy(sortField, "inclusion")
                        .withDirection(sortOrder.name());
                
                final Page<Card> page = cardService.listCards(null, pageRequest);
                
                this.setRowCount(page.getTotalPagesInt());
                
                return page.getContent();
            }
        };
    }
    
    /**
     * 
     */
    public void initializeListing() {
        this.viewState = ViewState.LISTING;
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
        this.updateAndOpenDialog("deleteCardDialog", "dialogDeleteCard");
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

            this.addInfo(true, "card.saved");
        } catch (InternalServiceError ex) {
            this.addError(true, ex.getMessage(), ex.getParameters());
        } catch (Exception ex) {
            this.addError(true, "error.undefined-error", ex.getMessage());
        }
    }

    /**
     *
     */
    public void doUpdate() {

        try {
            this.card = this.cardService.updateCard(this.card);
            this.addInfo(true, "card.updated");
        } catch (InternalServiceError ex) {
            this.addError(true, ex.getMessage(), ex.getParameters());
        } catch (Exception ex) {
            this.addError(true, "error.undefined-error", ex.getMessage());
        }
    }

    /**
     *
     */
    public void doDelete() {

        try {
            this.cardService.deleteCard(this.card);
            this.addInfo(true, "card.deleted");
        } catch (InternalServiceError ex) {
            this.addError(true, ex.getMessage(), ex.getParameters());
        } catch (Exception ex) {
            if (this.containsException(ConstraintViolationException.class, ex)) {
                this.addError(true, "error.card.integrity-violation");
            } else {
                this.addError(true, "error.undefined-error", ex.getMessage());
            }
        } finally {
            this.updateComponent("cardsList");
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
