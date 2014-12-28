package br.com.webbudget.domain.repository.user;

import br.com.webbudget.domain.entity.users.Permission;
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
public interface IPermissionRepository extends IGenericRepository<Permission, Long> {
    
    /**
     * 
     * @param user
     * @return 
     */
    public List<Permission> listByUser(User user);
}
