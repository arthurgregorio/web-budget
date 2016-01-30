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
import br.com.webbudget.domain.misc.ex.InternalServiceError;
import br.com.webbudget.domain.security.Group;
import br.com.webbudget.domain.service.AccountService;
import br.com.webbudget.domain.security.User;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import org.picketlink.Identity;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.2.0
 * @since 1.0.0, 02/03/2014
 */
@Named
@ViewScoped
public class UserBean extends AbstractBean {

    @Getter
    private User user;

    @Getter
    private List<User> users;
    @Getter
    private List<Group> groups;

    @Inject
    private transient Identity identity;
    
    @Inject
    private transient AccountService accountService;

    /**
     *
     */
    public void initializeListing() {
        this.viewState = ViewState.LISTING;
        this.users = this.accountService.listUsers(null);
    }

    /**
     * Inicializa a parte do profile do usuario
     */
    public void initializeProfile() {
        
        // setamos no usuario o usuario autenticado
        this.user = (User) this.identity.getAccount();
    }
    
    /**
     * @param userId
     */
    public void initializeForm(String userId) {
            
        this.groups = this.accountService.listGroups(null);

        if (userId.isEmpty()) {
            this.viewState = ViewState.ADDING;
            this.user = new User();
        } else {
            this.viewState = ViewState.EDITING;
            this.user = this.accountService.findUserById(userId);
        }
    }

    /**
     * @return o form de inclusao
     */
    public String changeToAdd() {
        return "formUser.xhtml?faces-redirect=true";
    }

    /**
     * @param userId
     * @return
     */
    public String changeToEdit(String userId) {
        return "formUser.xhtml?faces-redirect=true&userId=" + userId;
    }

    /**
     * @param userId
     */
    public void changeToDelete(String userId) {
        this.user = this.accountService.findUserById(userId);
//        this.openDialog("deleteUserDialog", "dialogDeleteUser");
    }

    /**
     *
     */
    public void doSave() {

        try {
            this.accountService.save(this.user);

            this.user = new User();

//            this.info("user.action.saved", true);
        } catch (InternalServiceError ex) {
            this.logger.error("UserBean#doSave has found erros", ex);
//            this.fixedError(ex.getMessage(), true);
        } catch (Exception ex) {
            this.logger.error("UserBean#doSave has found erros", ex);
//            this.fixedError("generic.operation-error", true, ex.getMessage());
        }
    }

    /**
     *
     */
    public void doUpdate() {

        try {
            this.accountService.update(this.user);

//            this.info("user.action.updated", true);
        } catch (InternalServiceError ex) {
            this.logger.error("UserBean#doUpdate has found erros", ex);
//            this.fixedError(ex.getMessage(), true);
        } catch (Exception ex) {
            this.logger.error("UserBean#doUpdate has found erros", ex);
//            this.fixedError("generic.operation-error", true, ex.getMessage());
        }
    }

    /**
     *
     */
    public void doDelete() {

        try {
            this.accountService.delete(this.user);
            this.users = this.accountService.listUsers(null);

//            this.info("user.action.deleted", true);
        } catch (Exception ex) {
            this.logger.error("UserBean#doDelete found erros", ex);
//            this.fixedError("generic.operation-error", true, ex.getMessage());
        } finally {
//            this.update("usersList");
            this.closeDialog("dialogDeleteUser");
        }
    }
    
    /**
     *
     */
    public void doProfileUpdate() {
        
        try {
            this.accountService.updateProfile(this.user);
//            this.info("user.action.profile-updated", true);
        } catch (InternalServiceError ex) {
            this.logger.error("UserBean#doProfileUpdate has found erros", ex);
//            this.fixedError(ex.getMessage(), true);
        } catch (Exception ex) {
            this.logger.error("UserBean#doProfileUpdate has found erros", ex);
//            this.fixedError("generic.operation-error", true, ex.getMessage());
        }
    }

    /**
     * @return
     */
    public String doCancel() {
        return "listUsers.xhtml?faces-redirect=true";
    }

    /**
     * @return
     */
    public String toDashboard() {
        return "/main/dashboard.xhtml?faces-redirect=true";
    }
}