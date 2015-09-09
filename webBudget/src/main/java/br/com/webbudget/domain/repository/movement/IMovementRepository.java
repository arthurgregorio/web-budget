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
import br.com.webbudget.domain.entity.card.CardType;
import br.com.webbudget.domain.entity.contact.Contact;
import br.com.webbudget.domain.entity.movement.CostCenter;
import br.com.webbudget.domain.entity.movement.FinancialPeriod;
import br.com.webbudget.domain.entity.movement.Movement;
import br.com.webbudget.domain.entity.movement.MovementClass;
import br.com.webbudget.domain.entity.movement.MovementClassType;
import br.com.webbudget.domain.entity.movement.MovementStateType;
import br.com.webbudget.domain.entity.movement.MovementType;
import br.com.webbudget.domain.misc.model.LazyLoaderAdapter;
import br.com.webbudget.domain.repository.IGenericRepository;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.1.0
 * @since 1.0.0, 04/03/2013
 */
public interface IMovementRepository 
        extends IGenericRepository<Movement, Long>, LazyLoaderAdapter<Movement> {

    /**
     *
     * @return
     */
    public List<Movement> listByActiveFinancialPeriod();
    
    /**
     * 
     * @param contact
     * @return 
     */
    public List<Movement> listByContact(Contact contact);

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
     * @param financialPeriod
     * @return 
     */
    public List<Movement> listByPeriod(FinancialPeriod financialPeriod);
    
    /**
     *
     * @param dueDate
     * @param showOverdue
     * @return
     */
    public List<Movement> listByDueDate(Date dueDate, boolean showOverdue);

    /**
     *
     * @param period
     * @param type
     * @return
     */
    public List<Movement> listByPeriodAndCardType(FinancialPeriod period, CardType type);

    /**
     *
     * @param period
     * @param direction
     * @return
     */
    public List<Movement> listByPeriodAndDirection(FinancialPeriod period, MovementClassType direction);

    /**
     *
     * @param period
     * @param state
     * @return
     */
    public List<Movement> listByPeriodAndState(FinancialPeriod period, MovementStateType state);

    /**
     *
     * @param period
     * @param card
     * @return
     */
    public List<Movement> listPaidWithoutInvoiceByPeriodAndCard(FinancialPeriod period, Card card);

    /**
     *
     * @param period
     * @param movementClass
     * @return
     */
    public BigDecimal countTotalByPeriodAndMovementClass(FinancialPeriod period, MovementClass movementClass);

    /**
     *
     * @param period
     * @param state
     * @param type
     * @return
     */
    public List<Movement> listByPeriodAndStateAndType(FinancialPeriod period, MovementStateType state, MovementType type);

    /**
     *
     * @param period
     * @param costCenter
     * @param direction
     * @return
     */
    public List<Movement> listByPeriodAndCostCenterAndDirection(FinancialPeriod period, CostCenter costCenter, MovementClassType direction);

    /**
     *
     * @param period
     * @param state
     * @param type
     * @param direction
     * @return
     */
    public List<Movement> listByPeriodAndStateAndTypeAndDirection(FinancialPeriod period, MovementStateType state, MovementType type, MovementClassType direction);
}
