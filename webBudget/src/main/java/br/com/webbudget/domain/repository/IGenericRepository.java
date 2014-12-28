package br.com.webbudget.domain.repository;

import br.com.webbudget.domain.entity.IPersistentEntity;
import java.util.List;
import java.io.Serializable;

/**
 * 
 * @param <T>
 * @param <ID> 
 * 
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 03/03/2013
 */
public interface IGenericRepository<T extends IPersistentEntity, ID extends Serializable> {
 
    /**
     * 
     * @return 
     */
    List<T> listAll();
    
    /**
     * 
     * @param id
     * @param lock
     * @return 
     */
    T findById(ID id, boolean lock);
 
    /**
     * 
     * @param entity
     * @return 
     */
    T save(T entity);
    
    /**
     * 
     * @param entity 
     */
    void delete(T entity);
}