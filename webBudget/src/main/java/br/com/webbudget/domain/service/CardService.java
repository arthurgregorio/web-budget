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

package br.com.webbudget.domain.service;

import br.com.webbudget.application.exceptions.ApplicationException;
import br.com.webbudget.domain.entity.card.Card;
import br.com.webbudget.domain.entity.card.CardInvoice;
import br.com.webbudget.domain.entity.card.CardType;
import br.com.webbudget.domain.entity.movement.Movement;
import br.com.webbudget.domain.entity.movement.MovementStateType;
import br.com.webbudget.domain.entity.movement.MovementType;
import br.com.webbudget.domain.entity.movement.Payment;
import br.com.webbudget.domain.entity.movement.PaymentMethodType;
import br.com.webbudget.domain.repository.card.ICardInvoiceRepository;
import br.com.webbudget.domain.repository.card.ICardRepository;
import br.com.webbudget.domain.repository.movement.IMovementRepository;
import br.com.webbudget.domain.repository.movement.IPaymentRepository;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 06/04/2014
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CardService {

    @Autowired
    private ICardRepository cardRepository;
    @Autowired
    private IPaymentRepository paymentRepository;
    @Autowired
    private IMovementRepository movementRepository;
    @Autowired
    private ICardInvoiceRepository cardInvoiceRepository;
    
    /**
     * 
     * @param card 
     */
    public void saveCard(Card card) {
        
        final Card found = this.findCardByNumberAndType(card.getNumber(), 
                card.getCardType());

        if (found != null) {
            throw new ApplicationException("card.validate.duplicated");
        }
        
        if (card.getCardType() == CardType.DEBIT && card.getWallet() == null) {
            throw new ApplicationException("card.validate.no-debit-wallet");
        }

        this.cardRepository.save(card);
    }
    
    /**
     * 
     * @param card
     * @return 
     */
    public Card updateCard(Card card) {
        
        final Card found = this.findCardByNumberAndType(card.getNumber(), 
                card.getCardType());

        if (found != null && !found.equals(card)) {
            throw new ApplicationException("card.validate.duplicated");
        }
        
        if (card.getCardType() == CardType.DEBIT && card.getWallet() == null) {
            throw new ApplicationException("card.validate.no-debit-wallet");
        }
        
        return this.cardRepository.save(card);
    }
    
    /**
     * 
     * @param card 
     */
    public void deleteCard(Card card) {
        this.cardRepository.delete(card);
    }
    
    /**
     * 
     * @param cardInvoice 
     */
    public void payInvoice(CardInvoice cardInvoice) {

        BigDecimal total = BigDecimal.ZERO;
        
        for (Movement movement : cardInvoice.getMovements()) {
            
            // marca que ja foi incluido em fatura e diz qual 
            movement.setCardInvoicePaid(true);
            movement.setCardInvoice(cardInvoice.getIdentification());
            
            // soma o valor para compor o valor da fatura
            total = total.add(movement.getValue());
            
            // atualizamos o movimento
            this.movementRepository.save(movement);
        }

        cardInvoice.setValue(total);
        
        // cria o movimento para debitar da conta onde a fatura sera paga
        Movement movement = new Movement();
        
        // identificacao da fatura
        movement.setDescription(cardInvoice.getIdentification() + " - "  
                + cardInvoice.getCard().getReadableName());
        
        // pegamos o dia de vencimento do cartao e setamos a conta para a data
        int dueDate = cardInvoice.getCard().getExpirationDay();

        if (dueDate != 0) {
            
            final Calendar periodCompentence = Calendar.getInstance();
            
            periodCompentence.setTime(cardInvoice.getFinancialPeriod().getEnd());

            final Calendar calendar = Calendar.getInstance();
            
            calendar.set(calendar.get(Calendar.YEAR), 
                    periodCompentence.get(Calendar.MONTH) + 1, dueDate);
            
            movement.setDueDate(calendar.getTime());
        } else {
            movement.setDueDate(cardInvoice.getFinancialPeriod().getEnd());
        }
        
        
//      movement.setMovementClass(cardInvoice.getMovementClass()); // FIXME arrumar quando o rateio estiver OK!
        movement.setFinancialPeriod(cardInvoice.getFinancialPeriod());
        movement.setMovementStateType(MovementStateType.PAID);
        movement.setValue(cardInvoice.getValue());
        movement.setMovementType(MovementType.CARD_INVOICE);
        
        // cria o pagamento
        Payment payment = new Payment();
        
        payment.setPaymentDate(new Date());
        payment.setPaymentMethodType(PaymentMethodType.IN_CASH);
        payment.setWallet(cardInvoice.getWallet());
        
        // salvamos a fatura
        cardInvoice  = this.cardInvoiceRepository.save(cardInvoice);
        
        // salvamos pagamento
        payment = this.paymentRepository.save(payment);

        // salva o movimento pago
        movement.setPayment(payment);
        movement = this.movementRepository.save(movement);
        
        // atualizamos a fatura com o movimento correspondente pago
        cardInvoice.setMovement(movement);
        this.cardInvoiceRepository.save(cardInvoice);
    }
    
    /**
     * 
     * @param cardId
     * @return 
     */
    @Transactional(readOnly = true)
    public Card findCardById(long cardId) {
        return this.cardRepository.findById(cardId, false);
    }
    
    /**
     * 
     * @param isBlocked
     * @return 
     */
    @Transactional(readOnly = true)
    public List<Card> listCards(Boolean isBlocked) {
        return this.cardRepository.listByStatus(isBlocked);
    }
    
    /**
     * 
     * @param isBlocked
     * @return 
     */
    @Transactional(readOnly = true)
    public List<Card> listCreditCards(Boolean isBlocked) {
        return this.cardRepository.listCredit(isBlocked);
    }
    
    /**
     * 
     * @param isBlocked
     * @return 
     */
    @Transactional(readOnly = true)
    public List<Card> listDebitCards(Boolean isBlocked) {
        return this.cardRepository.listDebit(isBlocked);
    }
    
    /**
     * 
     * @param number
     * @param cardType
     * @return 
     */
    @Transactional(readOnly = true)
    public Card findCardByNumberAndType(String number, CardType cardType) {
        return this.cardRepository.findByNumberAndType(number, cardType);
    }
    
    /**
     * 
     * @param cardInvoice
     * @return 
     */
    @Transactional(readOnly = true)
    public CardInvoice fillCardInvoice(CardInvoice cardInvoice) {
        
        final List<Movement> movements = this.movementRepository.listByPeriodAndCard(
                cardInvoice.getFinancialPeriod(), cardInvoice.getCard());
        
        cardInvoice.setMovements(movements);
        
        return cardInvoice;
    }
}
