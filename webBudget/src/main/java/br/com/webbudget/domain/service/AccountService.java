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
import br.com.webbudget.domain.entity.users.Permission;
import br.com.webbudget.domain.entity.users.User;
import br.com.webbudget.domain.repository.user.IPermissionRepository;
import br.com.webbudget.domain.repository.user.IUserRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 06/10/2013
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AccountService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IUserRepository userRepository;
    @Autowired
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
    @Transactional(readOnly = true)
    public boolean login(User user) throws ApplicationException {

        final String password = user.getPassword();
        user = this.userRepository.findByUsername(user.getUsername());

        if (user == null) {
            throw new ApplicationException("authentication.error.invalid-user");
        }

        try {
            final Authentication authenticate = this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), password));

            if (authenticate.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authenticate);
                return true;
            }
        } catch (AuthenticationException ex) {
            throw new ApplicationException("authentication.error");
        }
        return false;
    }

    /**
     * Cria uma nova conta de usuario
     *
     * @param user o usuario a ser criado no sistema
     */
    public void createAccount(User user) {

        // checa se o cara ja existe
        final User found = this.findUserByUsername(user.getUsername());

        if (found != null) {
            throw new ApplicationException("user-account.validate.username-used");
        }

        // checa se tem permissoes
        if (user.getPermissions().isEmpty()) {
            throw new ApplicationException("user-account.validate.no-permissions");
        }

        user.setPassword(this.passwordEncoder.encode(user.getUnsecurePassword()));

        // pegamos as novas permissoes antes de salvar o user, 
        // se pegar depois vem null pq o JPA da limpa o que e transient
        final Set<Permission> permissions = user.getPermissions();

        // salva o usuario
        user = this.userRepository.save(user);

        // salvamos novamente as permissions
        for (Permission permission : permissions) {
            permission.setUser(user);
            this.permissionRepository.save(permission);
        }
    }

    /**
     * Atualiza uma conta de usuario
     *
     * @param user a conta a ser atualizada
     */
    public void updateAccount(User user) {

        // checa se tem permissoes
        if (user.getPermissions().isEmpty()) {
            throw new ApplicationException("user-account.validate.no-permissions");
        }

        // atualiza o password se precisar
        if (user.getUnsecurePassword() != null && !user.getUnsecurePassword().isEmpty()) {
            user.setPassword(this.passwordEncoder.encode(user.getUnsecurePassword()));
        }

        // pegamos as novas permissoes antes de salvar o user, 
        // se pegar depois vem null pq o JPA da limpa o que e transient
        final Set<Permission> newPermissions = user.getPermissions();

        // salva o usuario
        user = this.userRepository.save(user);

        // excluimos todas as permissoes atuais
        final Set<Permission> oldPermissions = new HashSet<>(this.permissionRepository.listByUser(user));

        for (Permission permission : oldPermissions) {
            this.permissionRepository.delete(permission);
        }

        // salvamos novamente as permissions
        for (Permission permission : newPermissions) {
            permission.setUser(user);
            this.permissionRepository.save(permission);
        }
    }

    /**
     * Deleta a conta de usuario
     *
     * @param user a conta a ser deletada
     */
    public void deleteAccount(User user) {
        this.userRepository.delete(user);
    }

    /**
     * Retorna o usuario autenticado no contexto atual
     *
     * @return o usuario autenticado no contexto
     */
    public static User getCurrentAuthenticatedUser() {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }

    /**
     *
     * @param username
     * @return
     */
    @Transactional(readOnly = true)
    public User findUserByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    /**
     *
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public User findAccountById(long userId) {
        return this.userRepository.findById(userId, false);
    }

    /**
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<User> listAccounts() {
        return this.userRepository.listAll();
    }

    /**
     * Metodo default da interface UserDetailsService do spring security
     *
     * @see UserDetailsService#loadUserByUsername(java.lang.String)
     *
     * @param username
     * @return
     *
     * @throws UsernameNotFoundException
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        final User user = this.userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("authentication.error.invalid_user");
        }
        return user;
    }
}
