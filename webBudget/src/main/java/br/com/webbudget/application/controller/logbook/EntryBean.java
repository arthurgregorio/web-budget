/*
 * Copyright (C) 2016 Arthur Gregorio, AG.Software
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
package br.com.webbudget.application.controller.logbook;

import br.com.webbudget.application.component.table.AbstractLazyModel;
import br.com.webbudget.application.component.table.Page;
import br.com.webbudget.application.component.table.PageRequest;
import br.com.webbudget.application.controller.AbstractBean;
import br.com.webbudget.domain.model.entity.logbook.Entry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import lombok.Getter;
import org.primefaces.model.SortOrder;

/**
 * Controller para a view de ocorrencias do diario de bordo
 * 
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.3.0, 09/05/2016
 */
@Named
@ViewScoped
public class EntryBean extends AbstractBean {
    
    @Getter
    private Entry occurrence;
    
    @Getter
    private final AbstractLazyModel<Entry> occurrencesModel;

    /**
     * Incializa o lazy model das ocorrencias
     */
    public EntryBean() {
        
        this.occurrencesModel = new AbstractLazyModel<Entry>() {
            @Override
            public List<Entry> load(int first, int pageSize, String sortField, 
                    SortOrder sortOrder, Map<String, Object> filters) {
                
                final PageRequest pageRequest = new PageRequest();
                
                pageRequest
                        .setFirstResult(first)
                        .withPageSize(pageSize)
                        .sortingBy(sortField, "inclusion")
                        .withDirection(sortOrder.name());
                
                final Page<Entry> page = new Page<>(new ArrayList<>(), 0L);
                
                this.setRowCount(page.getTotalPagesInt());
                
                return page.getContent();
            }
        };
    }
    
    /**
     * Inicializa a view de listagem das ocorrencias o diario de bordo
     */
    public void initializeListing() {
        
    }
}
