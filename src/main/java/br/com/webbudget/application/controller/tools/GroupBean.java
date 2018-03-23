package br.com.webbudget.application.controller.tools;

import br.com.webbudget.application.controller.AbstractBean;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.entities.security.Authorization;
import br.com.webbudget.domain.entities.security.Grant;
import br.com.webbudget.domain.entities.security.Group;
import br.com.webbudget.domain.entities.security.Permissions;
import br.com.webbudget.domain.repositories.tools.AuthorizationRepository;
import br.com.webbudget.domain.repositories.tools.GroupRepository;
import br.com.webbudget.domain.services.UserAccountService;
import java.util.ArrayList;
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
 * @since 1.0.0, 09/01/2018
 */
@Named
@ViewScoped
public class GroupBean extends AbstractBean {

    @Getter
    @Setter
    private Group group;

    @Getter
    @Setter
    private String filter;
    @Getter
    @Setter
    private Boolean blocked;

    @Getter
    private TreeNode treeRoot;
    @Getter
    @Setter
    private TreeNode[] selectedAuthorizations;

    @Getter
    private List<Group> groups;

    @Inject
    private Permissions permissions;

    @Inject
    private GroupRepository groupRepository;
    @Inject
    private AuthorizationRepository authorizationRepository;

    @Inject
    private UserAccountService userAccountService;

    /**
     *
     */
    public void initializeListing() {
        this.filterList();
        this.viewState = ViewState.LISTING;
    }

    /**
     *
     * @param id
     * @param viewState
     */
    public void initializeForm(long id, String viewState) {

        // capturamos o estado da tela 
        this.viewState = ViewState.valueOf(viewState);

        // cria a tree de permissoes
        this.createAuthorizationsTree();

        // listamos os grupos
        this.groups = this.groupRepository.findAllActive();

        if (id == 0) {
            this.group = new Group();
        } else {
            this.group = this.groupRepository.findBy(id);
            this.selectAuthorizations();
        }
    }

    /**
     *
     * @param id
     * @param viewState
     */
    public void initializeDetail(long id, String viewState) {

        // capturamos o estado da tela 
        this.viewState = ViewState.valueOf(viewState);

        // cria a tree de permissoes
        this.createAuthorizationsTree();

        // listamos os grupos
        this.groups = this.groupRepository.findAllActive();
        this.group = this.groupRepository.findBy(id);

        // seleciona as permissoes
        this.selectAuthorizations();
    }

    /**
     *
     */
    public void filterList() {
        this.updateComponent("groupsList");
    }

    /**
     * @return o form de inclusao
     */
    public String changeToAdd() {
        return "formGroup.xhtml?faces-redirect=true&viewState="
                + ViewState.ADDING;
    }

    /**
     * @param groupId
     * @return
     */
    public String changeToEdit(String groupId) {
        return "formGroup.xhtml?faces-redirect=true&id="
                + groupId + "&viewState=" + ViewState.EDITING;
    }

    /**
     * @param groupId
     * @return
     */
    public String changeToDelete(String groupId) {
        return "detailGroup.xhtml?faces-redirect=true&id="
                + groupId + "&viewState=" + ViewState.DELETING;
    }

    /**
     * @return
     */
    public String changeTolist() {
        return "listGroups.xhtml?faces-redirect=true";
    }

    /**
     *
     */
    public void changeToDetail() {
        this.redirectTo("detailGroup.xhtml?faces-redirect=true&id="
                + this.group.getId() + "&viewState=" + ViewState.DETAILING);
    }

    /**
     *
     */
    public void doSave() {

        try {
            this.userAccountService.save(this.group, this.parseAuthorizations());
            this.group = new Group();
            this.unselectAuthorizations();
            this.addInfo(true, "group.saved");
        } catch (BusinessLogicException ex) {
            this.addError(true, ex.getMessage(), ex.getParameters());
        } catch (Exception ex) {
            this.logger.error("GroupBean#doSave has found erros", ex);
            this.addError(true, "error.generic-error", ex.getMessage());
        }
    }

    /**
     *
     */
    public void doUpdate() {

        try {
            this.userAccountService.update(this.group, this.parseAuthorizations());
            this.addInfo(true, "group.updated");
        } catch (BusinessLogicException ex) {
            this.addError(true, ex.getMessage(), ex.getParameters());
        } catch (Exception ex) {
            this.logger.error("GroupBean#doSave has found erros", ex);
            this.addError(true, "error.generic-error", ex.getMessage());
        }
    }

    /**
     *
     * @return
     */
    public String doDelete() {

        try {
            this.userAccountService.delete(this.group);
            return this.changeTolist();
        } catch (BusinessLogicException ex) {
            this.logger.error("GroupBean#doDelete has found erros", ex);
            this.addError(true, ex.getMessage(), ex.getParameters());
            return null;
        } catch (Exception ex) {
            this.logger.error("GroupBean#doSave has found erros", ex);
            this.addError(true, "error.generic-error", ex.getMessage());
            return null;
        }
    }

    /**
     * Converte a lista de items selecionados em uma lista de autorizacoes do
     * sistema para que sejam gravados no banco de dados
     *
     * @return a lista de autorizacoes
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
     * Cria o model para arvore de permissoes
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
     * Metodo que tira a selecao dos nodes, utilizamos ele para nao ter que
     * chamar novamente a construcao da tela o que tornaria a execucao lenta
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
     * Selecione os nodes de acordo com o grupo carregado
     */
    private void selectAuthorizations() {

        final Set<TreeNode> selected = new HashSet<>();

        for (Grant grant : this.group.getGrants()) {

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
     * Metodo criado para evitar repetir as internacionalizacoes das permissoes
     * e internacionalizar sem perder a capacidade de montagem das mesmas no
     * momento de transformar de String para Authorization
     *
     * @param nodeDescription a descricao do node
     * @return a parte internacionalizavel descartando a parte repetida
     */
    public String split(String nodeDescription) {
        final String splited[] = nodeDescription.split(":");
        return splited.length > 1 ? splited[1] : splited[0];
    }
}
