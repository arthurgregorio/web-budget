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
package br.com.webbudget.application.controller;

import br.com.webbudget.application.producer.qualifier.AuthenticatedUser;
import br.com.webbudget.domain.security.Authorization;
import br.com.webbudget.domain.security.Group;
import br.com.webbudget.domain.security.User;
import br.com.webbudget.domain.service.AccountService;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import org.omnifaces.util.Faces;
import org.picketlink.Identity;
import org.picketlink.authentication.event.LoggedInEvent;
import org.picketlink.authentication.event.PostLoggedOutEvent;

/**
 * Bean utlizado pelo sistema para requisitar as authorities disponiveis no
 * sistemas
 *
 * @author Arthur Gregorio
 *
 * @version 1.1.0
 * @since 2.0.0, 27/06/2015
 */
@Named
@SessionScoped
public class UserSessionBean implements Serializable {

    private List<Group> userGroups;

    @Getter
    @Inject
    private transient Authorization authorization;

    @Inject
    private transient Identity identity;

    @Inject
    private transient AccountService accountService;

    /**
     * Inicializamos a sessao do usuario carregando os grupos dele e suas roles
     *
     * @param event o evento de login
     */
    protected void initialize(@Observes LoggedInEvent event) {
        this.userGroups = this.accountService
                .listUserGroupsAndGrants(this.getAuthenticatedUser());
    }

    /**
     * Destruimos a sessao, forcando que em um proximo login os grupos sejam
     * carregados novamente
     *
     * @param event o evento de logout
     */
    protected void destroy(@Observes PostLoggedOutEvent event) {
        this.userGroups = null;
    }

    /**
     * Checa pela role de um respectivo usuario
     *
     * @param roleName a role que espera-se que este usuario tenha
     * @return se existe ou nao uma instancia desta role atribuida a ele
     */
    public boolean hasRole(String roleName) {

        boolean hasRole = false;

        for (Group group : this.userGroups) {
            hasRole = this.hasGrantTo(roleName, group);
        }

        return hasRole;
    }

    /**
     * Buscamos nos grupos do usuario que fez login se em algum deles existe um
     * grant para a role desejada
     *
     * @param role a role que esperamos que o usuario tenha
     * @param group o grupo que pretendemos checar pela role
     * @return se ha ou nao a o grant para aquela role em algum dos grupos
     */
    private boolean hasGrantTo(String role, Group group) {

        // se for um grupo parente, os grants vem vazio, entao preenchemos
        if (group.getGrants() == null) {
            group.setGrants(this.accountService.listGrantsByGroup(group));
        }

        // agora iteramos nos grupos
        if (!group.getGrants().isEmpty()) {

            if (group.getGrants().stream().anyMatch((grant)
                    -> (grant.getRole().getAuthorization().equals(role)))) {
                return true;
            }
        }

        // se nao tem acesso em primeira instancia, checamos pelos outros grupos
        // aninhados dentro daquele grupo
        if (group.getParent() != null) {
            return this.hasGrantTo(role, group.getParent());
        } else {
            return false;
        }
    }

    /**
     * @return o nome do usuario logado atualmente no sistema
     */
    public String getAuthenticatedUserName() {
        return this.getAuthenticatedUser().getName();
    }
    
    /**
     * @return o email do usuario logado
     */
    public String getAuthenticatedUserEmail() {
        return this.getAuthenticatedUser().getEmail();
    }

    /**
     * @return o grupo ao qual este usuario esta vinculado
     */
    public String getAuthenticatedUserGroup() {
        return this.userGroups.stream().findAny().get().getName();
    }

    /**
     * Armazena um valor na sessao para o usuario logado
     *
     * @param key a chave para o valor
     * @param value o valor
     */
    public void setOnUserSession(String key, Object value) {
        Faces.setSessionAttribute(this.generateKeyForUser(key), value);
    }

    /**
     * Captura um valor da sessao do usuario logado
     *
     * @param <T> o tipo do objeto
     * @param key a chave para busca
     * @return o valor previamente setado para este usuario
     */
    public <T> T getFromUserSession(String key) {
        return Faces.getSessionAttribute(this.generateKeyForUser(key));
    }

    /**
     * Remove um valor da sessao do usuario logado
     *
     * @param key a chave do valor
     */
    public void removeFromUserSession(String key) {
        Faces.removeSessionAttribute(this.generateKeyForUser(key));
    }

    /**
     * Gera uma chave para este atributo na sessao
     *
     * @param valueKey a chave para apendar e formar uma chave unica
     * @return a chave para este usuario
     */
    private String generateKeyForUser(String valueKey) {
        return this.getAuthenticatedUserName() + ":" + valueKey;
    }

    /**
     * @return o usuario autenticado
     */
    @Produces
    @RequestScoped
    @AuthenticatedUser
    protected User getAuthenticatedUser() {
        return (User) this.identity.getAccount();
    }
}
