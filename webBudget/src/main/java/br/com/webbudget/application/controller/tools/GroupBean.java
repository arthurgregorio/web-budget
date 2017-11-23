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
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 2.0.0, 22/07/2015
 */
@Named
@ViewScoped
public class GroupBean extends AbstractBean {

//    @Getter
//    private Group group;
//
//    @Getter
//    private TreeNode treeRoot;
//    @Getter
//    @Setter
//    private TreeNode[] selectedAuthorizations;
//    
//    @Getter
//    private List<Group> groups;
//
//    @Inject
//    private transient Authorization authorization;
//    @Inject
//    private transient AccountService accountService;
//
//    /**
//     *
//     */
//    public void initializeListing() {
//        this.viewState = ViewState.LISTING;
//        this.groups = this.accountService.listGroups(null);
//    }
//
//    /**
//     * @param groupId
//     */
//    public void initializeForm(String groupId) {
//            
//        this.groups = this.accountService.listGroups(null);
//        
//        this.createAuthorizationsTree();
//
//        if (groupId.isEmpty()) {
//            this.viewState = ViewState.ADDING;
//            this.group = new Group();
//        } else {
//            this.viewState = ViewState.EDITING;
//            this.group = this.accountService.findGroupById(groupId);
//            
//            this.selectAuthorizations();
//        }
//    }
//
//    /**
//     * @return o form de inclusao
//     */
//    public String changeToAdd() {
//        return "formGroup.xhtml?faces-redirect=true";
//    }
//
//    /**
//     * @param groupId
//     * @return
//     */
//    public String changeToEdit(String groupId) {
//        return "formGroup.xhtml?faces-redirect=true&groupId=" + groupId;
//    }
//
//    /**
//     * @param groupId
//     */
//    public void changeToDelete(String groupId) {
//        this.group = this.accountService.findGroupById(groupId);
//        this.updateAndOpenDialog("deleteGroupDialog", "dialogDeleteGroup");
//    }
//
//    /**
//     *
//     */
//    public void doSave() {
//
//        try {
//            this.accountService.save(this.group, this.nodesToAuthorizations());
//            this.group = new Group();
//            this.unselectAuthorizations();
//            this.addInfo(true, "group.saved");
//        } catch (InternalServiceError ex) {
//            this.addError(true, ex.getMessage());
//        } catch (Exception ex) {
//            this.logger.error(ex.getMessage(), ex);
//            this.addError(true, "error.undefined-error", ex.getMessage());
//        }
//    }
//
//    /**
//     *
//     */
//    public void doUpdate() {
//
//        try {
//            this.accountService.update(this.group, this.nodesToAuthorizations());
//            this.addInfo(true, "group.updated");
//        } catch (InternalServiceError ex) {
//            this.logger.error("GroupBean#doUpdate has found erros", ex);
//            this.addError(true, ex.getMessage());
//        } catch (Exception ex) {
//            this.logger.error(ex.getMessage(), ex);
//            this.addError(true, "error.undefined-error", ex.getMessage());
//        }
//    }
//
//    /**
//     *
//     */
//    public void doDelete() {
//
//        try {
//            this.accountService.delete(this.group);
//            this.groups = this.accountService.listGroups(null);
//            this.addInfo(true, "group.deleted");
//        } catch (Exception ex) {
//            if (this.containsException(IdentityManagementException.class, ex)) {
//                this.addError(true, "error.group.integrity-violation",
//                        this.group.getName());
//            } else {
//                this.logger.error(ex.getMessage(), ex);
//                this.addError(true, "error.undefined-error", ex.getMessage());
//            }
//        } finally {
//            this.updateComponent("groupsList");
//            this.closeDialog("dialogDeleteGroup");
//        }
//    }
//    
//    /**
//     * Transforma as permissoes selecionadas em uma lista de chaves para depois
//     * serem tranformadas em roles do sistema
//     * 
//     * @return a lista de chaves de autorizacao selecionadas
//     */
//    private List<String> nodesToAuthorizations() {
//
//        final List<String> authorizations = new ArrayList<>();
//
//        // pegamos as agrupadoras
//        final Set<String> groupers = 
//                this.authorization.listGroupedAuthorizations().keySet();
//
//        // filtramos manualmente
//        for (TreeNode node : this.selectedAuthorizations) {
//
//            final String authority = (String) node.getData();
//
//            // se estiver no set de keys, eh uma agrupadora e nao adicionamos
//            if (!groupers.contains(authority)) {
//                authorizations.add(authority);
//            }
//        }
//
//        return authorizations;
//    }
//    
//    /**
//     * Cria o model para arvore de permissoes
//     */
//    private void createAuthorizationsTree() {
//
//        // instancia o node principal, root
//        this.treeRoot = new DefaultTreeNode("roles");
//
//        // pega todas as authorities da lista de authorities do sistema
//        final HashMap<String, List<String>> roles = this.authorization.listGroupedAuthorizations();
//
//        for (String key : roles.keySet()) {
//
//            // criamos o agrupador para a permissao com base na key
//            final TreeNode rootNode = new DefaultTreeNode(key, this.treeRoot);
//
//            // pegamos todas as permissoes vinculadas a key e setamos no root dela
//            roles.get(key).stream().forEach((authority) -> {
//                rootNode.getChildren().add(new DefaultTreeNode(authority, rootNode));
//            });
//
//            // setamos tudo no root de todos
//            this.treeRoot.getChildren().add(rootNode);
//        }
//    }
//
//    /**
//     * Metodo que tira a selecao dos nodes, utilizamos ele para nao ter que
//     * chamar novamente a construcao da tela o que tornaria a execucao lenta
//     */
//    private void unselectAuthorizations() {
//
//        for (TreeNode node : this.treeRoot.getChildren()) {
//
//            if (!node.getChildren().isEmpty()) {
//                node.getChildren().stream().forEach((childNode) -> {
//                    childNode.setSelected(false);
//                });
//            }
//            node.setSelected(false);
//        }
//    }
//
//    /**
//     * Selecione os nodes de acordo com o grupo carregado
//     */
//    private void selectAuthorizations() {
//
//        final Set<TreeNode> selected = new HashSet<>();
//
//        for (Grant grant : this.group.getGrants()) {
//
//            for (TreeNode node : this.treeRoot.getChildren()) {
//
//                if (grant.getAuthorization().contains((String) node.getData())) {
//                    node.setSelected(true);
//                    selected.add(node);
//                }
//
//                if (!node.getChildren().isEmpty()) {
//                    for (TreeNode childNode : node.getChildren()) {
//                        if (grant.getAuthorization().contains((String) childNode.getData())) {
//                            childNode.setSelected(true);
//                            selected.add(childNode);
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//        this.selectedAuthorizations = selected.toArray(new TreeNode[]{});
//    }
//
//    /**
//     * @return
//     */
//    public String doCancel() {
//        return "listGroups.xhtml?faces-redirect=true";
//    }
}
