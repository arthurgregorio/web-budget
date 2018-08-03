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
package br.com.webbudget.application.controller.tools;

import br.com.webbudget.application.components.ViewState;
import br.com.webbudget.application.components.table.Page;
import br.com.webbudget.application.controller.FormBean;
import br.com.webbudget.domain.entities.tools.Authorization;
import br.com.webbudget.domain.entities.tools.Grant;
import br.com.webbudget.domain.entities.tools.Group;
import br.com.webbudget.domain.entities.tools.Permissions;
import br.com.webbudget.domain.repositories.tools.GroupRepository;
import br.com.webbudget.domain.services.UserAccountService;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.SortOrder;
import org.primefaces.model.TreeNode;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static br.com.webbudget.application.components.NavigationManager.PageType.*;

/**
 * The {@link Group} maintenance routine controller
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 2.0.0, 22/07/2017
 */
@Named
@ViewScoped
public class GroupBean extends FormBean<Group> {

    @Getter
    private TreeNode treeRoot;
    @Getter
    @Setter
    private TreeNode[] selectedAuthorizations;

    @Inject
    private Permissions permissions;

    @Inject
    private GroupRepository groupRepository;

    @Inject
    private UserAccountService userAccountService;

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
        
        this.data = this.groupRepository.findAllActive();

        this.createAuthorizationsTree();

        this.value = this.groupRepository.findOptionalById(id)
                .orElseGet(Group::new);

        if (this.viewState != ViewState.ADDING) {
            this.selectAuthorizations();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initializeNavigationManager() {
        this.navigation.addPage(LIST_PAGE, "listGroups.xhtml");
        this.navigation.addPage(ADD_PAGE, "formGroup.xhtml");
        this.navigation.addPage(UPDATE_PAGE, "formGroup.xhtml");
        this.navigation.addPage(DETAIL_PAGE, "detailGroup.xhtml");
        this.navigation.addPage(DELETE_PAGE, "detailGroup.xhtml");
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
    public Page<Group> load(int first, int pageSize, String sortField, SortOrder sortOrder) {
        return this.groupRepository.findAllBy(this.filter.getValue(), 
                this.filter.getEntityStatusValue(), first, pageSize);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void doSave() {
        this.userAccountService.save(this.value, this.parseAuthorizations());
        this.value = new Group();
        this.unselectAuthorizations();
        this.addInfo(true, "saved");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doUpdate() {
        this.userAccountService.update(this.value, this.parseAuthorizations());
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
     * Convert all the select items in the UI and transform this in a simple {@link List} of {@link Authorization}
     *
     * @return the {@link Authorization} list is on the way
     */
    private List<Authorization> parseAuthorizations() {

        final List<Authorization> authorizations = new ArrayList<>();

        for (TreeNode root : this.selectedAuthorizations) {

            final String[] nodeName = String.valueOf(root.getData()).split(":");

            if (nodeName.length > 1) {

                final String functionality = nodeName[0];
                final String permission = nodeName[1];

                authorizations.add(new Authorization(functionality, permission));
            }
        }
        return authorizations;
    }

    /**
     * Create the model to the tree
     */
    private void createAuthorizationsTree() {

        this.treeRoot = new DefaultTreeNode("permissions");

        final List<Authorization> authorizations
                = this.permissions.toAuthorizationList();

        authorizations.stream()
                .map(Authorization::getFunctionality)
                .distinct()
                .forEach(functionality -> {

                    final TreeNode functionalityNode
                            = new DefaultTreeNode(functionality, this.treeRoot);

                    authorizations.stream()
                            .filter(authz -> authz.isFunctionality(functionality))
                            .map(Authorization::getFullPermission)
                            .forEach(value -> {
                                functionalityNode.getChildren().add(
                                        new DefaultTreeNode(value, functionalityNode));
                            });

                    this.treeRoot.getChildren().add(functionalityNode);
                });
    }

    /**
     * Remove all the previous selected items
     */
    private void unselectAuthorizations() {

        this.treeRoot.getChildren()
                .stream()
                .peek(root -> root.setSelected(false))
                .map(TreeNode::getChildren)
                .forEach(childs -> {
                    if (!childs.isEmpty()) {
                        childs.stream().forEach(child -> {
                            child.setSelected(false);
                        });
                    }
                });
    }

    /**
     * Method to organize the tree
     */
    private void selectAuthorizations() {

        final Set<TreeNode> selected = new HashSet<>();

        for (Grant grant : this.value.getGrants()) {

            for (TreeNode root : this.treeRoot.getChildren()) {

                final String functionality = String.valueOf(root.getData());

                if (grant.getAuthorization().isFunctionality(functionality)) {
                    root.setSelected(true);
                    selected.add(root);
                }

                if (!root.getChildren().isEmpty()) {
                    for (TreeNode childNode : root.getChildren()) {

                        final String permission = String.valueOf(childNode.getData());

                        if (grant.getAuthorization().isPermission(permission)) {
                            childNode.setSelected(true);
                            selected.add(childNode);
                            break;
                        }
                    }
                }
            }
        }
        this.selectedAuthorizations = selected.toArray(new TreeNode[]{});
    }

    /**
     * Method to translate the enums on the UI
     *
     * @param nodeDescription the current identification node description
     * @return only the i18n part of the text
     */
    public String split(String nodeDescription) {
        final String splited[] = nodeDescription.split(":");
        return splited.length > 1 ? splited[1] : splited[0];
    }
}
