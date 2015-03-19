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
package br.com.webbudget.domain.repository;

import br.com.webbudget.domain.entity.IPersistentEntity;
import java.util.List;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import org.hibernate.Session;

/**
 *
 * @param <T>
 * @param <ID>
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 03/03/2013
 */
public abstract class GenericRepository<T extends IPersistentEntity, ID extends Serializable> implements IGenericRepository<T, ID>, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    private final Class<T> persistentClass;

    /**
     *
     */
    @SuppressWarnings({"unchecked", "unsafe"})
    public GenericRepository() {
        this.persistentClass = (Class< T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     *
     * @return
     */
    protected EntityManager getEntityManager() {
        if (this.entityManager == null) {
            throw new IllegalStateException("The entityManager is not initialized");
        }
        return this.entityManager;
    }

    /**
     *
     * @return
     */
    protected Session getSession() {
        return (Session) this.getEntityManager().getDelegate();
    }

    /**
     *
     * @return
     */
    public Class<T> getPersistentClass() {
        return this.persistentClass;
    }

    /**
     *
     * @param id
     * @param lock
     * @return
     */
    @Override
    public T findById(ID id, boolean lock) {

        final T entity;

        if (lock) {
            entity = (T) this.getEntityManager().find(
                    this.getPersistentClass(), id, LockModeType.OPTIMISTIC);
        } else {
            entity = (T) this.getEntityManager().find(
                    this.getPersistentClass(), id);
        }

        return entity;
    }

    /**
     *
     * @return
     */
    @Override
    public List<T> listAll() {
        final Query query = this.getEntityManager().createQuery(
                "from " + getPersistentClass().getSimpleName());

        return query.getResultList();
    }

    /**
     *
     * @param entity
     * @return
     */
    @Override
    public T save(T entity) {
        return this.getEntityManager().merge(entity);
    }

    /**
     *
     * @param entity
     */
    @Override
    public void delete(T entity) {

        final T persistentEntity = this.getEntityManager().getReference(
                this.getPersistentClass(), entity.getId());

        this.getEntityManager().remove(persistentEntity);
    }
}
