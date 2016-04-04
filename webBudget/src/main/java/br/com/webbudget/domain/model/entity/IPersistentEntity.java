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
package br.com.webbudget.domain.model.entity;

import java.io.Serializable;

/**
 * Interface que define os metodos minimos que uma entidade deve possuir para
 * ser considerada uma entitdade valida na regra de negocios deste sistema
 *
 * @param <T> qualquer coisa que seja serializavel
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 06/10/2013
 */
public interface IPersistentEntity<T extends Serializable> {

    /**
     * Getter para o ID da entidade
     *
     * @return o id da entidade
     */
    public T getId();

    /**
     * Metodo que indica se uma entidade ja foi ou nao persistida (salva)
     *
     * @return se a entidade ja foi persistida, retorna <code>true</code>
     * indicando
     * que a mesma ja foi salva se nao retorna <code>false</code>
     */
    public boolean isSaved();
}
