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

import br.com.webbudget.application.components.MessagesFactory;
import br.com.webbudget.domain.entity.movement.CostCenter;
import br.com.webbudget.domain.entity.movement.Movement;
import br.com.webbudget.domain.entity.movement.MovementClass;
import br.com.webbudget.domain.repository.movement.ICostCenterRepository;
import br.com.webbudget.domain.repository.movement.IMovementRepository;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.BarChartSeries;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *  
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 13/04/2014
 */
@Service
public class GraphModelService implements Serializable {

    @Autowired
    private MessagesFactory messages;
    @Autowired
    private IMovementRepository movementRepository;
    @Autowired
    private ICostCenterRepository costCenterRepository;
    
    /**
     * 
     * @return 
     */
    @Transactional(readOnly = true)
    public PieChartModel buildConsumeModel() {
        
        final List<CostCenter> costCenters = this.costCenterRepository.listByStatus(false);
//        final List<Movement> movements = this.movementRepository.listOutsByActiveFinancialPeriod();
        
        BigDecimal total = BigDecimal.ZERO;
        
        // FIXME arrumar quando o rateio estiver OK!
//        for (Movement movement : movements) {
//            total = total.add(movement.getValue());
//            
//            for (CostCenter costCenter : costCenters) {
//                if (movement.getMovementClass().getCostCenter().equals(costCenter)) {
//                    costCenter.setTotalMovements(costCenter.getTotalMovements().add(movement.getValue()));
//                }
//            }
//        }
        
        final PieChartModel model = new PieChartModel();
        
        for (CostCenter costCenter : costCenters) {
            if (costCenter.getTotalMovements() != BigDecimal.ZERO) {
                final BigDecimal divider = costCenter.getTotalMovements()
                        .multiply(new BigDecimal("100"));
                costCenter.setPercentage(divider.divide(total, RoundingMode.CEILING));
                
                model.set(costCenter.getName(), costCenter.getPercentage());
            }
        }
        
        model.setShadow(false);
        model.setLegendCols(3);
        model.setLegendRows(10);
        model.setLegendPosition("e");
        model.setShowDataLabels(true);
        
        return model;
    }
    
    /**
     * 
     * @return 
     */
    @Transactional(readOnly = true)
    public PieChartModel buildRevenueModel() {
        
        final List<CostCenter> costCenters = this.costCenterRepository.listByStatus(false);
//        final List<Movement> movements = this.movementRepository.listInsByActiveFinancialPeriod();
        
        BigDecimal total = BigDecimal.ZERO;
        
        // FIXME arrumar quando o rateio estiver OK!
//        for (Movement movement : movements) {
//            total = total.add(movement.getValue());
//            
//            for (CostCenter costCenter : costCenters) {
//                if (movement.getMovementClass().getCostCenter().equals(costCenter)) {
//                    costCenter.setTotalMovements(costCenter.getTotalMovements().add(movement.getValue()));
//                }
//            }
//        }
        
        final PieChartModel model = new PieChartModel();
        
        for (CostCenter costCenter : costCenters) {
            if (costCenter.getTotalMovements() != BigDecimal.ZERO) {
                final BigDecimal divider = costCenter.getTotalMovements()
                        .multiply(new BigDecimal("100"));
                costCenter.setPercentage(divider.divide(total, RoundingMode.CEILING));
                
                model.set(costCenter.getName(), costCenter.getPercentage());
            }
        }
        
        model.setShadow(false);
        model.setLegendCols(3);
        model.setLegendRows(10);
        model.setLegendPosition("e");
        model.setShowDataLabels(true);
        
        return model;
    }
    
    /**
     * 
     * @param classes
     * @return 
     */
    public CartesianChartModel buildTopClassesModel(List<MovementClass> classes) {
        
        final CartesianChartModel model = new BarChartModel();
        
        final BarChartSeries classesBar = this.buildClassesBar(classes);
        final LineChartSeries budgetLine = this.buildBudgetLine(classes);
        
        model.addSeries(classesBar);
        model.addSeries(budgetLine);
        
        model.setMouseoverHighlight(false);
        
        return model;
    }
    
    /**
     * 
     * @param classes
     * @return 
     */
    private BarChartSeries buildClassesBar(List<MovementClass> classes) {
        
        final BarChartSeries series = new BarChartSeries();
        
        series.setLabel(this.messages.getMessage("financial-period.chart.classes"));
        
        for (MovementClass movementClass : classes) {
            series.set(movementClass.getName(), movementClass.getTotalMovements());
        }
        
        return series;
    }
    
    /**
     * 
     * @param classes
     * @return 
     */
    private LineChartSeries buildBudgetLine(List<MovementClass> classes) {
        
        final LineChartSeries series = new LineChartSeries();
        
        series.setLabel(this.messages.getMessage("financial-period.chart.budget"));
        
        for (MovementClass movementClass : classes) {
            series.set(movementClass.getName(), movementClass.getBudget());
        }
        
        return series;
    }
}
