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
package br.com.webbudget.domain.model.repository.financial;

import br.com.webbudget.domain.model.entity.entries.Card;
import br.com.webbudget.domain.model.entity.entries.CardInvoice;
import br.com.webbudget.domain.model.entity.entries.CardType;
import br.com.webbudget.domain.model.entity.entries.Contact;
import br.com.webbudget.domain.model.entity.entries.CostCenter;
import br.com.webbudget.domain.model.entity.miscellany.FinancialPeriod;
import br.com.webbudget.domain.model.entity.financial.Movement;
import br.com.webbudget.domain.model.entity.entries.MovementClass;
import br.com.webbudget.domain.model.entity.entries.MovementClassType;
import br.com.webbudget.domain.model.entity.financial.MovementStateType;
import br.com.webbudget.domain.model.entity.financial.MovementType;
import br.com.webbudget.domain.misc.filter.MovementFilter;
import br.com.webbudget.application.component.table.Page;
import br.com.webbudget.application.component.table.PageRequest;
import br.com.webbudget.domain.model.repository.IGenericRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.1.0
 * @since 1.0.0, 04/03/2013
 */
public interface IMovementRepository extends IGenericRepository<Movement, Long> {

    /**
     * 
     * @param movementCode
     * @return 
     */
    public Movement findByCode(String movementCode);
    
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
    public List<Movement> listByDueDate(LocalDate dueDate, boolean showOverdue);

    /**
     * 
     * @param filter
     * @param pageRequest
     * @return 
     */
    public Page<Movement> listByFilter(MovementFilter filter, PageRequest pageRequest);
    
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
     * @param direction
     * @return
     */
    public List<Movement> listByPeriodAndDirection(FinancialPeriod period, MovementClassType direction);
    
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
