package br.com.webbudget.domain.repository.user;

import br.com.webbudget.domain.entity.users.PrivateMessage;
import br.com.webbudget.domain.entity.users.User;
import br.com.webbudget.domain.repository.IGenericRepository;
import java.util.List;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 04/03/2013
 */
public interface IPrivateMessageRepository extends IGenericRepository<PrivateMessage, Long> {

    /**
     * 
     * @param user
     * @return 
     */
    public List<PrivateMessage> listSent(User user);
    
    /**
     * 
     * @param user
     * @param showUnread
     * @return 
     */
    public List<PrivateMessage> listByUser(User user, boolean showUnread);
}
