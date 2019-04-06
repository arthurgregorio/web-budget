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
package br.com.webbudget.application.components.ui;

import br.com.webbudget.application.components.ui.table.LazyDataProvider;
import br.com.webbudget.application.components.ui.filter.LazyFilter;
import br.com.webbudget.application.components.ui.table.LazyModel;
import br.com.webbudget.domain.entities.PersistentEntity;
import lombok.Getter;
import org.primefaces.model.LazyDataModel;

import java.util.List;

/**
 * An implementation of the {@link FormBean} supporting lazy load on primefaces data tables
 *
 * @param <T> the type to be manipulated by this controller, must be a domain entity child of {@link PersistentEntity}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 10/12/2018
 */
public abstract class LazyFormBean<T extends PersistentEntity> extends FormBean<T> implements LazyDataProvider<T> {

    @Getter
    protected List<T> data;

    @Getter
    protected final LazyFilter filter;
    @Getter
    protected final LazyDataModel<T> dataModel;

    /**
     * Create the bean and initialize the default data
     */
    public LazyFormBean() {
        this.dataModel = new LazyModel<>(this);
        this.filter = LazyFilter.getInstance();
    }

    /**
     * Clear the form filters
     */
    public void clearFilters() {
        this.filter.clear();
    }
}
