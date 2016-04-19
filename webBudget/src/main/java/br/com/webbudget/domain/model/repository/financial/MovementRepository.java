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
import br.com.webbudget.application.component.table.PageRequest.SortDirection;
import br.com.webbudget.domain.model.repository.GenericRepository;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.1.0
 * @since 1.0.0, 18/10/2013
 */
public class MovementRepository extends GenericRepository<Movement, Long>
        implements IMovementRepository {

    /**
     * 
     * @param movementCode
     * @return 
     */
    @Override
    public Movement findByCode(String movementCode) {
        
        final Criteria criteria = this.createCriteria();

        criteria.add(Restrictions.eq("code", movementCode));

        return (Movement) criteria.uniqueResult();
    }

    /**
     *
     * @return
     */
    @Override
    public List<Movement> listByActiveFinancialPeriod() {

        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());

        criteria.createAlias("financialPeriod", "fp");
        criteria.add(Restrictions.eq("fp.closed", false));
        criteria.add(Restrictions.isNull("fp.closing"));

        criteria.addOrder(Order.desc("inclusion"));

        return criteria.list();
    }

    /**
     *
     * @param contact
     * @return
     */
    @Override
    public List<Movement> listByContact(Contact contact) {

        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());

        criteria.createAlias("contact", "ct");
        criteria.add(Restrictions.eq("ct.id", contact.getId()));

        criteria.addOrder(Order.desc("inclusion"));

        return criteria.list();
    }

    /**
     *
     * @param cardInvoice
     * @return
     */
    @Override
    public List<Movement> listByCardInvoice(CardInvoice cardInvoice) {

        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());

        criteria.createAlias("cardInvoice", "ci");
        criteria.add(Restrictions.eq("ci.id", cardInvoice.getId()));

        criteria.addOrder(Order.desc("inclusion"));

        return criteria.list();
    }

    /**
     *
     * @param filter
     * @param pageRequest
     * @return
     */
    @Override
    public Page<Movement> listByFilter(MovementFilter filter, PageRequest pageRequest) {

        final Criteria criteria = this.createCriteria();

        final List<Criterion> criterions = new ArrayList<>();

        criteria.createAlias("contact", "co", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("apportionments", "ap");
        criteria.createAlias("ap.movementClass", "mc");
        criteria.createAlias("ap.costCenter", "cc");
        criteria.createAlias("financialPeriod", "fp");

        // montramos os criterios de filtragem geral
        if (filter.hasCriteria()) {
            criterions.add(Restrictions.eq("code", filter.getCriteria()));
            criterions.add(Restrictions.ilike(
                    "description", "%" + filter.getCriteria() + "%"));
            criterions.add(Restrictions.ilike(
                    "mc.name", "%" + filter.getCriteria() + "%"));
            criterions.add(Restrictions.ilike(
                    "cc.name", "%" + filter.getCriteria() + "%"));
            criterions.add(Restrictions.ilike(
                    "co.name", "%" + filter.getCriteria() + "%"));

            // se conseguir castar para bigdecimal trata como um filtro
            try {
                criterions.add(Restrictions.eq(
                        "value", filter.criteriaToBigDecimal()));
            } catch (ParseException ex) { }
        }
        
        criteria.add(Restrictions.or(criterions.toArray(new Criterion[]{})));
        criteria.add(Restrictions.and(filter.getCustomFilters()));

        // projetamos para pegar o total de paginas possiveis
        criteria.setProjection(Projections.count("id"));

        final Long totalRows = (Long) criteria.uniqueResult();

        // limpamos a projection para que a criteria seja reusada
        criteria.setProjection(null);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        // paginamos
        criteria.setFirstResult(pageRequest.getFirstResult());
        criteria.setMaxResults(pageRequest.getPageSize());

        // aplica o multisort dos campos
        if (pageRequest.isMultiSort()) {
            pageRequest
                    .getMultiSortFields()
                    .stream()
                    .forEach(field -> {
                        if (field.getDirection() == SortDirection.ASC) {
                            criteria.addOrder(Order.asc(field.getSortField()));
                        } else if (field.getDirection() == SortDirection.DESC) {
                            criteria.addOrder(Order.desc(field.getSortField()));
                        }
                    });
        } else if (pageRequest.getSortDirection() == SortDirection.ASC) {
            criteria.addOrder(Order.asc(pageRequest.getSortField()));
        } else if (pageRequest.getSortDirection() == SortDirection.DESC) {
            criteria.addOrder(Order.desc(pageRequest.getSortField()));
        }

        // montamos o resultado paginado
        return new Page<>(criteria.list(), totalRows);
    }

    /**
     *
     * @param dueDate
     * @param showOverdue
     * @return
     */
    @Override
    public List<Movement> listByDueDate(LocalDate dueDate, boolean showOverdue) {

        final Criteria criteria = this.createCriteria();

        if (showOverdue) {
            criteria.add(Restrictions.le("dueDate", dueDate));
            criteria.add(Restrictions.eq("movementStateType", MovementStateType.OPEN));
        } else {
            criteria.add(Restrictions.eq("dueDate", dueDate));
        }

        criteria.addOrder(Order.asc("dueDate"));

        return criteria.list();
    }

    /**
     *
     * @param financialPeriod
     * @param type
     * @return
     */
    @Override
    public List<Movement> listByPeriodAndCardType(FinancialPeriod financialPeriod, CardType type) {

        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());

        criteria.createAlias("financialPeriod", "fp");
        criteria.add(Restrictions.eq("fp.id", financialPeriod.getId()));

        criteria.createAlias("payment", "py").createAlias("py.card", "cc");
        criteria.add(Restrictions.eq("cc.cardType", type));

        return criteria.list();
    }

    /**
     *
     * @param period
     * @param direction
     * @return
     */
    @Override
    public List<Movement> listByPeriodAndDirection(FinancialPeriod period, MovementClassType direction) {

        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());

        criteria.createAlias("financialPeriod", "fp");
        criteria.add(Restrictions.eq("fp.id", period.getId()));

        criteria.createAlias("apportionments", "ap");
        criteria.createAlias("ap.movementClass", "mc");
        criteria.add(Restrictions.eq("mc.movementClassType", direction));

        return criteria.list();
    }

    /**
     *
     * @param financialPeriod
     * @param state
     * @return
     */
    @Override
    public List<Movement> listByPeriodAndState(FinancialPeriod financialPeriod, MovementStateType state) {

        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());

        criteria.createAlias("financialPeriod", "fp");
        criteria.add(Restrictions.eq("fp.id", financialPeriod.getId()));

        if (state != null) {
            criteria.add(Restrictions.eq("movementStateType", state));
        }

        return criteria.list();
    }

    /**
     *
     * @param financialPeriod
     * @param card
     * @return
     */
    @Override
    public List<Movement> listPaidWithoutInvoiceByPeriodAndCard(FinancialPeriod financialPeriod, Card card) {

        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());

        criteria.add(Restrictions.isNull("cardInvoice"));
        criteria.add(Restrictions.eq("cardInvoicePaid", Boolean.FALSE));
        criteria.add(Restrictions.eq("movementStateType", MovementStateType.PAID));

        criteria.createAlias("financialPeriod", "fp");
        criteria.add(Restrictions.eq("fp.id", financialPeriod.getId()));

        criteria.createAlias("payment", "py").createAlias("py.card", "cc");
        criteria.add(Restrictions.eq("cc.id", card.getId()));

        return criteria.list();
    }

    /**
     *
     * @param period
     * @param movementClass
     * @return
     */
    @Override
    public BigDecimal countTotalByPeriodAndMovementClass(FinancialPeriod period, MovementClass movementClass) {

        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());

        criteria.createAlias("financialPeriod", "fp");
        criteria.add(Restrictions.eq("fp.id", period.getId()));

        criteria.createAlias("apportionments", "ap");
        criteria.createAlias("ap.movementClass", "mc");
        criteria.add(Restrictions.eq("mc.id", movementClass.getId()));

        criteria.setProjection(Projections.sum("value"));

        return (BigDecimal) criteria.uniqueResult();
    }

    /**
     *
     * @param period
     * @param state
     * @param type
     * @return
     */
    @Override
    public List<Movement> listByPeriodAndStateAndType(FinancialPeriod period, MovementStateType state, MovementType type) {
        return this.listByPeriodAndStateAndTypeAndDirection(period, state, type, null);
    }

    /**
     *
     * @param period
     * @param costCenter
     * @return
     */
    @Override
    public List<Movement> listByPeriodAndCostCenterAndDirection(FinancialPeriod period, CostCenter costCenter, MovementClassType direction) {

        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());

        criteria.createAlias("financialPeriod", "fp");
        criteria.add(Restrictions.eq("fp.id", period.getId()));

        criteria.createAlias("apportionments", "ap");
        criteria.createAlias("ap.movementClass", "mc");
        criteria.createAlias("mc.costCenter", "cc");

        criteria.add(Restrictions.eq("mc.movementClassType", direction));
        criteria.add(Restrictions.eq("cc.id", costCenter.getId()));

        return criteria.list();
    }

    /**
     *
     * @param period
     * @param state
     * @param type
     * @param direction
     * @return
     */
    @Override
    public List<Movement> listByPeriodAndStateAndTypeAndDirection(FinancialPeriod period,
            MovementStateType state, MovementType type, MovementClassType direction) {

        if (period == null) {
            throw new NullPointerException("Period can't be null");
        }
        
        final Criteria criteria = this.createCriteria();

        if (type != null) {
            criteria.add(Restrictions.eq("movementType", type));
        }

        if (state != null) {
            criteria.add(Restrictions.eq("movementStateType", state));
        }

        criteria.createAlias("financialPeriod", "fp");
        criteria.add(Restrictions.eq("fp.id", period.getId()));

        if (direction != null) {
            criteria.createAlias("apportionments", "ap");
            criteria.createAlias("ap.movementClass", "mc");
            criteria.add(Restrictions.eq("mc.movementClassType", direction));
        }

        return criteria.list();
    }
}
