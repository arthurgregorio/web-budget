package br.com.webbudget.application.controller.entries;

import br.com.webbudget.application.ViewState;
import br.com.webbudget.application.components.MessagesFactory;
import br.com.webbudget.domain.entity.card.Card;
import br.com.webbudget.domain.entity.card.CardType;
import br.com.webbudget.domain.entity.wallet.Wallet;
import br.com.webbudget.domain.service.CardService;
import br.com.webbudget.domain.service.WalletService;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import org.omnifaces.util.Messages;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 06/04/2014
 */
@ViewScoped
@ManagedBean
public class CardBean implements Serializable {

    @Getter
    private ViewState viewState;
    
    @Getter
    private Card card;
    @Getter
    private List<Card> cards;
    @Getter
    public List<Wallet> wallets;
    
    @Setter
    @ManagedProperty("#{cardService}")
    private transient CardService cardService;
    @Setter
    @ManagedProperty("#{messagesFactory}")
    private transient MessagesFactory messages;
    @Setter
    @ManagedProperty("#{walletService}")
    private transient WalletService walletService;
    
    private final Logger LOG = LoggerFactory.getLogger(CardBean.class);
    
    /**
     * 
     */
    public void initializeListing(){
        if (!FacesContext.getCurrentInstance().isPostback()) {
            this.viewState = ViewState.LISTING;
            this.cards = this.cardService.listCards(null);
        }
    }

    /**
     * 
     * @param cardId 
     */
    public void initializeForm(long cardId) {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            
            this.wallets = this.walletService.listWallets(false);
            
            if (cardId == 0) {
                this.viewState = ViewState.ADD;
                this.card = new Card();
            } else {
                this.viewState = ViewState.EDIT;
                this.card = this.cardService.findCardById(cardId);
            }
        }
    }
    
    /**
     * 
     * @return o form de inclusao
     */
    public String changeToAdd() {
        return "formCard.xhtml?faces-redirect=true";
    }
    
    /**
     * 
     * @return 
     */
    public String changeToListing() {
        return "listCards.xhtml?faces-redirect=true";
    }
    
    /**
     * 
     * @param cardId
     * @return 
     */
    public String changeToEdit(long cardId) {
        return "formCard.xhtml?faces-redirect=true&cardId=" + cardId;
    }
    
    /**
     * 
     * @param cardId 
     */
    public void changeToDelete(long cardId) {
        this.card = this.cardService.findCardById(cardId);
        RequestContext.getCurrentInstance().execute("PF('popupDeleteCard').show()");
    }
    
    /**
     * 
     */
    public void doSave() {
        
        try {
            this.cardService.saveCard(this.card);
            this.card = new Card();
            
            Messages.addInfo(null, messages.getMessage("card.action.saved"));
        }  catch (Exception ex) {
            LOG.error("CardBean#doSave found erros", ex);
            Messages.addError(null, messages.getMessage(ex.getMessage()));
        } finally {
            RequestContext.getCurrentInstance().update("cardForm");
            RequestContext.getCurrentInstance().execute("setTimeout(\"$(\'#messages\').slideUp(300)\", 5000)");
        }
    }
    
    /**
     * 
     */
    public void doUpdate() {
        
        try {
            this.card = this.cardService.updateCard(this.card);
            
            Messages.addInfo(null, messages.getMessage("card.action.updated"));
        } catch (Exception ex) {
            LOG.error("CardBean#doUpdate found erros", ex);
            Messages.addError(null, messages.getMessage(ex.getMessage()));
        } finally {
            RequestContext.getCurrentInstance().update("cardForm");
            RequestContext.getCurrentInstance().execute("setTimeout(\"$(\'#messages\').slideUp(300)\", 5000)");
        }
    }
    
    /**
     * 
     */
    public void doDelete() {
        
        try {
            this.cardService.deleteCard(this.card);
            this.cards = this.cardService.listCards(false);
            
            Messages.addWarn(null, messages.getMessage("card.action.deleted"));
        } catch (Exception ex) {
            LOG.error("CardBean#doDelete found erros", ex);
            Messages.addError(null, messages.getMessage(ex.getMessage()));
        } finally {
            RequestContext.getCurrentInstance().execute("PF('popupDeleteCard').hide()");
            RequestContext.getCurrentInstance().update("messages");
            RequestContext.getCurrentInstance().execute("setTimeout(\"$(\'#messages\').slideUp(300)\", 5000)");
            RequestContext.getCurrentInstance().update("cardsList");
        }
    }
    
    /**
     * Cancela e volta para a listagem
     * 
     * @return 
     */
    public String doCancel() {
        return "listCards.xhtml?faces-redirect=true";
    }
    
    /**
     * 
     * @return 
     */
    public CardType[] getAvailableCardTypes() {
        return CardType.values();
    }
    
    /**
     * 
     * @param id
     * @return 
     */
    public String getErrorMessage(String id) {
    
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        final Iterator<FacesMessage> iterator = facesContext.getMessages(id);
        
        if (iterator.hasNext()) {
            return this.messages.getMessage(iterator.next().getDetail());
        }
        return "";
    }
}
