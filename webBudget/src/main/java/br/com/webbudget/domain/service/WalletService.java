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

import br.com.webbudget.domain.entity.wallet.Wallet;
import br.com.webbudget.domain.entity.wallet.WalletBalance;
import br.com.webbudget.domain.entity.wallet.WalletBalanceType;
import br.com.webbudget.domain.entity.wallet.WalletType;
import br.com.webbudget.domain.misc.BalanceBuilder;
import br.com.webbudget.domain.misc.ex.InternalServiceError;
import br.com.webbudget.domain.repository.wallet.IWalletBalanceRepository;
import br.com.webbudget.domain.repository.wallet.IWalletRepository;
import java.math.BigDecimal;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;
import br.com.webbudget.domain.misc.events.UpdateBalance;
import br.com.webbudget.domain.misc.table.Page;
import br.com.webbudget.domain.misc.table.PageRequest;

/**
 * Serice para manutencao dos processos relacionados a carteiras e saldos 
 *
 * @author Arthur Gregorio
 *
 * @version 1.2.0
 * @since 1.0.0, 12/03/2014
 */
@ApplicationScoped
public class WalletService {

    @Inject
    private IWalletRepository walletRepository;
    @Inject
    private IWalletBalanceRepository walletBalanceRepository;

    /**
     *
     * @param wallet
     */
    @Transactional
    public void saveWallet(Wallet wallet) {

        final Wallet found = this.findWalletByNameAndBankAndType(wallet.getName(),
                wallet.getBank(), wallet.getWalletType());

        if (found != null) {
            throw new InternalServiceError("error.wallet.duplicated");
        }

        wallet = this.walletRepository.save(wallet);

        // se a carteira teve um saldo inicial != 0, entao ajustamos ela para o
        // saldo informado pelo usuario no momento da criacao
        if (wallet.getBalance().compareTo(BigDecimal.ZERO) != 0) {

            final BalanceBuilder builder = new BalanceBuilder();
            
            builder.forWallet(wallet)
                    .withOldBalance(BigDecimal.ZERO)
                    .withActualBalance(wallet.getBalance())
                    .withMovementedValue(wallet.getBalance())
                    .andType(WalletBalanceType.ADJUSTMENT);

            this.updateBalance(builder);
        }
    }

    /**
     *
     * @param wallet
     * @return
     */
    @Transactional
    public Wallet updateWallet(Wallet wallet) {

        final Wallet found = this.findWalletByNameAndBankAndType(wallet.getName(),
                wallet.getBank(), wallet.getWalletType());

        if (found != null && !found.equals(wallet)) {
            throw new InternalServiceError("error.wallet.duplicated");
        }

        return this.walletRepository.save(wallet);
    }

    /**
     *
     * @param wallet
     */
    @Transactional
    public void deleteWallet(Wallet wallet) {

        // checa se a carteira nao tem saldo menor ou maior que zero
        // se houve, dispara o erro, comente carteiras zeradas sao deletaveis
        if (wallet.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new InternalServiceError("error.wallet.has-balance");
        }

        final List<WalletBalance> balaces = this.listBalances(wallet);

        balaces.stream().forEach((balance) -> {
            this.walletBalanceRepository.delete(balance);
        });

        this.walletRepository.delete(wallet);
    }

    /**
     *
     * @param walletBalance
     */
    @Transactional
    public void transfer(WalletBalance walletBalance) {

        if (walletBalance.getSourceWallet().equals(walletBalance.getTargetWallet())) {
            throw new InternalServiceError("transfer.validate.same-wallet");
        }

        // atualizamos o destino
        final BalanceBuilder builderTarget = new BalanceBuilder();
        
        final Wallet target = walletBalance.getTargetWallet();
        
        final BigDecimal targetOldBalance = target.getBalance();
        final BigDecimal targetNewBalance = 
                targetOldBalance.add(walletBalance.getMovementedValue());

        builderTarget.forWallet(target)
                .withOldBalance(targetOldBalance)
                .withActualBalance(targetNewBalance)
                .withMovementedValue(walletBalance.getMovementedValue())
                .byTheReason(walletBalance.getReason())
                .andType(WalletBalanceType.TRANSFERENCE);
        
        this.updateBalance(builderTarget);

        // atualizamos a origem
        final BalanceBuilder builderSource = new BalanceBuilder();
        
        final Wallet source = walletBalance.getSourceWallet();
        
        final BigDecimal sourceOldBalance = source.getBalance();
        final BigDecimal sourceNewBalance = 
                sourceOldBalance.subtract(walletBalance.getMovementedValue());

        builderSource.forWallet(source)
                .withOldBalance(sourceOldBalance)
                .withActualBalance(sourceNewBalance)
                .withMovementedValue(walletBalance.getMovementedValue())
                .andType(WalletBalanceType.TRANSFER_ADJUSTMENT);
        
        this.updateBalance(builderSource);
    }

    /**
     * Chamada para ajuste do saldo da carteira
     * 
     * @param wallet a carteira a ser ajustada, dentro dela os dados do ajuste
     */
    @Transactional
    public void adjustBalance(Wallet wallet) {

        // atualizamos o novo saldo
        final BigDecimal oldBalance = wallet.getBalance();
        final BigDecimal newBalance = oldBalance.add(wallet.getAdjustmentValue());
        
        final BalanceBuilder builder = new BalanceBuilder();

        builder.forWallet(wallet)
                .withOldBalance(oldBalance)
                .withActualBalance(newBalance)
                .withMovementedValue(wallet.getAdjustmentValue())
                .byTheReason(wallet.getReason())
                .andType(WalletBalanceType.ADJUSTMENT);
        
        this.updateBalance(builder);
    }

    /**
     * Metodo que escuta por eventos de edicao do saldo das carteiras e entao
     * ao receber uma chamada, atualiza o saldo e grava o historico de saldo
     * 
     * OBS: todas as atualizacoes de saldo dentro do sistema DEVEM seguir o 
     * fluxo de evento chegando ate este metodo
     * 
     * @param builder o builder para contrucao do saldo
     */
    @Transactional
    public void updateBalance(@Observes @UpdateBalance BalanceBuilder builder) {
       
        final WalletBalance walletBalance = builder.build();
        
        final Wallet wallet = walletBalance.getTargetWallet();
        
        // seta o saldo na carteira
        wallet.setBalance(walletBalance.getActualBalance());

        // salva carteira
        this.walletRepository.save(wallet);

        // salva o saldo
        this.walletBalanceRepository.save(walletBalance);
    }
    
    /**
     *
     * @param walletId
     * @return
     */
    public Wallet findWalletById(long walletId) {
        return this.walletRepository.findById(walletId, false);
    }

    /**
     *
     * @param name
     * @param bank
     * @param walletType
     * @return
     */
    public Wallet findWalletByNameAndBankAndType(String name, String bank, WalletType walletType) {
        return this.walletRepository.findByNameAndBankAndType(name, bank, walletType);
    }

    /**
     * 
     * @param isBlocked
     * @param pageRequest
     * @return 
     */
    public Page<Wallet> listWalletsLazily(Boolean isBlocked, PageRequest pageRequest) {
        return this.walletRepository.listLazilyByStatus(isBlocked, pageRequest);
    }
    
    /**
     *S
     * @param isBlocked
     * @return
     */
    public List<Wallet> listWallets(Boolean isBlocked) {
        return this.walletRepository.listByStatus(isBlocked);
    }
    
    /**
     * 
     * @param wallet
     * @return 
     */
    public List<WalletBalance> listBalances(Wallet wallet) {
        return this.walletBalanceRepository.listByWallet(null, wallet);
    }
    
    /**
     *
     * @param source
     * @param target
     * @return
     */
    public List<WalletBalance> listTransfers(Wallet source, Wallet target) {
        return this.walletBalanceRepository.listByWallet(source, target, WalletBalanceType.TRANSFERENCE);
    }
}
