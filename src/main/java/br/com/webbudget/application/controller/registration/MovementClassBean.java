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

import br.com.webbudget.application.components.ViewState;
import br.com.webbudget.application.components.table.Page;
import br.com.webbudget.application.controller.FormBean;
import br.com.webbudget.domain.entities.registration.CostCenter;
import br.com.webbudget.domain.entities.registration.MovementClass;
import br.com.webbudget.domain.entities.registration.MovementClassType;
import br.com.webbudget.domain.repositories.registration.CostCenterRepository;
import br.com.webbudget.domain.repositories.registration.MovementClassRepository;
import br.com.webbudget.domain.services.ClassificationService;
import lombok.Getter;
import org.primefaces.model.SortOrder;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

import static br.com.webbudget.application.components.NavigationManager.PageType.*;

/**
 * The {@link MovementClass} maintenance routine controller
 *
 * @author Arthur Gregorio
 *
 * @version 1.4.0
 * @since 1.0.0, 04/03/2014
 */
@Named
@ViewScoped
public class MovementClassBean extends FormBean<MovementClass> {

    @Getter
    private List<CostCenter> costCenters;
    
    @Inject
    private CostCenterRepository costCenterRepository;
    @Inject
    private MovementClassRepository movementClassRepository;
    
    @Inject
    private ClassificationService classificationService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() {
        super.initialize();
        this.temporizeHiding(this.getDefaultMessagesComponentId());
    }

    /**
     * {@inheritDoc}
     * 
     * @param id
     * @param viewState 
     */
    @Override
    public void initialize(long id, ViewState viewState) {
        this.viewState = viewState;
        this.costCenters = this.costCenterRepository.findAllActive();
        this.value = this.movementClassRepository.findOptionalById(id)
                .orElseGet(MovementClass::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initializeNavigationManager() {
        this.navigation.addPage(LIST_PAGE, "listMovementClasses.xhtml");
        this.navigation.addPage(ADD_PAGE, "formMovementClass.xhtml");
        this.navigation.addPage(UPDATE_PAGE, "formMovementClass.xhtml");
        this.navigation.addPage(DETAIL_PAGE, "detailMovementClass.xhtml");
        this.navigation.addPage(DELETE_PAGE, "detailMovementClass.xhtml");
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
    public Page<MovementClass> load(int first, int pageSize, String sortField, SortOrder sortOrder) {
        return this.movementClassRepository.findAllBy(this.filter.getValue(), 
                this.filter.getEntityStatusValue(), first, pageSize);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void doSave() {
        this.classificationService.save(this.value);
        this.value = new MovementClass();
        this.addInfo(true, "movement-class.saved");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doUpdate() {
        this.value = this.classificationService.update(this.value);
        this.addInfo(true, "movement-class.updated");
    }

    /**
     * {@inheritDoc}
     * 
     * @return 
     */
    @Override
    public String doDelete() {
        this.classificationService.delete(this.value);
        this.addInfoAndKeep("movement-class.deleted");
        return this.changeToListing();
    }

    /**
     * Method to list the possible types of a {@link MovementClass}
     *
     * @return an array with the values of the {@link MovementClassType} enum
     */
    public MovementClassType[] getMovementClassTypes() {
        return MovementClassType.values();
    }
}
