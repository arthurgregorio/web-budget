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
