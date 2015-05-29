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

import br.com.webbudget.application.controller.miscellany.PeriodDetailsDTO;
import br.com.webbudget.domain.components.MovementsCalculator;
import br.com.webbudget.domain.entity.card.CardType;
import br.com.webbudget.domain.entity.movement.FinancialPeriod;
import br.com.webbudget.domain.entity.movement.Movement;
import br.com.webbudget.domain.entity.movement.MovementClass;
import br.com.webbudget.domain.entity.movement.MovementClassType;
import br.com.webbudget.domain.repository.movement.IFinancialPeriodRepository;
import br.com.webbudget.domain.repository.movement.IMovementRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.2.0
 * @since 1.0.0, 20/03/2014
 */
public class FinancialPeriodService {

    @Inject
    private MovementsCalculator movementsCalculator;

    @Inject
    private IMovementRepository movementRepository;
    @Inject
    private IFinancialPeriodRepository financialPeriodRepository;

    /**
     *
     * @param financialPeriod
     */
    @Transactional
    public void openPeriod(FinancialPeriod financialPeriod) {

        final FinancialPeriod found = this.findFinancialPeriodByIdentification(
                financialPeriod.getIdentification());

        if (found != null && !found.equals(financialPeriod)) {
//            throw new ApplicationException("financial-period.validate.duplicated");
        }

        // validamos se o periodo informado já foi contemplado em outro 
        // periodo existente
        final List<FinancialPeriod> periods = this.listFinancialPeriods(null);

        for (FinancialPeriod fp : periods) {
            if (financialPeriod.getStart().compareTo(fp.getEnd()) <= 0) {
//                throw new ApplicationException("financial-period.validate.truncated-dates");
            }
        }
        
        // se o fim for o mesmo dia ou anterior a data atual, erro!
        if (financialPeriod.getEnd().compareTo(LocalDate.now()) < 1) {
//            throw new ApplicationException("financial-period.validate.invalid-end");
        }

        this.financialPeriodRepository.save(financialPeriod);
    }
    
    /**
     * 
     * @param financialPeriod 
     */
    @Transactional
    public void deletePeriod(FinancialPeriod financialPeriod) {
        
        final List<Movement> movements = 
                this.movementRepository.listByPeriod(financialPeriod);
        
        // se houver movimentos, lanca o erro
        if (movements != null && !movements.isEmpty()) {
//            throw new ApplicationException("financial-period.validate.has-movements");
        } 
        
        // nao tem movimentos entao deleta
        this.financialPeriodRepository.delete(financialPeriod);
    }

    /**
     *
     * @return
     */
    @Transactional
    public FinancialPeriod findActiveFinancialPeriod() {

        final List<FinancialPeriod> financialPeriods = this.financialPeriodRepository.listOpen();

        FinancialPeriod activePeriod = null;

        for (FinancialPeriod financialPeriod : financialPeriods) {
            if (!financialPeriod.isExpired()) {
                activePeriod = financialPeriod;
                break;
            } else {
                activePeriod = financialPeriod;
            }
        }

        return activePeriod;
    }

    /**
     *
     * @param periodId
     * @return
     */
    @Transactional
    public PeriodDetailsDTO previewPeriod(long periodId) {

        final PeriodDetailsDTO periodDetailsDTO = new PeriodDetailsDTO();

        // buscamos o periodo
        final FinancialPeriod financialPeriod
                = this.financialPeriodRepository.findById(periodId, false);

        // calculamos os saldos
        final BigDecimal revenuesTotal = this.movementsCalculator.
                calculateTotalByDirection(financialPeriod, MovementClassType.IN);
        final BigDecimal expensesTotal = this.movementsCalculator.
                calculateTotalByDirection(financialPeriod, MovementClassType.OUT);

        // pegamos os totais de consumo por tipo de cartao
        final BigDecimal debitCardExpenses = this.movementsCalculator.
                calculateCardExpenses(financialPeriod, CardType.DEBIT);
        final BigDecimal creditCardExpenses = this.movementsCalculator.
                calculateCardExpenses(financialPeriod, CardType.CREDIT);

        final List<MovementClass> revenueClasses = this.movementsCalculator
                .calculateTotalByMovementClass(financialPeriod, MovementClassType.IN);
        final List<MovementClass> expensesClasses = this.movementsCalculator
                .calculateTotalByMovementClass(financialPeriod, MovementClassType.OUT);

        // preenchemos os detalhes
        periodDetailsDTO.setFinancialPeriod(financialPeriod);

        periodDetailsDTO.setExpenses(expensesTotal);
        periodDetailsDTO.setRevenues(revenuesTotal);

        periodDetailsDTO.setDebitCardExpenses(debitCardExpenses);
        periodDetailsDTO.setCreditCardExpenses(creditCardExpenses);

        periodDetailsDTO.setRevenueClasses(revenueClasses);
        periodDetailsDTO.setExpenseClasses(expensesClasses);

        periodDetailsDTO.sortMovementClasses();

        return periodDetailsDTO;
    }

    /**
     *
     * @param financialPeriodId
     * @return
     */
    @Transactional
    public FinancialPeriod findFinancialPeriodById(long financialPeriodId) {
        return this.financialPeriodRepository.findById(financialPeriodId, false);
    }

    /**
     *
     * @param identification
     * @return
     */
    @Transactional
    public FinancialPeriod findFinancialPeriodByIdentification(String identification) {
        return this.financialPeriodRepository.findByIdentification(identification);
    }

    /**
     *
     * @param isClosed
     * @return
     */
    @Transactional
    public List<FinancialPeriod> listFinancialPeriods(Boolean isClosed) {
        return this.financialPeriodRepository.listAll();
    }

    /**
     *
     * @return
     */
    @Transactional
    public List<FinancialPeriod> listOpenFinancialPeriods() {
        return this.financialPeriodRepository.listOpen();
    }
}
