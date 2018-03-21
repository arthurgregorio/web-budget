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
package br.com.webbudget.domain.entities;

import java.io.Serializable;

/**
 * Interface que define os metodos minimos que uma entidade deve possuir para
 * ser considerada uma entitdade valida na regra de negocios deste sistema
 *
 * @param <T> qualquer coisa que seja serializavel
 *
 * @author Arthur Gregorio
 *
 * @version 1.1.0
 * @since 1.0.0, 06/10/2013
 */
public interface IPersistentEntity<T extends Serializable> {

    /**
     * Define que toda as entidades devem ter um getter para o ID
     * 
     * @return o ID da entidade
     */
    T getId();

    /**
     * Metodo de utilidade para determinar o estado da entidade
     * 
     * @return se a entidade foi salva ou nao
     */
    boolean isSaved();
    
    /**
     * Metodo de conveniencia para utilizacao em streams com method reference
     * 
     * @return se a entidade ja foi ou nao salva
     */
    default boolean isNotSaved() {
        return !this.isSaved();
    }
}
