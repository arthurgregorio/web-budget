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
package br.com.webbudget.domain.services;

import br.com.webbudget.domain.entities.entries.Card;
import br.com.webbudget.domain.entities.entries.CardInvoice;
import br.com.webbudget.domain.entities.entries.CardType;
import br.com.webbudget.domain.entities.financial.Apportionment;
import br.com.webbudget.domain.entities.financial.Movement;
import br.com.webbudget.domain.entities.financial.MovementStateType;
import br.com.webbudget.domain.entities.financial.MovementType;
import br.com.webbudget.domain.entities.tools.Configuration;
import br.com.webbudget.domain.exceptions.ApplicationException;
import br.com.webbudget.application.components.table.Page;
import br.com.webbudget.application.components.table.PageRequest;
import br.com.webbudget.domain.repositories.entries.ICardInvoiceRepository;
import br.com.webbudget.domain.repositories.entries.ICardRepository;
import br.com.webbudget.domain.repositories.financial.IApportionmentRepository;
import br.com.webbudget.domain.repositories.financial.IMovementRepository;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import br.com.webbudget.domain.repositories.tools.ConfigurationRepository;

/**
 * Service responsavel por todas as operacoes que envolvem cartoes no sistema
 * desde a parte de fatura, cadastros e coisas do tipo
 *
 * @author Arthur Gregorio
 *
 * @version 1.2.0
 * @since 1.0.0, 06/04/2014
 */
@ApplicationScoped
public class CardService {

//    @Inject
//    private ICardRepository cardRepository;
//    @Inject
//    private IMovementRepository movementRepository;
//    @Inject
//    private ICardInvoiceRepository cardInvoiceRepository;
//    @Inject
//    private IApportionmentRepository apportionmentRepository;
//    @Inject
//    private IConfigurationRepository configurationRepository;
//
//    /**
//     *
//     * @param card
//     */
//    @Transactional
//    public void saveCard(Card card) {
//
//        final Card found = this.findCardByNumberAndType(card.getNumber(),
//                card.getCardType());
//
//        if (found != null) {
//            throw new InternalServiceError("error.card.duplicated");
//        }
//
//        if (card.getCardType() == CardType.DEBIT && card.getWallet() == null) {
//            throw new InternalServiceError("error.card.no-debit-wallet");
//        }
//
//        this.cardRepository.save(card);
//    }
//
//    /**
//     *
//     * @param card
//     * @return
//     */
//    @Transactional
//    public Card updateCard(Card card) {
//
//        final Card found = this.findCardByNumberAndType(card.getNumber(),
//                card.getCardType());
//
//        if (found != null && !found.equals(card)) {
//            throw new InternalServiceError("error.card.duplicated");
//        }
//
//        if (card.getCardType() == CardType.DEBIT && card.getWallet() == null) {
//            throw new InternalServiceError("error.card.no-debit-wallet");
//        }
//
//        return this.cardRepository.save(card);
//    }
//
//    /**
//     *
//     * @param card
//     */
//    @Transactional
//    public void deleteCard(Card card) {
//        this.cardRepository.delete(card);
//    }
//
//    /**
//     * Metodo que cria a movimentacao para a fatura referente ao cartao
//     *
//     * @param cardInvoice a fatura do cartao que sera transformada e movimento
//     */
//    @Transactional
//    public void createMovement(CardInvoice cardInvoice) {
//
//        final List<Movement> movements = cardInvoice.getMovements();
//
//        cardInvoice = this.cardInvoiceRepository.save(cardInvoice);
//
//        // atualizamos os movimentos vinculados
//        for (Movement movement : movements) {
//
//            // altera o movimento para atender ao pagamento
//            movement.setCardInvoicePaid(true);
//            movement.setCardInvoice(cardInvoice);
//
//            // salva ele
//            this.movementRepository.save(movement);
//        }
//
//        final Configuration config = this.configurationRepository.findDefault();
//
//        // checa se temos uma configuracao valida
//        if (!config.isValidForCardInvoice()) {
//            throw new InternalServiceError("error.card-invoice.no-default-config");
//        }
//        
//        // cria o movimento para aparecer no financeiro
//        Movement movement = new Movement();
//
//        // identificacao da fatura
//        movement.setDescription(cardInvoice.getIdentification()
//                + " ref: " + cardInvoice.getCard().getReadableName());
//
//        // pegamos o dia de vencimento do cartao e setamos a conta para a data
//        movement.setDueDate(cardInvoice.getInvoiceDueDate());
//
//        movement.setFinancialPeriod(cardInvoice.getFinancialPeriod());
//        movement.setMovementStateType(MovementStateType.OPEN);
//        movement.setValue(cardInvoice.getTotal());
//        movement.setMovementType(MovementType.CARD_INVOICE);
//
//        movement = this.movementRepository.save(movement);
//
//        // salvamos o rateio dela em 100% para o CC e MC configurado
//        final Apportionment apportionment = new Apportionment();
//
//        apportionment.setCostCenter(config.getInvoiceDefaultCostCenter());
//        apportionment.setMovementClass(config.getInvoiceDefaultMovementClass());
//        apportionment.setValue(cardInvoice.getTotal());
//        apportionment.setMovement(movement);
//
//        this.apportionmentRepository.save(apportionment);
//
//        // atualizamos a fatura com o movimento correspondente a ela
//        cardInvoice.setMovement(movement);
//        this.cardInvoiceRepository.save(cardInvoice);
//    }
//
//    /**
//     *
//     * @param cardId
//     * @return
//     */
//    public Card findCardById(long cardId) {
//        return this.cardRepository.findById(cardId, false);
//    }
//
//    /**
//     *
//     * @param isBlocked
//     * @return
//     */
//    public List<Card> listCards(Boolean isBlocked) {
//        return this.cardRepository.listByStatus(isBlocked);
//    }
//
//    /**
//     *
//     * @param isBlocked
//     * @param pageRequest
//     * @return
//     */
//    public Page<Card> listCards(Boolean isBlocked, PageRequest pageRequest) {
//        return this.cardRepository.listByStatus(isBlocked, pageRequest);
//    }
//
//    /**
//     *
//     * @param isBlocked
//     * @return
//     */
//    public List<Card> listCreditCards(Boolean isBlocked) {
//        return this.cardRepository.listCredit(isBlocked);
//    }
//
//    /**
//     *
//     * @param isBlocked
//     * @return
//     */
//    public List<Card> listDebitCards(Boolean isBlocked) {
//        return this.cardRepository.listDebit(isBlocked);
//    }
//
//    /**
//     *
//     * @param number
//     * @param cardType
//     * @return
//     */
//    public Card findCardByNumberAndType(String number, CardType cardType) {
//        return this.cardRepository.findByNumberAndType(number, cardType);
//    }
//
//    /**
//     *
//     * @param cardInvoice
//     * @return
//     */
//    public CardInvoice fillCardInvoice(CardInvoice cardInvoice) {
//
//        final List<Movement> movements = this.movementRepository
//                .listPaidWithoutInvoiceByPeriodAndCard(cardInvoice
//                        .getFinancialPeriod(), cardInvoice.getCard());
//
//        cardInvoice.setMovements(movements);
//
//        return cardInvoice;
//    }
//    
//    /**
//     * Lista a ultimas doze faturas de cartao filtrando pelo cartao indicado
//     * 
//     * @param card o cartao que servira de filtro
//     * @return as faturas
//     */
//    public List<CardInvoice> listInvoicesByCard(Card card) {
//        return this.cardInvoiceRepository.listByCard(card);
//    }
//
//    /**
//     * Lista todas as faturas de um determinado cartao recebido como parametro
//     *
//     * @param card o cartao da qual se deseja ver as faturas
//     * @param pageRequest a solicitacao da pagina
//     * @return a lista de faturas
//     */
//    public Page<CardInvoice> listInvoicesByCard(Card card, PageRequest pageRequest) {
//        return this.cardInvoiceRepository.listByCard(card, pageRequest);
//    }
}
