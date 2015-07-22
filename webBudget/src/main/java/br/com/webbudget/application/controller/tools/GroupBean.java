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
package br.com.webbudget.application.controller.tools;

import br.com.webbudget.application.controller.AbstractBean;
import br.com.webbudget.domain.security.Group;
import br.com.webbudget.domain.service.AccountService;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.0.0, 22/07/2015
 */
@Named
@ViewScoped
public class GroupBean extends AbstractBean {

    @Getter
    private Group group;

    @Getter
    private List<Group> groups;

    @Inject
    private transient AccountService accountService;

    /**
     *
     */
    public void initializeListing() {
        this.viewState = ViewState.LISTING;
        this.groups = this.accountService.listGroups(null);
    }

    /**
     *
     * @param groupId
     */
    public void initializeForm(String groupId) {
            
        this.groups = this.accountService.listGroups(null);

        if (groupId.isEmpty()) {
            this.viewState = ViewState.ADDING;
            this.group = new Group();
        } else {
            this.viewState = ViewState.EDITING;
            this.group = this.accountService.findGroupById(groupId);
        }
    }

    /**
     *
     * @return o form de inclusao
     */
    public String changeToAdd() {
        return "formGroup.xhtml?faces-redirect=true";
    }

    /**
     *
     * @param groupId
     * @return
     */
    public String changeToEdit(String groupId) {
        return "formGroup.xhtml?faces-redirect=true&groupId=" + groupId;
    }

    /**
     *
     * @param groupId
     */
    public void changeToDelete(String groupId) {
        this.group = this.accountService.findGroupById(groupId);
        this.openDialog("deleteGroupDialog", "dialogDeleteGroup");
    }

    /**
     *
     */
    public void doSave() {

        try {
            this.accountService.save(this.group);

            this.group = new Group();

            this.info("group.action.saved", true);
        } catch (Exception ex) {
            this.logger.error("GroupBean#doSave found erros", ex);
            this.fixedError("generic.operation-error", true, ex.getMessage());
        } 
    }

    /**
     *
     */
    public void doUpdate() {

        try {
            this.accountService.update(this.group);

            this.info("group.action.updated", true);
        } catch (Exception ex) {
            this.logger.error("GroupBean#doUpdate has found erros", ex);
            this.fixedError("generic.operation-error", true, ex.getMessage());
        }
    }

    /**
     *
     */
    public void doDelete() {

        try {
            this.accountService.delete(this.group);
            this.groups = this.accountService.listGroups(null);

            this.info("group.action.deleted", true);
        } catch (Exception ex) {
            this.logger.error("GroupBean#doDelete found erros", ex);
            this.fixedError("generic.operation-error", true, ex.getMessage());
        } finally {
            this.update("groupsList");
            this.closeDialog("dialogDeleteGroup");
        }
    }
    
    /**
     *
     */
    public void doPasswordUpdate() {

    }

    /**
     *
     * @return
     */
    public String doCancel() {
        return "listGroups.xhtml?faces-redirect=true";
    }

    /**
     *
     * @return
     */
    public String toDashboard() {
        return "/main/dashboard.xhtml?faces-redirect=true";
    }
}
