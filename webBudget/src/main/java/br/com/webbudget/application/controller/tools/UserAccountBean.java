package br.com.webbudget.application.controller.tools;

import br.com.webbudget.application.ViewState;
import br.com.webbudget.application.components.MessagesFactory;
import br.com.webbudget.application.components.permission.Authority;
import br.com.webbudget.application.exceptions.ApplicationException;
import br.com.webbudget.domain.entity.users.Permission;
import br.com.webbudget.domain.entity.users.User;
import br.com.webbudget.domain.service.AccountService;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import org.omnifaces.util.Messages;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 02/03/2014
 */
@ViewScoped
@ManagedBean
public class UserAccountBean implements Serializable {

    @Getter
    private ViewState viewState;
    
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
    @ManagedProperty("#{messagesFactory}")
    private transient MessagesFactory messages;
    @Setter
    @ManagedProperty("#{accountService}")
    private transient AccountService accountService;
    
    private final Logger LOG = LoggerFactory.getLogger(UserAccountBean.class);

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
                this.viewState = ViewState.ADD;
                this.user = new User();
            } else {
                this.viewState = ViewState.EDIT;
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
        RequestContext.getCurrentInstance().execute("PF('popupDeleteUserAccount').show()");
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
            
            Messages.addInfo(null, messages.getMessage("user-account.action.saved"));
        } catch (ApplicationException ex) {
            LOG.error("UserAccountBean#doSave found erros", ex);
            Messages.addError(null, messages.getMessage(ex.getMessage()));
        } finally {
            RequestContext.getCurrentInstance().update("permissionTree");
            RequestContext.getCurrentInstance().update("userAccountForm");
            RequestContext.getCurrentInstance().execute("setTimeout(\"$(\'#messages\').slideUp(300)\", 5000)");
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
            
            Messages.addInfo(null, messages.getMessage("user-account.action.updated"));
        } catch (ApplicationException ex) {
            LOG.error("UserAccountBean#doUpdate on {} has found erros", this.user.getUsername(), ex);
            Messages.addError(null, messages.getMessage(ex.getMessage()));
        } finally {
            RequestContext.getCurrentInstance().update("userAccountForm");
            RequestContext.getCurrentInstance().execute("setTimeout(\"$(\'#messages\').slideUp(300)\", 5000)");
        }
    }
    
    /**
     * 
     */
    public void doPasswordUpdate() {
        
        try {
            this.accountService.updateAccount(this.user);
            
            Messages.addInfo(null, messages.getMessage("user-account.action.updated"));
        } catch (ApplicationException ex) {
            LOG.error("UserAccountBean#doUpdate on {} has found erros", this.user.getUsername(), ex);
            Messages.addError(null, messages.getMessage(ex.getMessage()));
        } finally {
            RequestContext.getCurrentInstance().update("userAccountForm");
            RequestContext.getCurrentInstance().execute("setTimeout(\"$(\'#messages\').slideUp(300)\", 5000)");
        }
    }
    
    /**
     * 
     */
    public void doDelete() {
        
        try {
            this.accountService.deleteAccount(this.user);
            this.users = this.accountService.listAccounts();
            
            Messages.addWarn(null, messages.getMessage("user-account.action.deleted"));
        } catch (DataIntegrityViolationException ex) {
            LOG.error("UserAccountBean#doDelete found erros", ex);
            Messages.addError(null, messages.getMessage("user-account.action.delete-used"));
        } catch (Exception ex) {
            LOG.error("UserAccountBean#doDelete found erros", ex);
            Messages.addError(null, messages.getMessage(ex.getMessage()));
        } finally {
            RequestContext.getCurrentInstance().execute("PF('popupDeleteUserAccount').hide()");
            RequestContext.getCurrentInstance().update("messages");
            RequestContext.getCurrentInstance().execute("setTimeout(\"$(\'#messages\').slideUp(300)\", 5000)");
            RequestContext.getCurrentInstance().update("usersList");
        }
    }
    
    /**
     * Metodo responsavel por construir toda a arvore de permissoes
     */
    private void buildPermissionTree() {
        
        // instancia o node principal, root
        this.authorityNodes = new DefaultTreeNode(this.messages.getMessage(
                    "user-account.form.tree.permissions"), null);
        
        // pega todas as authorities da lista de authorities do sistema
        final HashMap<String, Set<String>> authorities = 
                new Authority().getAllAvailableAuthoritiesGrouped();
        
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
        final Set<String> authoritiesGroups = 
                new Authority().getAllAvailableAuthoritiesGrouped().keySet();
        
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
        return "/main/tools/users/listUserAccounts.xhtml?faces-redirect=true";
    }
    
    /**
     * 
     * @return 
     */
    public String backToHome() {
        return "/main/home.xhtml?faces-redirect=true";
    }
    
    /**
     * 
     * @param id
     * @return 
     */
    public String getErrorMessage(String id) {
    
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        final Iterator<FacesMessage> iterator = facesContext.getMessages(id);
        
        if (iterator.hasNext()) {
            return this.messages.getMessage(iterator.next().getDetail());
        }
        return "";
    }
}
