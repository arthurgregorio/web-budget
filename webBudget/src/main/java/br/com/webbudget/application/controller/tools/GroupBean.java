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
import br.com.webbudget.domain.security.Authorization;
import br.com.webbudget.domain.security.Grant;
import br.com.webbudget.domain.security.Group;
import br.com.webbudget.domain.security.Role;
import br.com.webbudget.domain.service.AccountService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

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
    private TreeNode treeRoot;
    @Getter
    @Setter
    private TreeNode[] selectedAuthorizations;
    
    @Getter
    private List<Group> groups;

    @Inject
    private transient Authorization authorization;
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
        
        this.createAuthorizationsTree();

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
    private void createAuthorizationsTree() {

        // instancia o node principal, root
        this.treeRoot = new DefaultTreeNode(this.translate("group.form.roles"), null);

        // pega todas as authorities da lista de authorities do sistema
        final HashMap<String, Set<String>> roles = this.authorization.listGroupedAuthorizations();

        for (String key : roles.keySet()) {

            // criamos o agrupador para a permissao com base na key
            final TreeNode rootNode = new DefaultTreeNode(key, this.treeRoot);

            // pegamos todas as permissoes vinculadas a key e setamos no root dela
            for (String authority : roles.get(key)) {
                rootNode.getChildren().add(new DefaultTreeNode(authority, rootNode));
            }

            // setamos tudo no root de todos
            this.treeRoot.getChildren().add(rootNode);
        }
    }
    
    /**
     * Metodo que tira a selecao dos nodes, utilizamos ele para nao ter que
     * chamar novamente a construcao da tela o que tornaria a execucao lenta
     */
    private void unselectAuthorizations() {

        for (TreeNode node : this.treeRoot.getChildren()) {

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
    private void selectAuthorizations() {

        final Set<TreeNode> selected = new HashSet<>();

        for (Grant grant : this.group.getGrants()) {

            for (TreeNode node : this.treeRoot.getChildren()) {

                if (grant.getGrantAuthorization().contains((String) node.getData())) {
                    node.setSelected(true);
                    selected.add(node);
                }

                if (!node.getChildren().isEmpty()) {
                    for (TreeNode childNode : node.getChildren()) {
                        if (grant.getGrantAuthorization().contains((String) childNode.getData())) {
                            childNode.setSelected(true);
                            selected.add(childNode);
                            break;
                        }
                    }
                }
            }
        }

        this.selectedAuthorizations = new TreeNode[selected.size()];
        this.selectedAuthorizations = selected.toArray(this.selectedAuthorizations);
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
