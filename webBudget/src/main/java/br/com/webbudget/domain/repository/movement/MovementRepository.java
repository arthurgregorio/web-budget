package br.com.webbudget.domain.repository.movement;

import br.com.webbudget.domain.entity.card.Card;
import br.com.webbudget.domain.entity.card.CardInvoice;
import br.com.webbudget.domain.entity.movement.CostCenter;
import br.com.webbudget.domain.entity.movement.FinancialPeriod;
import br.com.webbudget.domain.entity.movement.Movement;
import br.com.webbudget.domain.entity.movement.MovementClass;
import br.com.webbudget.domain.entity.movement.MovementClassType;
import br.com.webbudget.domain.entity.movement.MovementStateType;
import br.com.webbudget.domain.entity.movement.MovementType;
import br.com.webbudget.domain.entity.movement.PaymentMethodType;
import br.com.webbudget.domain.entity.wallet.Wallet;
import br.com.webbudget.domain.repository.GenericRepository;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 18/10/2013
 */
@Repository
public class MovementRepository extends GenericRepository<Movement, Long> implements IMovementRepository {

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
     * @return 
     */
    @Override
    public List<Movement> listInsByActiveFinancialPeriod() {
        
        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());
        
        criteria.createAlias("movementClass", "mc");
        criteria.add(Restrictions.eq("mc.movementClassType", MovementClassType.IN));
        
        criteria.createAlias("financialPeriod", "fp");
        criteria.add(Restrictions.eq("fp.closed", false));
        criteria.add(Restrictions.isNull("fp.closing"));
        
        criteria.addOrder(Order.desc("inclusion"));
        
        return criteria.list();
    }
    
    /**
     * 
     * @return 
     */
    @Override
    public List<Movement> listOutsByActiveFinancialPeriod() {
        
        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());

        criteria.add(Restrictions.eq("movementType", MovementType.MOVEMENT));
        
        criteria.createAlias("movementClass", "mc");
        criteria.add(Restrictions.eq("mc.movementClassType", MovementClassType.OUT));
        
        criteria.createAlias("financialPeriod", "fp");
        criteria.add(Restrictions.eq("fp.closed", false));
        criteria.add(Restrictions.isNull("fp.closing"));
        
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
        
        criteria.add(Restrictions.eq("cardInvoice", cardInvoice.getIdentification()));
       
        return criteria.list();
    }
    
    /**
     * 
     * @param filter
     * @param paid
     * @return 
     */
    @Override
    public List<Movement> listByFilter(String filter, Boolean paid) {
        
        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());

        if (paid != null && paid.equals(Boolean.TRUE)) {
            criteria.add(Restrictions.isNotNull("payment"));
        } 
        
        criteria.createAlias("movementClass", "mc");
        criteria.createAlias("mc.costCenter", "cc");
        criteria.createAlias("financialPeriod", "fp");
        
        // se conseguir castar para bigdecimal trata como um filtro
        try {
            final BigDecimal value = new BigDecimal(filter);
            criteria.add(Restrictions.or(Restrictions.eq("code", filter),
                Restrictions.eq("value", value),
                Restrictions.ilike("description", filter + "%"),
                Restrictions.ilike("mc.name", filter + "%"),
                Restrictions.ilike("cc.name", filter),
                Restrictions.ilike("fp.identification", filter + "%")));
        } catch (NumberFormatException ex) { 
            criteria.add(Restrictions.or(Restrictions.eq("code", filter),
                Restrictions.ilike("description", filter + "%"),
                Restrictions.ilike("mc.name", filter + "%"),
                Restrictions.ilike("cc.name", filter),
                Restrictions.ilike("fp.identification", filter + "%")));
        }
        
        criteria.addOrder(Order.desc("inclusion"));
        
        return criteria.list();        
    }
    
    /**
     * 
     * @param dueDate
     * @param showOverdue
     * @return 
     */
    @Override
    public List<Movement> listByDueDate(Date dueDate, boolean showOverdue) {
       
        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());
        
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
     * @return 
     */
    @Override
    public List<Movement> listByFinancialPeriod(FinancialPeriod financialPeriod) {
       
        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());
        
        criteria.createAlias("financialPeriod", "fp");
        criteria.add(Restrictions.eq("fp.id", financialPeriod.getId()));
        
        return criteria.list();
    }

    /**
     * 
     * @param financialPeriod
     * @return 
     */
    @Override
    public List<Movement> listInsByFinancialPeriod(FinancialPeriod financialPeriod) {
        
        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());
        
        criteria.createAlias("financialPeriod", "fp");
        criteria.add(Restrictions.eq("fp.id", financialPeriod.getId()));
        
        criteria.createAlias("movementClass", "mc");
        criteria.add(Restrictions.eq("mc.movementClassType", MovementClassType.IN));
        
        criteria.add(Restrictions.eq("movementType", MovementType.MOVEMENT));
        
        return criteria.list();
    }
    
    /**
     * 
     * @param financialPeriod
     * @return 
     */
    @Override
    public List<Movement> listOutsByFinancialPeriod(FinancialPeriod financialPeriod) {
        
        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());
        
        criteria.createAlias("financialPeriod", "fp");
        criteria.add(Restrictions.eq("fp.id", financialPeriod.getId()));
        
        criteria.createAlias("movementClass", "mc");
        criteria.add(Restrictions.eq("mc.movementClassType", MovementClassType.OUT));
        
        criteria.add(Restrictions.eq("movementType", MovementType.MOVEMENT));
        
        return criteria.list();
    }

    /**
     * 
     * @param financialPeriod
     * @return 
     */
    @Override
    public List<Movement> listOpenMovementsByPeriod(FinancialPeriod financialPeriod) {

        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());
        
        criteria.createAlias("financialPeriod", "fp");
        criteria.add(Restrictions.eq("fp.id", financialPeriod.getId()));
        
        criteria.add(Restrictions.eq("movementStateType", MovementStateType.OPEN));
        
        return criteria.list();
    }
    
    /**
     * 
     * @param period
     * @param costCenter
     * @return 
     */
    @Override
    public List<Movement> listByPeriodAndCostCenter(FinancialPeriod period, CostCenter costCenter) {
       
        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());

        if (period != null) {
            criteria.createAlias("financialPeriod", "fp");
            criteria.add(Restrictions.eq("fp.id", period.getId()));
        }

        if (costCenter != null) {
            criteria.createAlias("movementClass", "mc").createAlias("mc.costCenter", "cc");
            criteria.add(Restrictions.eq("cc.id", costCenter.getId()));
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
    public List<Movement> listByPeriodAndCard(FinancialPeriod financialPeriod, Card card) {
        
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
     * @param wallet
     * @param financialPeriod
     * @return 
     */
    @Override
    public BigDecimal findTotalOutsByWalletOnDebitCards(Wallet wallet, FinancialPeriod financialPeriod) {
        
        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());

        criteria.setProjection(Projections.sum("value"));
        
        criteria.add(Restrictions.eq("movementStateType", MovementStateType.PAID));
        
        criteria.createAlias("financialPeriod", "fp");
        criteria.add(Restrictions.eq("fp.id", financialPeriod.getId()));
        
        criteria.createAlias("payment", "py").createAlias("py.card", "cd")
                .createAlias("cd.wallet", "cwl");
        criteria.add(Restrictions.eq("py.paymentMethodType", PaymentMethodType.DEBIT_CARD));
        criteria.add(Restrictions.eq("cwl.id", wallet.getId()));
        
        criteria.createAlias("movementClass", "mc");
        criteria.add(Restrictions.eq("mc.movementClassType", MovementClassType.OUT));
        
        return (BigDecimal) criteria.uniqueResult(); 
    }
    
    /**
     * 
     * @param financialPeriod
     * @param movementClass
     * @return 
     */
    @Override
    public BigDecimal countMovementTotalByClassAndPeriod(FinancialPeriod financialPeriod, MovementClass movementClass) {
    
        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());

        criteria.setProjection(Projections.sum("value"));
        
        criteria.add(Restrictions.eq("movementClass", movementClass));
        criteria.add(Restrictions.eq("financialPeriod", financialPeriod));
        
        criteria.add(Restrictions.or(
                Restrictions.eq("movementStateType", MovementStateType.PAID),
                Restrictions.eq("movementStateType", MovementStateType.OPEN)));
        
        criteria.add(Restrictions.eq("movementType", MovementType.MOVEMENT));
        
        return (BigDecimal) criteria.uniqueResult(); 
    }
    
    /**
     * 
     * @param wallet
     * @param movementClassType
     * @param financialPeriod
     * @return 
     */
    @Override
    public BigDecimal findTotalByWallet(Wallet wallet, MovementClassType movementClassType, FinancialPeriod financialPeriod) {
        
        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());

        criteria.setProjection(Projections.sum("value"));
        
        criteria.add(Restrictions.eq("movementStateType", MovementStateType.PAID));
        
        criteria.createAlias("financialPeriod", "fp");
        criteria.add(Restrictions.eq("fp.id", financialPeriod.getId()));
        
        criteria.createAlias("payment", "py").createAlias("py.wallet", "wl");
        criteria.add(Restrictions.eq("wl.id", wallet.getId()));
        
        criteria.createAlias("movementClass", "mc");
        criteria.add(Restrictions.eq("mc.movementClassType", movementClassType));
        
        return (BigDecimal) criteria.uniqueResult(); 
    }
}
