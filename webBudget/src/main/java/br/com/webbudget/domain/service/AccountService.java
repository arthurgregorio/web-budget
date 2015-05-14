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
package br.com.webbudget.domain.service;

import br.com.webbudget.application.exceptions.ApplicationException;
import br.com.webbudget.domain.entity.users.User;
import br.com.webbudget.domain.repository.user.IPermissionRepository;
import br.com.webbudget.domain.repository.user.IUserRepository;
import java.util.List;
import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.1.0
 * @since 1.0.0, 06/10/2013
 */
public class AccountService {

    @Inject
    private IUserRepository userRepository;
    @Inject
    private IPermissionRepository permissionRepository;

    /**
     * Realiza o a autenticacao do usuario
     *
     * @param user o usuario a ser autenticado
     * @return true ou false indicando se ele pode ou nao realizar login
     *
     * @throws ApplicationException se houver algum erro ou o usuario for
     * invalido
     */
    @Transactional
    public boolean login(User user) throws ApplicationException {

        return true;
    }
    
    /**
     * Realiza o logout do usuario
     */
    public void logout() {
        
    }

    /**
     * Cria uma nova conta de usuario
     *
     * @param user o usuario a ser criado no sistema
     */
    @Transactional
    public void createAccount(User user) {

        
    }

    /**
     * Atualiza uma conta de usuario
     *
     * @param user a conta a ser atualizada
     */
    @Transactional
    public void updateAccount(User user) {

        
    }

    /**
     * Deleta a conta de usuario
     *
     * @param user a conta a ser deletada
     */
    @Transactional
    public void deleteAccount(User user) {
        this.userRepository.delete(user);
    }

    /**
     * Retorna o usuario autenticado no contexto atual
     *
     * @return o usuario autenticado no contexto
     */
    public static User getCurrentAuthenticatedUser() {

        
        return null;
    }

    /**
     *
     * @param username
     * @return
     */
    @Transactional
    public User findUserByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    /**
     *
     * @param userId
     * @return
     */
    @Transactional
    public User findAccountById(long userId) {
        return this.userRepository.findById(userId, false);
    }

    /**
     *
     * @return
     */
    @Transactional
    public List<User> listAccounts() {
        return this.userRepository.listAll();
    }
}
