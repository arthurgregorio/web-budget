/*
 * Copyright (C) 2014 Arthur Gregorio, AG.Software
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
package br.com.webbudget.application.controller.configuration;

import br.com.webbudget.application.components.ui.ViewState;
import br.com.webbudget.application.components.ui.table.Page;
import br.com.webbudget.application.components.ui.LazyFormBean;
import br.com.webbudget.domain.entities.configuration.Group;
import br.com.webbudget.domain.entities.configuration.StoreType;
import br.com.webbudget.domain.entities.configuration.User;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.repositories.configuration.GroupRepository;
import br.com.webbudget.domain.repositories.configuration.UserRepository;
import br.com.webbudget.domain.services.UserAccountService;
import br.com.webbudget.infrastructure.utils.Configurations;
import br.eti.arthurgregorio.shiroee.config.ldap.LdapUser;
import br.eti.arthurgregorio.shiroee.config.ldap.LdapUserProvider;
import lombok.Getter;
import org.primefaces.model.SortOrder;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

import static br.com.webbudget.application.components.ui.NavigationManager.PageType.*;

/**
 * The {@link User} maintenance routine controller
 *
 * @author Arthur Gregorio
 *
 * @version 3.0.0
 * @since 1.0.0, 02/03/2014
 */
@Named
@ViewScoped
public class UserBean extends LazyFormBean<User> {

    @Getter
    private List<Group> groups;

    @Inject
    private UserRepository userRepository;
    @Inject
    private GroupRepository groupRepository;

    @Inject
    private UserAccountService userAccountService;

    @Inject
    private LdapUserProvider ldapUserProvider;

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param viewState
     */
    @Override
    public void initialize(long id, ViewState viewState) {
        this.viewState = viewState;
        this.groups = this.groupRepository.findAllActive();
        this.value = this.userRepository.findById(id).orElseGet(User::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initializeNavigationManager() {
        this.navigation.addPage(LIST_PAGE, "listUsers.xhtml");
        this.navigation.addPage(ADD_PAGE, "formUser.xhtml");
        this.navigation.addPage(UPDATE_PAGE, "formUser.xhtml");
        this.navigation.addPage(DETAIL_PAGE, "detailUser.xhtml");
        this.navigation.addPage(DELETE_PAGE, "detailUser.xhtml");
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
    public Page<User> load(int first, int pageSize, String sortField, SortOrder sortOrder) {
        return this.userRepository.findAllBy(this.filter.getValue(), this.filter.getEntityStatusValue(), first, pageSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doSave() {
        this.userAccountService.save(this.value);
        this.value = new User();
        this.addInfo(true, "saved");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doUpdate() {
        this.userAccountService.update(this.value);
        this.addInfo(true, "updated");
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public String doDelete() {
        this.userAccountService.delete(this.value);
        this.addInfoAndKeep("deleted");
        return this.changeToListing();
    }

    /**
     * Method to find a given user on the LDAP/AD directory
     */
    public void findUserOnLdap() {

        final boolean ldapEnable = Configurations.getAsBoolean("ldap.enabled");

        if (!ldapEnable) {
            throw new IllegalStateException("error.user.ldap-not-enabled");
        }

        final String username = this.value.getUsername();

        final LdapUser userDetails = this.ldapUserProvider
                .search(username)
                .orElseThrow(() -> new BusinessLogicException("error.user.not-found-ldap", username));

        this.value.setUsername(userDetails.getSAMAccountName());
        this.value.setEmail(userDetails.getMail());
        this.value.setName(userDetails.getName());
    }

    /**
     * Get the possible values for the storage place of an {@link User}
     *
     * @return an array with {@link StoreType} values
     */
    public StoreType[] getStoreTypes() {
        return StoreType.values();
    }
}
