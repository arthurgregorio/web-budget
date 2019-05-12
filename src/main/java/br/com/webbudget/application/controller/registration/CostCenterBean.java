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
package br.com.webbudget.application.controller.registration;

import br.com.webbudget.application.components.ui.ViewState;
import br.com.webbudget.application.components.ui.table.Page;
import br.com.webbudget.application.components.ui.LazyFormBean;
import br.com.webbudget.domain.entities.registration.CostCenter;
import br.com.webbudget.domain.logics.registration.costcenter.CostCenterUpdatingLogic;
import br.com.webbudget.domain.repositories.registration.CostCenterRepository;
import br.com.webbudget.domain.logics.registration.costcenter.CostCenterSavingLogic;
import org.primefaces.model.SortOrder;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import static br.com.webbudget.application.components.ui.NavigationManager.PageType.*;

/**
 * The {@link CostCenter} maintenance routine controller
 *
 * @author Arthur Gregorio
 *
 * @version 1.4.0
 * @since 1.0.0, 04/03/2014
 */
@Named
@ViewScoped
public class CostCenterBean extends LazyFormBean<CostCenter> {

    @Inject
    private CostCenterRepository costCenterRepository;
    
    @Any
    @Inject
    private Instance<CostCenterSavingLogic> costCenterSavingLogics;
    @Any
    @Inject
    private Instance<CostCenterUpdatingLogic> costCenterUpdatingLogics;

    /**
     * {@inheritDoc}
     * 
     * @param id
     * @param viewState 
     */
    @Override
    public void initialize(long id, ViewState viewState) {
        this.viewState = viewState;
        this.data = this.costCenterRepository.findAllActive();
        this.value = this.costCenterRepository.findById(id).orElseGet(CostCenter::new);
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
     * 
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @return 
     */
    @Override
    public Page<CostCenter> load(int first, int pageSize, String sortField, SortOrder sortOrder) {
        return this.costCenterRepository.findAllBy(this.filter.getValue(), this.filter.getEntityStatusValue(), first, pageSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void doSave() {
        this.costCenterSavingLogics.forEach(logic -> logic.run(this.value));
        this.costCenterRepository.save(this.value);
        this.value = new CostCenter();
        this.data = this.costCenterRepository.findAllActive();
        this.addInfo(true, "saved");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void doUpdate() {
        this.costCenterUpdatingLogics.forEach(logic -> logic.run(this.value));
        this.costCenterRepository.saveAndFlushAndRefresh(this.value);
        this.data = this.costCenterRepository.findAllActive();
        this.addInfo(true, "updated");
    }

    /**
     * {@inheritDoc}
     * 
     * @return 
     */
    @Override
    @Transactional
    public String doDelete() {
        this.costCenterRepository.attachAndRemove(this.value);
        this.addInfoAndKeep("deleted");
        return this.changeToListing();
    }
}
