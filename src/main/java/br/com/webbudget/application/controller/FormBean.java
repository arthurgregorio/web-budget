package br.com.webbudget.application.controller;

import br.com.webbudget.application.components.NavigationManager;
import br.com.webbudget.application.components.ViewState;
import br.com.webbudget.application.components.table.LazyDataProvider;
import br.com.webbudget.application.components.table.LazyFilter;
import br.com.webbudget.application.components.table.LazyModel;
import static br.com.webbudget.application.components.NavigationManager.PageType.ADD_PAGE;
import static br.com.webbudget.application.components.NavigationManager.PageType.DELETE_PAGE;
import static br.com.webbudget.application.components.NavigationManager.PageType.DETAIL_PAGE;
import static br.com.webbudget.application.components.NavigationManager.PageType.LIST_PAGE;
import static br.com.webbudget.application.components.NavigationManager.PageType.UPDATE_PAGE;
import static br.com.webbudget.application.components.NavigationManager.Parameter.of;
import br.com.webbudget.domain.entities.PersistentEntity;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @param <T>
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 28/03/2018
 */
public abstract class FormBean<T extends PersistentEntity> extends AbstractBean 
        implements LazyDataProvider<T> {

    @Getter
    @Setter
    protected T value;

    @Getter
    protected List<T> data;

    @Getter
    protected ViewState viewState;

    @Getter
    protected final LazyFilter filter;
    @Getter
    protected final LazyDataModel<T> dataModel;

    protected final NavigationManager navigation;

    /**
     *
     */
    public FormBean() {

        this.dataModel = new LazyModel<>(this);

        this.filter = LazyFilter.initialize();

        this.navigation = NavigationManager.getInstance();

        this.initializeNavigationManager();
    }

    /**
     *
     */
    protected abstract void initializeNavigationManager();

    /**
     *
     * @param id
     * @param viewState
     */
    public abstract void initialize(long id, ViewState viewState);

    /**
     *
     */
    public abstract void doSave();

    /**
     *
     */
    public abstract void doUpdate();

    /**
     *
     * @return
     */
    public abstract String doDelete();

    /**
     *
     */
    public void initialize() {
        this.viewState = ViewState.LISTING;
    }
    
    /**
     *
     */
    public void updateListing() {
        this.updateComponent("itemsListing");
    }

    /**
     *
     */
    public void clearFilters() {
        this.filter.clear();
    }

    /**
     * @return
     */
    public String changeToListing() {
        return this.navigation.to(LIST_PAGE);
    }

    /**
     * @return
     */
    public String changeToAdd() {
        return this.navigation.to(ADD_PAGE);
    }

    /**
     * @param id
     * @return
     */
    public String changeToEdit(long id) {
        return this.navigation.to(UPDATE_PAGE, of("id", id));
    }

    /**
     *
     */
    public void changeToDetail() {
        this.navigation.redirect(DETAIL_PAGE, of("id", this.value.getId()));
    }

    /**
     *
     * @param id
     * @return
     */
    public String changeToDelete(long id) {
        return this.navigation.to(DELETE_PAGE, of("id", id));
    }
}
