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
 * @version 1.0
 * @since 1.0, 06/10/2013
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AccountService implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IPermissionRepository permissionRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    
    /**
     * 
     * @param user
     * @return
     * @throws ApplicationException 
     */
    @Transactional(readOnly = true)
    public boolean login(User user) throws ApplicationException {

        final String password = user.getPassword();
        user = this.userRepository.findByUsername(user.getUsername());

        if (user == null) {
            throw new ApplicationException("authentication.error.invalid_user");
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
     * 
     */
    public void logout() {
        SecurityContextHolder.clearContext();
        SecurityContextHolder.createEmptyContext();
    }

    /**
     * 
     * @param user 
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

        final String encodedPassword = this.passwordEncoder.encode(user.getUnsecurePassword());

        user.setPassword(encodedPassword);

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
     * 
     * @param user 
     */
    public void updateAccount(User user) {
        
        // checa se tem permissoes
        if (user.getPermissions().isEmpty()) {
            throw new ApplicationException("user-account.validate.no-permissions");
        }
        
        // atualiza o password se precisar
        if (user.getUnsecurePassword() != null && !user.getUnsecurePassword().isEmpty()) {
            
            final String encodedPassword = 
                    this.passwordEncoder.encode(user.getUnsecurePassword());
            
            user.setPassword(encodedPassword);
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
     * 
     * @param user 
     */
    public void deleteAccount(User user) {
        this.userRepository.delete(user);
    }
    
    /**
     * 
     * @return 
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
     * Lista os usuarios pelo seu status
     * 
     * @param blocked se quer o nao os usuarios bloqueados
     * @param removeCurrent remove o usuario logado da lista
     * 
     * @return lista de usuarios
     */
    public List<User> listUsersByStatus(boolean blocked, boolean removeCurrent) {
        
        if (removeCurrent) { 
            return this.userRepository.listByStatusAndRemoveAuthenticated(
                    blocked, AccountService.getCurrentAuthenticatedUser());
        } else {
            return this.userRepository.listByStatus(blocked);
        }
    }
    
    /**
     * 
     * @param username
     * @return
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
