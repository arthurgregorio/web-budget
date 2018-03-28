package br.com.webbudget.application.controller.tools;

import br.com.webbudget.application.controller.AbstractBean;
import br.com.webbudget.application.controller.ViewState;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.entities.security.Group;
import br.com.webbudget.domain.entities.security.User;
import br.com.webbudget.domain.repositories.tools.GroupRepository;
import br.com.webbudget.domain.repositories.tools.UserRepository;
import br.com.webbudget.domain.services.UserAccountService;
import java.util.ArrayList;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 09/01/2018
 */
@Named
@ViewScoped
public class UserBean extends AbstractBean {

    @Getter
    @Setter
    private User user;

    @Getter
    @Setter
    private String userFilter;

    @Getter
    private List<User> users;
    @Getter
    private List<Group> groups;

    @Getter
    @Setter
    private String filter;
    @Getter
    @Setter
    private Boolean blocked;
    
    @Inject
    private UserRepository userRepository;
    @Inject
    private GroupRepository groupRepository;
    
    @Inject
    private UserAccountService userAccountService;

    /**
     *
     */
    public void initializeList() {
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

        // cria a lista de usuarios vazia
        this.users = new ArrayList<>();

        // listamos os grupos
        this.groups = this.groupRepository.findAllActive();
        
        if (id == 0) {
            this.user = new User();
        } else {
            this.user = this.userRepository.findBy(id);
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

        // listamos os grupos
        this.groups = this.groupRepository.findAllActive();
        this.user = this.userRepository.findBy(id);
    }

    /**
     *
     */
    public void filterList() {
        this.updateComponent("usersList");
    }

    /**
     * @return o form de inclusao
     */
    public String changeToAdd() {
        return "formUser.xhtml?faces-redirect=true&viewState="
                + ViewState.ADDING;
    }

    /**
     * @param userId
     * @return
     */
    public String changeToEdit(String userId) {
        return "formUser.xhtml?faces-redirect=true&id="
                + userId + "&viewState=" + ViewState.EDITING;
    }

    /**
     * @param userId
     * @return
     */
    public String changeToDelete(String userId) {
        return "detailUser.xhtml?faces-redirect=true&id="
                + userId + "&viewState=" + ViewState.DELETING;
    }

    /**
     * @return
     */
    public String changeTolist() {
        return "listUsers.xhtml?faces-redirect=true";
    }

    /**
     * Redireciona para a pagina de detalhes do usuario
     */
    public void changeToDetail() {
        this.redirectTo("detailUser.xhtml?faces-redirect=true&id="
                + this.user.getId() + "&viewState=" + ViewState.DETAILING);
    }

    /**
     *
     */
    public void doSave() {
        try {
            this.userAccountService.save(this.user);
            this.user = new User();
            this.addInfo(true, "user.saved");
        } catch (BusinessLogicException ex) {
            this.addError(true, ex.getMessage(), ex.getParameters());
        } catch (Exception ex) {
            this.logger.error("UserBean#doSave has found erros", ex);
            this.addError(true, "error.generic-error", ex.getMessage());
        }
    }

    /**
     *
     */
    public void doUpdate() {
        try {
            this.userAccountService.update(this.user);
            this.addInfo(true, "user.updated");
        } catch (BusinessLogicException ex) {
            this.addError(true, ex.getMessage(), ex.getParameters());
        } catch (Exception ex) {
            this.logger.error("UserBean#doUpdate has found erros", ex);
            this.addError(true, "error.generic-error", ex.getMessage());
        }
    }

    /**
     *
     * @return
     */
    public String doDelete() {

        try {
            this.userAccountService.delete(this.user);
            return this.changeTolist();
        } catch (BusinessLogicException ex) {
            this.addError(true, ex.getMessage(), ex.getParameters());
            return null;
        } catch (Exception ex) {
            this.logger.error("UserBean#doDelete has found erros", ex);
            this.addError(true, "error.generic-error", ex.getMessage());
            return null;
        }
    }
}
