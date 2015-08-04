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
import org.hibernate.Criteria;
import org.hibernate.Session;

/**
 * A implementacao padrao do repositorio generico, com esta classe habilitamos
 * o suporte as funcionalidades basicas de um repositorio de dados no banco
 *
 * @param <T> a classe persistente para este repositorio
 * @param <ID> o tipo de nossos ID
 *
 * @author Arthur Gregorio
 *
 * @version 1.1.0
 * @since 1.0.0, 03/03/2013
 */
public abstract class GenericRepository<T extends IPersistentEntity, 
        ID extends Serializable> implements IGenericRepository<T, ID>, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    private final Class<T> persistentClass;

    /**
     * Inicia o repositorio identificando qual e a classe de nossa entidade, seu
     * tipo {@link Class<?>}
     */
    @SuppressWarnings({"unchecked", "unsafe"})
    public GenericRepository() {
        this.persistentClass = (Class< T>) ((ParameterizedType)
                this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * @return nosso entityManager, inicializado e configurado
     */
    protected EntityManager getEntityManager() {
        if (this.entityManager == null) {
            throw new IllegalStateException("The entityManager is not initialized");
        }
        return this.entityManager;
    }
    
    /**
     * @return a {@link Criteria} do hibernate setada para a classe do repositorio
     */
    protected Criteria getHbmCriteria() {
        return this.getSession().createCriteria(this.getPersistentClass());
    }

    /**
     * @return a {@link Session} do Hibernate para que possamos usar nossa 
     * {@link Criteria} para buscas
     */
    protected Session getSession() {
        return (Session) this.getEntityManager().getDelegate();
    }

    /**
     * @return a classe de nossa entidade persistente
     */
    public Class<T> getPersistentClass() {
        return this.persistentClass;
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
     *
     * @param entity
     * @return
     */
    @Override
    public T save(T entity) {
        return this.getEntityManager().merge(entity);
    }

    /**
     * {@inheritDoc}
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
