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

import br.com.webbudget.application.permission.Authority;
import br.com.webbudget.application.controller.AbstractBean;
import br.com.webbudget.application.exceptions.ApplicationException;
import br.com.webbudget.domain.entity.users.Permission;
import br.com.webbudget.domain.entity.users.User;
import br.com.webbudget.domain.service.AccountService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 02/03/2014
 */
@ViewScoped
@ManagedBean
public class UserAccountBean extends AbstractBean {

    @Getter
    private User user;

    @Getter
    private List<User> users;

    @Getter
    private TreeNode authorityNodes;
    @Getter
    @Setter
    private TreeNode[] selectedAuthorities;

    @Setter
    @ManagedProperty("#{accountService}")
    private transient AccountService accountService;

    /**
     *
     * @return
     */
    @Override
    protected Logger initializeLogger() {
        return LoggerFactory.getLogger(UserAccountBean.class);
    }

    /**
     * Inicializa o usuario para edicao da conta pelas preferencias
     */
    public void initializePreferences() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            final User inSession = AccountService.getCurrentAuthenticatedUser();
            this.user = this.accountService.findUserByUsername(inSession.getUsername());
        }
    }

    /**
     *
     */
    public void initializeListing() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            this.viewState = ViewState.LISTING;
            this.users = this.accountService.listAccounts();
        }
    }

    /**
     *
     * @param userId
     */
    public void initializeForm(long userId) {
        if (!FacesContext.getCurrentInstance().isPostback()) {

            this.buildPermissionTree();

            if (userId == 0) {
                this.viewState = ViewState.ADDING;
                this.user = new User();
            } else {
                this.viewState = ViewState.EDITING;
                this.user = this.accountService.findAccountById(userId);

                // seleciona as permissoes do usuario para edicao
                this.selectNodes();
            }
        }
    }

    /**
     *
     * @return o form de inclusao
     */
    public String changeToAdd() {
        return "formUserAccount.xhtml?faces-redirect=true";
    }

    /**
     *
     * @param userId
     * @return
     */
    public String changeToEdit(long userId) {
        return "formUserAccount.xhtml?faces-redirect=true&userId=" + userId;
    }

    /**
     *
     * @param userId
     */
    public void changeToDelete(long userId) {
        this.user = this.accountService.findAccountById(userId);
        this.openDialog("deleteUserAccountDialog", "dialogDeleteUserAccount");
    }

    /**
     *
     */
    public void doSave() {

        // convertemos as authorities em permissoes
        final Set<Permission> permissions = new HashSet<>();
        final Set<String> authorities = this.nodesToAuthorities();

        // criamos as permissoes de acordo com as authorities
        for (String authority : authorities) {
            final Permission permission = new Permission();
            permission.setAuthority(authority);
            permissions.add(permission);
        }

        // seta as permissoes do usuario
        this.user.setPermissions(permissions);

        try {
            this.accountService.createAccount(this.user);

            this.user = new User();
            this.selectedAuthorities = null;

            this.unselectNodes();

            this.info("user-account.action.saved", true);
        } catch (ApplicationException ex) {
            this.logger.error("UserAccountBean#doSave found erros", ex);
            this.fixedError(ex.getMessage(), true);
        } finally {
            this.update("permissionTree");
        }
    }

    /**
     *
     */
    public void doUpdate() {

        // convertemos as authorities em permissoes
        final Set<Permission> permissions = new HashSet<>();
        final Set<String> authorities = this.nodesToAuthorities();

        // criamos as permissoes de acordo com as authorities
        for (String authority : authorities) {
            final Permission permission = new Permission();
            permission.setAuthority(authority);
            permissions.add(permission);
        }

        // seta as permissoes do usuario
        this.user.setPermissions(permissions);

        try {
            this.accountService.updateAccount(this.user);

            this.selectNodes();

            this.info("user-account.action.updated", true);
        } catch (ApplicationException ex) {
            this.logger.error("UserAccountBean#doUpdate on {} has found erros",
                    this.user.getUsername(), ex);
            this.fixedError(ex.getMessage(), true);
        }
    }

    /**
     *
     */
    public void doPasswordUpdate() {

        try {
            this.accountService.updateAccount(this.user);

            this.info("user-account.action.updated", true);
        } catch (ApplicationException ex) {
            this.logger.error("UserAccountBean#doUpdate on {} has found erros",
                    this.user.getUsername(), ex);
            this.fixedError(ex.getMessage(), true);
        }
    }

    /**
     *
     */
    public void doDelete() {

        try {
            this.accountService.deleteAccount(this.user);
            this.users = this.accountService.listAccounts();

            this.info("user-account.action.deleted", true);
        } catch (DataIntegrityViolationException ex) {
            this.logger.error("UserAccountBean#doDelete found erros", ex);
            this.fixedError("user-account.action.delete-used", true);
        } catch (Exception ex) {
            this.logger.error("UserAccountBean#doDelete found erros", ex);
            this.fixedError(ex.getMessage(), true);
        } finally {
            this.update("usersList");
            this.closeDialog("dialogDeleteUserAccount");
        }
    }

    /**
     * Metodo responsavel por construir toda a arvore de permissoes
     */
    private void buildPermissionTree() {

        // instancia o node principal, root
        this.authorityNodes = new DefaultTreeNode(this.translate(
                "user-account.form.tree.permissions"), null);

        // pega todas as authorities da lista de authorities do sistema
        final HashMap<String, Set<String>> authorities
                = new Authority().getAllAvailableAuthoritiesGrouped();

        for (String key : authorities.keySet()) {

            // criamos o agrupador para a permissao com base na key
            final TreeNode rootNode = new DefaultTreeNode(key, this.authorityNodes);

            // pegamos todas as permissoes vinculadas a key e setamos no root dela
            for (String authority : authorities.get(key)) {
                rootNode.getChildren().add(new DefaultTreeNode(authority, rootNode));
            }

            // setamos tudo no root de todos
            this.authorityNodes.getChildren().add(rootNode);
        }
    }

    /**
     * Convertemos os nodes para as authorities do sistema
     *
     * @return a lista de authorities
     */
    private Set<String> nodesToAuthorities() {

        final Set<String> authorities = new HashSet<>();

        // pegamos as agrupadoras
        final Set<String> authoritiesGroups
                = new Authority().getAllAvailableAuthoritiesGrouped().keySet();

        // filtramos manualmente
        for (TreeNode node : this.selectedAuthorities) {

            final String authority = (String) node.getData();

            // se estiver no set de keys, eh uma agrupadora e nao adicionamos
            if (!authoritiesGroups.contains(authority)) {
                authorities.add(authority);
            }
        }

        return authorities;
    }

    /**
     * Metodo que tira a selecao dos nodes, utilizamos ele para nao ter que
     * chamar novamente a construcao da tela o que tornaria a execucao lenta
     */
    private void unselectNodes() {

        for (TreeNode node : this.authorityNodes.getChildren()) {

            if (!node.getChildren().isEmpty()) {
                for (TreeNode childNode : node.getChildren()) {
                    childNode.setSelected(false);
                }
            }
            node.setSelected(false);
        }
    }

    /**
     * Selecione os nodes de acordo com o usuario carregado
     */
    private void selectNodes() {

        final Set<TreeNode> selected = new HashSet<>();

        for (Permission permission : this.user.getPermissions()) {

            for (TreeNode node : this.authorityNodes.getChildren()) {

                if (permission.getAuthority().contains((String) node.getData())) {
                    node.setSelected(true);
                    selected.add(node);
                }

                if (!node.getChildren().isEmpty()) {
                    for (TreeNode childNode : node.getChildren()) {
                        if (permission.getAuthority().contains((String) childNode.getData())) {
                            childNode.setSelected(true);
                            selected.add(childNode);
                            break;
                        }
                    }
                }
            }
        }

        this.selectedAuthorities = new TreeNode[selected.size()];
        this.selectedAuthorities = selected.toArray(this.selectedAuthorities);
    }

    /**
     *
     * @return
     */
    public String doCancel() {
        return "listUserAccounts.xhtml?faces-redirect=true";
    }

    /**
     *
     * @return
     */
    public String toDashboard() {
        return "/main/dashboard.xhtml?faces-redirect=true";
    }
}
