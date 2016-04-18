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
package br.com.webbudget.domain.model.service;

import br.com.webbudget.domain.model.entity.entries.CostCenter;
import br.com.webbudget.domain.model.entity.miscellany.FinancialPeriod;
import br.com.webbudget.domain.model.entity.financial.Movement;
import br.com.webbudget.domain.model.entity.entries.MovementClass;
import br.com.webbudget.domain.model.entity.entries.MovementClassType;
import br.com.webbudget.domain.model.entity.financial.MovementStateType;
import br.com.webbudget.domain.model.entity.financial.MovementType;
import br.com.webbudget.application.component.chart.donut.DonutChartDataset;
import br.com.webbudget.application.component.chart.donut.DonutChartModel;
import br.com.webbudget.application.component.chart.line.LineChartDatasetBuilder;
import br.com.webbudget.application.component.chart.line.LineChartModel;
import br.com.webbudget.domain.model.repository.financial.IApportionmentRepository;
import br.com.webbudget.domain.model.repository.entries.IMovementClassRepository;
import br.com.webbudget.domain.model.repository.financial.IMovementRepository;
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
 * Classe que representa a montagem da tela de detalhes do periodo financeiro
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
    private IApportionmentRepository apportionmentRepository;
    @Inject
    private IMovementClassRepository movementClassRepository;

    /**
     * Metodo que busca as classes de movimentacao e seu respectivo valor 
     * movimento, ou seja, a somatoria de todos os rateios para a aquela classe
     *
     * @param period o periodo
     * @param direction qual tipo queremos, entrada ou saida
     * @return a lista de movimentos
     */
    public List<MovementClass> fetchTopClassesAndValues(
            FinancialPeriod period, MovementClassType direction) {

        final List<MovementClass> withValues = new ArrayList<>();

        // lista as classes sem pegar as bloqueadas
        final List<MovementClass> classes = this.movementClassRepository
                .listByTypeAndStatus(direction, Boolean.FALSE);

        // para cada classe pegamos o seu total em movimentacao
        classes.stream().forEach(clazz -> {

            final BigDecimal total = this.apportionmentRepository
                    .totalMovementsPerClassAndPeriod(period, clazz);

            if (total != null) {
                clazz.setTotalMovements(total);
                withValues.add(clazz);
            }
        });

        // ordena do maior para o menor
        withValues.sort((c1, c2)
                -> c2.getTotalMovements().compareTo(c1.getTotalMovements()));

        // retorna somente os 10 primeiros resultados
        return withValues.size() > 10 ? withValues.subList(0, 10) : withValues;
    }

    /**
     * Monta o model do grafico de consumo por centro de custo
     *
     * @param period o periodo
     * @param direction a direcao que vamos montar no grafico, entrada ou saida
     * @return o modelo do grafico para a view
     */
    public DonutChartModel buidCostCenterChart(FinancialPeriod period, MovementClassType direction) {

        final List<Movement> movements;

        // pela direcao, decide qual lista de movimentos usar
        if (direction == MovementClassType.OUT) {
            movements = this.getExpensesFor(period);
        } else {
            movements = this.getRevenuesFor(period);
        }

        // mapeia para cada centro de custo, seus movimentos
        final Map<CostCenter, List<Movement>> costCentersAndMovements
                = this.mapCostCenterAndMovements(movements);

        final DonutChartModel donutChartModel = new DonutChartModel();

        // para cada CC adiciona os dados do grafico
        costCentersAndMovements.keySet().stream().forEach(costCenter -> {

            final List<Movement> grouped
                    = costCentersAndMovements.get(costCenter);

            BigDecimal total = grouped.stream()
                    .map(Movement::getValue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            final String color = this.createRandomColor();
            
            donutChartModel.addData(new DonutChartDataset<>(total, "rgb("+ color +")",
                    "rgba("+ color +",0.8)", costCenter.getName()));
        });

        return donutChartModel;
    }

    /**
     * Metodo que monta o modelo do grafico de consumo por dia no periodo
     *
     * @param period o periodo
     * @return o model para a view
     */
    public LineChartModel bulidDailyChart(FinancialPeriod period) {

        // lista receitas e despesas do periodo
        final List<Movement> revenues = this.getRevenuesFor(period);
        final List<Movement> expenses = this.getExpensesFor(period);

        // agrupamos pelas datas das despesas e receitas
        final List<LocalDate> payDates = this.groupPaymentDates(
                ListUtils.union(revenues, expenses));

        // monta o grafico de linhas
        final LineChartModel model = new LineChartModel();

        // dados de despesas
        final LineChartDatasetBuilder<BigDecimal> expensesBuilder
                = new LineChartDatasetBuilder<>()
                .filledByColor("rgba(255,153,153,0.2)")
                .withStrokeColor("rgba(255,77,77,1)")
                .withPointColor("rgba(204,0,0,1)")
                .withPointStrokeColor("#fff")
                .withPointHighlightFillColor("#fff")
                .withPointHighlightStroke("rgba(204,0,0,1)");

        // dados de receitas
        final LineChartDatasetBuilder<BigDecimal> revenuesBuilder
                = new LineChartDatasetBuilder<>()
                .filledByColor("rgba(140,217,140,0.2)")
                .withStrokeColor("rgba(51,153,51,1)")
                .withPointColor("rgba(45,134,45,1)")
                .withPointStrokeColor("#fff")
                .withPointHighlightFillColor("#fff")
                .withPointHighlightStroke("rgba(45,134,45,1)");

        // para cada data de pagamento, printa o valor no dataset
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

        // joga os datasets no model
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
     * Pega todas as receitas para um determinado periodo considerando se ele
     * esta ou nao encerrado
     *
     * @param period o periodo
     * @return a lista de movimentos
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
     * Pega todas as despesas para um determinado periodo considerando se ele
     * esta ou nao encerrado
     *
     * @param period o periodo
     * @return a lista de movimentos
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
     * Mapeia os movimentos para dentro de seu respectivo centro de custo
     * 
     * @param movements o movimentos
     * @return o mapa de CC X movimentos
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
