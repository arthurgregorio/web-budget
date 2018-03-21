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
package br.com.webbudget.application.components.table;

import br.com.webbudget.domain.entities.IPersistentEntity;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

/**
 * LazyDataModel generico para uso nas datatables do sistema. Como ele podemos
 * definir a carga de um datatable on-demand
 *
 * @param <T> o tipo deste model
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 2.1.0, 05/09/2015
 */
public class LazyModel<T extends IPersistentEntity> extends LazyDataModel<T> {

    private final LazyDataProvider<T> provider;

    /**
     *
     * @param provider
     */
    public LazyModel(LazyDataProvider provider) {
        this.provider = provider;
    }

    /**
     *
     * @param first
     * @param pageSize
     * @param multiSortMeta
     * @param filters
     * @return
     */
    @Override
    public List<T> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {
        return this.provider.load(first, pageSize, multiSortMeta);
    }

    /**
     *
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @param filters
     * @return
     */
    @Override
    public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        return this.provider.load(first, pageSize, sortField, sortOrder);
    }

    /**
     * @see LazyDataModel#getRowKey(java.lang.Object)
     *
     * @param object
     * @return
     */
    @Override
    public Object getRowKey(T object) {
        return object.getId();
    }

    /**
     * @see LazyDataModel#getRowData(java.lang.String)
     *
     * @param rowKey
     * @return
     */
    @Override
    public T getRowData(String rowKey) {
        return this.getModelSource().stream()
                .filter(object -> object.getId().equals(Long.parseLong(rowKey)))
                .findFirst()
                .orElse(null);
    }

    /**
     * @return a lista encapsulada por este model
     */
    public List<T> getModelSource() {
        return (List<T>) this.getWrappedData();
    }
}
