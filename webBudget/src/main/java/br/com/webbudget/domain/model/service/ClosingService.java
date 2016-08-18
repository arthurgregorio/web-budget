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
package br.com.webbudget.domain.model.service;

import br.com.webbudget.domain.misc.MovementCalculator;
import br.com.webbudget.domain.model.entity.entries.Card;
import br.com.webbudget.domain.model.entity.miscellany.Closing;
import br.com.webbudget.domain.model.entity.miscellany.FinancialPeriod;
import br.com.webbudget.domain.model.entity.financial.Movement;
import br.com.webbudget.domain.model.entity.financial.MovementStateType;
import br.com.webbudget.domain.misc.events.PeriodClosed;
import br.com.webbudget.domain.misc.ex.InternalServiceError;
import br.com.webbudget.domain.model.repository.entries.ICardRepository;
import br.com.webbudget.domain.model.repository.miscellany.IClosingRepository;
import br.com.webbudget.domain.model.repository.miscellany.IFinancialPeriodRepository;
import br.com.webbudget.domain.model.repository.financial.IMovementRepository;
import java.math.BigDecimal;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 * Classe que realiza o processo de fechamento do periodo
 *
 * @author Arthur Gregorio
 *
 * @version 1.1.0
 * @since 1.0.0, 09/04/2014
 */
@ApplicationScoped
public class ClosingService {

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
     * @param period o periodo a ser processado
     * @return o resumo do fechamento
     */
    @Transactional
    public List<Movement> process(FinancialPeriod period) {

        if (period == null) {
            throw new InternalServiceError("error.closing.no-period");
        }
        
        // verificamos por cartoes de credito com debitos sem fatura
        final List<Card> cards = this.cardRepository.listByStatus(null);

        // se temos movimentos de cartao de credito que nao foram incluidos em
        // uma fatura do periodo
        cards.stream()
                .filter(Card::isCreditCard)
                .forEach(card -> {
                    final List<Movement> movements = this.movementRepository
                            .listPaidWithoutInvoiceByPeriodAndCard(period, card);
                    if (!movements.isEmpty()) {
                        throw new InternalServiceError(
                                "error.closing.movements-no-invoice");
                    }
                });

        // checamos se existem movimentos em aberto
        final List<Movement> movements = this.movementRepository
                .listByPeriodAndState(period, MovementStateType.OPEN);

        if (!movements.isEmpty()) {
            throw new InternalServiceError(
                    "error.closing.open-movements", movements.size());
        }

        return this.movementRepository.listByPeriod(period);
    }

    /**
     * Realiza o encerramento do periodo informado
     * 
     * @param financialPeriod o periodo a ser encerrado
     * @param calculator a calculadora com os movimentos a serem processados
     */
    @Transactional
    public void close(FinancialPeriod financialPeriod, MovementCalculator calculator) {

        // criamos e salvamos o fechamento
        Closing closing = new Closing();

        closing.setBalance(calculator.getBalance());
        closing.setRevenues(calculator.getRevenuesTotal());
        closing.setExpenses(calculator.getExpensesTotal());
        closing.setDebitCardExpenses(calculator.getTotalPaidOnDebitCard());
        closing.setCreditCardExpenses(calculator.getTotalPaidOnCreditCard());
        
        final BigDecimal accumulated = 
                this.closingRepository.findLastAccumulated();
                
        // se nao tem acumulado, poe o saldo no lugar
        if (accumulated != null) {
            closing.setAccumulated(accumulated.add(closing.getBalance()));
        } else {
            closing.setAccumulated(closing.getBalance());
        }

        closing = this.closingRepository.save(closing);        
        
        // atualiza o status dos movimentos
        calculator.getMovements().stream()
                .map(movement -> { 
                    movement.setMovementStateType(MovementStateType.CALCULATED);
                    return movement;
                }).forEach(movement -> {
                    this.movementRepository.save(movement);
                });

        // atualizamos o periodo para encerrado
        financialPeriod.setClosed(true);
        financialPeriod.setClosing(closing);

        // salva e dispara o evento informando que o periodo foi encerrado
        this.periodClosedEvent.fire(
                this.financialPeriodRepository.save(financialPeriod));
    }

    /**
     * @return todos os fechamentos ja realizados
     */
    public List<Closing> listAll() {
        return this.closingRepository.listAll();
    }
}
