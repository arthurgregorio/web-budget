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

package br.com.webbudget.application.controller.miscellany;

import br.com.webbudget.application.components.MessagesFactory;
import br.com.webbudget.domain.service.GraphModelService;
import br.com.webbudget.domain.entity.closing.Closing;
import br.com.webbudget.domain.entity.movement.FinancialPeriod;
import br.com.webbudget.domain.entity.movement.MovementClass;
import br.com.webbudget.domain.service.ClosingService;
import br.com.webbudget.domain.service.FinancialPeriodService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CartesianChartModel;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 11/04/2014
 */
@ViewScoped
@ManagedBean
public class FinancialPeriodDetailsBean implements Serializable {
    
    @Getter
    private Closing closing;
    @Getter
    private FinancialPeriod financialPeriod;

    @Getter
    private CartesianChartModel topInClasses;
    @Getter
    private CartesianChartModel topOutClasses;
    
    @Setter
    @ManagedProperty("#{messagesFactory}")
    private transient MessagesFactory messages;
    @Setter
    @ManagedProperty("#{closingService}")
    private transient ClosingService closingService;
    @Setter
    @ManagedProperty("#{graphModelService}")
    private transient GraphModelService graphModelService;
    @Setter
    @ManagedProperty("#{financialPeriodService}")
    private transient FinancialPeriodService financialPeriodService;
    
    /**
     * 
     * @param financialPeriodId 
     */
    public void initializeDetails(long financialPeriodId){
        if (!FacesContext.getCurrentInstance().isPostback()) {
            // busca o periodo 
            this.financialPeriod = this.financialPeriodService.findFinancialPeriodById(financialPeriodId);
            
            // chama a simulacao
            this.closing = this.closingService.simulate(this.financialPeriod);
            
            // cria os modelos dos graficos
            this.topInClasses = this.graphModelService.buildTopClassesModel(
                    this.closing.getTopFiveClassesIn());
            this.topOutClasses = this.graphModelService.buildTopClassesModel(
                    this.closing.getTopFiveClassesOut());
            
            this.formatGraphIn();
            this.formatGraphOut();
            
            // ordena corretamente as classes de entrada
            Collections.sort(this.closing.getTopFiveClassesIn(), new Comparator<MovementClass>() {
                @Override
                public int compare(MovementClass classOne, MovementClass classTwo) {
                    return classTwo.getBudget().compareTo(classOne.getBudget());
                }
            });
            
            // ordena corretamente as classes de saida
            Collections.sort(this.closing.getTopFiveClassesOut(), new Comparator<MovementClass>() {
                @Override
                public int compare(MovementClass classOne, MovementClass classTwo) {
                    return classTwo.getBudget().compareTo(classOne.getBudget());
                }
            });
        }
    }
    
    /**
     * Cancela e volta para a listagem
     *
     * @return
     */
    public String doCancel() {
        return "listFinancialPeriods.xhtml?faces-redirect=true";
    }
    
    /**
     * 
     * @return 
     */
    public String doRefresh() {
        return "detailFinancialPeriod.xhtml?faces-redirect=true&financialPeriodId=" + this.financialPeriod.getId();
    }
    
    /**
     * 
     */
    public void formatGraphIn() {
        
        final Axis yAxis = this.topInClasses.getAxis(AxisType.Y);
        
        yAxis.setMin(0);
        
        BigDecimal graphInMax = BigDecimal.ZERO;
        
        for (MovementClass movementClass : this.closing.getTopFiveClassesIn()) {
        
            BigDecimal max = movementClass.getBudget();
            final BigDecimal maxMovement = movementClass.getTotalMovements();
            
            if (maxMovement != null) {
                if (max.compareTo(maxMovement) < 0) {
                    max = maxMovement;
                }
            }
            
            if (max.compareTo(graphInMax) > 0) {
                graphInMax = max;
            }
        }
        
        graphInMax = graphInMax.add(new BigDecimal("100"));

        yAxis.setMax(graphInMax);
        
        this.topInClasses.setAnimate(true);
        this.topInClasses.setLegendPosition("ne");
        this.topInClasses.setTitle(this.messages.getMessage("financial-period.chart.in-classes"));
        this.topInClasses.setDatatipFormat("<span style=\"display:none;\">%s</span><span>R$ %s</span>");
    }
    
    /**
     * 
     */
    private void formatGraphOut() {
        
        final Axis yAxis = this.topInClasses.getAxis(AxisType.Y);
        
        yAxis.setMin(0);
        
        BigDecimal graphOutMax = BigDecimal.ZERO;
        
        for (MovementClass movementClass : this.closing.getTopFiveClassesOut()) {
            
            BigDecimal max = movementClass.getBudget();
            final BigDecimal maxMovement = movementClass.getTotalMovements();
            
            if (maxMovement != null) {
                if (max.compareTo(maxMovement) < 0) {
                    max = maxMovement;
                }
            }
            
            if (max.compareTo(graphOutMax) > 0) {
                graphOutMax = max;
            }
        }
        
        graphOutMax = graphOutMax.add(new BigDecimal("100"));

        yAxis.setMax(graphOutMax);
        
        this.topInClasses.setAnimate(true);
        this.topInClasses.setLegendPosition("ne");
        this.topInClasses.setTitle(this.messages.getMessage("financial-period.chart.out-classes"));
        this.topInClasses.setDatatipFormat("<span style=\"display:none;\">%s</span><span>R$ %s</span>");
    }
    
    /**
     * 
     * @return 
     */
    public boolean showInsChart() {
        return this.closing != null && this.closing.getTopFiveClassesIn().size() > 0;
    }
    
    /**
     * 
     * @return 
     */
    public boolean showOutsChart() {
        return this.closing != null && this.closing.getTopFiveClassesOut().size() > 0;
    }
}
