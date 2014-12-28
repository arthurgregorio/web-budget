package br.com.webbudget.domain.repository.wallet;

import br.com.webbudget.domain.entity.wallet.Wallet;
import br.com.webbudget.domain.entity.wallet.WalletType;
import br.com.webbudget.domain.repository.IGenericRepository;
import java.util.List;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 04/03/2013
 */
public interface IWalletRepository extends IGenericRepository<Wallet, Long> {

    /**
     * 
     * @param isBlocked
     * @return 
     */
    public List<Wallet> listByStatus(Boolean isBlocked);
    
    /**
     * 
     * @param name
     * @param bank
     * @param walletType
     * @return 
     */
    public Wallet findByNameAndBankAndType(String name, String bank, WalletType walletType);
}
