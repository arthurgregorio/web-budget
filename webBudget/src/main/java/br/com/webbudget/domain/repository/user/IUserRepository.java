package br.com.webbudget.domain.repository.user;

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
public interface IUserRepository extends IGenericRepository<User, Long> {

    /**
     * 
     * @param username
     * @return 
     */
    public User findByUsername(String username);
    
    /**
     * 
     * @param blocked
     * @return 
     */
    public List<User> listByStatus(Boolean blocked);
    
    /**
     * 
     * @param blocked
     * @param authenticated
     * @return 
     */
    public List<User> listByStatusAndRemoveAuthenticated(Boolean blocked, User authenticated);
}
