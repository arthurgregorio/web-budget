/*
 * Copyright (C) 2018 Arthur Gregorio, AG.Software
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
package br.com.webbudget.domain.repositories.financial;

import br.com.webbudget.application.components.ui.filter.PeriodMovementFilter;
import br.com.webbudget.application.components.ui.table.Page;
import br.com.webbudget.domain.entities.financial.Apportionment;
import br.com.webbudget.domain.entities.financial.Apportionment_;
import br.com.webbudget.domain.entities.financial.PeriodMovement;
import br.com.webbudget.domain.entities.financial.PeriodMovement_;
import br.com.webbudget.domain.entities.registration.*;
import br.com.webbudget.domain.repositories.DefaultRepository;
import org.apache.deltaspike.data.api.EntityGraph;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.Criteria;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * The {@link PeriodMovement} repository
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 04/12/2018
 */
@Repository
public interface PeriodMovementRepository extends DefaultRepository<PeriodMovement> {

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    @EntityGraph(value = "Movement.full")
    Optional<PeriodMovement> findById(Long id);

    /**
     * Find a {@link PeriodMovement} by the code
     *
     * @param movementCode used as a filter
     * @return an {@link Optional} of the {@link PeriodMovement}
     */
    @EntityGraph(value = "Movement.full")
    Optional<PeriodMovement> findByCode(String movementCode);

    /**
     * List all {@link PeriodMovement} by the {@link FinancialPeriod}
     *
     * @param period to be used as a filter
     * @return the {@link List} of {@link PeriodMovement} found
     */
    List<PeriodMovement> findByFinancialPeriod(FinancialPeriod period);

    /**
     * Calculate the total of paid or received {@link PeriodMovement} on a list of {@link FinancialPeriod}
     *
     * @return the total value of paid and received {@link PeriodMovement}
     */
    @Query("SELECT COALESCE(SUM(pm.paidValue), 0) " +
            "FROM PeriodMovement mv " +
            "INNER JOIN Payment pm ON pm.id = mv.payment.id " +
            "AND pm.paymentMethod <> 'CREDIT_CARD' " +
            "AND mv.periodMovementState <> 'OPEN'")
    BigDecimal calculateTotalPaidAndReceived();

    /**
     * Same as {@link #calculateTotalPaidAndReceived()} but filtering by {@link FinancialPeriod}
     *
     * @param periods the list of {@link FinancialPeriod} to search for
     * @return the total value of paid and received {@link PeriodMovement}
     */
    @Query("SELECT COALESCE(SUM(pm.paidValue), 0) " +
            "FROM PeriodMovement mv " +
            "INNER JOIN FinancialPeriod fp ON fp.id = mv.financialPeriod.id " +
            "INNER JOIN Payment pm ON pm.id = mv.payment.id " +
            "AND fp.id IN (?1) " +
            "AND pm.paymentMethod <> 'CREDIT_CARD' " +
            "AND mv.periodMovementState <> 'OPEN'")
    BigDecimal calculateTotalPaidAndReceived(List<Long> periods);

    /**
     * Calculate the total of open {@link PeriodMovement} on a list of {@link FinancialPeriod}
     *
     * @return the total value of open {@link PeriodMovement}
     */
    @Query("SELECT COALESCE(SUM(mv.value), 0) " +
            "FROM PeriodMovement mv " +
            "WHERE mv.periodMovementState = 'OPEN'")
    BigDecimal calculateTotalOpen();

    /**
     * Same as {@link #calculateTotalOpen()} but filtering by {@link FinancialPeriod}
     *
     * @param periods the list of {@link FinancialPeriod} to search for
     * @return the total value of open {@link PeriodMovement}
     */
    @Query("SELECT COALESCE(SUM(mv.value), 0) " +
            "FROM PeriodMovement mv " +
            "INNER JOIN FinancialPeriod fp ON fp.id = mv.financialPeriod.id " +
            "AND fp.id IN (?1) " +
            "AND mv.periodMovementState = 'OPEN'")
    BigDecimal calculateTotalOpen(List<Long> periods);

    /**
     * Calculate the total of expenses on a list of {@link FinancialPeriod}
     *
     * @return the total value of expenses
     */
    @Query("SELECT COALESCE(SUM(pm.paidValue), 0) " +
            "FROM PeriodMovement mv " +
            "INNER JOIN Payment pm ON pm.id = mv.payment.id " +
            "INNER JOIN Apportionment ap on ap.movement = mv.id " +
            "INNER JOIN MovementClass mc on mc.id = ap.movementClass.id " +
            "AND pm.paymentMethod <> 'CREDIT_CARD' " +
            "AND mc.movementClassType = 'EXPENSE' " +
            "AND mv.periodMovementState <> 'OPEN'")
    BigDecimal calculateTotalExpenses();

    /**
     * Same as {@link #calculateTotalExpenses()} but filtering by {@link FinancialPeriod}
     *
     * @param periods the list of {@link FinancialPeriod} to search for
     * @return the total value of expenses
     */
    @Query("SELECT COALESCE(SUM(pm.paidValue), 0) " +
            "FROM PeriodMovement mv " +
            "INNER JOIN FinancialPeriod fp ON fp.id = mv.financialPeriod.id " +
            "INNER JOIN Payment pm ON pm.id = mv.payment.id " +
            "INNER JOIN Apportionment ap on ap.movement = mv.id " +
            "INNER JOIN MovementClass mc on mc.id = ap.movementClass.id " +
            "AND fp.id IN (?1) " +
            "AND pm.paymentMethod <> 'CREDIT_CARD' " +
            "AND mc.movementClassType = 'EXPENSE' " +
            "AND mv.periodMovementState <> 'OPEN'")
    BigDecimal calculateTotalExpenses(List<Long> periods);

    /**
     * Calculate the total of revenues on a list of {@link FinancialPeriod}
     *
     * @return the total value of revenues
     */
    @Query("SELECT COALESCE(SUM(pm.paidValue), 0) " +
            "FROM PeriodMovement mv " +
            "INNER JOIN Payment pm ON pm.id = mv.payment.id " +
            "INNER JOIN Apportionment ap on ap.movement = mv.id " +
            "INNER JOIN MovementClass mc on mc.id = ap.movementClass.id " +
            "AND mc.movementClassType = 'REVENUE' " +
            "AND mv.periodMovementState <> 'OPEN'")
    BigDecimal calculateTotalRevenues();

    /**
     * Same as {@link #calculateTotalRevenues()} but filtering by {@link FinancialPeriod}
     *
     * @param periods the list of {@link FinancialPeriod} to search for
     * @return the total value of revenues
     */
    @Query("SELECT COALESCE(SUM(pm.paidValue), 0) " +
            "FROM PeriodMovement mv " +
            "INNER JOIN FinancialPeriod fp ON fp.id = mv.financialPeriod.id " +
            "INNER JOIN Payment pm ON pm.id = mv.payment.id " +
            "INNER JOIN Apportionment ap on ap.movement = mv.id " +
            "INNER JOIN MovementClass mc on mc.id = ap.movementClass.id " +
            "AND fp.id IN (?1) " +
            "AND mc.movementClassType = 'REVENUE' " +
            "AND mv.periodMovementState <> 'OPEN'")
    BigDecimal calculateTotalRevenues(List<Long> periods);

    /**
     * Use this method to find all {@link PeriodMovement} using the lazy load strategy
     *
     * @param filter the {@link PeriodMovementFilter}
     * @param start starting row
     * @param pageSize page size
     * @return the {@link Page} filled with the {@link PeriodMovement} found
     */
    default Page<PeriodMovement> findAllBy(PeriodMovementFilter filter, int start, int pageSize) {

        final int totalRows = this.countPages(filter);

        final Criteria<PeriodMovement, PeriodMovement> criteria = this.buildCriteria(filter);

        criteria.orderDesc(PeriodMovement_.financialPeriod);
        criteria.orderDesc(PeriodMovement_.createdOn);

        final List<PeriodMovement> data = criteria.createQuery()
                .setFirstResult(start)
                .setMaxResults(pageSize)
                .getResultList();

        return Page.of(data, totalRows);
    }

    /**
     * This method is used to count the total of rows found with the given filter
     *
     * @param filter the {@link PeriodMovementFilter}
     * @return total size of the pages
     */
    @SuppressWarnings("unchecked")
    default int countPages(PeriodMovementFilter filter) {
        return this.buildCriteria(filter)
                .select(Long.class, count(PeriodMovement_.id))
                .getSingleResult()
                .intValue();
    }

    /**
     * This method is used to build the {@link Criteria} used to find the {@link PeriodMovement}
     *
     * @param filter the {@link PeriodMovementFilter}
     * @return the {@link Criteria} with the restrictions to find the {@link PeriodMovement}
     */
    default Criteria<PeriodMovement, PeriodMovement> buildCriteria(PeriodMovementFilter filter) {

        final Criteria<PeriodMovement, PeriodMovement> criteria = this.criteria();

        // set the movement state filter if present
        if (filter.getPeriodMovementState() != null) {
            criteria.eq(PeriodMovement_.periodMovementState, filter.getPeriodMovementState());
        }

        // the movement type filter if present
        if (filter.getPeriodMovementType() != null) {
            criteria.eq(PeriodMovement_.periodMovementType, filter.getPeriodMovementType());
        }

        // now the OR filters, more generic
        if (isNotBlank(filter.getValue())) {

            final String anyFilter = this.likeAny(filter.getValue());

            final Set<Criteria<PeriodMovement, PeriodMovement>> restrictions = new HashSet<>();

            restrictions.add(this.criteria().eq(PeriodMovement_.code, anyFilter));
            restrictions.add(this.criteria().likeIgnoreCase(PeriodMovement_.description, anyFilter));
            restrictions.add(this.criteria().likeIgnoreCase(PeriodMovement_.identification, anyFilter));
            restrictions.add(this.criteria().join(PeriodMovement_.financialPeriod,
                    where(FinancialPeriod.class).likeIgnoreCase(FinancialPeriod_.identification, anyFilter)));

            // if we can cast the value of the filter to decimal, use this as filter
            filter.valueToBigDecimal()
                    .ifPresent(value -> restrictions.add(this.criteria().eq(PeriodMovement_.value, value)));

            criteria.or(restrictions);
        }

        // put the selected cost center as a filter
        if (filter.getCostCenter() != null) {
            criteria.join(PeriodMovement_.apportionments,
                    where(Apportionment.class).join(Apportionment_.costCenter,
                            where(CostCenter.class).eq(CostCenter_.id, filter.getCostCenter().getId())));

            // if we have a cost center them check if we have a movement class to filter too
            if (filter.getMovementClass() != null) {
                criteria.join(PeriodMovement_.apportionments,
                        where(Apportionment.class).join(Apportionment_.movementClass,
                                where(MovementClass.class).eq(MovementClass_.id, filter.getMovementClass().getId())));
            }
        }

        // put the selected financial periods as a filter
        if (filter.getSelectedFinancialPeriods() != null && !filter.getSelectedFinancialPeriods().isEmpty()) {
            criteria.join(PeriodMovement_.financialPeriod, where(FinancialPeriod.class)
                    .in(FinancialPeriod_.identification, filter.getSelectedFinancialPeriodsAsStringArray()));
        }

        return criteria;
    }
}
