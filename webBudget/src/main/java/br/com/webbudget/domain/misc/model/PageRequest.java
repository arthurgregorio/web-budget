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

import lombok.Getter;

/**
 * Builder para producao dos filtros da pesquisa lazy
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.1.0, 05/09/2015
 */
public class PageRequest {

    @Getter
    private int firstResult;
    @Getter
    private int pageSize;
    @Getter
    private SortDirection sortDirection;
    
    private String sortField;
    private String defaultField;
    
    /**
     * @param first o primeiro resultado 
     * @return o builder
     */
    public PageRequest setFirstResult(int first) {
        this.firstResult = first;
        return this;
    }
    
    /**
     * @param size o tamanho maximo da pagina
     * @return o builder
     */
    public PageRequest withPageSize(int size) {
        this.pageSize = size;
        return this;
    }
    
    /**
     * @param field o campo a ser usado como ordenador
     * @param defaultField o campo a ser usado como ordenador padrao
     * @return o builder
     */
    public PageRequest sortingBy(String field, String defaultField) {
        this.sortField = field;
        this.defaultField = defaultField;
        return this;
    }
            
    /**
     * @param direction a direcao de ordenacao da tabela
     * @return o builder
     */
    public PageRequest withDirection(String direction) {
        
        if (direction.equals("ASCENDING")) {
            this.sortDirection = SortDirection.ASC;
        } else if (direction.equals("DESCENDING")) {
            this.sortDirection = SortDirection.DESC;
        }
        
        return this;
    }

    /**
     * @return por qual campos estamos fazendo sort na tabela
     */
    public String getSortField() {
        
        if (this.sortField == null || this.sortField.isEmpty()) {
            return this.defaultField;
        } else {
            return this.sortField;
        }
    }
    
    /**
     * A direcao de sort do filtro
     */
    public enum SortDirection {
        ASC, DESC;
    }
}
