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
package br.com.webbudget.domain.model.repository;

import br.com.webbudget.domain.model.entity.IPersistentEntity;
import java.util.List;
import java.io.Serializable;

/**
 * Repositorio generico para facilitar o uso de repositorios no desenvolvimento
 * de novas funcionalidade com acesso aos dados do banco
 *
 * @param <T> o tipo de entidade deste repositorio
 * @param <ID> o tipo de id que sera usado
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 03/03/2013
 */
public interface IGenericRepository<T extends IPersistentEntity, ID extends Serializable> {

    /**
     * Lista todos os itens, sem filtro
     *
     * @return todos os itens daquela entidade
     */
    List<T> listAll();

    /**
     * Tras apenas um objeto, filtrado pelo seu ID
     *
     * @param id o id que se deseja buscar
     * @param lock se devemos usar ou nao o lock
     * 
     * @return o objeto pesquisado
     */
    T findById(ID id, boolean lock);
    
    /**
     * Conta todos os registros da tabela
     * 
     * @return o numero de registros (rows) da tabela
     */
    Long count();

    /**
     * Salva um entidade no banco caso ela nao exista ou atualiza ela caso o 
     * objeto passado como parametro ja exista 
     *
     * @param entity a entidade a ser salva (incluida ou atualizada)
     * 
     * @return a entidade manipulada. Se ela acaba de ser  incluida entao o seu
     *         o seu ID sera setado no objeto de retorno
     */
    T save(T entity);

    /**
     * Deleta uma entidade pelo seu objeto persistente
     *
     * @param entity a entidade a ser deletada
     */
    void delete(T entity);
}
