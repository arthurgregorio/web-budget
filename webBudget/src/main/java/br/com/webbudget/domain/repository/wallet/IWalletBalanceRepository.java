package br.com.webbudget.domain.repository.wallet;

import br.com.webbudget.domain.entity.wallet.Wallet;
import br.com.webbudget.domain.entity.wallet.WalletBalance;
import br.com.webbudget.domain.entity.wallet.WalletBalanceType;
import br.com.webbudget.domain.repository.IGenericRepository;
import java.util.List;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 04/03/2013
 */
public interface IWalletBalanceRepository extends IGenericRepository<WalletBalance, Long> {

    /**
     * 
     * @param wallet
     * @return 
     */
    public WalletBalance findLastWalletBalance(Wallet wallet);
    
    /**
     * 
     * @param walletBalanceType
     * @return 
     */
    public List<WalletBalance> listByType(WalletBalanceType walletBalanceType);
    
    /**
     * 
     * @param wallet
     * @return 
     */
    public List<WalletBalance> listByWallet(Wallet wallet, WalletBalanceType walletBalanceType);
}
