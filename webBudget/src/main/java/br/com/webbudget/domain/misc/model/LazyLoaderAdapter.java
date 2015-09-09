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
package br.com.webbudget.domain.misc.model;

import br.com.webbudget.domain.entity.IPersistentEntity;
import java.util.List;

/**
 * Adapter para que o nosso lazy model consiga carregar os dados de maneira 
 * lazy invocando a instancia do repositorio da classe atrelada ao model
 *
 * @param <T> a entidade que corresponde a este model
 * 
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.1.0, 05/09/2015
 */
public interface LazyLoaderAdapter<T extends IPersistentEntity> {

    /**
     * Quando invocado, deve chamar o metodo de paginacao definido no repositorio
     * corresponde a classe do tipo generico
     * 
     * @param filterBuilder o filtro a ser aplicado
     * @return a lista de dados
     */
    public List<T> loadLazy(FilterBuilder filterBuilder);
    
    /**
     * Contamos a quantidade de registros para que a paginacao funcione
     * 
     * @return a quantidade de registros para paginacao
     */
    public Long count();
}
