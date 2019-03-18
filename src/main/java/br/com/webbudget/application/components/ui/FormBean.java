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

import br.com.webbudget.domain.entities.PersistentEntity;
import lombok.Getter;
import lombok.Setter;

import static br.com.webbudget.application.components.ui.NavigationManager.PageType.*;
import static br.com.webbudget.application.components.ui.NavigationManager.Parameter.of;

/**
 * The abstract form controller, this class hold all the basic features that a single form will have
 *
 * @param <T> the type to be manipulated by this controller, must be a domain entity child of {@link PersistentEntity}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 28/03/2018
 */
public abstract class FormBean<T extends PersistentEntity> extends AbstractBean {

    @Getter
    @Setter
    protected T value;

    @Getter
    protected ViewState viewState;

    protected final NavigationManager navigation;

    /**
     * Create the bean and initialize the default data
     */
    public FormBean() {
        this.navigation = new NavigationManager();
        this.initializeNavigationManager();
    }

    /**
     * This method should be used to initialize the {@link NavigationManager} for this controller
     */
    protected abstract void initializeNavigationManager();

    /**
     * Use this to initialize the basic attributes of the form model
     *
     * @param id the id of the entity to search on the database or leave null to initialize a new instance of the model
     * @param viewState the actual view state to initialize the view
     */
    public abstract void initialize(long id, ViewState viewState);

    /**
     * Perform a save operation
     */
    public abstract void doSave();

    /**
     * Perform a update operation
     */
    public abstract void doUpdate();

    /**
     * Perform a delete operation
     *
     * After delete, this method needs to return the navigation case to the after delete action page
     *
     * @return the page to redirect the user after de delete operation
     */
    public abstract String doDelete();

    /**
     * This is the default initialization method, if you want more logic here just override this.
     *
     * By default, this method initialize the view in listing mode by setting the {@link ViewState} to LISTING value
     */
    public void initialize() {
        this.viewState = ViewState.LISTING;
    }

    /**
     * Redirect the user to the listing page defined in the {@link NavigationManager}
     *
     * @return the listing page
     */
    public String changeToListing() {
        return this.navigation.to(LIST_PAGE);
    }

    /**
     * Redirect the user to the add page defined in the {@link NavigationManager}
     *
     * @return the add page
     */
    public String changeToAdd() {
        return this.navigation.to(ADD_PAGE);
    }

    /**
     * Redirect the user to the edit page defined in the {@link NavigationManager}
     *
     * @param id the id of the entity to be loaded
     * @return the edit page
     */
    public String changeToEdit(long id) {
        return this.navigation.to(UPDATE_PAGE, of("id", id));
    }

    /**
     * Redirect the user to the detail page defined in the {@link NavigationManager} with implicit navigation
     *
     * @param id the id of the entity to be loaded
     * @return the detail page
     */
    public String changeToDetail(long id) {
        return this.navigation.to(DETAIL_PAGE, of("id", id));
    }

    /**
     * Redirect the user to the detail page defined in the {@link NavigationManager} with a servlet redirect
     *
     * Use this method where you can't pass a action to a {@link org.primefaces.component.commandbutton.CommandButton}
     */
    public void changeToDetail() {
        this.navigation.redirect(DETAIL_PAGE, of("id", this.value.getId()));
    }

    /**
     * Redirect the user to the delete page defined in the {@link NavigationManager}
     *
     * @return the delete page
     */
    public String changeToDelete(long id) {
        return this.navigation.to(DELETE_PAGE, of("id", id));
    }
}
