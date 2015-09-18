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
package br.com.webbudget.application.controller.financial;

import br.com.webbudget.application.controller.AbstractBean;
import br.com.webbudget.domain.entity.movement.FixedMovement;
import br.com.webbudget.domain.misc.model.AbstractLazyModel;
import java.util.List;
import java.util.Map;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import lombok.Getter;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.1.0, 18/09/2015
 */
@Named
@ViewScoped
public class FixedMovementBean extends AbstractBean {

    @Getter
    private String filter;
    
    @Getter
    private final AbstractLazyModel<FixedMovement> fixedMovementsModel;
    
    /**
     * 
     */
    public FixedMovementBean(){

        this.fixedMovementsModel = new AbstractLazyModel<FixedMovement>() {
            @Override
            public List<FixedMovement> load(int first, int pageSize, String sortField, 
                    SortOrder sortOrder, Map<String, Object> filters) {
                return null;
            }
        };
    }
    
    /**
     * 
     */
    public void initializeListing() {
        
        
    }
}
