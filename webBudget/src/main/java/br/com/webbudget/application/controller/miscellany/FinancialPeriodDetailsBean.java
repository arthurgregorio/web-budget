package br.com.webbudget.application.controller.miscellany;

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
    
    @Getter
    private BigDecimal graphInMax;
    @Getter
    private BigDecimal graphOutMax;
    
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
            
            this.graphInMax = BigDecimal.ZERO;
            this.graphOutMax = BigDecimal.ZERO;
            
            // calculamos os pontos maximos em Y para o grafico de movimentos
            this.calculateGraphInMax();
            this.calculateGraphOutMax();
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
     * @return 
     */
    public BigDecimal getMaxGraphIn() {
        final BigDecimal max = this.closing.getTopFiveClassesIn().get(0).getBudget();
        return max.add(new BigDecimal("100"));
    }
    
    /**
     * Calcula o maximo Y do grafico de entradas
     */
    public void calculateGraphInMax() {
        
        for (MovementClass movementClass : this.closing.getTopFiveClassesIn()) {
        
            BigDecimal max = movementClass.getBudget();
            final BigDecimal maxMovement = movementClass.getTotalMovements();
            
            if (maxMovement != null) {
                if (max.compareTo(maxMovement) < 0) {
                    max = maxMovement;
                }
            }
            
            if (max.compareTo(this.graphInMax) > 0) {
                this.graphInMax = max;
            }
        }
        
        this.graphInMax = this.graphInMax.add(new BigDecimal("100"));
    }
    
    /**
     * Calcula o maximo Y do grafico de saidas
     */
    public void calculateGraphOutMax() {
        
        for (MovementClass movementClass : this.closing.getTopFiveClassesOut()) {
            
            BigDecimal max = movementClass.getBudget();
            final BigDecimal maxMovement = movementClass.getTotalMovements();
            
            if (maxMovement != null) {
                if (max.compareTo(maxMovement) < 0) {
                    max = maxMovement;
                }
            }
            
            if (max.compareTo(this.graphOutMax) > 0) {
                this.graphOutMax = max;
            }
        }
        
        this.graphOutMax = this.graphOutMax.add(new BigDecimal("100"));
    }
    
    /**
     * 
     * @return 
     */
    public String getDatatipFormat() {
        return "<span style=\"display:none;\">%s</span><span>R$ %s</span>";
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
