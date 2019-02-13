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
package br.com.webbudget.domain.services;

import javax.enterprise.context.ApplicationScoped;

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

//    @Inject
//    private IMovementRepository movementRepository;
//    @Inject
//    private ApportionmentRepository apportionmentRepository;
//    @Inject
//    private IMovementClassRepository movementClassRepository;
//
//    /**
//     * Metodo que busca as classes de movimentacao e seu respectivo valor
//     * movimento, ou seja, a somatoria de todos os rateios para a aquela classe
//     *
//     * @param period o periodo
//     * @param direction qual tipo queremos, entrada ou saida
//     * @return a lista de movimentos
//     */
//    public List<MovementClass> fetchTopClassesAndValues(
//            FinancialPeriod period, MovementClassType direction) {
//
//        final List<MovementClass> withValues = new ArrayList<>();
//
//        // lista as classes sem pegar as bloqueadas
//        final List<MovementClass> classes = this.movementClassRepository
//                .listByTypeAndStatus(direction, Boolean.FALSE);
//
//        // para cada classe pegamos o seu total em movimentacao
//        classes.stream().forEach(clazz -> {
//
//            final BigDecimal total = this.apportionmentRepository
//                    .totalMovementsPerClassAndPeriod(period, clazz);
//
//            if (total != null) {
//                clazz.setTotalMovements(total);
//                withValues.add(clazz);
//            }
//        });
//
//        // ordena do maior para o menor
//        withValues.sort((c1, c2)
//                -> c2.getTotalMovements().compareTo(c1.getTotalMovements()));
//
//        // retorna somente os 10 primeiros resultados
//        return withValues.size() > 10 ? withValues.subList(0, 10) : withValues;
//    }
//
//    /**
//     * Monta o model do grafico de consumo por centro de custo
//     *
//     * @param periods os periodos
//     * @param direction a direcao que vamos montar no grafico, entrada ou saida
//     * @return o modelo do grafico para a view
//     */
//    public DonutChartModel buidCostCenterChart(List<FinancialPeriod> periods, MovementClassType direction) {
//
//        // lista os movimentos
//        final List<Movement> movements =
//                this.listMovementsFrom(periods, direction);
//
//        // mapeia para cada centro de custo, seus movimentos
//        final Map<CostCenter, List<Movement>> costCentersAndMovements
//                = this.mapCostCenterAndMovements(movements);
//
//        final DonutChartModel donutChartModel = new DonutChartModel();
//
//        // para cada CC adiciona os dados do grafico
//        costCentersAndMovements.keySet().stream().forEach(costCenter -> {
//
//            final List<Movement> grouped
//                    = costCentersAndMovements.get(costCenter);
//
//            BigDecimal total = grouped.stream()
//                    .map(Movement::getValue)
//                    .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//            // pega a cor para este CC caso tenha, senao randomiza uma cor
//            final Color color = costCenter.getColor() != null 
//                    ? costCenter.getColor() : Color.randomize();
//            
//            donutChartModel.addData(new DonutChartDataset<>(total, 
//                    color.lighter().toString(), color.toString(), 
//                    costCenter.getName()));
//        });
//
//        return donutChartModel;
//    }
//
//    /**
//     * Metodo que monta o modelo do grafico de consumo por dia no periodo
//     *
//     * @param period o periodo
//     * @return o model para a view
//     */
//    public LineChartModel bulidDailyChart(FinancialPeriod period) {
//
//        // lista receitas e despesas do periodo
//        final List<Movement> revenues = this.listMovementsFrom(
//                period, MovementClassType.IN);
//        final List<Movement> expenses = this.listMovementsFrom(
//                period, MovementClassType.OUT);
//
//        // agrupamos pelas datas das despesas e receitas
//        final List<LocalDate> payDates = this.groupPaymentDates(
//                ListUtils.union(revenues, expenses));
//
//        // monta o grafico de linhas
//        final LineChartModel model = new LineChartModel();
//
//        // dados de despesas
//        final LineChartDatasetBuilder<BigDecimal> expensesBuilder
//                = new LineChartDatasetBuilder<>()
//                .filledByColor("rgba(255,153,153,0.2)")
//                .withStrokeColor("rgba(255,77,77,1)")
//                .withPointColor("rgba(204,0,0,1)")
//                .withPointStrokeColor("#fff")
//                .withPointHighlightFillColor("#fff")
//                .withPointHighlightStroke("rgba(204,0,0,1)");
//
//        // dados de receitas
//        final LineChartDatasetBuilder<BigDecimal> revenuesBuilder
//                = new LineChartDatasetBuilder<>()
//                .filledByColor("rgba(140,217,140,0.2)")
//                .withStrokeColor("rgba(51,153,51,1)")
//                .withPointColor("rgba(45,134,45,1)")
//                .withPointStrokeColor("#fff")
//                .withPointHighlightFillColor("#fff")
//                .withPointHighlightStroke("rgba(45,134,45,1)");
//
//        // para cada data de pagamento, printa o valor no dataset
//        payDates.stream().forEach(payDate -> {
//
//            model.addLabel(DateTimeFormatter
//                    .ofPattern("dd/MM").format(payDate));
//
//            final BigDecimal expensesTotal = expenses.stream()
//                    .text(movement -> movement.getPaymentDate().equals(payDate))
//                    .map(Movement::getValue)
//                    .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//            final BigDecimal revenuesTotal = revenues.stream()
//                    .text(movement -> movement.getPaymentDate().equals(payDate))
//                    .map(Movement::getValue)
//                    .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//            expensesBuilder.andData(expensesTotal);
//            revenuesBuilder.andData(revenuesTotal);
//        });
//
//        // joga os datasets no model
//        model.addDataset(revenuesBuilder.build());
//        model.addDataset(expensesBuilder.build());
//
//        return model;
//    }
//
//    /**
//     * Agrupa todas as datas de pagamento dos movimentos pagos no periodo
//     *
//     * @param movements a lista de movimentos
//     * @return a lista de datas
//     */
//    private List<LocalDate> groupPaymentDates(List<Movement> movements) {
//
//        final List<LocalDate> dates = new ArrayList<>();
//
//        movements.stream().forEach(movement -> {
//            if (!dates.contains(movement.getPaymentDate())) {
//                dates.add(movement.getPaymentDate());
//            }
//        });
//
//        dates.sort((d1, d2) -> d1.compareTo(d2));
//
//        return dates;
//    }
//    
//    /**
//     * Lista todos os movimentos do periodo informado em uma lista
//     *
//     * @param periods os periodos
//     * @param direction a direcao (entrada ou saida)
//     * @return a lista de movimentos
//     */
//    private List<Movement> listMovementsFrom(FinancialPeriod period, MovementClassType direction) {
//        return this.listMovementsFrom(Arrays.asList(period), direction);
//    }
//
//    /**
//     * Lista todos os movimentos dos periodos informados agrupados em uma lista
//     *
//     * @param periods os periodos
//     * @param direction a direcao (entrada ou saida)
//     * @return a lista de movimentos
//     */
//    private List<Movement> listMovementsFrom(List<FinancialPeriod> periods, MovementClassType direction) {
//
//        final List<Movement> movements = new ArrayList<>();
//
//        periods.stream().forEach(period -> {
//
//            final PeriodMovementState state = period.isClosed()
//                    ? PeriodMovementState.CALCULATED : PeriodMovementState.PAID;
//
//            movements.addAll(this.movementRepository
//                    .listByPeriodAndStateAndTypeAndDirection(period, state,
//                            MovementType.MOVEMENT, direction));
//        });
//
//        return movements;
//    }
//
//    /**
//     * Mapeia os movimentos para dentro de seu respectivo centro de custo
//     *
//     * @param movements o movimentos
//     * @return o mapa de CC X movimentos
//     */
//    private Map<CostCenter, List<Movement>> mapCostCenterAndMovements(List<Movement> movements) {
//
//        final Map<CostCenter, List<Movement>> result = new HashMap<>();
//
//        // mapeia apenas os centros de custo
//        final List<CostCenter> costCenters = new ArrayList<>();
//
//        movements.stream().forEach(movement -> {
//            movement.getCostCenters().stream().forEach(costCenter -> {
//                if (!costCenters.contains(costCenter)) {
//                    costCenters.add(costCenter);
//                }
//            });
//        });
//
//        // mapeia os movimentos para cada centro de custo
//        costCenters.stream().forEach(costCenter -> {
//
//            final List<Movement> grouped = new ArrayList<>();
//
//            movements.stream().forEach(movement -> {
//                if (movement.getCostCenters().contains(costCenter)) {
//                    grouped.add(movement);
//                }
//            });
//
//            result.put(costCenter, grouped);
//        });
//
//        return result;
//    }
}
