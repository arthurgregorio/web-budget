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

import br.com.webbudget.domain.misc.MovementsCalculator1;
import br.com.webbudget.domain.entity.card.Card;
import br.com.webbudget.domain.entity.card.CardType;
import br.com.webbudget.domain.entity.closing.Closing;
import br.com.webbudget.domain.entity.movement.FinancialPeriod;
import br.com.webbudget.domain.entity.movement.Movement;
import br.com.webbudget.domain.entity.movement.MovementClassType;
import br.com.webbudget.domain.entity.movement.MovementStateType;
import br.com.webbudget.domain.misc.events.PeriodClosed;
import br.com.webbudget.domain.repository.card.ICardRepository;
import br.com.webbudget.domain.repository.movement.IClosingRepository;
import br.com.webbudget.domain.repository.movement.IFinancialPeriodRepository;
import br.com.webbudget.domain.repository.movement.IMovementRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 * Classe que realiza todo o processo de fechamento do periodo
 *
 * @author Arthur Gregorio
 *
 * @version 1.1.0
 * @since 1.0.0, 09/04/2014
 */
@ApplicationScoped
public class ClosingService {

    @Inject
    private MovementsCalculator1 movementsCalculator;

    @Inject
    private ICardRepository cardRepository;
    @Inject
    private IClosingRepository closingRepository;
    @Inject
    private IMovementRepository movementRepository;
    @Inject
    private IFinancialPeriodRepository financialPeriodRepository;
    
    @Inject
    @PeriodClosed
    private Event<FinancialPeriod> periodClosedEvent;

    /**
     * Processa o fechamento do mes, verificando por incosistencias de
     * movimentos
     *
     * @param financialPeriod o periodo a ser processado
     * @return o resumo do fechamento
     */
    @Transactional
    public Closing process(FinancialPeriod financialPeriod) {

        final Closing closing = new Closing();

        // verificamos por cartoes de credito com debitos sem fatura
        final List<Card> cards = this.cardRepository.listByStatus(false);

        for (Card card : cards) {
            if (card.getCardType() != CardType.DEBIT) {

                final List<Movement> movements = this.movementRepository
                        .listPaidWithoutInvoiceByPeriodAndCard(financialPeriod, card);

                if (!movements.isEmpty()) {
                    closing.setMovementsWithoutInvoice(true);
                    break;
                }
            }
        }

        // atualizamos a lista de movimentos em aberto
        final List<Movement> openMovements = this.movementRepository
                .listByPeriodAndState(financialPeriod, MovementStateType.OPEN);

        closing.setOpenMovements(openMovements);

        return closing;
    }

    /**
     * Encerra um periodo, gerando toda a movimentacao necessaria e calculando
     * os valores de receitas e despesas
     *
     * @param financialPeriod
     */
    @Transactional
    public void close(FinancialPeriod financialPeriod) {

        // calculamos os saldos
        final BigDecimal revenuesTotal = this.movementsCalculator.
                calculateTotalByDirection(financialPeriod, MovementClassType.IN);
        final BigDecimal expensesTotal = this.movementsCalculator.
                calculateTotalByDirection(financialPeriod, MovementClassType.OUT);

        // calculamos o saldo final
        final BigDecimal balance = revenuesTotal.subtract(expensesTotal);

        // pegamos os totais de consumo por tipo de cartao
        final BigDecimal debitCardExpenses = this.movementsCalculator.
                calculateCardExpenses(financialPeriod, CardType.DEBIT);
        final BigDecimal creditCardExpenses = this.movementsCalculator.
                calculateCardExpenses(financialPeriod, CardType.CREDIT);

        // pegamos tudo que foi paga de movimento e entao alteramos o status
        // para calculado a fim de manter tudo inalterado
        final List<Movement> movements = this.movementRepository
                .listByPeriodAndState(financialPeriod, MovementStateType.PAID);
        
        movements.stream().map((movement) -> {
            movement.setMovementStateType(MovementStateType.CALCULATED);
            return movement;
        }).forEach((movement) -> {
            this.movementRepository.save(movement);
        });
        
        // criamos e salvamos o fechamento
        Closing closing = new Closing();

        closing.setClosingDate(new Date());

        closing.setBalance(balance);
        closing.setRevenues(revenuesTotal);
        closing.setExpenses(expensesTotal);
        closing.setDebitCardExpenses(debitCardExpenses);
        closing.setCreditCardExpenses(creditCardExpenses);

        closing = this.closingRepository.save(closing);

        // atualizamos o período para encerrado
        financialPeriod.setClosed(true);
        financialPeriod.setClosing(closing);

        final FinancialPeriod closed = 
                this.financialPeriodRepository.save(financialPeriod);
        
        // dispara o evento notificando quem precisar
        this.periodClosedEvent.fire(closed);
    }
    
    /**
     * @return todos os fechamentos ja realizados
     */
    public List<Closing> listAll() {
        return this.closingRepository.listAll();
    }
}
