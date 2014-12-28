package br.com.webbudget.domain.repository.user;

import br.com.webbudget.domain.entity.users.PrivateMessage;
import br.com.webbudget.domain.entity.users.User;
import br.com.webbudget.domain.entity.users.UserPrivateMessage;
import br.com.webbudget.domain.repository.IGenericRepository;
import java.util.List;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 04/03/2013
 */
public interface IUserPrivateMessageRepository extends IGenericRepository<UserPrivateMessage, Long> {

    /**
     * 
     * @param user
     * @param showUnread
     * @return 
     */
    public List<UserPrivateMessage> listByUser(User user, Boolean showUnread);
    
    /**
     * 
     * @param privateMessage
     * @return 
     */
    public List<UserPrivateMessage> listReceipts(PrivateMessage privateMessage);
}
