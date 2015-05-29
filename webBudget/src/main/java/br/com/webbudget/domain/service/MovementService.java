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

import br.com.webbudget.domain.entity.card.CardInvoice;
import br.com.webbudget.domain.entity.movement.Apportionment;
import br.com.webbudget.domain.entity.movement.CostCenter;
import br.com.webbudget.domain.entity.movement.FinancialPeriod;
import br.com.webbudget.domain.entity.movement.Movement;
import br.com.webbudget.domain.entity.movement.MovementClass;
import br.com.webbudget.domain.entity.movement.MovementClassType;
import br.com.webbudget.domain.entity.movement.MovementStateType;
import br.com.webbudget.domain.entity.movement.Payment;
import br.com.webbudget.domain.entity.movement.PaymentMethodType;
import br.com.webbudget.domain.entity.wallet.Wallet;
import br.com.webbudget.domain.entity.wallet.WalletBalance;
import br.com.webbudget.domain.entity.wallet.WalletBalanceType;
import br.com.webbudget.domain.repository.card.ICardInvoiceRepository;
import br.com.webbudget.domain.repository.movement.IApportionmentRepository;
import br.com.webbudget.domain.repository.movement.ICostCenterRepository;
import br.com.webbudget.domain.repository.movement.IMovementRepository;
import br.com.webbudget.domain.repository.movement.IMovementClassRepository;
import br.com.webbudget.domain.repository.movement.IPaymentRepository;
import br.com.webbudget.domain.repository.wallet.IWalletBalanceRepository;
import br.com.webbudget.domain.repository.wallet.IWalletRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.2.0
 * @since 1.0.0, 04/03/2014
 */
public class MovementService {

    @Inject
    private IWalletRepository walletRepository;
    @Inject
    private IPaymentRepository paymentRepository;
    @Inject
    private IMovementRepository movementRepository;
    @Inject
    private ICostCenterRepository costCenterRepository;
    @Inject
    private ICardInvoiceRepository cardInvoiceRepository;
    @Inject
    private IApportionmentRepository apportionmentRepository;
    @Inject
    private IMovementClassRepository movementClassRepository;
    @Inject
    private IWalletBalanceRepository walletBalanceRepository;

    /**
     *
     * @param movementClass
     */
    public void saveMovementClass(MovementClass movementClass) {

        final MovementClass found = this.findMovementClassByNameAndTypeAndCostCenter(movementClass.getName(),
                movementClass.getMovementClassType(), movementClass.getCostCenter());

        if (found != null) {
//            throw new ApplicationException("movement-class.validate.duplicated");
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
//            throw new ApplicationException("movement-class.validate.duplicated");
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
//                throw new ApplicationException("movement.validate.apportionment-value",
//                        movement.getApportionmentsDifference());
            }
        } else {
//            throw new ApplicationException("movement.validate.empty-apportionment");
        }

        // se for uma edicao, checa se existe alguma inconsistencia
        if (movement.isSaved()) {

            if (movement.getFinancialPeriod().isClosed()) {
//                throw new ApplicationException("maintenance.validate.closed-financial-period");
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

        // atualizamos os saldos das carteiras quando pagamento em dinheiro
        if (payment.getPaymentMethodType() == PaymentMethodType.IN_CASH
                || payment.getPaymentMethodType() == PaymentMethodType.DEBIT_CARD) {

            Wallet wallet;

            if (payment.getPaymentMethodType() == PaymentMethodType.DEBIT_CARD) {
                wallet = payment.getCard().getWallet();
            } else {
                wallet = payment.getWallet();
            }

            // atualizamos o novo saldo
            final BigDecimal oldBalance = wallet.getBalance();

            // cria o historico do saldo
            final WalletBalance walletBalance = new WalletBalance();

            walletBalance.setMovementCode(movement.getCode());
            walletBalance.setOldBalance(oldBalance);
            walletBalance.setMovimentedValue(movement.getValue());

            BigDecimal newBalance;

            if (movement.getDirection() == MovementClassType.OUT) {
                newBalance = oldBalance.subtract(movement.getValue());
                walletBalance.setWalletBalanceType(WalletBalanceType.PAYMENT);
            } else {
                newBalance = oldBalance.add(movement.getValue());
                walletBalance.setWalletBalanceType(WalletBalanceType.REVENUE);
            }

            walletBalance.setTargetWallet(wallet);
            walletBalance.setActualBalance(newBalance);

            // salva o historico do saldo
            this.walletBalanceRepository.save(walletBalance);

            wallet.setBalance(newBalance);

            // atualiza a carteira com o novo saldo
            this.walletRepository.save(wallet);
        }
    }

    /**
     *
     * @param movement
     */
    public void deleteMovement(Movement movement) {

        if (movement.getFinancialPeriod().isClosed()) {
//            throw new ApplicationException("maintenance.validate.closed-financial-period");
        }

        // se tem vinculo com fatura, nao pode ser excluido
        if (movement.isCardInvoicePaid()) {
//            throw new ApplicationException("maintenance.validate.has-card-invoice");
        }

        // devolve o saldo na carteira se for o caso
        if (movement.getMovementStateType() == MovementStateType.PAID
                && movement.getPayment().getPaymentMethodType() == PaymentMethodType.IN_CASH) {

            Wallet paymentWallet;

            final Payment payment = movement.getPayment();

            if (payment.getPaymentMethodType() == PaymentMethodType.DEBIT_CARD) {
                paymentWallet = payment.getCard().getWallet();
            } else {
                paymentWallet = payment.getWallet();
            }

            final BigDecimal movimentedValue;
            
            // se entrada, valor negativo, se saida valor positivo
            if (movement.getDirection() == MovementClassType.OUT) {
                movimentedValue = movement.getValue();
            } else {
                movimentedValue = movement.getValue().negate();
            }
            
            final BigDecimal oldBalance = paymentWallet.getBalance();

            this.returnBalance(paymentWallet, oldBalance, 
                    oldBalance.add(movimentedValue), movimentedValue);
        }

        this.movementRepository.delete(movement);
    }

    /**
     * Metodo que realiza o processo de deletar um movimento vinculado a uma
     * fatura de cartao, deletando a fatura e limpando as flags dos movimentos
     * vinculados a ela
     *
     * @param movement o movimento referenciando a invoice
     */
    public void deleteCardInvoiceMovement(Movement movement) {

        final CardInvoice cardInvoice
                = this.cardInvoiceRepository.findByMovement(movement);

        // se a invoice for de um periodo fechado, bloqueia o processo
        if (cardInvoice.getFinancialPeriod().isClosed()) {
//            throw new ApplicationException("maintenance.validate.closed-financial-period");
        }

        // listamos os movimentos da invoice
        final List<Movement> invoiceMovements
                = this.movementRepository.listByCardInvoice(cardInvoice);

        // limpamos as flags para cada moveimento
        invoiceMovements.stream().map((invoiceMovement) -> {
            invoiceMovement.setCardInvoice(null);
            return invoiceMovement;
        }).map((invoiceMovement) -> {
            invoiceMovement.setCardInvoicePaid(false);
            return invoiceMovement;
        }).forEach((invoiceMovement) -> {
            this.movementRepository.save(invoiceMovement);
        });

        // se houve pagamento, devolve o valor para a origem
        if (movement.getMovementStateType() == MovementStateType.PAID) {

            final Wallet paymentWallet = movement.getPayment().getWallet();

            final BigDecimal oldBalance = paymentWallet.getBalance();
            final BigDecimal newBalance = oldBalance.add(movement.getValue());

            this.returnBalance(paymentWallet, oldBalance, newBalance, movement.getValue());
        }

        // deletamos a movimentacao da invoice
        this.movementRepository.delete(movement);

        // deletamos a invoice
        this.cardInvoiceRepository.delete(cardInvoice);
    }

    /**
     *
     * @param costCenter
     */
    public void saveCostCenter(CostCenter costCenter) {

        final CostCenter found = this.findCostCenterByNameAndParent(costCenter.getName(),
                costCenter.getParentCostCenter());

        if (found != null) {
//            throw new ApplicationException("cost-center.validate.duplicated");
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
//            throw new ApplicationException("cost-center.validate.duplicated");
        }

        return this.costCenterRepository.save(costCenter);
    }

    /**
     *
     * @param wallet
     * @param oldBalance
     * @param newBalance
     * @param movimentedValue
     *
     * @return
     */
    private WalletBalance returnBalance(Wallet wallet, BigDecimal oldBalance,
            BigDecimal newBalance, BigDecimal movimentedValue) {

        // seta o saldo na carteira
        wallet.setBalance(newBalance);

        // salva carteira
        this.walletRepository.save(wallet);

        // coloca um novo saldo no historico
        final WalletBalance balance = new WalletBalance();

        balance.setTargetWallet(wallet);
        balance.setOldBalance(oldBalance);
        balance.setActualBalance(newBalance);
        balance.setMovimentedValue(movimentedValue);
        balance.setWalletBalanceType(WalletBalanceType.BALANCE_RETURN);

        return this.walletBalanceRepository.save(balance);
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
    @Transactional
    public MovementClass findMovementClassById(long movementClassId) {
        return this.movementClassRepository.findById(movementClassId, false);
    }

    /**
     *
     * @param costCenterId
     * @return
     */
    @Transactional
    public CostCenter findCostCenterById(long costCenterId) {
        return this.costCenterRepository.findById(costCenterId, false);
    }

    /**
     *
     * @param movementId
     * @return
     */
    @Transactional
    public Movement findMovementById(long movementId) {
        return this.movementRepository.findById(movementId, false);
    }

    /**
     *
     * @param isBlocked
     * @return
     */
    @Transactional
    public List<MovementClass> listMovementClasses(Boolean isBlocked) {
        return this.movementClassRepository.listByStatus(isBlocked);
    }

    /**
     *
     * @param costCenter
     * @param type
     * @return
     */
    @Transactional
    public List<MovementClass> listMovementClassesByCostCenterAndType(CostCenter costCenter, MovementClassType type) {
        return this.movementClassRepository.listByCostCenterAndType(costCenter, type);
    }

    /**
     *
     * @param isBlocked
     * @return
     */
    @Transactional
    public List<CostCenter> listCostCenters(Boolean isBlocked) {
        return this.costCenterRepository.listByStatus(isBlocked);
    }

    /**
     * 
     * @param financialPeriod
     * @return 
     */
    @Transactional
    public List<Movement> listMovementsByPeriod(FinancialPeriod financialPeriod) {
        return this.movementRepository.listByPeriod(financialPeriod);
    }
    
    /**
     *
     * @return
     */
    @Transactional
    public List<Movement> listMovementsByActiveFinancialPeriod() {
        return this.movementRepository.listByActiveFinancialPeriod();
    }

    /**
     *
     * @param dueDate
     * @param showOverdue
     * @return
     */
    @Transactional
    public List<Movement> listMovementsByDueDate(Date dueDate, boolean showOverdue) {
        return this.movementRepository.listByDueDate(dueDate, showOverdue);
    }

    /**
     *
     * @param filter
     * @param paid
     * @return
     */
    @Transactional
    public List<Movement> listMovementsByFilter(String filter, Boolean paid) {
        return this.movementRepository.listByFilter(filter, paid);
    }

    /**
     *
     * @param name
     * @param type
     * @param costCenter
     * @return
     */
    @Transactional
    public MovementClass findMovementClassByNameAndTypeAndCostCenter(String name, MovementClassType type, CostCenter costCenter) {
        return this.movementClassRepository.findByNameAndTypeAndCostCenter(name, type, costCenter);
    }

    /**
     *
     * @param name
     * @param parent
     * @return
     */
    @Transactional
    public CostCenter findCostCenterByNameAndParent(String name, CostCenter parent) {
        return this.costCenterRepository.findByNameAndParent(name, parent);
    }
    
    /**
     * 
     * @param cardInvoice
     * @return 
     */
    @Transactional
    public List<Movement> listMovementsByCardInvoice(CardInvoice cardInvoice) {
        return this.movementRepository.listByCardInvoice(cardInvoice);
    }
}
