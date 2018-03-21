/*
 * Copyright (C) 2018 Arthur Gregorio, AG.Software
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
package br.com.webbudget.application.components.table;

import br.com.webbudget.domain.entities.IPersistentEntity;
import java.util.Collections;
import java.util.List;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

/**
 * 
 * @param <T>
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 20/03/2018
 */
public interface LazyDataProvider<T extends IPersistentEntity> {

    /**
     * 
     * @param first
     * @param pageSize
     * @param sortFields
     * @return 
     */
    default public List<T> load(int first, int pageSize, List<SortMeta> sortFields) {
        return Collections.emptyList();
    }
    
    /**
     * 
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @return 
     */
    public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder);
}
