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

import br.com.webbudget.application.exceptions.ApplicationException;
import br.com.webbudget.domain.entity.card.CardInvoice;
import br.com.webbudget.domain.entity.movement.Apportionment;
import br.com.webbudget.domain.entity.movement.CostCenter;
import br.com.webbudget.domain.entity.movement.FinancialPeriod;
import br.com.webbudget.domain.entity.movement.Movement;
import br.com.webbudget.domain.entity.movement.MovementClass;
import br.com.webbudget.domain.entity.movement.MovementClassType;
import br.com.webbudget.domain.entity.movement.MovementStateType;
import br.com.webbudget.domain.entity.movement.Payment;
import br.com.webbudget.domain.repository.card.ICardInvoiceRepository;
import br.com.webbudget.domain.repository.movement.IApportionmentRepository;
import br.com.webbudget.domain.repository.movement.ICostCenterRepository;
import br.com.webbudget.domain.repository.movement.IMovementRepository;
import br.com.webbudget.domain.repository.movement.IMovementClassRepository;
import br.com.webbudget.domain.repository.movement.IPaymentRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 04/03/2014
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MovementService {

    @Autowired
    private IPaymentRepository paymentRepository;
    @Autowired
    private IMovementRepository movementRepository;
    @Autowired
    private ICostCenterRepository costCenterRepository;
    @Autowired
    private ICardInvoiceRepository cardInvoiceRepository;
    @Autowired
    private IApportionmentRepository apportionmentRepository;
    @Autowired
    private IMovementClassRepository movementClassRepository;

    /**
     *
     * @param movementClass
     */
    public void saveMovementClass(MovementClass movementClass) {

        final MovementClass found = this.findMovementClassByNameAndTypeAndCostCenter(movementClass.getName(),
                movementClass.getMovementClassType(), movementClass.getCostCenter());

        if (found != null) {
            throw new ApplicationException("movement-class.validate.duplicated");
        }

        this.movementClassRepository.save(movementClass);
    }

    /**
     *
     * @param movementClass
     * @return
     */
    public MovementClass updateMovementClass(MovementClass movementClass) {

        final MovementClass found = this.findMovementClassByNameAndTypeAndCostCenter(movementClass.getName(),
                movementClass.getMovementClassType(), movementClass.getCostCenter());

        if (found != null && !found.equals(movementClass)) {
            throw new ApplicationException("movement-class.validate.duplicated");
        }

        return this.movementClassRepository.save(movementClass);
    }

    /**
     *
     * @param movementClass
     */
    public void deleteMovementClass(MovementClass movementClass) {
        this.movementClassRepository.delete(movementClass);
    }

    /**
     *
     * @param movement
     * @return
     */
    public Movement saveMovement(Movement movement) {

        // validamos se os rateios estao corretos
        if (!movement.getApportionments().isEmpty()) {
            if (!movement.isApportionmentsValid()) {
                throw new ApplicationException("movement.validate.apportionment-value",
                        movement.getApportionmentsDifference());
            }
        } else {
            throw new ApplicationException("movement.validate.empty-apportionment");
        }

        // se for uma edicao, checa se existe alguma inconsistencia
        if (movement.isSaved()) {

            if (movement.getFinancialPeriod().isClosed()) {
                throw new ApplicationException("maintenance.validate.closed-financial-period");
            }
            
            // remove algum rateio editado
            for (Apportionment apportionment : movement.getDeletedApportionments()) {
                this.apportionmentRepository.delete(apportionment);
            }
        }
        
        // pega os rateios antes de salvar o movimento para nao perder a lista
        final List<Apportionment> apportionments = movement.getApportionments();

        // salva o movimento
        movement = this.movementRepository.save(movement);

        // salva os rateios
        for (Apportionment apportionment : apportionments) {
            apportionment.setMovement(movement);
            this.apportionmentRepository.save(apportionment);
        }
        
        // busca novamente as classes
        movement.getApportionments().clear();
        movement.setApportionments(new ArrayList<>(
                this.apportionmentRepository.listByMovement(movement)));

        return movement;
    }

    /**
     *
     * @param movement
     */
    public void payAndUpdateMovement(Movement movement) {

        // salva o pagamento
        final Payment payment = this.paymentRepository.save(movement.getPayment());

        // seta no pagamento e atualiza o movimento
        movement.setPayment(payment);
        movement.setMovementStateType(MovementStateType.PAID);

        this.movementRepository.save(movement);
    }

    /**
     *
     * @param movement
     */
    public void deleteMovement(Movement movement) {

        if (movement.getFinancialPeriod().isClosed()) {
            throw new ApplicationException("maintenance.validate.closed-financial-period");
        }

        this.movementRepository.delete(movement);
    }

    /**
     * Metodo que realiza o processo de deletar um movimento vinculado a uma
     * fatura de cartao, deletando a fatura e limpando as flags dos movimentos
     * vinculados a ela
     *
     * @param cardInvoiceMovement o movimento referenciando a invoice
     */
    public void deleteCardInvoiceMovement(Movement cardInvoiceMovement) {

        final CardInvoice cardInvoice = this.cardInvoiceRepository.findByMovement(cardInvoiceMovement);

        // se a invoice for de um periodo fechado, bloqueia o processo
        if (cardInvoice.getFinancialPeriod().isClosed()) {
            throw new ApplicationException("maintenance.validate.closed-financial-period");
        }

        // listamos os movimentos da invoice
        final List<Movement> invoiceMovements = this.movementRepository.listByCardInvoice(cardInvoice);

        // limpamos as flags para cada moveimento
        for (Movement movement : invoiceMovements) {

            movement.setCardInvoice(null);
            movement.setCardInvoicePaid(false);

            this.movementRepository.save(movement);
        }

        // deletamos a invoice
        this.cardInvoiceRepository.delete(cardInvoice);

        // deletamos a movimentacao da invoice
        this.movementRepository.delete(cardInvoiceMovement);
    }

    /**
     *
     * @param costCenter
     */
    public void saveCostCenter(CostCenter costCenter) {

        final CostCenter found = this.findCostCenterByNameAndParent(costCenter.getName(),
                costCenter.getParentCostCenter());

        if (found != null) {
            throw new ApplicationException("cost-center.validate.duplicated");
        }

        this.costCenterRepository.save(costCenter);
    }

    /**
     *
     * @param costCenter
     * @return
     */
    public CostCenter updateCostCenter(CostCenter costCenter) {

        final CostCenter found = this.findCostCenterByNameAndParent(costCenter.getName(),
                costCenter.getParentCostCenter());

        if (found != null && !found.equals(costCenter)) {
            throw new ApplicationException("cost-center.validate.duplicated");
        }

        return this.costCenterRepository.save(costCenter);
    }

    /**
     *
     * @param costCenter
     */
    public void deleteCostCenter(CostCenter costCenter) {
        this.costCenterRepository.delete(costCenter);
    }

    /**
     *
     * @param movementClassId
     * @return
     */
    @Transactional(readOnly = true)
    public MovementClass findMovementClassById(long movementClassId) {
        return this.movementClassRepository.findById(movementClassId, false);
    }

    /**
     *
     * @param costCenterId
     * @return
     */
    @Transactional(readOnly = true)
    public CostCenter findCostCenterById(long costCenterId) {
        return this.costCenterRepository.findById(costCenterId, false);
    }

    /**
     *
     * @param movementId
     * @return
     */
    @Transactional(readOnly = true)
    public Movement findMovementById(long movementId) {
        return this.movementRepository.findById(movementId, false);
    }

    /**
     *
     * @param isBlocked
     * @return
     */
    @Transactional(readOnly = true)
    public List<MovementClass> listMovementClasses(Boolean isBlocked) {
        return this.movementClassRepository.listByStatus(isBlocked);
    }

    /**
     *
     * @param costCenter
     * @param type
     * @return
     */
    @Transactional(readOnly = true)
    public List<MovementClass> listMovementClassesByCostCenterAndType(CostCenter costCenter, MovementClassType type) {
        return this.movementClassRepository.listByCostCenterAndType(costCenter, type);
    }

    /**
     *
     * @param isBlocked
     * @return
     */
    @Transactional(readOnly = true)
    public List<CostCenter> listCostCenters(Boolean isBlocked) {
        return this.costCenterRepository.listByStatus(isBlocked);
    }

    /**
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<Movement> listMovementsByActiveFinancialPeriod() {
        return this.movementRepository.listByActiveFinancialPeriod();
    }

    /**
     *
     * @param period
     * @param costCenter
     * @return
     */
    @Transactional(readOnly = true)
    public List<Movement> listMovementsByPeriodAndCostCenter(FinancialPeriod period, CostCenter costCenter) {
        return this.movementRepository.listByPeriodAndCostCenter(period, costCenter);
    }

    /**
     *
     * @param dueDate
     * @param showOverdue
     * @return
     */
    @Transactional(readOnly = true)
    public List<Movement> listMovementsByDueDate(Date dueDate, boolean showOverdue) {
        return this.movementRepository.listByDueDate(dueDate, showOverdue);
    }

    /**
     *
     * @param filter
     * @param paid
     * @return
     */
    @Transactional(readOnly = true)
    public List<Movement> listByFilter(String filter, Boolean paid) {
        return this.movementRepository.listByFilter(filter, paid);
    }

    /**
     *
     * @param name
     * @param type
     * @param costCenter
     * @return
     */
    @Transactional(readOnly = true)
    public MovementClass findMovementClassByNameAndTypeAndCostCenter(String name, MovementClassType type, CostCenter costCenter) {
        return this.movementClassRepository.findByNameAndTypeAndCostCenter(name, type, costCenter);
    }

    /**
     *
     * @param name
     * @param parent
     * @return
     */
    @Transactional(readOnly = true)
    public CostCenter findCostCenterByNameAndParent(String name, CostCenter parent) {
        return this.costCenterRepository.findByNameAndParent(name, parent);
    }
}
