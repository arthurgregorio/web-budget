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
package br.com.webbudget.application.controller.entries;

import static br.com.webbudget.application.components.NavigationManager.PageType.ADD_PAGE;
import static br.com.webbudget.application.components.NavigationManager.PageType.DELETE_PAGE;
import static br.com.webbudget.application.components.NavigationManager.PageType.DETAIL_PAGE;
import static br.com.webbudget.application.components.NavigationManager.PageType.LIST_PAGE;
import static br.com.webbudget.application.components.NavigationManager.PageType.UPDATE_PAGE;
import br.com.webbudget.application.components.ViewState;
import br.com.webbudget.application.components.table.Page;
import br.com.webbudget.application.controller.FormBean;
import br.com.webbudget.domain.entities.registration.CostCenter;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import br.com.webbudget.domain.repositories.entries.CostCenterRepository;
import br.com.webbudget.domain.services.ClassificationService;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.4.0
 * @since 1.0.0, 04/03/2014
 */
@Named
@ViewScoped
public class CostCenterBean extends FormBean<CostCenter> {

    @Inject
    private CostCenterRepository costCenterRepository;
    
    @Inject
    private ClassificationService classificationService;
    
    /**
     * 
     */
    @Override
    public void initialize() {
        super.initialize();
        this.temporizeHiding(this.getDefaultMessagesComponentId());
    }

    /**
     * 
     * @param id
     * @param viewState 
     */
    @Override
    public void initialize(long id, ViewState viewState) {
        this.viewState = viewState;
        this.data = this.costCenterRepository.findAllUnblocked();
        this.value = this.costCenterRepository.findOptionalById(id)
                .orElseGet(CostCenter::new);
    }

    /**
     * 
     */
    @Override
    protected void initializeNavigationManager() {
        this.navigation.addPage(LIST_PAGE, "listCostCenters.xhtml");
        this.navigation.addPage(ADD_PAGE, "formCostCenter.xhtml");
        this.navigation.addPage(UPDATE_PAGE, "formCostCenter.xhtml");
        this.navigation.addPage(DETAIL_PAGE, "detailCostCenter.xhtml");
        this.navigation.addPage(DELETE_PAGE, "detailCostCenter.xhtml");        
    }

    /**
     * 
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @return 
     */
    @Override
    public Page<CostCenter> load(int first, int pageSize, String sortField, SortOrder sortOrder) {
        return this.costCenterRepository.findAllBy(this.filter.getValue(), 
                this.filter.getEntityStatusValue(), first, pageSize);
    }

    /**
     *
     */
    @Override
    public void doSave() {
        this.classificationService.save(this.value);
        this.value = new CostCenter();
        this.data = this.costCenterRepository.findAllUnblocked();
        this.addInfo(true, "cost-center.saved");
    }

    /**
     *
     */
    @Override
    public void doUpdate() {
        this.value = this.classificationService.update(this.value);
        this.data = this.costCenterRepository.findAllUnblocked();
        this.addInfo(true, "cost-center.updated");
    }

    /**
     * 
     * @return 
     */
    @Override
    public String doDelete() {
        this.classificationService.delete(this.value);
        this.addInfoAndKeep("cost-center.deleted");
        return this.changeToListing();
    }
}
