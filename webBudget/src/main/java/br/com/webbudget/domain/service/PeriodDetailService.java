/*
 * Copyright (C) 2016 Arthur Gregorio, AG.Software
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

import br.com.webbudget.domain.entity.movement.CostCenter;
import br.com.webbudget.domain.entity.movement.FinancialPeriod;
import br.com.webbudget.domain.entity.movement.Movement;
import br.com.webbudget.domain.entity.movement.MovementClass;
import br.com.webbudget.domain.entity.movement.MovementClassType;
import br.com.webbudget.domain.entity.movement.MovementStateType;
import br.com.webbudget.domain.entity.movement.MovementType;
import br.com.webbudget.domain.misc.chart.donut.DonutChartDataset;
import br.com.webbudget.domain.misc.chart.donut.DonutChartModel;
import br.com.webbudget.domain.misc.chart.line.LineChartDatasetBuilder;
import br.com.webbudget.domain.misc.chart.line.LineChartModel;
import br.com.webbudget.domain.repository.movement.IApportionmentRepository;
import br.com.webbudget.domain.repository.movement.ICostCenterRepository;
import br.com.webbudget.domain.repository.movement.IMovementClassRepository;
import br.com.webbudget.domain.repository.movement.IMovementRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.apache.commons.collections.ListUtils;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.2.0, 22/02/2016
 */
@ApplicationScoped
public class PeriodDetailService {

    @Inject
    private IMovementRepository movementRepository;
    @Inject
    private ICostCenterRepository costCenterRepository;
    @Inject
    private IApportionmentRepository apportionmentRepository;
    @Inject
    private IMovementClassRepository movementClassRepository;

    /**
     *
     * @param period
     * @param direction
     * @return
     */
    public List<MovementClass> fetchTopClassesAndValues(
            FinancialPeriod period, MovementClassType direction) {

        final List<MovementClass> withValues = new ArrayList<>();

        // lista as classes sem pegar as bloqueadas
        final List<MovementClass> classes = this.movementClassRepository
                .listByTypeAndStatus(direction, Boolean.FALSE);

        classes.stream().forEach(clazz -> {

            final BigDecimal total
                    = this.apportionmentRepository.totalMovementsPerClass(clazz);

            if (total != null) {
                clazz.setTotalMovements(total);
                withValues.add(clazz);
            }
        });

        // ordena do maior para o menor
        withValues.sort((c1, c2)
                -> c2.getTotalMovements().compareTo(c1.getTotalMovements()));

        return withValues.size() > 10 ? withValues.subList(0, 10) : withValues;
    }

    /**
     *
     * @param period
     * @param direction
     * @return
     */
    public DonutChartModel buidCostCenterChart(FinancialPeriod period, MovementClassType direction) {

        final List<Movement> movements;

        if (direction == MovementClassType.OUT) {
            movements = this.getExpensesFor(period);
        } else {
            movements = this.getRevenuesFor(period);
        }

        final Map<CostCenter, List<Movement>> costCentersAndMovements
                = this.mapCostCenterAndMovements(movements);

        final DonutChartModel donutChartModel = new DonutChartModel();

        costCentersAndMovements.keySet().stream().forEach(costCenter -> {

            final List<Movement> grouped
                    = costCentersAndMovements.get(costCenter);

            BigDecimal total = grouped.stream()
                    .map(Movement::getValue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            final String color = this.createRandomColor();
            
            donutChartModel.addData(new DonutChartDataset<BigDecimal>(total,
                    "rgb("+ color +")", "rgba("+ color +",0.8)", costCenter.getName()));
        });

        return donutChartModel;
    }

    /**
     *
     * @param period
     * @return
     */
    public LineChartModel bulidDailyChart(FinancialPeriod period) {

        final List<Movement> revenues = this.getRevenuesFor(period);

        final List<Movement> expenses = this.getExpensesFor(period);

        // agrupamos pelas datas das despesas e receitas
        final List<LocalDate> payDates = this.groupPaymentDates(
                ListUtils.union(revenues, expenses));

        final LineChartModel model = new LineChartModel();

        final LineChartDatasetBuilder<BigDecimal> expensesBuilder
                = new LineChartDatasetBuilder<>()
                .filledByColor("rgba(255,153,153,0.2)")
                .withStrokeColor("rgba(255,77,77,1)")
                .withPointColor("rgba(204,0,0,1)")
                .withPointStrokeColor("#fff")
                .withPointHighlightFillColor("#fff")
                .withPointHighlightStroke("rgba(204,0,0,1)");

        final LineChartDatasetBuilder<BigDecimal> revenuesBuilder
                = new LineChartDatasetBuilder<>()
                .filledByColor("rgba(140,217,140,0.2)")
                .withStrokeColor("rgba(51,153,51,1)")
                .withPointColor("rgba(45,134,45,1)")
                .withPointStrokeColor("#fff")
                .withPointHighlightFillColor("#fff")
                .withPointHighlightStroke("rgba(45,134,45,1)");

        payDates.stream().forEach(payDate -> {

            model.addLabel(DateTimeFormatter
                    .ofPattern("dd/MM").format(payDate));

            final BigDecimal expensesTotal = expenses.stream()
                    .filter(movement -> movement.getPaymentDate().equals(payDate))
                    .map(Movement::getValue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            final BigDecimal revenuesTotal = revenues.stream()
                    .filter(movement -> movement.getPaymentDate().equals(payDate))
                    .map(Movement::getValue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            expensesBuilder.andData(expensesTotal);
            revenuesBuilder.andData(revenuesTotal);
        });

        model.addDataset(revenuesBuilder.build());
        model.addDataset(expensesBuilder.build());

        return model;
    }

    /**
     * Agrupa todas as datas de pagamento dos movimentos pagos no periodo
     *
     * @param movements a lista de movimentos
     * @return a lista de datas
     */
    private List<LocalDate> groupPaymentDates(List<Movement> movements) {

        final List<LocalDate> dates = new ArrayList<>();

        movements.stream().forEach(movement -> {
            if (!dates.contains(movement.getPaymentDate())) {
                dates.add(movement.getPaymentDate());
            }
        });

        dates.sort((d1, d2) -> d1.compareTo(d2));

        return dates;
    }

    /**
     *
     * @param period
     * @return
     */
    private List<Movement> getRevenuesFor(FinancialPeriod period) {

        MovementStateType state = MovementStateType.PAID;

        if (period.isClosed()) {
            state = MovementStateType.CALCULATED;
        }

        return this.movementRepository.listByPeriodAndStateAndTypeAndDirection(
                period, state, MovementType.MOVEMENT, MovementClassType.IN);
    }

    /**
     *
     * @param period
     * @return
     */
    private List<Movement> getExpensesFor(FinancialPeriod period) {

        MovementStateType state = MovementStateType.PAID;

        if (period.isClosed()) {
            state = MovementStateType.CALCULATED;
        }

        return this.movementRepository.listByPeriodAndStateAndTypeAndDirection(
                period, state, MovementType.MOVEMENT, MovementClassType.OUT);
    }

    /**
     * @return gera uma cor hex randomica para o grafico
     */
    public String createRandomColor() {
        
        final StringBuilder builder = new StringBuilder();
        
        for (int i = 0; i < 3; i++) {
            builder.append(ThreadLocalRandom.current().nextInt(1, 255 + 1));
            if (i != 2) {
                builder.append(",");
            }
        }
        
        return builder.toString();
    }

    /**
     *
     * @param movements
     * @return
     */
    private Map<CostCenter, List<Movement>> mapCostCenterAndMovements(List<Movement> movements) {

        final Map<CostCenter, List<Movement>> result = new HashMap<>();

        // mapeia apenas os centros de custo
        final List<CostCenter> costCenters = new ArrayList<>();

        movements.stream().forEach(movement -> {
            movement.getCostCenters().stream().forEach(costCenter -> {
                if (!costCenters.contains(costCenter)) {
                    costCenters.add(costCenter);
                }
            });
        });

        // mapeia os movimentos para cada centro de custo
        costCenters.stream().forEach(costCenter -> {

            final List<Movement> grouped = new ArrayList<>();

            movements.stream().forEach(movement -> {
                if (movement.getCostCenters().contains(costCenter)) {
                    grouped.add(movement);
                }
            });

            result.put(costCenter, grouped);
        });

        return result;
    }
}
