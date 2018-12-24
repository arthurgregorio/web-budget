package br.com.webbudget.application.controller.financial;

import br.com.webbudget.domain.entities.registration.CostCenter;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.repositories.financial.PeriodMovementRepository;
import br.com.webbudget.domain.repositories.registration.CostCenterRepository;
import br.com.webbudget.domain.repositories.registration.FinancialPeriodRepository;
import br.com.webbudget.domain.repositories.registration.MovementClassRepository;
import lombok.Getter;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 16/12/2018
 */
@Named
@Dependent
public class PeriodMovementFormBean implements Serializable {

    @Getter
    private FinancialPeriod currentPeriod;

    @Getter
    private List<CostCenter> costCenters;
    @Getter
    private List<FinancialPeriod> financialPeriods;

    @Inject
    private CostCenterRepository costCenterRepository;
    @Inject
    private MovementClassRepository movementClassRepository;
    @Inject
    private PeriodMovementRepository periodMovementRepository;
    @Inject
    private FinancialPeriodRepository financialPeriodRepository;

    /**
     *
     */
    protected void initializeForAdding() {

        this.costCenters = this.costCenterRepository.findAllActive();
        this.financialPeriods = this.financialPeriodRepository.findByClosed(false);

        this.currentPeriod = this.financialPeriods.stream()
                .filter(FinancialPeriod::isCurrent)
                .findFirst()
                .orElse(null);
    }

    /**
     *
     */
    protected void initializeForEditing() {

    }

    /**
     *
     * @return
     */
    public String getCurrentPeriodStart() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.currentPeriod.getStart());
    }

    /**
     *
     * @return
     */
    public String getCurrentPeriodEnd() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.currentPeriod.getEnd());
    }
}
