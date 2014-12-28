package br.com.webbudget.domain.service;

import br.com.webbudget.application.exceptions.ApplicationException;
import br.com.webbudget.domain.entity.movement.FinancialPeriod;
import br.com.webbudget.domain.repository.movement.IFinancialPeriodRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 20/03/2014
 */
@Service
public class FinancialPeriodService {

    @Autowired
    private IFinancialPeriodRepository financialPeriodRepository;
    
    /**
     * 
     * @param financialPeriod 
     */
    @Transactional(rollbackFor = Exception.class)
    public void openPeriod(FinancialPeriod financialPeriod) {
        
        final FinancialPeriod found = this.findFinancialPeriodByIdentification(
                financialPeriod.getIdentification());
        
        if (found != null && !found.equals(financialPeriod)) {
            throw new ApplicationException("financial-period.validate.duplicated");
        }
        
        // validamos se o periodo informado já foi contemplado em outro 
        // periodo existente
        final List<FinancialPeriod> periods = this.listFinancialPeriods(null);
        
        for (FinancialPeriod fp : periods) {
            if (financialPeriod.getStart().compareTo(fp.getEnd()) <= 0) {
                throw new ApplicationException("financial-period.validate.truncated-dates");
            }
        }
        
        this.financialPeriodRepository.save(financialPeriod);
    }
    
    /**
     * 
     * @return 
     */
    @Transactional(readOnly = true)
    public FinancialPeriod findActiveFinancialPeriod() {
        
        final List<FinancialPeriod> financialPeriods = this.financialPeriodRepository.listOpen();
        
        FinancialPeriod activePeriod = null;
        
        for (FinancialPeriod financialPeriod : financialPeriods) {
            if (!financialPeriod.isExpired()) {
                activePeriod = financialPeriod;
                break;
            } else {
                activePeriod = financialPeriod;
            }
        }
        
        return activePeriod;
    }
    
    /**
     * 
     * @param financialPeriodId
     * @return 
     */
    @Transactional(readOnly = true)
    public FinancialPeriod findFinancialPeriodById(long financialPeriodId) {
        return this.financialPeriodRepository.findById(financialPeriodId, false);
    }
    
    /**
     * 
     * @param identification
     * @return 
     */
    @Transactional(readOnly = true)
    public FinancialPeriod findFinancialPeriodByIdentification(String identification) {
        return this.financialPeriodRepository.findByIdentification(identification);
    }
    
    /**
     * 
     * @param isClosed
     * @return 
     */
    @Transactional(readOnly = true)
    public List<FinancialPeriod> listFinancialPeriods(Boolean isClosed) {
        return this.financialPeriodRepository.listAll();
    }
    
    /**
     * 
     * @return 
     */
    @Transactional(readOnly = true)
    public List<FinancialPeriod> listOpenFinancialPeriods() {
        return this.financialPeriodRepository.listOpen();
    }
}
