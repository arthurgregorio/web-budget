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
import br.com.webbudget.domain.misc.ex.WbDomainException;
import br.com.webbudget.domain.repository.wallet.IWalletBalanceRepository;
import br.com.webbudget.domain.repository.wallet.IWalletRepository;
import java.math.BigDecimal;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

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
            throw new WbDomainException("wallet.validate.duplicated");
        }

        wallet = this.walletRepository.save(wallet);

        // se a carteira teve um saldo inicial != 0, entao ajustamos ela para o
        // saldo informado pelo usuario
        if (wallet.getBalance().compareTo(BigDecimal.ZERO) != 0) {

            // saldo antigo era 0
            final BigDecimal oldBalance = BigDecimal.ZERO;

            final WalletBalance walletBalance = new WalletBalance();

            // gravamos o historico do saldo
            walletBalance.setTargetWallet(wallet);
            walletBalance.setMovimentedValue(wallet.getBalance());
            walletBalance.setOldBalance(oldBalance);
            walletBalance.setActualBalance(oldBalance.add(wallet.getBalance()));
            walletBalance.setWalletBalanceType(WalletBalanceType.ADJUSTMENT);

            this.walletBalanceRepository.save(walletBalance);
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
            throw new WbDomainException("wallet.validate.duplicated");
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
            throw new WbDomainException("wallet.validate.has-balance");
        }

        final List<WalletBalance> balaces = this.listBalancesByWallet(wallet);

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
            throw new WbDomainException("transfer.validate.same-wallet");
        }

        // atualizamos a origem
        final Wallet source = walletBalance.getSourceWallet();
        final BigDecimal sourceOldBalance = source.getBalance();

        source.setBalance(sourceOldBalance.subtract(walletBalance.getMovimentedValue()));

        this.walletRepository.save(source);

        // atualizamos o destino
        final Wallet target = walletBalance.getTargetWallet();
        final BigDecimal targetOldBalance = target.getBalance();

        target.setBalance(targetOldBalance.add(walletBalance.getMovimentedValue()));

        this.walletRepository.save(target);

        // completamos a transferencia para o destino
        walletBalance.setOldBalance(targetOldBalance);
        walletBalance.setActualBalance(target.getBalance());
        walletBalance.setWalletBalanceType(WalletBalanceType.TRANSFERENCE);

        this.walletBalanceRepository.save(walletBalance);

        // criamos novo saldo para a origem
        final WalletBalance sourceBalance = new WalletBalance();

        sourceBalance.setTargetWallet(source);
        sourceBalance.setOldBalance(sourceOldBalance);
        sourceBalance.setActualBalance(source.getBalance());
        sourceBalance.setMovimentedValue(walletBalance.getMovimentedValue());
        sourceBalance.setWalletBalanceType(WalletBalanceType.TRANSFER_ADJUSTMENT);

        this.walletBalanceRepository.save(sourceBalance);
    }

    /**
     *
     * @param wallet
     */
    @Transactional
    public void adjustBalance(Wallet wallet) {

        // atualizamos o novo saldo
        final BigDecimal oldBalance = wallet.getBalance();
        final BigDecimal newBalance = oldBalance.add(wallet.getAdjustmentValue());

        wallet.setBalance(newBalance);

        this.walletRepository.save(wallet);

        final WalletBalance walletBalance = new WalletBalance();

        // gravamos o ultimo saldo como historico
        walletBalance.setTargetWallet(wallet);
        walletBalance.setMovimentedValue(wallet.getAdjustmentValue());
        walletBalance.setOldBalance(oldBalance);
        walletBalance.setActualBalance(newBalance);
        walletBalance.setReason(wallet.getReason());
        walletBalance.setWalletBalanceType(WalletBalanceType.ADJUSTMENT);

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
     * @param isBlocked
     * @return
     */
    public List<Wallet> listWallets(Boolean isBlocked) {
        return this.walletRepository.listByStatus(isBlocked);
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
     * @return
     */
    public List<WalletBalance> listTransferences() {
        return this.walletBalanceRepository.listByType(WalletBalanceType.TRANSFERENCE);
    }

    /**
     *
     * @param wallet
     * @return
     */
    public List<WalletBalance> listTransfersByWallet(Wallet wallet) {
        return this.walletBalanceRepository.listByWallet(wallet, WalletBalanceType.TRANSFERENCE);
    }

    /**
     *
     * @param source
     * @param target
     * @return
     */
    public List<WalletBalance> listTransfersByWallet(Wallet source, Wallet target) {
        return this.walletBalanceRepository.listByWallet(source, target, WalletBalanceType.TRANSFERENCE);
    }

    /**
     *
     * @param wallet
     * @return
     */
    public List<WalletBalance> listBalancesByWallet(Wallet wallet) {
        return this.walletBalanceRepository.listByWallet(wallet);
    }
}
