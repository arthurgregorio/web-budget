package br.com.webbudget.domain.service;

import br.com.webbudget.application.exceptions.ApplicationException;
import br.com.webbudget.domain.entity.card.Card;
import br.com.webbudget.domain.entity.card.CardType;
import br.com.webbudget.domain.entity.closing.Closing;
import br.com.webbudget.domain.entity.movement.FinancialPeriod;
import br.com.webbudget.domain.entity.movement.Movement;
import br.com.webbudget.domain.entity.movement.MovementClass;
import br.com.webbudget.domain.entity.movement.MovementClassType;
import br.com.webbudget.domain.entity.movement.MovementStateType;
import br.com.webbudget.domain.entity.wallet.Wallet;
import br.com.webbudget.domain.entity.wallet.WalletBalance;
import br.com.webbudget.domain.entity.wallet.WalletBalanceType;
import br.com.webbudget.domain.repository.card.ICardRepository;
import br.com.webbudget.domain.repository.movement.IClosingRepository;
import br.com.webbudget.domain.repository.movement.IFinancialPeriodRepository;
import br.com.webbudget.domain.repository.movement.IMovementClassRepository;
import br.com.webbudget.domain.repository.movement.IMovementRepository;
import br.com.webbudget.domain.repository.wallet.IWalletBalanceRepository;
import br.com.webbudget.domain.repository.wallet.IWalletRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
 * @since 1.0, 09/04/2014
 */
@Service
public class ClosingService {

    @Autowired
    private ICardRepository cardRepository;
    @Autowired
    private IWalletRepository walletRepository;
    @Autowired
    private IClosingRepository closingRepository;
    @Autowired
    private IMovementRepository movementRepository;
    @Autowired
    private IWalletBalanceRepository walletBalanceRepository;
    @Autowired
    private IMovementClassRepository movementClassRepository;
    @Autowired
    private IFinancialPeriodRepository financialPeriodRepository;

    /**
     * 
     * @param financialPeriod
     * @return 
     */
    @Transactional(readOnly = true)
    public Closing simulate(FinancialPeriod financialPeriod) {
        
        // comecamos o processo de calculo instanciando o fechamento com os 
        // valores individuais por tipos
        final Closing closing = this.calculateInAndOutsTotals(financialPeriod);
        
        // top 5 classes com mais entradas
        final List<MovementClass> classesIn = this.countTopConsumeClassesByType(
                MovementClassType.IN, financialPeriod);
        
        closing.setTopFiveClassesIn(new ArrayList<>(classesIn));
        
        // top 5 classes com mais saidas
        final List<MovementClass> classesOut = this.countTopConsumeClassesByType(
                MovementClassType.OUT, financialPeriod);
        
        closing.setTopFiveClassesOut(new ArrayList<>(classesOut));
        
        // fim dos calculos, retorna pra view
        return closing;
    }
    
    /**
     * 
     * @param financialPeriod 
     */
    @Transactional
    public void close(FinancialPeriod financialPeriod) {
        
        // processamos os valores de entradas e saidas
        Closing closing = this.calculateInAndOutsTotals(financialPeriod);
        
        closing.setInValue(closing.getTotalInsOpen().add(closing.getTotalInsPaid()));
        closing.setOutValue(closing.getTotalOutsOpen().add(closing.getTotalOutsPaid()));
        
        closing.setClosingDate(new Date());
        
        // salvamos o fechamento
        closing = this.closingRepository.save(closing);
        
        // construimos o saldo final das carteiras 
        final List<Wallet> wallets = this.walletRepository.listByStatus(false);
        
        for (Wallet wallet : wallets) {
            
            BigDecimal totalIns = this.movementRepository
                    .findTotalByWallet(wallet, MovementClassType.IN, financialPeriod);
            BigDecimal totalOuts = this.movementRepository
                    .findTotalByWallet(wallet, MovementClassType.OUT, financialPeriod);
            
            // listamos todos os pagamentos no cartao de debito para a carteira
            BigDecimal totalOutsDebit = this.movementRepository
                    .findTotalOutsByWalletOnDebitCards(wallet, financialPeriod);
            
            // se zerar os valores, calculamos igual mas com tudo zerado
            if (totalIns == null) {
                totalIns = BigDecimal.ZERO;
            }

            if (totalOuts == null) {
                totalOuts = BigDecimal.ZERO;
            }
            
            if (totalOutsDebit == null) {
                totalOutsDebit = BigDecimal.ZERO;
            }
            
            // somamos os pagos no debito e os pagos direto
            totalOuts = totalOuts.add(totalOutsDebit);
            
            // geramos os saldos
            final WalletBalance newBalance = new WalletBalance();
            final WalletBalance lastBalance = this.walletBalanceRepository.findLastWalletBalance(wallet);

            // calculamos com base no ultimo saldo ou no salredo inicial da conta
            if (lastBalance != null) {
                final BigDecimal balance = lastBalance.getBalance().add(totalIns);
                newBalance.setBalance(balance.subtract(totalOuts));
            } else {
                final BigDecimal balance = wallet.getBalance().add(totalIns);
                newBalance.setBalance(balance.subtract(totalOuts));
            }

            // dizemos qual wallet e fechamento o balanco pertence
            newBalance.setWallet(wallet);
            newBalance.setClosing(closing);
            newBalance.setWalletBalanceType(WalletBalanceType.CLOSING_BALANCE);
            
            // setamos os valores totais
            newBalance.setIns(totalIns);
            newBalance.setOuts(totalOuts);

            // atualizamos o saldo na carteira
            wallet.setBalance(newBalance.getBalance());
            this.walletRepository.save(wallet);
            
            // salvamos
            this.walletBalanceRepository.save(newBalance);
        }
        
        // listamos todos os movimentos para marcar como calculado
        final List<Movement> movements = this.movementRepository
                .listByFinancialPeriod(financialPeriod);
        
        for (Movement movement : movements) {
            movement.setMovementStateType(MovementStateType.CALCULATED);
            this.movementRepository.save(movement);
        }
        
        // fechamos o periodo e salvamos
        financialPeriod.setClosed(true);
        financialPeriod.setClosing(closing);
        
        this.financialPeriodRepository.save(financialPeriod);
    } 
    
    /**
     * 
     * @param financialPeriod
     * @param closing
     * @return 
     */
    @Transactional
    public Closing process(FinancialPeriod financialPeriod, Closing closing) {
        
        if (closing == null) {
            closing = new Closing();
        } else {
            // buscamos o novo periodo para contas que serao transferidas
            final List<FinancialPeriod> openPeriods = this.financialPeriodRepository.listOpen();
            
            if (openPeriods != null && !openPeriods.isEmpty()) {

                final FinancialPeriod newPeriod = openPeriods.get(0);
                
                // transferimos ou deletamos os movimentos abertos
                for (Movement movement : closing.getOpenMovements()) {
                    if (movement.isTransfer()) {
                        if (newPeriod.equals(movement.getFinancialPeriod())) {
                            throw new ApplicationException("closing.validate.cant-move-to-same-period");
                        } else {
                            movement.setFinancialPeriod(newPeriod);
                            this.movementRepository.save(movement);
                        }
                    } else if (movement.isDelete()) {
                        this.movementRepository.delete(movement);
                    }
                }
            } else {
                throw new ApplicationException("closing.validate.no-period-to-transfer");
            }
        }
       
        // verificamos por cartoes de credito com debitos sem fatura
        final List<Card> cards = this.cardRepository.listByStatus(false);
        
        for (Card card : cards) {
            if (card.getCardType() != CardType.DEBIT) {
                final List<Movement> movements = this.movementRepository.listByPeriodAndCard(financialPeriod, card);

                if (!movements.isEmpty()) {
                    throw new ApplicationException("closing.validate.movements-no-invoice");
                }
            }
        }
        
        // atualizamos a lista de movimentos em aberto
        final List<Movement> openMovements = this.movementRepository.listOpenMovementsByPeriod(financialPeriod);
        closing.setOpenMovements(openMovements);
        
        return closing;
    } 
    
    /**
     * 
     * @param financialPeriod
     * @param closing
     * @return 
     */
    private Closing calculateInAndOutsTotals(FinancialPeriod financialPeriod) {
        
        final Closing closing = new Closing();
        
        final List<Movement> ins = this.movementRepository.listInsByFinancialPeriod(financialPeriod);
        
        BigDecimal totalIns = BigDecimal.ZERO;
        BigDecimal totalInsPaid = BigDecimal.ZERO;
        BigDecimal totalInsOpen = BigDecimal.ZERO;
        
        // iteramos nos movimentos de entrada
        for (Movement movement : ins) {
            
            // pegamos somente movimentos abertos ou pagos
            if (movement.getMovementStateType() == MovementStateType.OPEN) {
                totalInsOpen = totalInsOpen.add(movement.getValue());
            } else if (movement.getMovementStateType() == MovementStateType.PAID) {
                totalInsPaid = totalInsPaid.add(movement.getValue());
            }
            
            // soma total de movimentos
            totalIns = totalIns.add(totalInsOpen.add(totalInsPaid));
        }

        final List<Movement> outs = this.movementRepository.listOutsByFinancialPeriod(financialPeriod);
        
        BigDecimal totalOuts = BigDecimal.ZERO;
        BigDecimal totalOutsPaid = BigDecimal.ZERO;
        BigDecimal totalOutsOpen = BigDecimal.ZERO;
        
        // iteramos nos movimentos de saida
        for (Movement movement : outs) {
            
            // pegamos somente movimentos abertos ou pagos
            if (movement.getMovementStateType() == MovementStateType.OPEN) {
                totalOutsOpen = totalOutsOpen.add(movement.getValue());
            } else if (movement.getMovementStateType() == MovementStateType.PAID) {
                totalOutsPaid = totalOutsPaid.add(movement.getValue());
            }
            
            // soma total de movimentos
            totalOuts = totalOuts.add(totalOutsOpen.add(totalOutsPaid));
        }
        
        // setamos os totais individuais
        closing.setTotalInsOpen(totalInsOpen);
        closing.setTotalInsPaid(totalInsPaid);
        closing.setTotalOutsOpen(totalOutsOpen);
        closing.setTotalOutsPaid(totalOutsPaid);
        
        // setamos o total geral
        closing.setInValue(totalIns);
        closing.setOutValue(totalOuts);
        
        return closing;
    }
    
    /**
     * 
     * @param type
     * @param financialPeriod
     * @return 
     */
    private List<MovementClass> countTopConsumeClassesByType(MovementClassType type, FinancialPeriod financialPeriod) {
        
        final List<MovementClass> topClasses = new ArrayList<>();
        final List<MovementClass> classes = this.movementClassRepository.listByTypeAndStatus(type, false);

        for (MovementClass movementClass : classes) {
                         
           final BigDecimal total = this.movementRepository
                    .countMovementTotalByClassAndPeriod(financialPeriod, movementClass);
            
            movementClass.setTotalMovements(total);
        }
        
        // ordenamos a lista pelos maiores
        Collections.sort(classes, new Comparator<MovementClass>() {
            @Override
            public int compare(MovementClass classOne, MovementClass classTwo) {
                
                if (classOne.getTotalMovements() == null) {
                    return 1;
                } else if (classTwo.getTotalMovements() == null) {
                    return -1;
                } else {
                    return classTwo.getTotalMovements().compareTo(classOne.getTotalMovements());
                }
            }
        });
        
        if (classes.size() < 5) {
            topClasses.addAll(new ArrayList<>(classes.subList(0, classes.size())));
        } else {
            topClasses.addAll(new ArrayList<>(classes.subList(0, 5)));
        }
        
        return topClasses;
    }
}
