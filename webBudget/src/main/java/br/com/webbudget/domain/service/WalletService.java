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
import br.com.webbudget.domain.entity.wallet.Wallet;
import br.com.webbudget.domain.entity.wallet.WalletBalance;
import br.com.webbudget.domain.entity.wallet.WalletBalanceType;
import br.com.webbudget.domain.entity.wallet.WalletType;
import br.com.webbudget.domain.repository.wallet.IWalletBalanceRepository;
import br.com.webbudget.domain.repository.wallet.IWalletRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 12/03/2014
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WalletService {

    @Autowired
    private IWalletRepository walletRepository;
    @Autowired
    private IWalletBalanceRepository walletBalanceRepository;

    /**
     *
     * @param wallet
     */
    public void saveWallet(Wallet wallet) {

        final Wallet found = this.findWalletByNameAndBankAndType(wallet.getName(),
                wallet.getBank(), wallet.getWalletType());

        if (found != null) {
            throw new ApplicationException("wallet.validate.duplicated");
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
    public Wallet updateWallet(Wallet wallet) {

        final Wallet found = this.findWalletByNameAndBankAndType(wallet.getName(),
                wallet.getBank(), wallet.getWalletType());

        if (found != null && !found.equals(wallet)) {
            throw new ApplicationException("wallet.validate.duplicated");
        }

        return this.walletRepository.save(wallet);
    }

    /**
     *
     * @param wallet
     */
    public void deleteWallet(Wallet wallet) {

        // checa se a carteira nao tem saldo menor ou maior que zero
        // se houve, dispara o erro, comente carteiras zeradas sao deletaveis
        if (wallet.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new ApplicationException("wallet.validate.has-balance");
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
    public void transfer(WalletBalance walletBalance) {

        if (walletBalance.getSourceWallet().equals(walletBalance.getTargetWallet())) {
            throw new ApplicationException("transfer.validate.same-wallet");
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
    @Transactional(readOnly = true)
    public Wallet findWalletById(long walletId) {
        return this.walletRepository.findById(walletId, false);
    }

    /**
     *
     * @param isBlocked
     * @return
     */
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public Wallet findWalletByNameAndBankAndType(String name, String bank, WalletType walletType) {
        return this.walletRepository.findByNameAndBankAndType(name, bank, walletType);
    }

    /**
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<WalletBalance> listTransferences() {
        return this.walletBalanceRepository.listByType(WalletBalanceType.TRANSFERENCE);
    }

    /**
     *
     * @param wallet
     * @return
     */
    @Transactional(readOnly = true)
    public List<WalletBalance> listTransfersByWallet(Wallet wallet) {
        return this.walletBalanceRepository.listByWallet(wallet, WalletBalanceType.TRANSFERENCE);
    }

    /**
     *
     * @param source
     * @param target
     * @return
     */
    @Transactional(readOnly = true)
    public List<WalletBalance> listTransfersByWallet(Wallet source, Wallet target) {
        return this.walletBalanceRepository.listByWallet(source, target, WalletBalanceType.TRANSFERENCE);
    }

    /**
     *
     * @param wallet
     * @return
     */
    @Transactional(readOnly = true)
    public List<WalletBalance> listBalancesByWallet(Wallet wallet) {
        return this.walletBalanceRepository.listByWallet(wallet);
    }
}
