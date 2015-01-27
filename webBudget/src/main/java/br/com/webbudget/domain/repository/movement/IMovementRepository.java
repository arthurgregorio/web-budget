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

package br.com.webbudget.domain.repository.movement;

import br.com.webbudget.domain.entity.card.Card;
import br.com.webbudget.domain.entity.card.CardInvoice;
import br.com.webbudget.domain.entity.movement.CostCenter;
import br.com.webbudget.domain.entity.movement.FinancialPeriod;
import br.com.webbudget.domain.entity.movement.Movement;
import br.com.webbudget.domain.entity.movement.MovementClass;
import br.com.webbudget.domain.entity.movement.MovementClassType;
import br.com.webbudget.domain.entity.wallet.Wallet;
import br.com.webbudget.domain.repository.IGenericRepository;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 04/03/2013
 */
public interface IMovementRepository extends IGenericRepository<Movement, Long> {

    /**
     * 
     * @return 
     */
    public List<Movement> listByActiveFinancialPeriod();
    
    /**
     * 
     * @return 
     */
    public List<Movement> listInsByActiveFinancialPeriod();
    
    /**
     * 
     * @return 
     */
    public List<Movement> listOutsByActiveFinancialPeriod();

    /**
     * 
     * @param filter
     * @param paid
     * @return 
     */
    public List<Movement> listByFilter(String filter, Boolean paid);

    /**
     * 
     * @param cardInvoice
     * @return 
     */
    public List<Movement> listByCardInvoice(CardInvoice cardInvoice);
    
    /**
     * 
     * @param dueDate
     * @param showOverdue
     * @return 
     */
    public List<Movement> listByDueDate(Date dueDate, boolean showOverdue);
    
    /**
     * 
     * @param financialPeriod
     * @return 
     */
    public List<Movement> listByFinancialPeriod(FinancialPeriod financialPeriod);
    
    /**
     * 
     * @param financialPeriod
     * @return 
     */
    public List<Movement> listInsByFinancialPeriod(FinancialPeriod financialPeriod);
    
    /**
     * 
     * @param financialPeriod
     * @return 
     */
    public List<Movement> listOutsByFinancialPeriod(FinancialPeriod financialPeriod);
    
    /**
     * 
     * @param financialPeriod
     * @return 
     */
    public List<Movement> listOpenMovementsByPeriod(FinancialPeriod financialPeriod);
    
    /**
     * 
     * @param financialPeriod
     * @param card
     * @return 
     */
    public List<Movement> listByPeriodAndCard(FinancialPeriod financialPeriod, Card card);

    /**
     * 
     * @param period
     * @param costCenter
     * @return 
     */
    public List<Movement> listByPeriodAndCostCenter(FinancialPeriod period, CostCenter costCenter);

    /**
     * 
     * @param wallet
     * @param financialPeriod
     * @return 
     */
    public BigDecimal findTotalOutsByWalletOnDebitCards(Wallet wallet, FinancialPeriod financialPeriod);
    
    /**
     * 
     * @param financialPeriod
     * @param movementClass
     * @return 
     */
    public BigDecimal countMovementTotalByClassAndPeriod(FinancialPeriod financialPeriod, MovementClass movementClass);
    
    /**
     * 
     * @param wallet
     * @param movementClassType
     * @param financialPeriod
     * @return 
     */
    public BigDecimal findTotalByWallet(Wallet wallet, MovementClassType movementClassType, FinancialPeriod financialPeriod);
}
