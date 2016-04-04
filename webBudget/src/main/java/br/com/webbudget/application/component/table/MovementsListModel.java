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
package br.com.webbudget.application.component.table;

import br.com.webbudget.domain.model.entity.movement.Movement;
import br.com.webbudget.domain.misc.filter.MovementFilter;
import br.com.webbudget.domain.model.service.MovementService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.1.2, 24/12/2015
 */
public class MovementsListModel extends AbstractLazyModel<Movement> {

    private final MovementService movementService;
    private final CustomFilterAdapter<MovementFilter> filterAdapter;

    /**
     * 
     * @param movementService
     * @param filterAdapter 
     */
    public MovementsListModel(MovementService movementService, CustomFilterAdapter filterAdapter) {
        this.filterAdapter = filterAdapter;
        this.movementService = movementService;
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
    public List<Movement> load(int first, int pageSize, 
            List<SortMeta> multiSortMeta, Map<String, Object> filters) {
        
        final PageRequest pageRequest = new PageRequest();

        pageRequest
                .setFirstResult(first)
                .withPageSize(pageSize)
                .multiSortingBy(multiSortMeta, "inclusion");

        final Page<Movement> page = movementService.listMovementsByFilter(
                this.filterAdapter.getFilter(), pageRequest);

        this.setRowCount(page.getTotalPagesInt());

        return page.getContent();
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
    public List<Movement> load(int first, int pageSize, String sortField, 
            SortOrder sortOrder, Map<String, Object> filters) {

        final PageRequest pageRequest = new PageRequest();

        pageRequest
                .setFirstResult(first)
                .withPageSize(pageSize)
                .sortingBy(sortField, "inclusion")
                .withDirection(sortOrder.name());

        final Page<Movement> page = movementService.listMovementsByFilter(
                this.filterAdapter.getFilter(), pageRequest);

        this.setRowCount(page.getTotalPagesInt());

        return page.getContent();
    }
}
