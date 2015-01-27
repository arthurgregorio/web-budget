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
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 12/03/2014
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

        this.walletRepository.save(wallet);
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
        this.walletRepository.delete(wallet);
    }

    /**
     * 
     * @param walletBalance 
     */
    public void transfer(WalletBalance walletBalance) {
       
        if (walletBalance.getSourceWallet().equals(walletBalance.getWallet())) {
            throw new ApplicationException("transfer.validate.same-wallet");
        }
        
        // atualizamos a origem
        final Wallet source = walletBalance.getSourceWallet();
        
        source.setBalance(source.getBalance().subtract(walletBalance.getTransferValue()));
        
        this.walletRepository.save(source);
        
        // atualizamos o destino
        final Wallet destiny = walletBalance.getWallet();
        
        destiny.setBalance(destiny.getBalance().add(walletBalance.getTransferValue()));
        
        this.walletRepository.save(destiny);
        
        // completamos a transferencia para o destino
        walletBalance.setBalance(destiny.getBalance());
        walletBalance.setWalletBalanceType(WalletBalanceType.TRANSFERENCE);

        this.walletBalanceRepository.save(walletBalance);
        
        // criamos novo saldo para a origem
        final WalletBalance sourceBalance = new WalletBalance();
        
        sourceBalance.setWallet(source);
        sourceBalance.setBalance(source.getBalance());
        sourceBalance.setAdjustmentValue(walletBalance.getTransferValue());
        sourceBalance.setWalletBalanceType(WalletBalanceType.TRANSFER_ADJUSTMENT);
        
        this.walletBalanceRepository.save(sourceBalance);
    }
    
    /**
     * 
     * @param wallet 
     */
    public void adjustBalance(Wallet wallet) {

        final WalletBalance walletBalance = new WalletBalance();
        
        // gravamos o ultimo saldo como historico
        walletBalance.setWallet(wallet);
        walletBalance.setAdjustmentValue(wallet.getAdjustmentValue());
        walletBalance.setOldBalance(wallet.getBalance());
        walletBalance.setBalance(wallet.getAdjustmentValue());
        walletBalance.setWalletBalanceType(WalletBalanceType.ADJUSTMENT);

        this.walletBalanceRepository.save(walletBalance);
        
        // atualizamos o novo saldo
        wallet.setBalance(walletBalance.getAdjustmentValue());
        
        this.walletRepository.save(wallet);
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
}
